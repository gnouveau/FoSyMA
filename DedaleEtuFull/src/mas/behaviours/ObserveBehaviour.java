package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;

import java.io.IOException;
import java.util.List;

import mas.agents.FosymaAgent;
import env.Attribute;
import env.Couple;

public class ObserveBehaviour extends SimpleBehaviour{
	
	private static final long serialVersionUID = 2124328421405412686L;
	private FosymaAgent myFosymaAgent;
	private boolean initStart;
	
	public ObserveBehaviour(final FosymaAgent agent){
		super(agent);
		myFosymaAgent = agent;
		initStart = true;
	}

	@Override
	public void action() {
		
		if(initStart){
			// Little pause to allow you to follow what is going on
			try {
				System.out.println(this.myAgent.getName()
						+ ": INITIALISATION");
				System.out.println("Press a key to allow the agent "
						+ this.myAgent.getName()
						+ " to execute its next move");
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			initStart = false;
		}
		
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		
		if (myPosition!=""){
			//List of observable from the agent's current position
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();
			System.out.println(this.myAgent.getName()+" -- list of observables: "+lobs);
			
			if(!myFosymaAgent.getMyPath().isEmpty()){		
				if(!lobs.get(0).getRight().isEmpty() 
						&& myFosymaAgent.getMyPath().get(myFosymaAgent.getMyPath().size()-1).getId().equals(lobs.get(0).getLeft()) 
						&& lobs.get(0).getRight().get(0).equals(Attribute.TREASURE)
						&& (int)lobs.get(0).getRight().get(0).getValue() <= myFosymaAgent.getMyCapacity())
				{
					myFosymaAgent.pick();
					myFosymaAgent.setMyCapacity(myFosymaAgent.getBackPackFreeSpace());
					lobs=((mas.abstractAgent)this.myAgent).observe();
//					System.out.println(this.myAgent.getName()+" -- list of observables: "+lobs);
				}
				myFosymaAgent.getMyPath().remove(0);
			}
			// The agent updates his knowledges
			myFosymaAgent.getMyKnowledge().majMap(lobs);
		}
	}

	@Override
	public boolean done() {
		return true;
	}
	
	public int onEnd(){
		return 1;
	}

}
