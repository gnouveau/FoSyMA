package data;

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Node> fils = new ArrayList<>();
	private String id;
	private int value;
	private String type;
	
	
	public Node(){}
	public Node(String id, int value)
	{
	this.id = id;
	this.value = value;
	}
	public Node(String id, int value, String type)
	{
		this(id,value);
		this.type = type;
	}
	
	public void addFils(Node f)
	{
		fils.add(f);
	}
	public ArrayList<Node> getFils() {
		return fils;
	}
	public void setFils(ArrayList<Node> fils) {
		this.fils = fils;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public void switchSonToFather(Node p)
	{
		String id = p.getId();
		for (int i = 0;i<fils.size();i++)
		{
			if(fils.get(i).getId() == id)
			{
				fils.remove(i);
				this.addFils(p);
			}
		}
		
	}
	public String toString()
	{
		String s="Pere id : "+id+" value : "+value+"\n Fils : ";
		for(Node f : fils)
		{
			s+=""+f.getId()+", ";
		}
		
		return s+"\n";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}
