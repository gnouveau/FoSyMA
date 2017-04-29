package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;

import java.util.ArrayList;

import mas.agents.FosymaAgent;
import data.Node;

public class ExplorationBehaviour extends SimpleBehaviour{

	private static final long serialVersionUID = 3451800439188170065L;
	private FosymaAgent myFosymaAgent;
	private MultiBehaviour mb;
	
	
	public ExplorationBehaviour(final FosymaAgent agent, MultiBehaviour mb){
		super(agent);
		myFosymaAgent = agent;
		this.mb = mb;
	}

	@Override
	public void action() {
		String myPosition = ((mas.abstractAgent) this.myAgent).getCurrentPosition();
		
		/**
		 * Reset his letter box and all the id of previous conversations 
		 */
		myFosymaAgent.setList_IdConversation(new ArrayList<>());
		myFosymaAgent.setList_IdConvGoal(new ArrayList<>());
		
		// L'agent vide sa boite aux lettres
		while(myAgent.receive() != null){}
			
		if (!myFosymaAgent.getMyPath().isEmpty()){
			Node nodeToGo = myFosymaAgent.getMyPath().get(0);
			
			System.out.println("ExplorationBehaviour : "+ myAgent.getName() + " : I'm in position : "+ myPosition +". I'm moving in "+ nodeToGo.getId());
			((mas.abstractAgent) this.myAgent).moveTo(nodeToGo.getId());
		}
	}

	@Override
	public boolean done() {
		return true;
	}
	
	public int onEnd(){
		mb.resetStates(mb.getTabStates());
		mb.reset();
		return -1;
	}
}
