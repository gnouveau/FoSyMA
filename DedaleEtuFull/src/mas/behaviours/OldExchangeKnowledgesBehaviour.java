package mas.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import env.Attribute;
import env.Couple;

public class OldExchangeKnowledgesBehaviour extends TickerBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -151223954117725110L;
	private ArrayList<AID> agentsList;
	private boolean inContact;
	private boolean waiting;
	private long t;

	public OldExchangeKnowledgesBehaviour(final Agent agent) {
		super(agent, 1000);
		inContact = false;
		waiting = false;
		t = 0;
	}

	@Override
	public void onTick() {
		//Little pause to allow you to follow what is going on
		try {
			System.out.println("Press a key to allow the agent "+this.myAgent.getName() +" to execute its next move");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(this.myAgent.getName() +": t = "+ t);
		System.out.println(this.myAgent.getName() +": System.currentTimeMillis() = "+ System.currentTimeMillis());
		long tmp = System.currentTimeMillis() - t;
		System.out.println(this.myAgent.getName() +": time spent = "+ tmp);
		
		MessageTemplate firstContactFilter = MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF);
		ACLMessage firstContactMsg = this.myAgent.receive(firstContactFilter);

		MessageTemplate firstContactResponseFilter = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		ACLMessage firstContactResponseMsg = this.myAgent.receive(firstContactResponseFilter);

		System.out.println(myAgent.getName() +": I'm checking my letter box...");
		if (firstContactMsg != null) {
			System.out.println(myAgent.getName() +": I heard someone!");
			inContact = true;
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setSender(this.myAgent.getAID());
			AID senderAID = firstContactMsg.getSender();
			msg.addReceiver(senderAID);
			System.out.println(this.myAgent.getName() + ": Hey! Dear "+ senderAID.getName() + ", "+ this.myAgent.getName() + " here!");
			msg.setContent(this.myAgent.getName() + ": Hey! Dear "+ senderAID.getName() + ", "+ this.myAgent.getName() + " here!");
			((mas.abstractAgent) this.myAgent).sendMessage(msg);
		} else if (firstContactResponseMsg != null) {
			System.out.println(myAgent.getName() +": I've got a response!");
			inContact = true;
			ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
			msg.setSender(this.myAgent.getAID());
			AID senderAID = firstContactResponseMsg.getSender();
			msg.addReceiver(senderAID);
			System.out.println(this.myAgent.getName()+ ": Hi! nice to meet you, " + senderAID.getName()+ ". Shall we exchange our knowledges?");
			msg.setContent(this.myAgent.getName()+ ": Hi! nice to meet you, " + senderAID.getName()+ ". Shall we exchange our knowledges?");
			((mas.abstractAgent) this.myAgent).sendMessage(msg);
		} else {
			System.out.println(myAgent.getName() +": no messages :'(");
			if (waiting && System.currentTimeMillis() - t < 5000) {
				System.out.println(myAgent.getName() +": block's time!");
				block();
			} else if (waiting && System.currentTimeMillis() - t > 5000) {
				System.out.println(myAgent.getName() +": I've waited too much, let's move!");
				waiting = false;
				move();
			} else {
				System.out.println(myAgent.getName() +": sending a message in a bottle");
				ACLMessage msg = new ACLMessage(ACLMessage.QUERY_IF);
				msg.setSender(this.myAgent.getAID());
				getReceivers();
				for (AID aid : agentsList) {
					msg.addReceiver(aid);
				}
				System.out.println(this.myAgent.getName()+ ": Is there someone?");
				((mas.abstractAgent) this.myAgent).sendMessage(msg);
				msg.setContent(this.myAgent.getName()+ ": Is there someone?");
				((mas.abstractAgent) this.myAgent).sendMessage(msg);
				waiting = true;
				t = System.currentTimeMillis();
			}
		}

	}

//	 @Override
//	 public boolean done() {
//	 // TODO Auto-generated method stub
//	 return endBehaviour;
//	 }

	private void getReceivers() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("communication");
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
	}
	
	private void move(){
		String myPosition = ((mas.abstractAgent) this.myAgent)
				.getCurrentPosition();

		if (myPosition != "" && inContact == false) {
			// List of observable from the agent's current position
			List<Couple<String, List<Attribute>>> lobs = ((mas.abstractAgent) this.myAgent)
					.observe();// myPosition

			// Random move from the current position
			Random r = new Random();
			int moveId = r.nextInt(lobs.size());

			// The move action (if any) MUST be the last action of your
			// behaviour
			((mas.abstractAgent) this.myAgent).moveTo(lobs.get(moveId)
					.getLeft());
		}
	}
}
