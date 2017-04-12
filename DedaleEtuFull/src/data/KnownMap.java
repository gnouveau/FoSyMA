package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KnownMap implements Serializable{

	private static final long serialVersionUID = 1L;
	private HashMap<String, Node> dicoPere = new HashMap<>();
	private HashMap<String, Node> dicoFils = new HashMap<>();
	
	
	public KnownMap(){

	}
	public void addNode(Node n)
	{
		dicoPere.put(n.getId(), n);
		dicoFils.remove(n.getId());
		
		for (Node f : n.getFils())
		{
			if(!dicoPere.containsKey(f.getId())){
				dicoFils.put(f.getId(), f);
			}
		}
	}
	public void addNodeMerged(Node n)
	{
		dicoPere.put(n.getId(), n);
		for (Node f : n.getFils())
		{
			dicoFils.put(f.getId(), f);
		}
	}
	//renvoi les peres du fils
	public ArrayList<Node> getPere(Node fils)
	{
		ArrayList<Node> list = new ArrayList<>();
		for(Map.Entry<String, Node> entry : dicoPere.entrySet())
		{
			Node n = entry.getValue();
			for(Node f : n.getFils())
			{
				if(f.getId() == fils.getId())
					list.add(n);
			}
			
		}
		return list;
	}
	
	public String toString()
	{
		String s = "";
		ArrayList<Node> list = new ArrayList<>(dicoPere.values());
		for (Node node : list) {
			s += node.toString() +"\n";
		}
		return s;
	}
	
	public HashMap<String, Node> getDicoPere() {
		return dicoPere;
	}
	public void setDicoPere(HashMap<String, Node> dicoPere) {
		this.dicoPere = dicoPere;
	}
	public HashMap<String, Node> getDicoFils() {
		return dicoFils;
	}
	public void setDicoFils(HashMap<String, Node> dicoFils) {
		this.dicoFils = dicoFils;
	}



}
