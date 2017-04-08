package data;

import java.util.ArrayList;
import java.util.HashMap;

public class testingz {

	public static void main(String[] args) {
	
//		HashMap<Node, Node> map = new HashMap<>();
//		
//		map.put(new Node("1", 4), new Node("2",9));
//		
//		Node a = new Node("1", 0);
//				
//		if (map.containsKey(a)) { System.out.println("COOL");}else{ System.out.println("SUM");}
//		
//		class Key 
//		{
//			Integer truc = 1;
//			String name = " MACHIN";
//			
//			Key(int i,String s){truc = i;name=s;}
//		}
//		HashMap<Key, String> mapped = new HashMap<>();
//		Key k = new Key(4,"SALUT");
//		mapped.put(k, "2");
//		Key j = new Key(4,"SALUT");
//
//		if (mapped.containsKey(j
//				)) { System.out.println("COOL");}else{ System.out.println("SUM");}
//		
		Node p1 = new Node("1",2);
		Node p1f1 = new Node("2",1);
		Node p1f2 = new Node("10",1);
		Node p1f3 = new Node("4",1);
		Node p2 = new Node("5",1);
		Node p2f1 = new Node("6",1);
		Node p2f2 = new Node("7",0);
		
		Node p3 = new Node("2",1);
		Node p3f1 = new Node("10",2);
		Node p3f2 = new Node("5",1);
		KnownMap map1 = new KnownMap();
		KnownMap map2 = new KnownMap();
		
		p1.addFils(p1f1);p1.addFils(p1f2);p1.addFils(p1f3);
		p2.addFils(p2f1);p2.addFils(p2f2);
		p3.addFils(p3f1);p3.addFils(p3f2);
		
		map1.addNode(p1);map1.addNode(p2);
		
		map2.addNode(p3);
		System.out.println("INFO MAP");
		System.out.println(map1.toString());
		System.out.println(map2.toString());
		System.out.println("INFO MAP MANAGER");
		ManageMap managerMap = new ManageMap("g1",map1);
		managerMap.getListKnownMap().add(map1);
		System.out.println(managerMap.getListKnownMap().get(0));

		managerMap.mergeMap(map2);
		System.out.println("FIN MERGE map2 a map 1");
		System.out.println(managerMap.getListKnownMap().get(0));
		
		
		HashMap<String, Node> fils = managerMap.getListKnownMap().get(0).getDicoFils();
		HashMap<String, Node> pere = managerMap.getListKnownMap().get(0).getDicoPere();
		ArrayList<Node> listf = new ArrayList<>(fils.values());
		ArrayList<Node> listp = new ArrayList<>(pere.values());
		System.out.println("DEBUT DEBUG");
		System.out.println("DICO FILS");
		for (Node node : listf) {
			System.out.println(node);
			
		}
		System.out.println("DICO PERE");
		for (Node node : listp) {
			System.out.println(node);
			
		}
		System.out.println("debug valeur des fils");	
		for (Node node : listp) {
			System.out.println(node.getFils());
			
		}
//		
//		ManageExplo explo = new ManageExplo();
//		ArrayList<Node> path = explo.solveProblemByDepth(p1, 0);
//		System.out.println(path);

		
	}
}
