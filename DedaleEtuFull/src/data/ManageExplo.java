package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import mas.agents.FosymaAgent;

public class ManageExplo {


	public  ArrayList<Node> visited;
	private boolean find = true;
	private ArrayList<Node> explo;
	private String type;
	private FosymaAgent myFosymaAgent;
	private boolean other = false;

	public ManageExplo(String type,FosymaAgent myFosymaAgent){
		this.type = type;
		this.myFosymaAgent =myFosymaAgent ;

	}

	private  void closeByWidth(ArrayList<Node> listNode, ArrayList<Node> path,Integer value) throws StopParcoursException
	{


		ArrayList<Node> stockFils = new ArrayList<>();
		for(Node n : listNode)
		{
			if(n.getFils().isEmpty() && n.getValue() <= value)
			{
				path.add(n);
				throw new StopParcoursException(path);
			}
			for (Node fils : n.getFils())
			{
				if(fils.getId() != n.getId())
				{
					for(Node alreadyVisited : visited)
					{
						if(fils.getId() != alreadyVisited.getId())
						{
							if(fils.getFils().isEmpty() && n.getValue() <= value)
							{
								path.add(fils);
								throw new StopParcoursException(path);
							}

							visited.add(fils);
							stockFils.add(fils);
						}
					}
				}
			}
		}
		closeByWidth(stockFils, path,value);
	}

	private  void closeByDepth(Node n,ArrayList<Node> path, Integer value, int prof) throws StopParcoursException
	{
		System.out.println("je suis "+n+" j ai pour fils : "+n.getFils());
		String s="";
		for(Node test : path)
		{
			s += test.getId()+" ";
		}
		System.out.println("et je viens de : "+s);
		for (Node fils : n.getFils())
		{
			
			fils  = this.myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getNodeInMap(fils.getId());
			System.out.println("je pars sur le fils : "+fils);
			boolean check = false;	
			if(fils.getId() != n.getId())
			{
				//	//////System.out.println("OK");
				for(Node alreadyVisited : visited)
				{
					if(fils.getId().equals(alreadyVisited.getId()))
					{	
						////////System.out.println("Noeud deja traite");
						check = true;
						break;
					}
				}
			}
			if(!check)
			{
				if(!myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getDicoFils().containsKey(fils.getId()) && fils.getValue() ==-1)
				{
					System.out.println("SA FUCK UP");
					System.out.println("le noeud qui bug : "+fils);
					System.out.println("adresse fils : "+fils.getClass()+"adresse du pere existant : "+myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getDicoPere().get(fils.getId()));

					System.out.println("il vient de  : "+ n);
					System.out.println("ma connaissance : "+this.myFosymaAgent.getMyKnowledge().getListKnownMap().get(0));
					System.out.println("fils : "+myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getDicoFils());
					System.exit(0);
				}
				//System.out.println("je suis le noeud : "+fils+" de valeur "+fils.getValue());
				if((fils.getType().equals(this.type) && fils.getValue() <= value && fils.getValue()>0) || fils.getValue() ==-1 && fils.getValue() ==-1 && find && myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getDicoFils().containsKey(fils.getId()))
				{

					if(fils.getValue() ==-1 && find && myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getDicoFils().containsKey(fils.getId()) && prof < 15)
					{
						find = false;
						explo = path;
						explo.add(fils);
					}else{
						//System.out.println("SOLUTION TROUVEE 2");
						//System.out.println("Je suis le Node FINAL :"+fils);
						path.add(fils);
						throw new StopParcoursException(path);	
					}
				}
				visited.add(fils);
				path.add(fils);
				closeByDepth(fils, path, value,prof++);
			}
		}
	}


	private  void closeByDepth(Node n,ArrayList<Node> path, Node goal,int prof) throws StopParcoursException
	{

		for (Node fils : n.getFils())
		{
			fils  = this.myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getNodeInMap(fils.getId());
			boolean check = false;	
			if(fils.getId() != n.getId())
			{

				//	//////System.out.println("OK");

				for(Node alreadyVisited : visited)
				{

					if(fils.getId().equals(alreadyVisited.getId()))
					{	
						////////System.out.println("Noeud deja traite");
						check = true;
						break;
					}

				}
			}
			if(!check)
			{
				System.out.println("je suis le noeud : "+fils+" de valeur "+fils.getValue());
				System.out.println("je suis en profodeur : "+prof);
				if(fils.getId().equals(goal.getId()) | prof>5)
				{


					////////System.out.println("SOLUTION TROUVEE 1");
					////////System.out.println("Je suis le Node FINAL :"+fils);
					path.add(fils);
					throw new StopParcoursException(path);	
				}
			}
			visited.add(fils);
			ArrayList<Node> temp = path;
			temp.add(fils);
			if(!myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getDicoFils().containsKey(fils.getId()))
			{
				closeByDepth(fils, temp, goal,prof++);
			}

		}
	}
	private  void closeByDepth(Node n,ArrayList<Node> path) throws StopParcoursException
	{

		for (Node fils : n.getFils())
		{
			fils  = this.myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getNodeInMap(fils.getId());
			boolean check = false;	
			if(fils.getId() != n.getId())
			{

				//	//////System.out.println("OK");

				for(Node alreadyVisited : visited)
				{

					if(fils.getId().equals(alreadyVisited.getId()))
					{	
						////////System.out.println("Noeud deja traite");
						check = true;
						break;
					}

				}
			}
			if(!check)
			{
				////////System.out.println("SOLUTION TROUVEE 1");
				////////System.out.println("Je suis le Node FINAL :"+fils);
				path.add(fils);
				throw new StopParcoursException(path);	

			}

		}
	}

	// renvoie un chemin menant a un node de valeur inferieure ou egale a value
	// par exploration en largeur
	public  ArrayList<Node> solveProblemByWidth(Node n, Integer value)
	{
		if(visited.isEmpty())
		{
			visited = new ArrayList<>();	
		}
		visited.add(n);
		ArrayList<Node> path = new ArrayList<>();
		ArrayList<Node> node = new ArrayList<>();
		n = this.myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getNodeInMap(n.getId());
		node.add(n);
		//		long t= //////System.currentTimeMillis();

		try {
			closeByWidth(node, path,value);
		} catch (StopParcoursException e) { path=e.getFin();}

		////////System.out.println("temps de traitement en ms : "+Double.toString(//////System.currentTimeMillis()-t));
		return path;
	}
	// renvoie un chemin menant a un node de valeur inferieure ou egale a value et sup stric a 0
	// par exploration en profondeur
	public  ArrayList<Node> solveProblemByDepth(Node n, Integer value)
	{

		if(visited ==  null )
		{
			visited = new ArrayList<>();	
		}else{
			System.out.println(visited);
		}
		explo = new ArrayList<Node>();
		ArrayList<Node> path = new ArrayList<>();
		n = this.myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getNodeInMap(n.getId());
		visited.add(n);
		//		long t= //////System.currentTimeMillis();
		try {
			closeByDepth(n, path,value,0);
		} catch (StopParcoursException e) { path=e.getFin();}

		////////System.out.println("temps de traitement en ms : "+Double.toString(//////System.currentTimeMillis()-t));
		if(path.isEmpty())
		{
			if (explo.isEmpty()) 
			{

				//System.out.println("je n'ai plus rien a explorer ou aucun tresor trouvé de capacité inférieure ou égale à "+value+" avec un type de : "+this.type);
				path.add(n);
				return path;
			}else{
				//System.out.println("je rend la feuille : "+explo.get(explo.size()-1));
				return explo;
			}
		}else{
			return path;
		}
	}
	// meme qu'avant avec ajout de noeud conflit et on cherche un noeud but à la place d'une valeur
	// ESQUIVE
	public  ArrayList<Node> solveProblemByDepth(Node n, Node goal,ArrayList<Node> conflits)
	{
		System.out.println("on cherche un autre chemin pour  : "+goal+"en partant de "+n+" avec debut à :"+other);
		if(visited ==  null )
		{
			visited = new ArrayList<>();	
		}
		explo = new ArrayList<Node>();
		visited= conflits;
		n = this.myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getNodeInMap(n.getId());
		visited.add(n);
		ArrayList<Node> path = new ArrayList<>();
		//		long t= //////System.currentTimeMillis();
		if(other)
		{
			try {
				System.out.println("je lance le large");
				closeByDepth(n, path, goal, 5);
			} catch (StopParcoursException e) {
				path=e.getFin();
			}
			return path;
		}else{
			System.out.println("je lance le prof");
			return breadthResearch(n, goal, myFosymaAgent.getMyKnowledge(), 20, visited);
		}


		////////System.out.println("temps de traitement en ms : "+Double.toString(//////System.currentTimeMillis()-t));

	}
	//renvoie le premier noeud qui n'est pas un noeud de conflit
	// LIBERER LE CHEMIN
	public  ArrayList<Node> solveProblemByDepth(Node n,ArrayList<Node> conflit)
	{
		if(visited ==  null )
		{



			visited = new ArrayList<>();	
		}
		explo = new ArrayList<Node>();
		for(Node nc : conflit)
		{
			visited.add(nc);
		}
		ArrayList<Node> path = new ArrayList<>();
		//		long t= //////System.currentTimeMillis();
		try {
			closeByDepth(n, path);
		} catch (StopParcoursException e) { path=e.getFin();}
		////////System.out.println("temps de traitement en ms : "+Double.toString(//////System.currentTimeMillis()-t));
		if(path.isEmpty())
		{
			return explo;
		}else{
			return path;
		}
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
	public ArrayList<Node> breadthResearch(Node paramNode, ManageMap myKnowledge, int myCapacity, int maxDepth){
		//System.out.println("DEBUT ALGO LARGEUR");
		//System.out.println(myKnowledge.getListKnownMap().get(0));
		//System.out.println(myKnowledge.getListKnownMap().get(0).getDicoFils());
		//////System.out.println("DEBUG : breadthResearch : ma capacite : "+ myCapacity);
		ArrayList<Node> path = new ArrayList<Node>();

		HashMap<String, Node> dicoPere = myKnowledge.getListKnownMap().get(0).getDicoPere();
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
			 * 
			 * WARNING : CONDITION NON TOTALEMENT VALIDE. ON PEUT RENTRER DANS CETTE CONDITION SI LE GRAPHE
			 * CONTIENT UN CYCLE AVEC QUE DES PERES, DONC DES NOEUDS QUI NE M'INTERESSE PAS,
			 * A CAUSE DU CYCLE, LES FILS DU NOEUD QUE JE REGARDE ONT DEJA ETE INTEGRES DANS LA LISTE DE NOEUDS
			 * A EXPLORER DONC EMPECHE D'ATTEINDRE LA PROFONDEUR MAX QUI EST AUSSI UN CAS D'ARRET
			 */
			if(nodeListToExplore.isEmpty()){
				//				//System.out.println("DEBUG : breadthResearch : tous les noeuds observe et pas de tresor interessant");
				break;				
			}

			Node node = nodeListToExplore.remove(0);
			explored.add(node);

			for (Node n : node.getFils()) {
				/*
				 * Profondeur maximale atteinte
				 */
				if(depthDict.get(node.getId()) == maxDepth){
					//System.out.println("#########################################################");
					//System.out.println("DEBUG : breadthResearch : profondeur maximale atteinte");
					//System.out.println("DEBUG : breadthResearch : affichage depthDict :");
					//System.out.println(depthDict);
					//System.out.println("#########################################################");
					whoIsYourDaddy.put(n.getId(),node);
					maxDepthNode = node;
					loop = false;
					break;
				}

				/*
				 * J'ai trouve un tresor qui m'interesse
				 */
				////System.out.println("je suis le noeud : "+n.getId());
				////System.out.println(n.getType()+" et "+this.type);
				////System.out.println(0 < n.getValue());
				////System.out.println(n.getValue() <= myCapacity);
				////System.out.println(treasureFound == null);
				if(0 < n.getValue() && n.getValue() <= myCapacity && treasureFound == null && myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getDicoFils().containsKey(n.getId()) && n.getType().equals(this.type) ){
					//////System.out.println("DEBUG : breadthResearch : j'ai trouve un tresor interessant");
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
					//////System.out.println("DEBUG : breadthResearch : j'ai trouve une feuille ("+n.getId()+")ou potentiellement aller");
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
			//System.out.println("DEBUG : breadthResearch : tresor ! "+ treasureFound.getId() +" "+ treasureFound.getValue());
			finalNode = treasureFound;
		}else if(leafFound != null){
			//System.out.println("DEBUG : breadthResearch : feuille ! "+ leafFound.getId());
			finalNode = leafFound;
		}else if(maxDepthNode != null){
			//System.out.println("DEBUG : breadthResearch : profondeur max atteinte ! "+ maxDepthNode.getId() +" "+ depthDict.get(maxDepthNode));
			ManageExplo managerExplo = new ManageExplo(type,this.myFosymaAgent);
			return managerExplo.solveProblemByDepth(paramNode, myCapacity);
		}else{
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("DEBUG : breadthResearch : pas de chemin objectif trouve !");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			//			path.add(paramNode);
			//			return path;
			// Je n'ai pas trouve de tresor ni de feuille et j'ai regarde tous les noeuds

			ManageExplo managerExplo = new ManageExplo(type,this.myFosymaAgent);
			return managerExplo.solveProblemByDepth(paramNode, myCapacity);
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

		// String str pour le debug
		String str = "";
		for(Node n : path){
			str += n.getId()+" ";
		}
		//////System.out.println("DEBUG : breadthResearch : chemin final : "+str);
		path.remove(0); // On enleve le noeud racine qui est le noeud actuel ou se trouve l'agent
		return path;
	}


	public ArrayList<Node> breadthResearch(Node paramNode,Node goal, ManageMap myKnowledge, int maxDepth, ArrayList<Node> visited){
		//System.out.println("DEBUT ALGO LARGEUR");
		//System.out.println(myKnowledge.getListKnownMap().get(0));
		//System.out.println(myKnowledge.getListKnownMap().get(0).getDicoFils());
		//////System.out.println("DEBUG : breadthResearch : ma capacite : "+ myCapacity);
		ArrayList<Node> path = new ArrayList<Node>();

		HashMap<String, Node> dicoPere = myKnowledge.getListKnownMap().get(0).getDicoPere();
		ArrayList<Node> nodeListToExplore = new ArrayList<Node>();
		HashSet<Node> explored = new HashSet<Node>();
		for(Node visi : visited)
		{
			explored.add(visi);
		}

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
			 * 
			 * WARNING : CONDITION NON TOTALEMENT VALIDE. ON PEUT RENTRER DANS CETTE CONDITION SI LE GRAPHE
			 * CONTIENT UN CYCLE AVEC QUE DES PERES, DONC DES NOEUDS QUI NE M'INTERESSE PAS,
			 * A CAUSE DU CYCLE, LES FILS DU NOEUD QUE JE REGARDE ONT DEJA ETE INTEGRES DANS LA LISTE DE NOEUDS
			 * A EXPLORER DONC EMPECHE D'ATTEINDRE LA PROFONDEUR MAX QUI EST AUSSI UN CAS D'ARRET
			 */
			if(nodeListToExplore.isEmpty()){
				//				//System.out.println("DEBUG : breadthResearch : tous les noeuds observe et pas de tresor interessant");
				break;				
			}

			Node node = nodeListToExplore.remove(0);
			explored.add(node);

			for (Node n : node.getFils()) {
				/*
				 * Profondeur maximale atteinte
				 */
				if(depthDict.get(node.getId()) == maxDepth){
					//System.out.println("#########################################################");
					//System.out.println("DEBUG : breadthResearch : profondeur maximale atteinte");
					//System.out.println("DEBUG : breadthResearch : affichage depthDict :");
					//System.out.println(depthDict);
					//System.out.println("#########################################################");
					whoIsYourDaddy.put(n.getId(),node);
					maxDepthNode = node;
					loop = false;
					break;
				}

				/*
				 * J'ai trouve un tresor qui m'interesse
				 */
				////System.out.println("je suis le noeud : "+n.getId());
				////System.out.println(n.getType()+" et "+this.type);
				////System.out.println(0 < n.getValue());
				////System.out.println(n.getValue() <= myCapacity);
				////System.out.println(treasureFound == null);
				if(n.getId().equals(goal.getId()) && treasureFound == null && myFosymaAgent.getMyKnowledge().getListKnownMap().get(0).getDicoFils().containsKey(n.getId()) && n.getType().equals(this.type) ){
					//////System.out.println("DEBUG : breadthResearch : j'ai trouve un tresor interessant");
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
					//////System.out.println("DEBUG : breadthResearch : j'ai trouve une feuille ("+n.getId()+")ou potentiellement aller");
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
			//System.out.println("DEBUG : breadthResearch : tresor ! "+ treasureFound.getId() +" "+ treasureFound.getValue());
			finalNode = treasureFound;
		}else if(leafFound != null){
			//System.out.println("DEBUG : breadthResearch : feuille ! "+ leafFound.getId());
			finalNode = leafFound;
		}else if(maxDepthNode != null){
			//System.out.println("DEBUG : breadthResearch : profondeur max atteinte ! "+ maxDepthNode.getId() +" "+ depthDict.get(maxDepthNode));
			other = true;
			ManageExplo managerExplo = new ManageExplo(type,this.myFosymaAgent);

			return managerExplo.solveProblemByDepth(paramNode, goal,visited);
		}else{
			//			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			//			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			//			//System.out.println("DEBUG : breadthResearch : pas de chemin objectif trouve !");
			//			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			//			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			//			path.add(paramNode);
			//			return path;
			// Je n'ai pas trouve de tresor ni de feuille et j'ai regarde tous les noeuds
			other = true;
			ManageExplo managerExplo = new ManageExplo(type,this.myFosymaAgent);
			return managerExplo.solveProblemByDepth(paramNode,goal,visited);
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

		// String str pour le debug
		String str = "";
		for(Node n : path){
			str += n.getId()+" ";
		}
		//////System.out.println("DEBUG : breadthResearch : chemin final : "+str);
		path.remove(0); // On enleve le noeud racine qui est le noeud actuel ou se trouve l'agent
		return path;
	}

	//	public ArrayList<Node> findBestPathToClosestLeaf(Node paramNode,ManageMap myKnowledge){
	//	ArrayList<Node> path = new ArrayList<Node>();
	//	/*
	//	 * On regarde si il existe encore des feuilles dans le graphe a explorer pour l'agent.
	//	 * Si il n'y en n'a plus, on renvoit un chemin vide
	//	 * 
	//	 */
	//	HashMap<String, Node> dicoFils = myKnowledge.getListKnownMap().get(0).getDicoFils();
	//	if(dicoFils.isEmpty()){
	//		return path;
	//	}
	//
	//	HashMap<String, Node> dicoPere = myKnowledge.getListKnownMap().get(0).getDicoPere();
	//	ArrayList<Node> nodeListToExplore = new ArrayList<Node>();
	//	HashSet<Node> explored = new HashSet<Node>();
	//	/*
	//	 * whoIsYourDaddy :
	//	 * cle : ID du noeud
	//	 * valeur : ID de son pere
	//	 * But : permet de recreer le chemin que l'agent va parcourir pour arriver
	//	 * au nouveau noeud a explorer
	//	 */
	//	HashMap<String, Node> whoIsYourDaddy = new HashMap<String, Node>();
	//
	//	nodeListToExplore.add(paramNode);
	//	whoIsYourDaddy.put(paramNode.getId(), null);
	//	Node leafFound = null;
	//
	//	boolean loop = true;
	//	while(loop){
	//		// Ne dois jamais arriver
	//		if(nodeListToExplore.isEmpty()){
	//			//////System.out.println("nodeListToExplore empty!");
	//			return path;
	//		}
	//
	//		Node node = nodeListToExplore.remove(0);
	//		explored.add(node);
	//
	//		for (Node n : node.getFils()) {
	//			//				//////System.out.println("fils : "+ n.getId());
	//			/*
	//			 * Si on ne trouve pas le noeud que l'on etudie dans la liste des noeuds peres,
	//			 * alors cas terminal : on a trouve une feuille, objectif atteint, on arrete la recherche 
	//			 */
	//			//				//////System.out.println("cmp : " + dicoPere.get(n.getId()));
	//			if (dicoPere.get(n.getId()) == null) {
	//				whoIsYourDaddy.put(n.getId(),node);
	//				leafFound = n;
	//				loop = false;
	//				break;
	//			}
	//
	//			/*
	//			 * Le noeud actuel n'est pas une feuille, donc si ce noeud n'est pas deja dans
	//			 * la liste des noeuds deja explore et qu'il n'est pas dans la liste des noeuds a explorer,
	//			 * alors on l'ajoute a la liste des noeuds a explorer
	//			 */
	//			if (!explored.contains(n) && !nodeListToExplore.contains(n)) {
	//				nodeListToExplore.add(n);
	//				whoIsYourDaddy.put(n.getId(),node);
	//			}
	//		}
	//	}
	//
	//	/*
	//	 * Reconstruction du chemin a partir du dico whoIsYourDaddy
	//	 */
	//	path.add(leafFound);
	//	Node nodeFather = whoIsYourDaddy.get(leafFound.getId());
	//
	//	while(nodeFather != null){
	//		path.add(0, nodeFather);
	//		nodeFather = whoIsYourDaddy.get(nodeFather.getId());
	//	}
	//	path.remove(0); // On enleve le noeud racine qui est le noeud actuel ou se trouve l'agent
	//	return path;
	//}

	public ArrayList<Node> getVisited() {
		return visited;
	}

	public void setVisited(ArrayList<Node> visited) {
		this.visited = visited;
	}

	public boolean isFind() {
		return find;
	}

	public void setFind(boolean find) {
		this.find = find;
	}

	public ArrayList<Node> getExplo() {
		return explo;
	}

	public void setExplo(ArrayList<Node> explo) {
		this.explo = explo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
}
