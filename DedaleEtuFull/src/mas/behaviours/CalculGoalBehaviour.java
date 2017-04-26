package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import mas.agents.FosymaAgent;
import data.ManageExplo;
import data.Node;
import env.Attribute;

public class CalculGoalBehaviour extends SimpleBehaviour {
	private static final long serialVersionUID = -5357674075048934805L;
	private FosymaAgent myFosymaAgent;
	private ManageExplo managerExplo = new ManageExplo(myFosymaAgent.getMyTreasureType());

	public CalculGoalBehaviour(final FosymaAgent agent) {
		super(agent);
		myFosymaAgent = agent;
	}

	@Override
	public void action() {
		String myPosition = ((mas.abstractAgent) this.myAgent).getCurrentPosition();

		if(myPosition != ""){
			if (myFosymaAgent.getMyPath().isEmpty()) {
				Node node = null;
				HashMap<String, Node> dicoPere = myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getDicoPere();
				ArrayList<Node> fatherNodeList = new ArrayList<Node>(dicoPere.values());

				for (Node n : fatherNodeList) {
					if (n.getId().equals(myPosition)) {
						node = n;
						break;
					}
				}
				// Explo Gilles et mathias wombo combo
				int maxDepth = 5;
				if(myFosymaAgent.getBackPackFreeSpace() != 0)
				{
					myFosymaAgent.setMyPath(managerExplo.breadthResearch(node, myFosymaAgent.getMyKnowledge(), myFosymaAgent.getMyCapacity(), maxDepth));
				}else{
					ArrayList<Node> tmp = new ArrayList<Node>();
					tmp.add(node);
					myFosymaAgent.setMyPath(tmp);
				}

				// Explo Mathias
//				ArrayList<Node> goalPath = managerExplo.solveProblemByDepth(node,myFosymaAgent.getMyCapacity());
//				myFosymaAgent.setMyPath(goalPath);

				/**
				 * TODO : fusion des 2 explos 
				 */
				
				/**
				 * Cas pseudo terminal : je n'ai plus d'objectif
				 * cad que j'ai explor� toute la carte (plus de noeud feuille a visiter)
				 * et plus de tresors, inferieur a ma capacite, a ramasser
				 */
				if (myFosymaAgent.getMyPath().isEmpty()){
					System.out.println(myAgent.getName() + ": I have explored all the map");
					/**
					 *  TODO
					 *  Dorenavant je vais maintenant aussi ramasser les tresors qui sont plus gros que ma capacite,
					 *  TODO
					 *  Que faire si mon sac est plein ? Je m'arrete (?)
					 */

					if(myFosymaAgent.getBackPackFreeSpace() != 0){
						Random rand = new Random();
						myFosymaAgent.setMyCapacity(myFosymaAgent.getMyCapacity() + rand.nextInt(6) + 1);
					}
				}
			}
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
