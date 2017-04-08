package mas.behaviours;

import jade.core.behaviours.SimpleBehaviour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import mas.agents.FosymaAgent;
import data.ManageExplo;
import data.Node;

public class CalculGoalBehaviour extends SimpleBehaviour {
	private static final long serialVersionUID = -5357674075048934805L;
	private FosymaAgent myFosymaAgent;
	private ManageExplo managerExplo = new ManageExplo();

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
				// Explo Gilles
//				myFosymaAgent.setMyPath(findBestPathToClosestLeaf(node));
//				myFosymaAgent.setMyPath(breadthResearch(node, myFosymaAgent.getMyCapacity(), 5));

				// Explo Mathias
				ArrayList<Node> goalPath = managerExplo.solveProblemByDepth(node,myFosymaAgent.getMyCapacity());
				myFosymaAgent.setMyPath(goalPath);

				/**
				 * TODO : fusion des 2 explos 
				 */
				
				/**
				 * Cas pseudo terminal : je n'ai plus d'objectif
				 * cad que j'ai exploré toute la carte (plus de noeud feuille a visiter)
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

	private ArrayList<Node> findBestPathToClosestLeaf(Node paramNode){
		ArrayList<Node> path = new ArrayList<Node>();
		/*
		 * On regarde si il existe encore des feuilles dans le graphe a explorer pour l'agent.
		 * Si il n'y en n'a plus, on renvoit un chemin vide
		 * 
		 */
		HashMap<String, Node> dicoFils = myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getDicoFils();
		if(dicoFils.isEmpty()){
			return path;
		}
		
		HashMap<String, Node> dicoPere = myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getDicoPere();
		ArrayList<Node> nodeListToExplore = new ArrayList<Node>();
		HashSet<Node> explored = new HashSet<Node>();
		/*
		 * whoIsYourDaddy :
		 * cle : ID du noeud
		 * valeur : ID de son pere
		 * But : permet de recreer le chemin que l'agent va parcourir pour arriver
		 * au nouveau noeud a explorer
		 */
		HashMap<String, Node> whoIsYourDaddy = new HashMap<String, Node>();
		
		nodeListToExplore.add(paramNode);
		whoIsYourDaddy.put(paramNode.getId(), null);
		Node leafFound = null;
		
		boolean loop = true;
		while(loop){
			// Ne dois jamais arriver
			if(nodeListToExplore.isEmpty()){
				System.out.println("nodeListToExplore empty!");
				return path;
			}
			
			Node node = nodeListToExplore.remove(0);
			explored.add(node);
			
			for (Node n : node.getFils()) {
//				System.out.println("fils : "+ n.getId());
				/*
				 * Si on ne trouve pas le noeud que l'on etudie dans la liste des noeuds peres,
				 * alors cas terminal : on a trouve une feuille, objectif atteint, on arrete la recherche 
				 */
//				System.out.println("cmp : " + dicoPere.get(n.getId()));
				if (dicoPere.get(n.getId()) == null) {
					whoIsYourDaddy.put(n.getId(),node);
					leafFound = n;
					loop = false;
					break;
				}

				/*
				 * Le noeud actuel n'est pas une feuille, donc si ce noeud n'est pas deja dans
				 * la liste des noeuds deja explore et qu'il n'est pas dans la liste des noeuds a explorer,
				 * alors on l'ajoute a la liste des noeuds a explorer
				 */
				if (!explored.contains(n) && !nodeListToExplore.contains(n)) {
					nodeListToExplore.add(n);
					whoIsYourDaddy.put(n.getId(),node);
				}
			}
		}
		
		/*
		 * Reconstruction du chemin a partir du dico whoIsYourDaddy
		 */
		path.add(leafFound);
		Node nodeFather = whoIsYourDaddy.get(leafFound.getId());
		
		while(nodeFather != null){
			path.add(0, nodeFather);
			nodeFather = whoIsYourDaddy.get(nodeFather.getId());
		}
		path.remove(0); // On enleve le noeud racine qui est le noeud actuel ou se trouve l'agent
		return path;
	}
	
	/**
	 * recherche en largeur. retourne le chemin vers le tresor le plus proche
	 * d'une valeur inferieure a la capacite du sac de l'agent.
	 * Si aucun tresor trouve, vers la feuille la plus proche
	 * @param paramNode
	 * @param myCapacity
	 * @param maxDepth
	 * @return
	 */
	private ArrayList<Node> breadthResearch(Node paramNode, int myCapacity, int maxDepth){
		System.out.println("DEBUG : breadthResearch : ma capacite : "+ myFosymaAgent.getMyCapacity());
		ArrayList<Node> path = new ArrayList<Node>();
		
		// TODO : ameliorer. S'il il n'y a plus de feuilles, chercher des tresors.
//		HashMap<String, Node> dicoFils = myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getDicoFils();
//		if(dicoFils.isEmpty()){
//			return path;
//		}
		
		HashMap<String, Node> dicoPere = myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getDicoPere();
		ArrayList<Node> nodeListToExplore = new ArrayList<Node>();
		HashSet<Node> explored = new HashSet<Node>();
		/*
		 * whoIsYourDaddy :
		 * cle : ID du noeud
		 * valeur : ID de son pere
		 * But : permet de recreer le chemin que l'agent va parcourir pour arriver
		 * au nouveau noeud a explorer
		 */
		HashMap<String, Node> whoIsYourDaddy = new HashMap<String, Node>();
		
		/*
		 * depthDict
		 * cle : ID du noeud
		 * valeur : profondeur dans l'arbre de recherche en largeur
		 * But : permet d'arreter la recherche si on atteint la profondeur max
		 */
		HashMap<String, Integer> depthDict = new HashMap<String, Integer>();
		
		nodeListToExplore.add(paramNode);
		whoIsYourDaddy.put(paramNode.getId(), null);
		depthDict.put(paramNode.getId(), 0);
		Node leafFound = null;
		Node treasureFound = null;
		Node maxDepthNode = null;
		
		boolean loop = true;
		while(loop){
			/*
			 * J'ai regarde tous les noeuds que je connais
			 * et je n'ai pas trouve de tresor => je break
			 */
			if(nodeListToExplore.isEmpty()){
				System.out.println("DEBUG : breadthResearch : tous les noeuds observe et pas de tresor interessant");
				break;				
			}
			
			Node node = nodeListToExplore.remove(0);
			explored.add(node);
			
			for (Node n : node.getFils()) {
				/*
				 * Profondeur maximale atteinte
				 */
				if(depthDict.get(node.getId()) == maxDepth){
					System.out.println("DEBUG : breadthResearch : profondeur maximale atteinte");
					System.out.println("DEBUG : breadthResearch : affichage depthDict :");
					System.out.println(depthDict);
					whoIsYourDaddy.put(n.getId(),node);
					maxDepthNode = node;
					loop = false;
					break;
				}
				
				/*
				 * J'ai trouve un tresor qui m'interesse
				 */
				if(0 < n.getValue() && n.getValue() <= myCapacity && treasureFound == null){
					System.out.println("DEBUG : breadthResearch : j'ai trouve un tresor interessant");
					whoIsYourDaddy.put(n.getId(),node);
					treasureFound = n;
					loop = false;
					break;
				}
				
				/*
				 * J'ai trouve ma premiere feuille
				 * cad, on ne trouve pas le noeud que l'on etudie dans la liste des noeuds peres,
				 * alors on l'enregistre. On ira vers cette feuille si on ne trouve pas un tresor
				 * qui nous interesse 
				 */
				if (dicoPere.get(n.getId()) == null && leafFound == null) {
					System.out.println("DEBUG : breadthResearch : j'ai trouve une feuille ("+n.getId()+")ou potentiellement aller");
					whoIsYourDaddy.put(n.getId(),node);
					if(!depthDict.containsKey(n)){
						depthDict.put(n.getId(), depthDict.get(node.getId()) + 1);
					}
					leafFound = n;
				}

				/*
				 * Le noeud actuel n'est pas une feuille ni un tresor qui m'interesse,
				 * donc si ce noeud n'est pas deja dans la liste des noeuds deja explore
				 * et qu'il n'est pas dans la liste des noeuds a explorer,
				 * alors on l'ajoute a la liste des noeuds a explorer
				 * de plus, on ajoute sa profondeur dans l'arbre de recherche en largeur
				 */
				if (!explored.contains(n) && !nodeListToExplore.contains(n)) {
					nodeListToExplore.add(n);
					whoIsYourDaddy.put(n.getId(),node);
					if(!depthDict.containsKey(n)){
						depthDict.put(n.getId(), depthDict.get(node.getId()) + 1);
					}
				}
			}
		}
		
		Node finalNode = null;
		if(treasureFound != null){
			System.out.println("DEBUG : breadthResearch : tresor ! "+ treasureFound.getId() +" "+ treasureFound.getValue());
			finalNode = treasureFound;
		}else if(leafFound != null){
			System.out.println("DEBUG : breadthResearch : feuille ! "+ leafFound.getId());
			finalNode = leafFound;
		}else if(maxDepthNode != null){
			System.out.println("DEBUG : breadthResearch : profondeur max atteinte ! "+ maxDepthNode.getId() +" "+ depthDict.get(maxDepthNode));
			finalNode = maxDepthNode;
		}else{
			System.out.println("DEBUG : breadthResearch : pas de chemin objectif trouve !");
			return path; // Je n'ai pas trouve de tresor ni de feuille et j'ai regarde tous les noeuds
		}
				
		/*
		 * Reconstruction du chemin a partir du dico whoIsYourDaddy
		 */
		path.add(finalNode);
		Node nodeFather = whoIsYourDaddy.get(finalNode.getId());
		
		while(nodeFather != null){
			path.add(0, nodeFather);
			nodeFather = whoIsYourDaddy.get(nodeFather.getId());
		}
		
		String str = "";
		for(Node n : path){
			str += n.getId()+" ";
		}
		System.out.println("DEBUG : breadthResearch : chemin final : "+str);
		path.remove(0); // On enleve le noeud racine qui est le noeud actuel ou se trouve l'agent
		return path;
	}
}
