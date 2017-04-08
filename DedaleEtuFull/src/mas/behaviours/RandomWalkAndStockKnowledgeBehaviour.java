package mas.behaviours;

import jade.core.behaviours.TickerBehaviour;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import data.ManageMap;
import env.Attribute;
import env.Couple;

public class RandomWalkAndStockKnowledgeBehaviour extends TickerBehaviour{

	private static final long serialVersionUID = 1655154643838826795L;
	
	private ManageMap myKnowledge;

	public RandomWalkAndStockKnowledgeBehaviour(final mas.abstractAgent myagent) {
		super(myagent, 1000);
		myKnowledge  = new ManageMap(this.myAgent.getName());
	}
	
	@Override
	public void onTick(){
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		
		
		if (myPosition!=""){
			// List of observable from the agent's current position
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition
			System.out.println(this.myAgent.getName()+" -- list of observables: "+lobs);
			
			//The agent updates his knowledges
			myKnowledge.majMap(lobs);
			System.out.println(this.myAgent.getName()+" -- SHOW MY KNOWLEDGES");
			System.out.println(myKnowledge.getListKnownMap().get(0).toString());
			
			
			//Little pause to allow you to follow what is going on
			try {
				System.out.println("Press a key to allow the agent "+this.myAgent.getName() +" to execute its next move");
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//Random move from the current position
			Random r= new Random();
			int moveId=r.nextInt(lobs.size());
			
			//The move action (if any) MUST be the last action of your behaviour
			((mas.abstractAgent)this.myAgent).moveTo(lobs.get(moveId).getLeft());
		}
	}

}
