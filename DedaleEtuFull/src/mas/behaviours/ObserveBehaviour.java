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
				System.out.println("Press a key to allow the agent with type "
						+ this.myFosymaAgent.getMyTreasureType()
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
			System.out.println("ObserveBehaviour : "+ this.myAgent.getName()+" -- list of observables: "+lobs);
			System.out.println("jai un capacite de reele de : "
					+this.myFosymaAgent.getBackPackFreeSpace()
					+"et une capacité estimée de "
					+this.myFosymaAgent.getMyCapacity()
					+" je peux prendre : "
					+this.myFosymaAgent.getMyTreasureType());
			if(!myFosymaAgent.getMyPath().isEmpty()){
				if(!lobs.get(0).getRight().isEmpty())
				{
					//					System.out.println(myFosymaAgent.getMyPath().get(myFosymaAgent.getMyPath().size()-1).getId().equals(lobs.get(0).getLeft()));
					//					System.out.println(lobs.get(0).getRight().get(0).getName()+" et "+myFosymaAgent.getMyTreasureType());
					//					System.out.println((int)lobs.get(0).getRight().get(0).getValue() <= myFosymaAgent.getMyCapacity());
				}
				if(!lobs.get(0).getRight().isEmpty() 
						&& myFosymaAgent.getMyPath().get(myFosymaAgent.getMyPath().size()-1).getId().equals(lobs.get(0).getLeft()) 
						&& lobs.get(0).getRight().get(0).getName().equals(myFosymaAgent.getMyTreasureType())
						&& (int)lobs.get(0).getRight().get(0).getValue() <= myFosymaAgent.getMyCapacity())
				{	System.out.println("Je suis l'agent "
						+this.myFosymaAgent.getName()
						+" Je prend un tresors de type :"
						+lobs.get(0).getRight().get(0).name()
						+"avec une capacite reelle actuelle de :"
						+this.myFosymaAgent.getBackPackFreeSpace());

				myFosymaAgent.pick();
				myFosymaAgent.setMyCapacity(myFosymaAgent.getBackPackFreeSpace());
				lobs=((mas.abstractAgent)this.myAgent).observe();
				System.out.println("je suis l'agent : "
						+this.myFosymaAgent.getName()
						+"j'ai apres pick une capacité réelle de "
						+this.myFosymaAgent.getBackPackFreeSpace());
				System.out.println("ObserveBehaviour : "+ this.myAgent.getName()+" -- list of observables: "+lobs);
				}


				boolean test = true;
				for(Couple<String, List<Attribute>> c : lobs)
				{
			
					if(myFosymaAgent.getMyPath().get(0).getId().equals(c.getLeft()))
					{
						test=false;
						break;
					}
				}
				if(test)
				{
					System.out.println("je veux aller dans un voisinage qui n'existe pas");
					System.out.println("avec ce but : ");
					System.out.println(myFosymaAgent.getMyPath());
					System.out.println("et cette connaissance :");
					System.out.println(this.myFosymaAgent.getMyKnowledge().getListKnownMap().get(0));
					
					System.exit(0);
				}
				//				System.out.println("je sort du test");
				if(myPosition.equals(myFosymaAgent.getMyPath().get(0).getId()))
				{
					//					System.out.println("TOTO"); 
					myFosymaAgent.getMyPath().remove(0);
					//					System.out.println("TATATA");
					myFosymaAgent.getMyGoal().setGoalPath(myFosymaAgent.getMyPath());

				}
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
