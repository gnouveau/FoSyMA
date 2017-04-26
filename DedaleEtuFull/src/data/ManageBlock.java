package data;

import java.util.ArrayList;

import env.Couple;

public class ManageBlock {




	private ArrayList<Couple<String,ArrayList<Node>>> goalAgents = new ArrayList<>();
	private ArrayList<Node> myGoal = new ArrayList<>();
	private Goal myObjectGoal;

	private ArrayList<Couple<String,ArrayList<Node>>> conflictNode = new ArrayList<>();
	private ArrayList<Couple<String,Integer>> listIndiceConflits = new ArrayList<>();

	private ManageExplo managerExplo;
	private ArrayList<Node> finalGoal = new ArrayList<>();
	
	
	private Priorite priorite;
	private ArrayList<Couple<String,Goal>> listGoalAgents = new ArrayList<>();

	private Integer mycapacity;
	private ArrayList<Couple<String,Integer>> listCapcityAgents = new ArrayList<>();


	public ManageBlock(ArrayList<Node> listgoal,Integer capacity,Goal goal, Goal mygoalinit)
	{
		myObjectGoal = mygoalinit;
		myGoal = myObjectGoal.getGoalPath();
		mycapacity = capacity;
		priorite = mygoalinit.getPriorite();
		Couple<String, ArrayList<Node>> c = new Couple<String, ArrayList<Node>>(goal.getNameAgt(), goal.getGoalPath());
		goalAgents.add(c);

		Couple<String,Integer> c1 = new Couple<String, Integer>(goal.getNameAgt(), goal.getMyCapacity());
		listCapcityAgents.add(c1);
	}

	public void addGoalAgent(Goal goal)
	{
		Couple<String, ArrayList<Node>> c = new Couple<String, ArrayList<Node>>(goal.getNameAgt(), goal.getGoalPath());
		goalAgents.add(c);

		Couple<String,Integer> c1 = new Couple<String, Integer>(goal.getNameAgt(), goal.getMyCapacity());
		listCapcityAgents.add(c1);
	}

	public ArrayList<Node> solveBlock()
	{
		
		
		managerExplo = new ManageExplo(myObjectGoal.getMyType());
		String s="/!\\ /!\\ /!\\ /!\\ /!\\ SYSOUT SOLVE BLOCK /!\\ /!\\ /!\\ /!\\ /!\\ \n Mon but est : ";
		for(Node n : myGoal)
		{
			s += n.getId()+" ";
		}
		System.out.println(s);
		s="";
		for(Node n : goalAgents.get(0).getRight())
		{
			s += n.getId()+" ";
		}
		System.out.println("Le but de l'autre agent est : "+s);
		
		this.detectSameGoal();
		System.out.println("apres detection du meme but on reaffiche les goals choisis");
		
		s="";
		for(Node n : myGoal)
		{
			s += n.getId()+" ";
		}
		System.out.println("Mon but est : "+ s);
		s="";
		for(Node n : goalAgents.get(0).getRight())
		{
			s += n.getId()+" ";
		}
		System.out.println("Le but de l'autre agent est : "+s);
		
		
		this.detectConflictNode();
		
		
		if(!conflictNode.isEmpty()){
			this.dodgeOrNotDodge();
		}else{
			finalGoal = myGoal;
		}
		s="";
		for(Node n : finalGoal)
		{
			s += n.getId()+" ";
		}
		System.out.println("Mon but est final est  : "+ s);
		System.out.println("/!\\ /!\\ /!\\ /!\\ /!\\ SYSOUT SOLVE BLOCK /!\\ /!\\ /!\\ /!\\ /!\\");
		return finalGoal;
	}

	private void detectConflictNode()
	{
		boolean sortie = false;
		for(Couple<String, ArrayList<Node>> c : goalAgents)
		{
			int i;
			ArrayList<Node> conflict = new ArrayList<>();
			for(i = 0; i < myGoal.size() - 1 && i< c.getRight().size() - 1; i++ )
			{				
				Node nodeG1 = myGoal.get(i);
				Node nodeG2 = c.getRight().get(i);

				String name1 = nodeG1.getId();
				String name2 = nodeG2.getId();

				Node nodeG11 = myGoal.get(i+1);
				Node nodeG22 = c.getRight().get(i+1);

				String name11 = nodeG11.getId();
				String name22 = nodeG22.getId();

				if(name1.equals(name11))
				{
					conflict.add(nodeG1);
					sortie = true;
					break;
				}
				if(name1.equals(name22) && name11.equals(name2))
				{
					conflict.add(nodeG1);
					sortie = true;
					break;
				}
				if(i > 4) {break;}
			}
			if(i<=4 && sortie)
			{
				System.out.println("#######################################################");
				System.out.println("NOEUD CONFLIT DETECTE : "+ conflict + " en indice : "+i);
				System.out.println("#######################################################");
				conflictNode.add(new Couple<String, ArrayList<Node>>(c.getLeft(), conflict));
				listIndiceConflits.add( new Couple<String, Integer>(c.getLeft(), i));
			}
		}
	}
	// ne marche que pour 2 agents
	private void dodgeOrNotDodge()
	{
		managerExplo = new ManageExplo(myObjectGoal.getMyType());
		ArrayList<Node> pathGoalag1 = this.myGoal;
		ArrayList<Node> pathGoalag2 = goalAgents.get(0).getRight();

		Node conflict = conflictNode.get(0).getRight().get(0);
		int indiceConflict =0;
		
		for(Couple<String, Integer> c : listIndiceConflits)
		{
			if(c.getLeft().equals(conflictNode.get(0).getLeft()))
			{
				indiceConflict = Math.max(indiceConflict, c.getRight());
			}
		}

		ArrayList<Node> pathAg1DodgeAg2 = managerExplo.solveProblemByDepth(pathGoalag1.get(0), pathGoalag1.get(pathGoalag1.size()-1), conflictNode.get(0).getRight());

		ArrayList<Node> pathAg2DodgeAg1 = managerExplo.solveProblemByDepth(pathGoalag2.get(0), pathGoalag2.get(pathGoalag2.size()-1), conflictNode.get(0).getRight());
		
		int sizePathDodgeAg1=9999999;
		int sizePathDodgeAg2=9999999;

		if(!pathAg1DodgeAg2.isEmpty())
		{
			sizePathDodgeAg1=pathAg1DodgeAg2.size();
		}
		if(!pathAg2DodgeAg1.isEmpty())
		{
			sizePathDodgeAg2=pathAg2DodgeAg1.size();
		}

		//si les deux ne peuvent pas s'esquiver celui avec le chemin le plus court laisse la place a celui qui a le chemin le plus long
		// car celui avec le chemin le plus long veut partir de la zone de conflit
		if (sizePathDodgeAg1 ==  9999999 && sizePathDodgeAg2 == 9999999)
		{
			if(pathGoalag1.size() < pathGoalag2.size())
			{
				//on recupere les noeuds allant de la position de début de l'agent au chemin le plus long jusqu'au noeud conflit
				ArrayList<Node> forbid = new ArrayList<>();
				for(int i=0; i<indiceConflict;i++)
				{
					forbid.add(pathGoalag2.get(i));
				}
				finalGoal = managerExplo.solveProblemByDepth(pathGoalag1.get(0), forbid);
				//on attend a l'emplacement trouvé le temps que l'autre agent soit passer par le noeud conflit
				if(finalGoal.size() < indiceConflict)
				{
					Node wait = finalGoal.get(finalGoal.size()-1);
					for(int i = (finalGoal.size()-1); i < indiceConflict + 1 ; i ++)
					{
						finalGoal.add(wait);
					}
				}
			}else{
				finalGoal = pathGoalag1;
			}
		}

		// si la taille de leur 2 chemins sont égales alors je donne une priorité a un chemin en fonction de la valeur en ID du noeud de départ
		if ((pathGoalag1.size() + sizePathDodgeAg2) ==  (sizePathDodgeAg1 + pathGoalag2.size()))
		{
			if( Integer.valueOf(pathGoalag1.get(0).getId()) > Integer.valueOf(pathGoalag2.get(0).getId()))
			{
				this.finalGoal=pathGoalag1;
			}else{
				this.finalGoal=pathAg1DodgeAg2;
			}
		}else{

			//si la taille du chemin de ag1 et le chemin de ag2 qui esquive ag1 est plus petite que la taille du chemin de ag2 qui esquive ag1
			if(  (pathGoalag1.size() + sizePathDodgeAg2) <  (sizePathDodgeAg1 + pathGoalag2.size()) )
			{
				this.finalGoal=pathGoalag1;
			}else{
				this.finalGoal=pathAg1DodgeAg2;
			}
		}
	}

	// ne marche que pour 2 agents
	private void detectSameGoal()
	{
		
		Node mygoal = myGoal.get(myGoal.size()-1);	
		Node ag2goal = goalAgents.get(0).getRight().get(goalAgents.get(0).getRight().size()-1);

		//si ils ont le meme but
		System.out.println("On test si on a le meme but");
		if (mygoal.getId().equals(ag2goal.getId()))
		{
			Integer valueTresor = mygoal.getValue();
			// si ag1 a la capacité parfaite
			System.out.println("On a le meme but \n On test si ag1 a la capacité parfaite");
			if(valueTresor == mycapacity)
			{
				//si ag2 aussi
				System.out.println("On test si ag2 a la capacité parfaite aussi");
				if(valueTresor == listCapcityAgents.get(0).getRight())
				{
					// si la taille de leur 2 chemins sont égales alors je donne une priorité a un chemin en fonction de la valeur en ID du noeud de départ 
					System.out.println("on teste si ils ont la meme taille de chemin");
					if(myGoal.size() == goalAgents.get(0).getRight().size())
					{
						if( Integer.valueOf(myGoal.get(0).getId()) > Integer.valueOf(goalAgents.get(0).getRight().get(0).getId()))
						{
							System.out.println("Ag 2 change de chemin malgres égalité");
							// on ajoute le noeud goal on noeud conflits de manaExplo
							ArrayList<Node>temp = new ArrayList<>();
							temp.add(mygoal);
							managerExplo.setVisited(temp);
							managerExplo.setType(ag2goal.getType());
							//on fait un nouveau chemin pour ag2
							goalAgents.set(0, new Couple<String, ArrayList<Node>>(goalAgents.get(0).getLeft(), managerExplo.solveProblemByDepth(goalAgents.get(0).getRight().get(0),listCapcityAgents.get(0).getRight())));

						}else{
							//sinon c'est le goal de l'agent 1 qui change
							System.out.println("Ag 1 change de chemin malgres égalité");
							ArrayList<Node>temp = new ArrayList<>();
							temp.add(mygoal);
							managerExplo.setVisited(temp);
							myGoal = managerExplo.solveProblemByDepth(myGoal.get(0), mycapacity);
						}
					}else{

						//si ag1 va plus vite vers sont but , ou si ag2 va plus vite vers sont but
						System.out.println("on regarde qui a le chemin le plus court entre les deux");
						if(myGoal.size() < goalAgents.get(0).getRight().size())
						{
							System.out.println("Ag 2 change de chemin");
							// on ajoute le noeud goal on noeud conflits de manaExplo
							ArrayList<Node>temp = new ArrayList<>();
							temp.add(mygoal);
							managerExplo.setVisited(temp);
							managerExplo.setType(ag2goal.getType());
							//on fait un nouveau chemin pour ag2
							goalAgents.set(0, new Couple<String, ArrayList<Node>>(goalAgents.get(0).getLeft(), managerExplo.solveProblemByDepth(goalAgents.get(0).getRight().get(0),listCapcityAgents.get(0).getRight())));
						}else{
							System.out.println("Ag 1 change de chemin");
							//sinon c'est le goal de l'agent 1 qui change
							ArrayList<Node>temp = new ArrayList<>();
							temp.add(mygoal);
							managerExplo.setVisited(temp);
							myGoal = managerExplo.solveProblemByDepth(myGoal.get(0), mycapacity);
						}
					}
				}else{
					//sinon c'est l'ag2 qui doit changer
					System.out.println("Ag 2 change de chemin car ag1 a une meilleur capacité");
					// on ajoute le noeud goal on noeud conflits de manaExplo
					ArrayList<Node>temp = new ArrayList<>();
					temp.add(mygoal);
					managerExplo.setVisited(temp);
					managerExplo.setType(ag2goal.getType());
					//on fait un nouveau chemin pour ag2
					goalAgents.set(0, new Couple<String, ArrayList<Node>>(goalAgents.get(0).getLeft(), managerExplo.solveProblemByDepth(goalAgents.get(0).getRight().get(0),listCapcityAgents.get(0).getRight())));
				}
			}else{
				//sinon je regarde si c'est l'agent 2 qui a une capacité égale
				//si oui c'est l'ag1 qui doit changer
				System.out.println("on regarde si l'agent 2 à une capacité parfaite");
				if(valueTresor == listCapcityAgents.get(0).getRight())
				{
					ArrayList<Node>temp = new ArrayList<>();
					temp.add(mygoal);
					managerExplo.setVisited(temp);
					myGoal = managerExplo.solveProblemByDepth(myGoal.get(0), mycapacity);
				}
			}
			System.out.println("on regarde si l'agent 1 à un meilleur capa que ag 2");
			if(mycapacity >listCapcityAgents.get(0).getRight())
			{
				//c'est a l'agt2 de changer de but
				// on ajoute le noeud goal on noeud conflits de manaExplo
				System.out.println("c'est a l'agt2 de changer de but");
				System.out.println("le but a eviter est :");
				ArrayList<Node>temp = new ArrayList<>();
				temp.add(mygoal);
				System.out.println("le but a eviter est :"+ temp);
				managerExplo.setVisited(temp);
				managerExplo.setType(ag2goal.getType());
				//on fait un nouveau chemin pour ag2
				ArrayList<Node> newPath = managerExplo.solveProblemByDepth(goalAgents.get(0).getRight().get(0),listCapcityAgents.get(0).getRight());
				
				String s="";
				for(Node n : newPath)
				{
					s += n.getId()+" ";
				}
				System.out.println("le nouveau but est: "+s);
				System.out.println("fin de sysout dans sameGoald");
				Couple<String, ArrayList<Node>> c = new Couple<String, ArrayList<Node>>(goalAgents.get(0).getLeft(), newPath);
				goalAgents.set(0 , c);
			}else{
				//sinon c'est a l'agt1 de changer de but
				System.out.println("c'est a l'agt1 de changer de but");
				ArrayList<Node>temp = new ArrayList<>();
				temp.add(mygoal);
				managerExplo.setVisited(temp);
				myGoal = managerExplo.solveProblemByDepth(myGoal.get(0), mycapacity);
			}
		}
	}
}
