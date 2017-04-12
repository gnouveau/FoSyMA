package data;


import java.util.ArrayList;


public class StopParcoursException extends Exception{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Node> fin;
	
	public StopParcoursException(ArrayList<Node> list)
	{
	fin=list;	
	}

	public ArrayList<Node> getFin() {
		return fin;
	}

	public void setFin(ArrayList<Node> fin) {
		this.fin = fin;
	}

}
