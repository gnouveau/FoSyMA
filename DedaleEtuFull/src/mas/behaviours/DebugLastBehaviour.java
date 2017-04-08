package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import mas.agents.FosymaAgent;

public class DebugLastBehaviour extends SimpleBehaviour {
	
	private static final long serialVersionUID = 9144688701851688402L;
	private FosymaAgent myFosymaAgent;

	public DebugLastBehaviour(final FosymaAgent agent) {
		super(agent);
		myFosymaAgent = agent;
	}

	@Override
	public void action() {
		System.out.println(this.myFosymaAgent.getName()+ ": J'AI TERMINE");
		block();
	}

	@Override
	public boolean done() {
		return true;
	}

}
