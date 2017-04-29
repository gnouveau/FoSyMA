package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import mas.agents.FosymaAgent;
import data.KnownMap;
import env.Couple;

public class ExchangeMapBehaviour extends SimpleBehaviour {

	private static final long serialVersionUID = -2610883159873347198L;
	private FosymaAgent myFosymaAgent;
	private long t;
	private boolean mapSent;
	private boolean finish;
	private int maxWaitingTime = 500;
	private int nbAckWanted;

	public ExchangeMapBehaviour(final FosymaAgent agent) {
		super(agent);
		myFosymaAgent = agent;
		mapSent = false;
		finish = false;
		nbAckWanted = 0;
	}

	@Override
	public void action() {
		
		// Waiting part
		Random rand = new Random();
		myFosymaAgent.doWait(rand.nextInt(500)+499);
//		System.out.println("ExchangeGoalBehaviour : "+ myFosymaAgent.getName() +" : debut de ExchangeMapBehaviour");
		
		/**
		 * I didn't sent my map => I have to send my map
		 */
		if(!mapSent){
//			System.out.println("ExchangeGoalBehaviour : "+ myFosymaAgent.getName() +" : j'envois ma carte a tout le monde");
			for (Couple<String, Couple<String, String>> couple : myFosymaAgent.getList_IdConversation()) {

				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setSender(myFosymaAgent.getAID());
				msg.addReceiver(myFosymaAgent.getAIDFromName(couple.getLeft()));
				msg.setConversationId(couple.getRight().getLeft() +"_MAP");
				
				try {
					msg.setContentObject(myFosymaAgent.getMyKnowledge().sendMap(couple.getLeft()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				nbAckWanted++;
				((mas.abstractAgent) this.myAgent).sendMessage(msg);
			}
			mapSent = true;
			t = System.currentTimeMillis();
		}		
		
		// Listening part1		
		ACLMessage ackMessage = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM));
		
		// Listening part2
		ACLMessage knownMapReceptionMsg = null;
		int index = -1;
		for (int i = 0; i < myFosymaAgent.getFilterMapList().size(); i++) {
			knownMapReceptionMsg = this.myAgent.receive(myFosymaAgent.getFilterMapList().get(i));
			if (knownMapReceptionMsg != null) {
				index = i;
				break;
			}
		}
				
		/**
		 * CASE 1a: I receive an acknowledge
		 * 
		 * WARNING: CASE 1a and CASE 1b can append together, they are independent
		 */
		if(ackMessage != null){
//			System.out.println("ExchangeGoalBehaviour : "+ myFosymaAgent.getName() 
//					+" : je recois un acknowledge de : "+ackMessage.getSender().getName());
			for (Couple<String, Couple<String, String>> couple : myFosymaAgent.getList_IdConversation()){
				if(couple.getLeft().equals(ackMessage.getSender().getName())
						&& couple.getRight().getRight().equals(ackMessage.getConversationId())){
					
						// Maj de la liste d'id conversation specifique pour l'echange de goal
						myFosymaAgent.getList_IdConvGoal().add(couple);
						
						// Maj du filtre pour continuer a n'?couter que ceux dont j'ai recu la carte
						String otherAgentTickTime = ackMessage.getConversationId().split("_MAP")[0];
						MessageTemplate goalPathReceptionFilter = MessageTemplate.MatchPerformative(ACLMessage.INFORM)
								.MatchSender(ackMessage.getSender())
								.MatchConversationId(otherAgentTickTime+"_GOAL");
						myFosymaAgent.getFilterGoalList().add(goalPathReceptionFilter);
						
						// On supprime dans myKnowledge le sous graphe specifique a cet agent, car il l'a bien recu
						myFosymaAgent.getMyKnowledge().AckMap(couple.getLeft());
						
						nbAckWanted--;
						
						break;
				}
			}
		}
		
		/**
		 * CASE 1b: An agent sent his map to me
		 * AND I answer to him, to confirm the reception!!
		 * 
		 * WARNING: CASE 1a and CASE 1b can append together, they are independent
		 */
		if (knownMapReceptionMsg != null) {
//			System.out.println("ExchangeGoalBehaviour : "+ myFosymaAgent.getName() 
//					+" : j'ai recu une carte de "+ knownMapReceptionMsg.getSender().getName() 
//					+" je lui envois un acknowledge");
			myFosymaAgent.getFilterMapList().remove(index);
			
			try {
				myFosymaAgent.getMyKnowledge().mergeMap((KnownMap) knownMapReceptionMsg.getContentObject());
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			
			/*
			 * Sending the acknowledge
			 */
			ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
			msg.setSender(myFosymaAgent.getAID());
			msg.addReceiver(knownMapReceptionMsg.getSender());
			
			for (Couple<String, Couple<String, String>> couple : myFosymaAgent.getList_IdConversation()){
				if(myFosymaAgent.getAIDFromName(couple.getLeft()).equals(knownMapReceptionMsg.getSender())){
					msg.setConversationId(couple.getRight().getLeft());
					break;
				}
			}
			
			((mas.abstractAgent) this.myAgent).sendMessage(msg);
		}
		
		/**
		 * CASE 2: Got no messages, but still waiting for receiving some maps
		 */
		if(knownMapReceptionMsg == null && ackMessage == null && myFosymaAgent.getFilterMapList().size() > 0){
//			System.out.println("ExchangeGoalBehaviour : "+ myFosymaAgent.getName() 
//					+" : j'attends des messages");
			if (mapSent && System.currentTimeMillis() - t < maxWaitingTime) {
				block();
			} else {
				System.out.println("ExchangeGoalBehaviour : "+ myFosymaAgent.getName() 
						+" : j'ai trop attendu je passe a la suite !");
				finish = true;
			}
		}
		
		/**
		 * CASE 3: Terminal case: the agent sent his map and received all the maps AND acknowledge he was waiting for
		 */
		else if(myFosymaAgent.getFilterMapList().size() == 0 && nbAckWanted == 0 && mapSent){
//			System.out.println("ExchangeGoalBehaviour : "+ myFosymaAgent.getName()
//					+" : j'ai tout recu je passe a la suite !");
			finish = true;
		}
	}

	@Override
	public boolean done() {
		return finish;
	}

	public int onEnd() {
		mapSent = false;
		finish = false;
		myFosymaAgent.setFilterMapList(new ArrayList<>());
		myFosymaAgent.setMyPath(new ArrayList<>());
		nbAckWanted = 0;
		return 4;
	}
}