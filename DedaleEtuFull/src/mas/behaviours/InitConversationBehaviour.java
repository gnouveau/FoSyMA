package mas.behaviours;

import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.Random;

import mas.agents.FosymaAgent;
import env.Couple;

public class InitConversationBehaviour extends SimpleBehaviour {

	private static final long serialVersionUID = -151223954117725110L;
	private boolean onContact;
	private boolean timeOut;
	private boolean waiting;
	private long t;
	private long IdTickTime;
	private FosymaAgent myFosymaAgent;
	private int maxWaitingTime = 500;

	public InitConversationBehaviour(final FosymaAgent agent) {
		super(agent);
		myFosymaAgent = agent;
		timeOut = false;
		waiting = false;
		onContact = false;
	}

	@Override
	public void action() {
//		System.out.println("InitConversationBehaviourBehaviour : "+ myFosymaAgent.getName() +" :");
		Random rand = new Random();
		myFosymaAgent.doWait(rand.nextInt(500)+499);
		
		MessageTemplate firstContactFilter = MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF);
		ACLMessage firstContactMsg = this.myAgent.receive(firstContactFilter);

		String msgContent = "firstContactResponse";
		MessageTemplate firstContactResponseFilter = MessageTemplate.MatchPerformative(ACLMessage.INFORM).MatchContent(msgContent);
		ACLMessage firstContactResponseMsg = this.myAgent.receive(firstContactResponseFilter);
		
		if (firstContactMsg != null) {
//			System.out.println("InitConversationBehaviourBehaviour : "+ myFosymaAgent.getName() +" : ON M'A CONTACTE");
			IdTickTime = System.currentTimeMillis();
//			onContact = true;
			
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setSender(this.myAgent.getAID());
			AID senderAID = firstContactMsg.getSender();
			msg.addReceiver(senderAID);
			
			msg.setConversationId(String.valueOf(IdTickTime));
			Couple<String, String> c1 = new Couple<String, String>(String.valueOf(IdTickTime),firstContactMsg.getConversationId());
			Couple<String, Couple<String, String>> c2 = new Couple<String, Couple<String, String>>(firstContactMsg.getSender().getName(), c1);
			myFosymaAgent.getList_IdConversation().add(c2);
			
			msg.setContent(msgContent);
			((mas.abstractAgent) this.myAgent).sendMessage(msg);
						
		} else if (firstContactResponseMsg != null) {
//			System.out.println("InitConversationBehaviourBehaviour : "+ myFosymaAgent.getName() +" : ON M'A REPONDU");
//			onContact = true;
						
			Couple<String, String> c1 = new Couple<String, String>(String.valueOf(IdTickTime),firstContactResponseMsg.getConversationId());
			Couple<String, Couple<String, String>> c2 = new Couple<String, Couple<String, String>>(firstContactResponseMsg.getSender().getName(), c1);
			
			myFosymaAgent.getList_IdConversation().add(c2);
			
		} else {
			if (waiting && System.currentTimeMillis() - t < maxWaitingTime) {
				block();
			} else if (waiting && System.currentTimeMillis() - t > maxWaitingTime) {
				timeOut = true;
			} else {
				
				IdTickTime = System.currentTimeMillis();
				
				ACLMessage msg = new ACLMessage(ACLMessage.QUERY_IF);
				msg.setSender(this.myAgent.getAID());
				msg.setConversationId(String.valueOf(IdTickTime));
				ArrayList<AID> agentsList = getReceivers();
				
				for (AID aid : agentsList) {
					msg.addReceiver(aid);
				}
				
				((mas.abstractAgent) this.myAgent).sendMessage(msg);
				waiting = true;
				t = System.currentTimeMillis();
			}
		}
	}

	 @Override
	 public boolean done() {
		 return timeOut || onContact;
	 }
	 
	 public int onEnd(){				
		 if(!myFosymaAgent.getList_IdConversation().isEmpty()){				
			for (Couple<String, Couple<String, String>> couple : myFosymaAgent.getList_IdConversation()) {
				String name = couple.getLeft();
				String id = couple.getRight().getRight();
				MessageTemplate knownMapReceptionFilter = MessageTemplate
						.MatchPerformative(ACLMessage.INFORM)
						.MatchSender(myFosymaAgent.getAIDFromName(name))
						.MatchConversationId(id+"_MAP");
				
				myFosymaAgent.getFilterMapList().add(knownMapReceptionFilter);
				
				MessageTemplate goalPathReceptionFilter = MessageTemplate.MatchPerformative(ACLMessage.INFORM).MatchSender(myFosymaAgent.getAIDFromName(name)).MatchConversationId(id+"_GOAL");
				myFosymaAgent.getFilterGoalList().add(goalPathReceptionFilter);
			}
			 return 2; 
		 }else{
			 return 3;
		 }
	}

	public void reset(){
		timeOut = false;
		waiting = false;
		onContact = false;
		t = 0;
		
	}
	private ArrayList<AID> getReceivers() {
		ArrayList<AID> agentsList = new ArrayList<AID>();
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("fosyma");
		template.addServices(sd);

		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			agentsList = new ArrayList<AID>(result.length);
			for (int i = 0; i < result.length; ++i) {
				if (!result[i].getName().equals(myAgent.getAID())) {
					agentsList.add(result[i].getName());
				}
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		return agentsList;
	}
}
