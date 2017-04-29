package data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import env.Attribute;
import env.Couple;

public class ManageMap {


	private ArrayList<String> listAgentShare = new ArrayList<>();
	private ArrayList<KnownMap> listKnownMap = new ArrayList<>();



	public ManageMap(String ag)
	{
		listAgentShare.add(ag);
		listKnownMap.add(new KnownMap());

	}
	public ManageMap(String ag, KnownMap map)
	{
		listAgentShare.add(ag);
		listKnownMap.add(map);

	}
	public void addAgentShare(String ag)
	{
		listAgentShare.add(ag);
		listKnownMap.add(new KnownMap());
	}

	public KnownMap sendMap(String ag)
	{
		//on choisi la bonne map a envoyer
		int indmap = listAgentShare.indexOf(ag);
		if (indmap == -1)
		{
			indmap = 0;
			listAgentShare.add(ag);
			listKnownMap.add(new KnownMap());
		}
		return listKnownMap.get(indmap);

	}

	public void AckMap(String ag)
	{
		//on choisi la bonne map a envoyer
		int indmap = listAgentShare.indexOf(ag);
		if (indmap != -1)
		{
			listKnownMap.remove(indmap);
		}


	}
	public void mergeMap(KnownMap map)
	{
		KnownMap mergedMap = new KnownMap();
		mergedMap.setDicoPere(listKnownMap.get(0).getDicoPere());
		System.out.println("on ajoute a la nouvelleMap les noeuds differents");
		for(Map.Entry<String, Node> entry : map.getDicoPere().entrySet())
		{
			String id1 = entry.getKey();
			Node n1 = entry.getValue();
			if(!listKnownMap.get(0).getDicoPere().containsKey(id1))
			{
				mergedMap.getDicoPere().put(n1.getId(), n1);
			}


		}
		System.out.println("on verifie si dans la liste de fils on n'as pas deux fils identiques");
		for(Map.Entry<String, Node> entry : listKnownMap.get(0).getDicoFils().entrySet())
		{
			String id1 = entry.getKey();
			Node n1 = entry.getValue();

			Node existFilsIdentique = map.getDicoFils().get(id1);
			if (existFilsIdentique != null)
			{
				System.out.println("si on a des fils identique on fait en sorte que leurs pere pointe sur le meme objet");
				ArrayList<Node> listPere = listKnownMap.get(0).getPere(n1);
				for(Node p : listPere)
				{
					p.switchSonToFather(existFilsIdentique);
				}
				map.getDicoFils().remove(existFilsIdentique);
			}
			mergedMap.getDicoFils().put(n1.getId(), n1);
		}
		for(Map.Entry<String, Node> entry : map.getDicoFils().entrySet())
		{
			Node n1 = entry.getValue();
			mergedMap.getDicoFils().put(n1.getId(), n1);

		}




		System.out.println("on verifie si dans le dico de Fils si des Fils ne sont pas pere");

		boolean needToBreak;
		while (true)
		{
			needToBreak= false;
			String idfils="";
			Node nodefils;
			for(Map.Entry<String, Node> entry : mergedMap.getDicoFils().entrySet())
			{
				idfils = entry.getKey();
				nodefils = entry.getValue();
				Node existPere = mergedMap.getDicoPere().get(idfils);
				System.out.println("si des pere sont aussi des fils qui existe on modifie leurs fils pour que l'objet Node pere soit le meme et un pere");
				if (existPere != null)
				{
					System.out.println("J'ai un pere et un fils !");
					System.out.println("le fils est : "+nodefils);
					System.out.println("le pere est : "+existPere);
					ArrayList<Node> listPere = mergedMap.getPere(nodefils);
					for(Node p : listPere)
					{
						System.out.println(p);
						p.switchSonToFather(existPere);
					}
					needToBreak=true;
					break;
				}


			}
			System.out.println("DEBUT VERIF FILS");
			ArrayList<Node> listf = new ArrayList<>(mergedMap.getDicoFils().values());
			for (Node node : listf) {
				System.out.println(node);

			}
			if(needToBreak)
			{
				mergedMap.getDicoFils().remove(idfils);
			}else{
				break;
			}
		}
		System.out.println("ICI");

		listKnownMap.remove(0);
		listKnownMap.add(0, mergedMap);




	}
	public void  majMap(List<Couple<String,List<Attribute>>> data)
	{

		boolean father = true;
		Node pere = new Node();

		for (Couple<String,List<Attribute>> couple : data)
		{
			int value = -1;
			String id = couple.getLeft();
			String type ="";
			if(father)
			{
				value = 0;
			}
			List<Attribute> listAttribute = couple.getRight();
			for(Attribute a:listAttribute){

				switch (a) {

				case TREASURE:

					value=(int) a.getValue();
					type = a.getName();
					break;
				case DIAMONDS:

					value=(int) a.getValue();
					type = a.getName();
					break;
				default:
					break;
				}
			}
			//System.out.println("YOOSSSHHH "+type);
			Node f = new Node(id,value,type);
			if(father)
			{

				if(listKnownMap.get(0).getDicoPere().containsKey(f.getId()))
				{
					listKnownMap.get(0).getDicoPere().get(f.getId()).setValue(value);
					listKnownMap.get(0).getDicoPere().get(f.getId()).setType(type);
					return;
				}else{
					Node exist = listKnownMap.get(0).getDicoFils().get(id);
					if(exist == null)
					{
						pere = f;
					}else{
						pere = exist;
						pere.setValue(value);
						pere.setType(type);
					}
					father = false;
				}

			}else{
				Node exist = listKnownMap.get(0).getDicoPere().get(id);
				if(exist == null)
				{
					exist = listKnownMap.get(0).getDicoFils().get(id);
					if(exist == null)
					{
						pere.addFils(f);
					}else{
						pere.addFils(exist);
					}

				}else{
					exist.switchSonToFather(pere);
					pere.addFils(exist);
				}
			}
		}

		for(KnownMap map : listKnownMap)
		{
			map.addNode(pere);
		}
	}


	public ArrayList<String> getListAgentShare() {
		return listAgentShare;
	}

	public void setListAgentShare(ArrayList<String> listAgentShare) {
		this.listAgentShare = listAgentShare;
	}

	public ArrayList<KnownMap> getListKnownMap() {
		return listKnownMap;
	}

	public void setListKnownMap(ArrayList<KnownMap> listKnownMap) {
		this.listKnownMap = listKnownMap;
	}


}
