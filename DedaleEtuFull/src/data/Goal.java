package data;

import java.io.Serializable;
import java.util.ArrayList;

public class Goal implements Serializable{

	private static final long serialVersionUID = 1L;
	private String nameAgt;
	private ArrayList<Node> goalPath;
	private Integer myCapacity;
	
	public Goal(String name, ArrayList<Node> goal, Integer capacity )
	{
		this.nameAgt=name;
		this.goalPath=goal;
		this.myCapacity=capacity;
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

	
}
