package data;

import java.util.ArrayList;

public class ManageExplo {


	public  ArrayList<Node> visited;
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
	private boolean find = true;
	private ArrayList<Node> explo;

	public ManageExplo(){ }

	private  void closeByWidth(ArrayList<Node> listNode, ArrayList<Node> path,Integer value) throws StopParcourException
	{


		ArrayList<Node> stockFils = new ArrayList<>();
		for(Node n : listNode)
		{
			if(n.getFils().isEmpty() && n.getValue() <= value)
			{
				path.add(n);
				throw new StopParcourException(path);
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
								throw new StopParcourException(path);
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

	private  void closeByDepth(Node n,ArrayList<Node> path, Integer value) throws StopParcourException
	{
		//System.out.println("je suis le Node : "+n);
		//System.out.println("JE VIENS DE  :"+path);
		//		if((n.getValue() <= value && n.getValue()>0) || n.getValue() ==-1)
		//		{
		//			if(n.getValue() ==-1 && find)
		//			{
		//				find = false;
		//				explo = (ArrayList<Node>) path.clone();
		//				explo.add(n);
		//			}else{
		//				//System.out.println("SOLUTION TROUVEE 1");
		//				path.add(n);
		//				throw new StopParcourException(path);
		//			}
		//		}
		for (Node fils : n.getFils())
		{
			boolean check = false;	
			if(fils.getId() != n.getId())
			{

				//	System.out.println("OK");

				for(Node alreadyVisited : visited)
				{

					if(fils.getId() == alreadyVisited.getId())
					{	
						//System.out.println("Noeud deja traite");
						check = true;
						break;
					}

				}
			}
			if(!check)
			{
				if((fils.getValue() <= value && fils.getValue()>0) || fils.getValue() ==-1)
				{

					if(fils.getValue() ==-1 && find)
					{
						find = false;
						explo = (ArrayList<Node>) path.clone();
						explo.add(fils);
					}else{
						//System.out.println("SOLUTION TROUVEE 2");
						//System.out.println("Je suis le Node FINAL :"+fils);
						path.add(fils);
						throw new StopParcourException(path);	
					}
				}
				visited.add(fils);
				ArrayList<Node> temp = (ArrayList<Node>) path.clone();
				temp.add(fils);
				closeByDepth(fils, temp, value);
			}
		}
	}


	private  void closeByDepth(Node n,ArrayList<Node> path, Node goal) throws StopParcourException
	{
		//System.out.println("je suis le Node : "+n);
		//System.out.println("JE VIENS DE  :"+path);
		//		if((n.getValue() <= value && n.getValue()>0) || n.getValue() ==-1)
		//		{
		//			if(n.getValue() ==-1 && find)
		//			{
		//				find = false;
		//				explo = (ArrayList<Node>) path.clone();
		//				explo.add(n);
		//			}else{
		//				//System.out.println("SOLUTION TROUVEE 1");
		//				path.add(n);
		//				throw new StopParcourException(path);
		//			}
		//		}
		for (Node fils : n.getFils())
		{
			boolean check = false;	
			if(fils.getId() != n.getId())
			{

				//	System.out.println("OK");

				for(Node alreadyVisited : visited)
				{

					if(fils.getId() == alreadyVisited.getId())
					{	
						//System.out.println("Noeud deja traite");
						check = true;
						break;
					}

				}
			}
			if(!check)
			{
				if(fils.getId().equals(goal.getId()))
				{


					//System.out.println("SOLUTION TROUVEE 1");
					//System.out.println("Je suis le Node FINAL :"+fils);
					path.add(fils);
					throw new StopParcourException(path);	
				}
			}
			visited.add(fils);
			ArrayList<Node> temp = (ArrayList<Node>) path.clone();
			temp.add(fils);
			closeByDepth(fils, temp, goal);
		}
	}
	private  void closeByDepth(Node n,ArrayList<Node> path) throws StopParcourException
	{
		//System.out.println("je suis le Node : "+n);
		//System.out.println("JE VIENS DE  :"+path);
		//		if((n.getValue() <= value && n.getValue()>0) || n.getValue() ==-1)
		//		{
		//			if(n.getValue() ==-1 && find)
		//			{
		//				find = false;
		//				explo = (ArrayList<Node>) path.clone();
		//				explo.add(n);
		//			}else{
		//				//System.out.println("SOLUTION TROUVEE 1");
		//				path.add(n);
		//				throw new StopParcourException(path);
		//			}
		//		}
		for (Node fils : n.getFils())
		{
			boolean check = false;	
			if(fils.getId() != n.getId())
			{

				//	System.out.println("OK");

				for(Node alreadyVisited : visited)
				{

					if(fils.getId() == alreadyVisited.getId())
					{	
						//System.out.println("Noeud deja traite");
						check = true;
						break;
					}

				}
			}
			if(!check)
			{
				//System.out.println("SOLUTION TROUVEE 1");
				//System.out.println("Je suis le Node FINAL :"+fils);
				path.add(fils);
				throw new StopParcourException(path);	

			}

		}
	}

	// renvoie un chemin menant a un node de valeur inferieure ou egale a value
	// par exploration en largeur
	public  ArrayList<Node> solveProblemByWidth(Node n, Integer value)
	{
		visited = new ArrayList<>();
		ArrayList<Node> path = new ArrayList<>();
		ArrayList<Node> node = new ArrayList<>();
		node.add(n);
		long t= System.currentTimeMillis();

		try {
			closeByWidth(node, path,value);
		} catch (StopParcourException e) { path=e.getFin();}

		//System.out.println("temps de traitement en ms : "+Double.toString(System.currentTimeMillis()-t));
		return path;
	}
	// renvoie un chemin menant a un node de valeur inferieure ou egale a value et sup stric a 0
	// par exploration en profondeur
	public  ArrayList<Node> solveProblemByDepth(Node n, Integer value)
	{
		visited = new ArrayList<>();
		explo = new ArrayList<Node>();
		ArrayList<Node> path = new ArrayList<>();
		long t= System.currentTimeMillis();
		try {
			closeByDepth(n, path,value);
		} catch (StopParcourException e) { path=e.getFin();}

		//System.out.println("temps de traitement en ms : "+Double.toString(System.currentTimeMillis()-t));
		if(path.isEmpty())
		{
			if (explo.isEmpty()) 
			{

				System.out.println("je n'ai plus rien a explorer ou aucun tresor trouvé");
				
				return path;
			}else{
				return explo;
			}
		}else{
			return path;
		}
	}
	// meme qu'avant avec ajout de noeud conflit et on cherche un noeud but à la place d'une valeur
	public  ArrayList<Node> solveProblemByDepth(Node n, Node Goal,Node conflit)
	{
		visited = new ArrayList<>();
		explo = new ArrayList<Node>();
		visited.add(conflit);
		ArrayList<Node> path = new ArrayList<>();
		long t= System.currentTimeMillis();
		try {
			closeByDepth(n, path,Goal);
		} catch (StopParcourException e) { path=e.getFin();}

		//System.out.println("temps de traitement en ms : "+Double.toString(System.currentTimeMillis()-t));
		if(path.isEmpty())
		{
			return explo;
		}else{
			return path;
		}
	}
	//renvoie le premier noeud qui n'est pas un noeud de conflit
	public  ArrayList<Node> solveProblemByDepth(Node n,ArrayList<Node> conflit)
	{
		visited = new ArrayList<>();
		explo = new ArrayList<Node>();
		for(Node nc : conflit)
		{
			visited.add(nc);
		}
		ArrayList<Node> path = new ArrayList<>();
		long t= System.currentTimeMillis();
		try {
			closeByDepth(n, path);
		} catch (StopParcourException e) { path=e.getFin();}

		//System.out.println("temps de traitement en ms : "+Double.toString(System.currentTimeMillis()-t));
		if(path.isEmpty())
		{
			return explo;
		}else{
			return path;
		}
	}

}
