package mas.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import data.ManageMap;
import data.Node;
import env.Attribute;
import env.Couple;

public class WalkForwardClosestLeafBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -8548381871874977070L;

	private ManageMap myKnowledge;
	private ArrayList<String> path;

	public WalkForwardClosestLeafBehaviour(Agent a) {
		super(a, 1000);
		myKnowledge = new ManageMap(this.myAgent.getName());
		path = new ArrayList<String>();
	}

	@Override
	public void onTick() {

//		System.out.println(this.myAgent.getName()
//				+ ": I'M STARTING MY BEHAVIOUR");
		String myPosition = ((mas.abstractAgent) this.myAgent)
				.getCurrentPosition();

		if (myPosition != "") {
			// List of observable from the agent's current position
			List<Couple<String, List<Attribute>>> lobs = ((mas.abstractAgent) this.myAgent).observe();// myPosition
			System.out.println(this.myAgent.getName()+ " -- list of observables: " + lobs);

			// The agent updates his knowledges
			myKnowledge.majMap(lobs);
//			System.out.println(this.myAgent.getName()+ " -- FATHER LIST");
			HashMap<String, Node> dicoPere = myKnowledge.getListKnownMap().get(0).getDicoPere();
			ArrayList<Node> fatherNodeList = new ArrayList<Node>(dicoPere.values());
//			System.out.println(fatherNodeList.toString());
//			System.out.println(this.myAgent.getName()+ " -- SON LIST");
//			HashMap<String, Node> dicoFils = myKnowledge.getListKnownMap().get(0).getDicoFils();
//			ArrayList<Node> childrenNodeList = new ArrayList<Node>(dicoFils.values());
//			System.out.println(childrenNodeList.toString());

			// Little pause to allow you to follow what is going on
			try {
				System.out.println("Press a key to allow the agent "
						+ this.myAgent.getName()
						+ " to execute its next move");
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Node node = null;
			if (path.size() == 0) {
//				System.out.println(this.myAgent.getName()
//						+ ": i'm searching where to go...");
				for (Node n : fatherNodeList) {
					if (n.getId().equals(myPosition)) {
						node = n;
						break;
					}
				}
//				System.out.println(this.myAgent.getName() + ": I'm at: "+ myPosition);
				path = findBestPathToClosestLeaf(node);
	
				if (path != null){
//					System.out.println(this.myAgent.getName()+ ": I know where to go!");
//					System.out.println(this.myAgent.getName()+ ": I'm following this path: "+ path.toString());
				}else{
					System.out.println("end of the behaviour");
					return;
				}
			}

			String nodeToGo = path.remove(0);
			System.out.println(myAgent.getName() + ": I'm moving in "+ nodeToGo);
			((mas.abstractAgent) this.myAgent).moveTo(nodeToGo);
		}
	}

	private ArrayList<String> findBestPathToClosestLeaf(Node paramNode){
//		System.out.println("#################");
//		System.out.println("FUNCTION STARTING");
//		System.out.println("#################");
		
		/*
		 * On regarde si il existe encore des noeuds du graphe a explorer pour l'agent.
		 * Si il n'y en n'a plus, on considere que l'agent a tout explore, il s'arrete.
		 * 
		 * /!\ : Comportement a ameliorer dans la suite du projet
		 * 
		 */
		HashMap<String, Node> dicoFils = myKnowledge.getListKnownMap().get(0).getDicoFils();
		if(dicoFils.size() == 0){
//			System.out.println("No more leaf to exploration. Exploration finished!");
			return null;
		}
		
		HashMap<String, Node> dicoPere = myKnowledge.getListKnownMap().get(0).getDicoPere();
		ArrayList<Node> nodeListToExplore = new ArrayList<Node>();
		HashSet<String> explored = new HashSet<String>();
		/*
		 * whoIsYourDaddy :
		 * cle : ID du noeud
		 * valeur : ID de son pere
		 * But : permet de recreer le chemin que l'agent va parcourir pour arriver
		 * au nouveau noeud a explorer
		 */
		HashMap<String, String> whoIsYourDaddy = new HashMap<String, String>();
		boolean loop = true;
		
		nodeListToExplore.add(paramNode);
		whoIsYourDaddy.put(paramNode.getId(), null);
		String leafIdFound = null;
		
		while(loop){
			// Ne dois jamais arriver ?
			if(nodeListToExplore.size() == 0){
				System.out.println("nodeListToExplore empty!");
				return null;
			}
			
			Node node = nodeListToExplore.remove(0);
			explored.add(node.getId());
			
//			System.out.println("(function) node developped: "+ node.toString());
//			System.out.println("(function) nodes explored: "+ explored.toString());
			
			for (Node n : node.getFils()) {
//				System.out.println("(function) watching node: " + n.toString());

				/*
				 * Si on ne trouve pas le noeud que l'on etudie dans la liste des noeuds peres,
				 * alors cas terminal : on a trouve une feuille, objectif atteint, on arrete la recherche 
				 */
				if (dicoPere.get(n.getId()) == null) {
//					System.out.println("(function) NEW NODE TO EXPLORE: "+ n.toString());
					whoIsYourDaddy.put(n.getId(),node.getId());
					leafIdFound = n.getId();
					loop = false;
					break;
				}

				/*
				 * Le noeud actuel n'est pas une feuille, donc si ce noeud n'est pas deja dans
				 * la liste des noeuds deja explore et dans la liste des noeuds a explorer,
				 * alors on l'ajoute a la liste des noeuds a explorer
				 */
				if (!explored.contains(n.getId()) && !nodeListToExplore.contains(n)) {
//					System.out.println("(function) add to list of breadth research: "+ n.toString());
					nodeListToExplore.add(n);
					whoIsYourDaddy.put(n.getId(),node.getId());
				}
			}
		}
		
		/*
		 * Reconstruction du chemin a partir du dico whoIsYourDaddy
		 */
//		System.out.println("leafIdFound: "+leafIdFound);
//		System.out.println("whoIsYourDaddy: "+ whoIsYourDaddy.toString());
		ArrayList<String> path = new ArrayList<String>();
		path.add(leafIdFound);
		String iDFather = whoIsYourDaddy.get(leafIdFound);
		
		while(iDFather != null){
			path.add(0, iDFather);
			iDFather = whoIsYourDaddy.get(iDFather);
		}
		path.remove(0); // On enleve le noeud racine qui est le noeud actuel ou se trouve l'agent
		return path;
	}

}
