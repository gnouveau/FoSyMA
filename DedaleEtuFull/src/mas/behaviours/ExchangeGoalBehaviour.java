package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import mas.agents.FosymaAgent;
import data.Goal;
import data.ManageBlock;
import data.Node;
import env.Attribute;
import env.Couple;

public class ExchangeGoalBehaviour extends SimpleBehaviour {

	private static final long serialVersionUID = 3727938303152375233L;
	private FosymaAgent myFosymaAgent;
	private boolean goalSent;
	private boolean finish;
	private long t;
	private int maxWaitingTime = 500;
	private ArrayList<Goal> othersGoalList = new ArrayList<Goal>();
	
	public ExchangeGoalBehaviour(final FosymaAgent agent) {
		super(agent);
		myFosymaAgent = agent;
		goalSent = false;
		finish = false;
	}
	
	@Override
	public void action() {				
		// L'agent attend un peu sur une duree aleatoirement (Cree une forme de desynchronisation)
		Random rand = new Random();
		myFosymaAgent.doWait(rand.nextInt(500)+499);
		
		System.out.println("ExchangeGoalBehaviour : "+ myFosymaAgent.getName() +" : debut de ExchangeGoalBehaviour");
		System.out.println("ExchangeGoalBehaviour : "+ myFosymaAgent.getName() +" : mon list_IdConversation() : "+ myFosymaAgent.getList_IdConversation());
		System.out.println("ExchangeGoalBehaviour : "+ myFosymaAgent.getName() +" : mon list_IdConvGoal(), J'ENVOIS QU'A EUX : "+ myFosymaAgent.getList_IdConvGoal());
		
		// Creation de son propre but qu'il veut transmettre
		ArrayList<Node> goalPath = myFosymaAgent.getMyPath();
		Goal myGoal = myFosymaAgent.getMyGoal();
		
		/**
		 * I didn't sent my goal => I have to send my goal
		 */
		if(!goalSent){
			System.out.println("ExchangeGoalBehaviour : "+ myFosymaAgent.getName() +" :J'ENVOIS MON GOAL");
			
			for (Couple<String, Couple<String, String>> couple : myFosymaAgent.getList_IdConvGoal()) {

				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setSender(myFosymaAgent.getAID());
				msg.addReceiver(myFosymaAgent.getAIDFromName(couple.getLeft()));
				msg.setConversationId(couple.getRight().getLeft() +"_GOAL");
				
				try {
					msg.setContentObject(myGoal);
				} catch (IOException e) {
					e.printStackTrace();
				}
				((mas.abstractAgent) this.myAgent).sendMessage(msg);
			}
			goalSent = true;
			t = System.currentTimeMillis();
		}
		
		// L'agent recupere le premier message de sa boite au lettre correspondant a la reception d'un goal
		ACLMessage goalPathReceptionMsg = null;
		int index = -1;
		for (int i = 0; i < myFosymaAgent.getFilterGoalList().size(); i++) {
			goalPathReceptionMsg = this.myAgent.receive(myFosymaAgent.getFilterGoalList().get(i));
			if (goalPathReceptionMsg != null) {
				index = i;
				break;
			}
		}
		
		/**
		 * CASE 1: An agent sent his goal to me
		 */
		if (goalPathReceptionMsg != null) {
			myFosymaAgent.getFilterGoalList().remove(index);
			try {
				// Stockage du but de l'autre agent
				othersGoalList.add((Goal) goalPathReceptionMsg.getContentObject());
				System.out.println("ExchangeGoalBehaviour : "+ myFosymaAgent.getName() +" :J'AI RECU LE GOAL DE : "+ othersGoalList.get(othersGoalList.size() - 1).getNameAgt());
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * CASE 2: Got no message, but still waiting for receiving some goals
		 */
		else if(myFosymaAgent.getFilterGoalList().size() > 0){
			System.out.println("ExchangeGoalBehaviour : "+ myFosymaAgent.getName() +" :J'ATTENDS");
			if (goalSent && System.currentTimeMillis() - t < maxWaitingTime) {
				block();
			} else {
				finish = true;
			}
		}
		
		/**
		 * CASE 3: Terminal case: the agent sent his goal and received all the goals he was waiting for
		 */
		else if(myFosymaAgent.getFilterGoalList().isEmpty() && goalSent){
			// Resolution des conflits entre son propre chemin objectif et celui des autres agents
			for(Goal g : othersGoalList){
				System.out.println("ExchangeGoalBehaviour : "+ myAgent.getName() +" : MON BUT : "+ myFosymaAgent.getMyPath());
				System.out.println("ExchangeGoalBehaviour : "+ myAgent.getName() +" : RESOLUTION CONFLITS avec : "+ g.getNameAgt()+" qui a pour but : "+ g.getGoalPath());
				
				ManageBlock managerBlock = new ManageBlock(myGoal,g,myFosymaAgent);
				myFosymaAgent.setMyGoal(managerBlock.solveBlock());				
			}
			finish = true;
		}
	}

	@Override
	public boolean done() {
		return finish;
	}
	
	public int onEnd() {
		goalSent = false;
		finish = false;
		t = 0;
		myFosymaAgent.setFilterGoalList(new ArrayList<>());
		othersGoalList = new ArrayList<Goal>();
		return 7;
	}
}
