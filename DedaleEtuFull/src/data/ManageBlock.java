package data;

import java.util.ArrayList;

import env.Couple;
import mas.agents.FosymaAgent;

public class ManageBlock {




	private ArrayList<Couple<String,ArrayList<Node>>> goalAgents = new ArrayList<>();
	private ArrayList<Node> myGoal = new ArrayList<>();
	private Goal myObjectGoal;
	private Goal otherObjectGoal;
	private ArrayList<Couple<String,ArrayList<Node>>> conflictNode = new ArrayList<>();
	private ArrayList<Couple<String,Integer>> listIndiceConflits = new ArrayList<>();

	private ManageExplo managerExplo;
	private ArrayList<Node> finalGoal = new ArrayList<>();

	private Node myCurrentPos;
	private Priorite priorite;
	private Priorite finalPriorite;
	private ArrayList<Couple<String,Goal>> listGoalAgents = new ArrayList<>();

	private Integer myCapacity;
	private ArrayList<Couple<String,Integer>> listCapcityAgents = new ArrayList<>();
	private FosymaAgent myFosymaAgent;

	public ManageBlock(Goal myg,Goal otherGoal,FosymaAgent myFosymaAgent)
	{
		this.myFosymaAgent = myFosymaAgent;
		myObjectGoal = myg;
		myGoal = myg.getGoalPath();
		myCapacity = myg.getMyCapacity();
		priorite = myg.getPriorite();
		myCurrentPos = myg.getCurrentPos();
		Couple<String, ArrayList<Node>> c = new Couple<String, ArrayList<Node>>(otherGoal.getNameAgt(), otherGoal.getGoalPath());
		goalAgents.add(c);
		otherObjectGoal = otherGoal;
		Couple<String,Integer> c1 = new Couple<String, Integer>(otherGoal.getNameAgt(), otherGoal.getMyCapacity());
		listCapcityAgents.add(c1);

		Couple<String, Goal> c2 = new Couple<String, Goal>(otherGoal.getNameAgt(), otherGoal);
		listGoalAgents.add(c2);
		
		finalPriorite = priorite;
	}



	public Goal solveBlock()
	{


		managerExplo = new ManageExplo(myObjectGoal.getMyType(),myFosymaAgent);
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
		System.out.println(finalGoal.get(0)+""+finalGoal.get(0));
		System.out.println("je suis de priorité : "+this.finalPriorite);
		System.out.println("/!\\ /!\\ /!\\ /!\\ /!\\ SYSOUT SOLVE BLOCK /!\\ /!\\ /!\\ /!\\ /!\\");
		Goal g = new Goal(myObjectGoal.getNameAgt(), finalGoal, myCapacity, myObjectGoal.getMyType(),myObjectGoal.getCurrentPos(),finalPriorite);
		return g;
	}

	private void detectConflictNode()
	{

		boolean sortie = false;
		for(Couple<String, ArrayList<Node>> c : goalAgents)
		{
			ArrayList<Node> tmpag1 = (ArrayList<Node>) myGoal.clone();
			ArrayList<Node> tmpag2 = (ArrayList<Node>) c.getRight().clone();
			tmpag1.add(0, myCurrentPos);
			tmpag2.add(0,otherObjectGoal.getCurrentPos());
			System.out.println("detection conflit");
			int i=0;
			ArrayList<Node> conflict = new ArrayList<>();

			Node nodeG1 = tmpag1.get(0);
			Node nodeG2 = tmpag2.get(0);

			String name1 = nodeG1.getId();
			String name2 = nodeG2.getId();
			//g2 n'est pas le noeud courant de ag1
			//g1 n'est pas le noeud courant de 


			//			System.out.println("noeud courant ag1 "+myObjectGoal.getCurrentPos());
			//			System.out.println("1er noeud ag2"+nodeG2);
			//			if(myObjectGoal.getCurrentPos().getId().equals(name2))
			//			{
			//				System.out.println("noeud courant ag1 "+myObjectGoal.getCurrentPos());
			//				System.out.println("1er noeud ag2"+nodeG2);
			//				conflict.add(nodeG1);
			//				sortie = true;
			//			
			//			}
			//			System.out.println("noeud courant ag2 "+otherObjectGoal.getCurrentPos());
			//			System.out.println("1er noeud ag1"+nodeG1);
			//			if(otherObjectGoal.getCurrentPos().getId().equals(name1))
			//			{
			//				System.out.println("noeud courant ag2 "+otherObjectGoal.getCurrentPos());
			//				System.out.println("1er noeud ag1"+nodeG1);
			//				conflict.add(nodeG2);
			//				sortie = true;
			//				
			//			}

			if(name1.equals(name2))
			{
				System.out.println("conflit premier noeud");
				conflict.add(nodeG2);
				sortie = true;
			}
			for(i = 0; i < tmpag1.size() - 1 && i< tmpag2.size() - 1; i++ )
			{				
				nodeG1 = tmpag1.get(i);
				nodeG2 = tmpag2.get(i);

				name1 = nodeG1.getId();
				name2 = nodeG2.getId();

				Node nodeG11 = tmpag1.get(i+1);
				Node nodeG22 = tmpag2.get(i+1);

				String name11 = nodeG11.getId();
				String name22 = nodeG22.getId();

				if(name1.equals(name2))
				{
					conflict.add(nodeG1);
					sortie = true;
					break;
				}
				if(name1.equals(name22) && name11.equals(name2))
				{
					conflict.add(nodeG1);
					conflict.add(nodeG2);
					sortie = true;
					break;
				}
				if(name22.equals(name11))
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
				break;
			}
		}
	}
	// ne marche que pour 2 agents
	private void dodgeOrNotDodge()
	{
		managerExplo = new ManageExplo(myObjectGoal.getMyType(),myFosymaAgent);
		ArrayList<Node> pathGoalag1 = this.myGoal;
		ArrayList<Node> pathGoalag2 = goalAgents.get(0).getRight();

		Priorite prioag1 = priorite;
		Priorite prioag2 = listGoalAgents.get(0).getRight().getPriorite();

		Node conflict = conflictNode.get(0).getRight().get(0);
		int indiceConflict =0;

		for(Couple<String, Integer> c : listIndiceConflits)
		{
			if(c.getLeft().equals(conflictNode.get(0).getLeft()))
			{
				indiceConflict = Math.max(indiceConflict, c.getRight());
			}
		}
		int i=indiceConflict ;
		for( i=indiceConflict; i<pathGoalag1.size() -1 && i<indiceConflict+4;i++)
		{	
		}
		int j=indiceConflict ;
		for(j=indiceConflict ; j<pathGoalag2.size()-1 && j<indiceConflict+4;j++)
		{
		}
		System.out.println("on cherche à dodge");
		ArrayList<Node> pathAg1DodgeAg2 = managerExplo.solveProblemByDepth(this.myCurrentPos,pathGoalag1.get(i), conflictNode.get(0).getRight());
		System.out.println("pathAg1DodgeAg2 :"+pathAg1DodgeAg2);
		managerExplo = new ManageExplo(otherObjectGoal.getMyType(), myFosymaAgent);

		ArrayList<Node> pathAg2DodgeAg1 = managerExplo.solveProblemByDepth(this.otherObjectGoal.getCurrentPos(), pathGoalag2.get(j), conflictNode.get(0).getRight());



		System.out.println("pathAg2DodgeAg1 :"+pathAg2DodgeAg1);
		
		int sizePathDodgeAg1=9999999;
		int sizePathDodgeAg2=9999999;
		if(myObjectGoal.getPriorite().value < otherObjectGoal.getPriorite().value)
		{
			pathAg2DodgeAg1 = new ArrayList<>();
		}
		if(myObjectGoal.getPriorite().value > otherObjectGoal.getPriorite().value)
		{
			pathAg1DodgeAg2 = new ArrayList<>();
		}
		if(!pathAg1DodgeAg2.isEmpty())
		{
			sizePathDodgeAg1=pathAg1DodgeAg2.size();
		}
		if(!pathAg2DodgeAg1.isEmpty())
		{
			sizePathDodgeAg2=pathAg2DodgeAg1.size();
		}
		System.out.println("sizePathDodgeAg1 : "+sizePathDodgeAg1);
		System.out.println("sizePathDodgeAg2 : "+sizePathDodgeAg2);
		//si les deux ne peuvent pas s'esquiver celui avec le chemin le plus court laisse la place a celui qui a le chemin le plus long
		// car celui avec le chemin le plus long veut partir de la zone de conflit
		if (sizePathDodgeAg1 ==  9999999 && sizePathDodgeAg2 == 9999999)
		{

			if(pathGoalag1.size() == pathGoalag2.size())
			{
				if( Integer.valueOf(pathGoalag1.get(0).getId()) > Integer.valueOf(pathGoalag2.get(0).getId()))
				{
					this.finalGoal=pathGoalag1;
					
				
				}else{
					this.finalGoal=pathAg1DodgeAg2;
					this.finalPriorite=Priorite.PRIORITAIRE;
				}

			}else{
				if(pathGoalag1.size() < pathGoalag2.size())
				{
					//on recupere les noeuds allant de la position de début de l'agent au chemin le plus long jusqu'au noeud conflit
					ArrayList<Node> forbid = new ArrayList<>();
					for( i=0; i<indiceConflict;i++)
					{
						forbid.add(pathGoalag2.get(i));
					}
					finalGoal = managerExplo.solveProblemByDepth(this.myCurrentPos, forbid);
					//on attend a l'emplacement trouvé le temps que l'autre agent soit passer par le noeud conflit
					if(finalGoal.size() < indiceConflict)
					{
						Node wait = finalGoal.get(finalGoal.size()-1);
						for( i = (finalGoal.size()-1); i < indiceConflict + 1 ; i ++)
						{
							finalGoal.add(wait);
						}
					}
					this.finalPriorite=Priorite.ATTENTE;
				}else{
					finalGoal = pathGoalag1;
				}
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
				this.finalPriorite=Priorite.PRIORITAIRE;
			}
		}else{

			//si la taille du chemin de ag1 et le chemin de ag2 qui esquive ag1 est plus petite que la taille du chemin de ag2 qui esquive ag1
			if(  (pathGoalag1.size() + sizePathDodgeAg2) <  (sizePathDodgeAg1 + pathGoalag2.size()) )
			{
				this.finalGoal=pathGoalag1;
			}else{
				this.finalGoal=pathAg1DodgeAg2;
				this.finalPriorite=Priorite.PRIORITAIRE;
			}
		}
	}

	// ne marche que pour 2 agents
	private void detectSameGoal()
	{
		
		Node mygoal = myGoal.get(myGoal.size()-1);	
		Node ag2goal = goalAgents.get(0).getRight().get(goalAgents.get(0).getRight().size()-1);

		
		if(myObjectGoal.getPriorite().value < otherObjectGoal.getPriorite().value)
		{
			//sinon c'est a l'agt1 de changer de but
			System.out.println("c'est a l'agt1 de changer de but car non  prio");
			ArrayList<Node>temp = new ArrayList<>();
			temp.add(mygoal);
			managerExplo.setVisited(temp);
			myGoal = managerExplo.solveProblemByDepth(this.myCurrentPos, myCapacity);
			return;
		}
		if(myObjectGoal.getPriorite().value > otherObjectGoal.getPriorite().value)
		{
			//c'est a l'agt2 de changer de but
			// on ajoute le noeud goal on noeud conflits de manaExplo
			System.out.println("c'est a l'agt2 de changer de but car non prio");
			ArrayList<Node>temp = new ArrayList<>();
			temp.add(mygoal);
			System.out.println("le but a eviter est :"+ temp);
			managerExplo.setVisited(temp);
			managerExplo.setType(ag2goal.getType());
			//on fait un nouveau chemin pour ag2
			ArrayList<Node> newPath = managerExplo.solveProblemByDepth(otherObjectGoal.getCurrentPos(),listCapcityAgents.get(0).getRight());

			String s="";
			for(Node n : newPath)
			{
				s += n.getId()+" ";
			}
			System.out.println("le nouveau but est: "+s);
			System.out.println("fin de sysout dans sameGoal");
			Couple<String, ArrayList<Node>> c = new Couple<String, ArrayList<Node>>(goalAgents.get(0).getLeft(), newPath);
			goalAgents.set(0 , c);
			return;
		}
		//prio égale donc on resout
		
		//si ils ont le meme but
		System.out.println("On test si on a le meme but");
		if (mygoal.getId().equals(ag2goal.getId()))
		{
			Integer valueTresor = mygoal.getValue();
			// si ag1 a la capacité parfaite
			System.out.println("On a le meme but \n On test si ag1 a la capacité parfaite");
			System.out.println(valueTresor +" pour capa ag1 : "+myCapacity );
			System.out.println(valueTresor +" pour capa ag2 : "+listCapcityAgents.get(0).getRight() );
			if(valueTresor == myCapacity)
			{
				//si ag2 aussi
				System.out.println("On test si ag2 a la capacité parfaite aussi");
				if(valueTresor == listCapcityAgents.get(0).getRight())
				{
					// si la taille de leur 2 chemins sont égales alors je donne une priorité a un chemin en fonction de la valeur en ID du noeud de départ 
					System.out.println("on teste si ils ont la meme taille de chemin");
					if(myGoal.size() == goalAgents.get(0).getRight().size())
					{
						if( Integer.valueOf(this.myCurrentPos.getId()) > Integer.valueOf(otherObjectGoal.getCurrentPos().getId()))
						{
							System.out.println("Ag 2 change de chemin malgres égalité");
							// on ajoute le noeud goal on noeud conflits de manaExplo
							ArrayList<Node>temp = new ArrayList<>();
							temp.add(mygoal);
							managerExplo.setVisited(temp);
							managerExplo.setType(ag2goal.getType());
							//on fait un nouveau chemin pour ag2
							goalAgents.set(0, new Couple<String, ArrayList<Node>>(goalAgents.get(0).getLeft(), managerExplo.solveProblemByDepth(otherObjectGoal.getCurrentPos(),listCapcityAgents.get(0).getRight())));

						}else{
							//sinon c'est le goal de l'agent 1 qui change
							System.out.println("Ag 1 change de chemin malgres égalité");
							ArrayList<Node>temp = new ArrayList<>();
							temp.add(mygoal);
							managerExplo.setVisited(temp);
							myGoal = managerExplo.solveProblemByDepth(this.myCurrentPos, myCapacity);
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
							goalAgents.set(0, new Couple<String, ArrayList<Node>>(goalAgents.get(0).getLeft(), managerExplo.solveProblemByDepth(otherObjectGoal.getCurrentPos(),listCapcityAgents.get(0).getRight())));
						}else{
							System.out.println("Ag 1 change de chemin");
							//sinon c'est le goal de l'agent 1 qui change
							ArrayList<Node>temp = new ArrayList<>();
							temp.add(mygoal);
							managerExplo.setVisited(temp);
							myGoal = managerExplo.solveProblemByDepth(this.myCurrentPos, myCapacity);
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
					goalAgents.set(0, new Couple<String, ArrayList<Node>>(goalAgents.get(0).getLeft(), managerExplo.solveProblemByDepth(otherObjectGoal.getCurrentPos(),listCapcityAgents.get(0).getRight())));
				}
			}else if(valueTresor == listCapcityAgents.get(0).getRight())
			{
				//sinon je regarde si c'est l'agent 2 qui a une capacité égale
				//si oui c'est l'ag1 qui doit changer
				System.out.println("on regarde si l'agent 2 à une capacité parfaite");
				System.out.println(valueTresor +" pour capa ag2 : "+listCapcityAgents.get(0).getRight() );
				ArrayList<Node>temp = new ArrayList<>();
				temp.add(mygoal);
				managerExplo.setVisited(temp);
				myGoal = managerExplo.solveProblemByDepth(this.myCurrentPos, myCapacity);

			}else if (myCapacity >listCapcityAgents.get(0).getRight())
			{
				System.out.println("on regarde si l'agent 1 à un meilleur capa que ag 2");
				System.out.println(valueTresor +" pour capa ag2 : "+listCapcityAgents.get(0).getRight() );
				if(valueTresor ==listCapcityAgents.get(0).getRight())
				{
					System.out.println("LE bordel");
					System.exit(0);
				}
				
				//c'est a l'agt2 de changer de but
				// on ajoute le noeud goal on noeud conflits de manaExplo
				System.out.println("c'est a l'agt2 de changer de but");
				ArrayList<Node>temp = new ArrayList<>();
				temp.add(mygoal);
				System.out.println("le but a eviter est :"+ temp);
				managerExplo.setVisited(temp);
				managerExplo.setType(ag2goal.getType());
				//on fait un nouveau chemin pour ag2
				System.out.println("pos current agent 2 : "+ otherObjectGoal.getCurrentPos());
				ArrayList<Node> newPath = managerExplo.solveProblemByDepth(otherObjectGoal.getCurrentPos(),listCapcityAgents.get(0).getRight());

				String s="";
				for(Node n : newPath)
				{
					s += n.getId()+" ";
				}
				System.out.println("le nouveau but est: "+s);
				System.out.println("fin de sysout dans sameGoal");
				Couple<String, ArrayList<Node>> c = new Couple<String, ArrayList<Node>>(goalAgents.get(0).getLeft(), newPath);
				goalAgents.set(0 , c);
			}else{
				//sinon c'est a l'agt1 de changer de but
				System.out.println("c'est a l'agt1 de changer de but");
				ArrayList<Node>temp = new ArrayList<>();
				temp.add(mygoal);
				managerExplo.setVisited(temp);
				System.out.println("pos current agent 2 : "+ this.myCurrentPos);
				myGoal = managerExplo.solveProblemByDepth(this.myCurrentPos, myCapacity);
			}
		}
	}
}
