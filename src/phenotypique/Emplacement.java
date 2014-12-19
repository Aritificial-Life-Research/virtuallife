package phenotypique;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import physique.Point;
import physique.PointVirtuel;
import toolbox.Recyclable;

public class Emplacement extends HashMap<PointVirtuel, Point> implements Recyclable{

	
	public void nettoie() {
		//il n'y a rien a faire, toutes les valeurs seront reaffectees
	}

	public Emplacement() {
		super(4, 1.0F);
		// TODO Auto-generated constructor stub
	}
/*
	public Emplacement(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		// TODO Auto-generated constructor stub
	}

	public Emplacement(int initialCapacity) {
		super(initialCapacity);
		// TODO Auto-generated constructor stub
	}
*/
	public Emplacement(Map<? extends PointVirtuel, ? extends Point> m) {
		super(m);
		// TODO Auto-generated constructor stub
	}
}
