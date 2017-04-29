package data;

public enum Priorite {
	NORMAL("NORMAL",1),
	PRIORITAIRE("PRIORITAIRE",2),
	ATTENTE("ATTENTE",3);
	
	private String name ="INIT";
	private int value =-1;
	
	Priorite(String name, int value)
	{
		this.name = name;
		this.value = value;
	}
}