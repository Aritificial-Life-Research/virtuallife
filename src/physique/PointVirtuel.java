package physique;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import toolbox.Numerotable;


import aleatoire.Aleatoire;


public class PointVirtuel implements Numerotable, Comparable<PointVirtuel>{
	/*
	 * Essentiellement un sommet de graphe, intervient dans Phenotype. 
	 * Non mutable, car destine a etre dans un Set.
	 */
	
	private int indice;
	
	public final Map<Lettre, PointVirtuel> successeurs;
	
	public PointVirtuel(int s){
		indice = s;
		successeurs = new HashMap<Lettre, PointVirtuel>(4, 1.0F);
	}
	
	public int hashCode(){
		return indice;
	}
	
	public boolean equals(PointVirtuel s){//si c'est bien fait, ne devrait meme pas etre necessaire
		return s.getIndice()==indice;
	}
	
	public boolean equals(Object o){
		//cette methode ne devrait pas etre invoquee
		if(o.getClass()==this.getClass()) return o.hashCode()==indice;
		else{
			return false;
		}
	}

	public int getIndice() {
		return indice;
	}

	public String toString(){
		return "S".concat(Integer.toString(indice));
	}

	@Override
	public int indice() {
		// TODO Auto-generated method stub
		return indice;
	}

	@Override
	public int compareTo(PointVirtuel arg0) {
		if(this.indice < arg0.getIndice()) return 1;
		else{
			if(this.indice > arg0.getIndice()) return -1;
			else return 0;
		}
	}


}
