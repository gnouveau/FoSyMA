package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
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

	public ExchangeMapBehaviour(final FosymaAgent agent) {
		super(agent);
		myFosymaAgent = agent;
		mapSent = false;
		finish = false;
	}

	@Override
	public void action() {
		
		Random rand = new Random();
		myFosymaAgent.doWait(rand.nextInt(500)+499);
		
		/**
		 * I didn't sent my map => I have to send my map
		 */
		if(!mapSent){
			for (Couple<String, Couple<String, String>> couple : myFosymaAgent.getList_IdConversation()) {

				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setSender(myFosymaAgent.getAID());
				msg.addReceiver(myFosymaAgent.getAIDFromName(couple.getLeft()));
				msg.setConversationId(couple.getRight().getLeft() +"_MAP");
				
				try {
					msg.setContentObject(myFosymaAgent.getMyKnowledge().getListKnownMap().get(0));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				((mas.abstractAgent) this.myAgent).sendMessage(msg);
			}

			mapSent = true;
			t = System.currentTimeMillis();
		}
		
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
		 * CASE 1: An agent sent his map to me
		 */
		if (knownMapReceptionMsg != null) {
			myFosymaAgent.getFilterMapList().remove(index);
			try {
				myFosymaAgent.getMyKnowledge().mergeMap((KnownMap) knownMapReceptionMsg.getContentObject());
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * CASE 2: Got no message, but still waiting for receiving some maps
		 */
		else if(myFosymaAgent.getFilterMapList().size() > 0){
			if (mapSent && System.currentTimeMillis() - t < maxWaitingTime) {
				block();
			} else {
				finish = true;
			}
		}
		
		/**
		 * CASE 3: Terminal case: the agent sent his map and received all the maps he was waiting for
		 */
		else if(myFosymaAgent.getFilterMapList().size() == 0 && mapSent){
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
		myFosymaAgent.setMyPath(new ArrayList<>());
		return 4;
	}
}