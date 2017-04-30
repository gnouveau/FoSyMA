package data;

public enum Priorite {
	FAIBLE("FAIBLE",0),
	NORMAL("NORMAL",1),
	PRIORITAIRE("PRIORITAIRE",2),
	ATTENTE("ATTENTE",3);
	
	public String name ="INIT";
	public int value =-1;
	
	Priorite(String name, int value)
	{
		this.name = name;
		this.value = value;
	}
}