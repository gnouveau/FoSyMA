package mas.agents;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import mas.abstractAgent;
import mas.behaviours.OldExchangeKnowledgesBehaviour;
import env.Environment;

public class CommunicationAgent extends abstractAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7292102971720467005L;

	protected void setup() {
		super.setup();

		final Object[] args = getArguments();

		if (args[0] != null) {
			deployAgent((Environment) args[0]);
			
			// Register the service in the yellow pages
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setType("communication");
			sd.setName("JADE-communication");
			dfd.addServices(sd);
			try {
				DFService.register(this, dfd);
			} catch (FIPAException fe) {
				fe.printStackTrace();
			}
		} else {
			System.err
					.println("Malfunction during parameter's loading of agent"
							+ this.getClass().getName());
			System.exit(-1);
		}

		doWait(2000);
		
		addBehaviour(new OldExchangeKnowledgesBehaviour(this));
		
		System.out.println("the agent "+this.getName()+ " is started");
	}

	protected void takeDown() {
		// Deregister from the yellow pages
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		// Printout a dismissal message
		System.out.println("Seller-agent " + getAID().getName()
				+ " terminating.");
	}

}
