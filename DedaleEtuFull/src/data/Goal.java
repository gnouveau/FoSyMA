package data;

import java.io.Serializable;
import java.util.ArrayList;

public class Goal implements Serializable{

	private static final long serialVersionUID = 1L;
	private String nameAgt;
	private ArrayList<Node> goalPath;
	private Integer myCapacity;
	private String myType;
	private Priorite priorite;
	
	public Goal(String name, ArrayList<Node> goal, Integer capacity, String type )
	{
		this.nameAgt=name;
		this.goalPath=goal;
		this.myCapacity=capacity;
		this.myType=type;
		priorite = Priorite.NORMAL;
	}
	public Goal(String name, ArrayList<Node> goal, Integer capacity, String type, Priorite prio)
	{
		this(name,goal,capacity,type);
		this.priorite = prio;
	}
	
	public String getNameAgt() {
		return nameAgt;
	}

	public void setNameAgt(String nameAgt) {
		this.nameAgt = nameAgt;
	}

	public ArrayList<Node> getGoalPath() {
		return goalPath;
	}

	public void setGoalPath(ArrayList<Node> goalPath) {
		this.goalPath = goalPath;
	}

	public Integer getMyCapacity() {
		return myCapacity;
	}

	public void setMyCapacity(Integer myCapacity) {
		this.myCapacity = myCapacity;
	}
	
	public String toString(){
		String str = "";
		str += "****************************************"+"\n";
		str += "Agent : "+ nameAgt +" de capacite : "+ myCapacity+"\n";
		str += "Mon chemin objectif : "+"\n";
		for(Node n : goalPath){
			str += n.getId()+" ";
		}
		str += "\n";
		str += "****************************************"+"\n";
		return str;
	}

	public String getMyType() {
		return myType;
	}

	public void setMyType(String myType) {
		this.myType = myType;
	}
	public Priorite getPriorite() {
		return priorite;
	}
	public void setPriorite(Priorite priorite) {
		this.priorite = priorite;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
