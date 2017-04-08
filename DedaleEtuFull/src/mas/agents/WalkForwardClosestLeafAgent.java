package mas.agents;

import mas.abstractAgent;
import mas.behaviours.WalkForwardClosestLeafBehaviour;
import env.Environment;

public class WalkForwardClosestLeafAgent extends abstractAgent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5224364214117970748L;
	
	protected void setup() {

		super.setup();

		// get the parameters given into the object[]. In the current case, the
		// environment where the agent will evolve
		final Object[] args = getArguments();
		if (args[0] != null) {

			deployAgent((Environment) args[0]);

		} else {
			System.err
					.println("Malfunction during parameter's loading of agent"
							+ this.getClass().getName());
			System.exit(-1);
		}

		doWait(2000);
		
		addBehaviour(new WalkForwardClosestLeafBehaviour(this));
		
		System.out.println("the agent "+this.getName()+ " is started");
	}

	protected void takeDown() {

	}
}
