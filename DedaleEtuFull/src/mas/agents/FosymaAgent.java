package mas.agents;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;

import mas.abstractAgent;
import mas.behaviours.MultiBehaviour;
import data.ManageMap;
import data.Node;
import env.Couple;
import env.Environment;

public class FosymaAgent extends abstractAgent {

	private static final long serialVersionUID = 1L;
	private ManageMap myKnowledge;
	private ArrayList<Node> myPath;
	private ArrayList<Couple<String,Couple<String,String>>> list_IdConversation;
	private ArrayList<MessageTemplate> filterMapList;
	private ArrayList<MessageTemplate> filterGoalList;
	private Integer myCapacity;

	protected void setup() {

		super.setup();
		
		// get the parameters given into the object[]. In the current case, the
		// environment where the agent will evolve
		final Object[] args = getArguments();
		if (args[0] != null) {
			myKnowledge = new ManageMap(this.getName());
			myPath = new ArrayList<Node>();
			list_IdConversation = new ArrayList<>();
			filterMapList = new ArrayList<MessageTemplate>();
			filterGoalList = new ArrayList<MessageTemplate>();
			
			// Register the service in the yellow pages
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setType("fosyma");
			sd.setName("JADE-fosyma");
			dfd.addServices(sd);
			try {
				DFService.register(this, dfd);
			} catch (FIPAException fe) {
				fe.printStackTrace();
			}

			deployAgent((Environment) args[0]);

		} else {
			System.err
					.println("Malfunction during parameter's loading of agent"
							+ this.getClass().getName());
			System.exit(-1);
		}

		doWait(2000);

		addBehaviour(new MultiBehaviour(this));
		this.myCapacity = this.getBackPackFreeSpace();
		System.out.println("the agent " + this.getName() + " is started");
	}

	protected void takeDown() {

	}

	public ManageMap getMyKnowledge() {
		return myKnowledge;
	}

	public ArrayList<Node> getMyPath() {
		return myPath;
	}

	public void setMyPath(ArrayList<Node> myPath) {
		this.myPath = myPath;
	}
	
	public ArrayList<Couple<String, Couple<String, String>>> getList_IdConversation() {
		return list_IdConversation;
	}

	public void setList_IdConversation(
			ArrayList<Couple<String, Couple<String, String>>> list_IdConversation) {
		this.list_IdConversation = list_IdConversation;
	}

	public ArrayList<MessageTemplate> getFilterMapList() {
		return filterMapList;
	}

	public void setFilterMapList(ArrayList<MessageTemplate> filterList) {
		this.filterMapList = filterList;
	}
	
	public AID getAIDFromName(String name) {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("fosyma");
		template.addServices(sd);

		try {
			DFAgentDescription[] result = DFService.search(this, template);
			for (int i = 0; i < result.length; ++i) {
				if (result[i].getName().getName().equals(name)) {
					return result[i].getName();
				}
			}

		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		return null; // NE DOIS PAS ARRIVER
	}

	public ArrayList<MessageTemplate> getFilterGoalList() {
		return filterGoalList;
	}

	public void setFilterGoalList(ArrayList<MessageTemplate> filterGoalList) {
		this.filterGoalList = filterGoalList;
	}

	public Integer getMyCapacity() {
		return myCapacity;
	}

	public void setMyKnowledge(ManageMap myKnowledge) {
		this.myKnowledge = myKnowledge;
	}

	public void setMyCapacity(Integer myCapacity) {
		this.myCapacity = myCapacity;
	}

}
