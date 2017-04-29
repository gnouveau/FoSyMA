package mas.behaviours;

import jade.core.behaviours.FSMBehaviour;
import mas.agents.FosymaAgent;

public class MultiBehaviour extends FSMBehaviour{
	
	private static final long serialVersionUID = 1L;
	private FosymaAgent myFosymaAgent;
	private String[] tabStates = {"observation" , "initConversation","exchangeMap", "calculGoal", "exchangeGoal", "explore", "theEnd"};
	
	public MultiBehaviour(final FosymaAgent agent){
		super(agent);
		myFosymaAgent = agent;
		
		registerFirstState(new ObserveBehaviour(myFosymaAgent), "observation");
		
		registerState(new InitConversationBehaviour(myFosymaAgent), "initConversation");
		registerState(new ExchangeMapBehaviour(myFosymaAgent), "exchangeMap");
		registerState(new CalculGoalBehaviour(myFosymaAgent), "calculGoal");
		registerState(new ExchangeGoalBehaviour(myFosymaAgent), "exchangeGoal");
		registerState(new ExplorationBehaviour(myFosymaAgent, this), "explore");
		
		registerLastState(new DebugLastBehaviour(myFosymaAgent), "theEnd");
		
		
		this.registerTransition("observation", "initConversation",1);
		
		this.registerTransition("initConversation", "exchangeMap", 2);
		this.registerTransition("initConversation", "calculGoal",3);
		
		this.registerTransition("exchangeMap", "calculGoal",4);
		
		this.registerTransition("calculGoal", "exchangeGoal",5);
		
		this.registerTransition("calculGoal", "explore",6);
		
		this.registerTransition("exchangeGoal", "explore", 7);
		
		this.registerTransition("explore", "observation",-1);
//		this.registerTransition("explore", "theEnd",1000000);
		
	}

	public FosymaAgent getMyFosymaAgent() {
		return myFosymaAgent;
	}

	public String[] getTabStates() {
		return tabStates;
	}

	public void setMyFosymaAgent(FosymaAgent myFosymaAgent) {
		this.myFosymaAgent = myFosymaAgent;
	}

	public void setTabStates(String[] tabStates) {
		this.tabStates = tabStates;
	}
		
}
