package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import mas.agents.FosymaAgent;
import data.Goal;
import data.ManageExplo;
import data.Node;
import env.Attribute;

public class CalculGoalBehaviour extends SimpleBehaviour {
	private static final long serialVersionUID = -5357674075048934805L;
	private FosymaAgent myFosymaAgent;
	private ManageExplo managerExplo;

	public CalculGoalBehaviour(final FosymaAgent agent) {
		super(agent);
		myFosymaAgent = agent;
	}

	@Override
	public void action() {
		managerExplo = new ManageExplo(myFosymaAgent.getMyTreasureType());
		String myPosition = ((mas.abstractAgent) this.myAgent).getCurrentPosition();
		System.out.println("je suis l'agent : "+this.myFosymaAgent.getName());
		System.out.println(this.myFosymaAgent.getMyKnowledge().getListKnownMap().get(0));
		System.out.println(this.myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getDicoFils());
		
		
		if(myPosition != ""){
			if (myFosymaAgent.getMyPath().isEmpty()	) {
				Node node = myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getDicoPere().get(myPosition);
//				ArrayList<Node> fatherNodeList = new ArrayList<Node>(dicoPere.values());
//
//				for (Node n : fatherNodeList) {
//					if (n.getId().equals(myPosition)) {
//						node = n;
//						break;
//					}
//				}
				// Explo Gilles et mathias wombo combo

			

				int maxDepth = 6;

				if(myFosymaAgent.getBackPackFreeSpace() != 0 && this.myFosymaAgent.getMyCapacity()<100)
				{
					Goal g = new Goal(myFosymaAgent.getName(), null, myFosymaAgent.getMyCapacity(), myFosymaAgent.getMyTreasureType(),node);
					ArrayList<Node> tmp =managerExplo.breadthResearch(node, myFosymaAgent.getMyKnowledge(), myFosymaAgent.getMyCapacity(), maxDepth);
					if(tmp.get(0).getId().equals(node.getId()))
					{
						Random rand = new Random();
						myFosymaAgent.setMyCapacity(myFosymaAgent.getMyCapacity() + rand.nextInt(6) + 1);
					}
					g.setGoalPath(tmp);
					myFosymaAgent.setMyGoal(g);
				}else{
					ArrayList<Node> tmp = new ArrayList<Node>();
					tmp.add(node);
					Goal g = myFosymaAgent.getMyGoal();
					g.setGoalPath(tmp);
					myFosymaAgent.setMyGoal(g);
					System.out.println("¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶");
					System.out.println("¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶");
					System.out.println("¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶");
					System.out.println("je suis l'agent : "+this.myFosymaAgent.getName());
					System.out.println("¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶");
					System.out.println("¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶");
					System.out.println("¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶");
				}

				// Explo Mathias
				//				ArrayList<Node> goalPath = managerExplo.solveProblemByDepth(node,myFosymaAgent.getMyCapacity());
				//				myFosymaAgent.setMyPath(goalPath);



				/**
				 * Cas pseudo terminal : je n'ai plus d'objectif
				 * cad que j'ai explor� toute la carte (plus de noeud feuille a visiter)
				 * et plus de tresors, inferieur a ma capacite, a ramasser
				 */
//				if (myFosymaAgent.getMyPath().isEmpty()){
//					System.out.println(myAgent.getName() + ": I have explored all the map");
//
//					if(myFosymaAgent.getBackPackFreeSpace() != 0){
//						
//						if(myFosymaAgent.getMyCapacity()<100)
//						{
//							myFosymaAgent.setMyCapacity(myFosymaAgent.getMyCapacity() + rand.nextInt(6) + 1);
//							Goal g = new Goal(myFosymaAgent.getName(), null, myFosymaAgent.getMyCapacity(), myFosymaAgent.getMyTreasureType());
//							g.setGoalPath(managerExplo.breadthResearch(node, myFosymaAgent.getMyKnowledge(), myFosymaAgent.getMyCapacity(), maxDepth));
//							myFosymaAgent.setMyGoal(g);
//						}else{
//							ArrayList<Node> tmp = new ArrayList<Node>();
//							tmp.add(node);
//							Goal g = myFosymaAgent.getMyGoal();
//							g.setGoalPath(tmp);
//							myFosymaAgent.setMyGoal(g);
//						}
//					}
//				}
			}
			System.out.println(this.myFosymaAgent+" comme chemin objectif "+this.myFosymaAgent.getMyPath());
		}
	}

	@Override
	public boolean done() {
		return true;
	}

	public int onEnd(){
		if(myFosymaAgent.getList_IdConversation().isEmpty()){
			return 6;
		}else{
			return 5;
		}
	}
}
