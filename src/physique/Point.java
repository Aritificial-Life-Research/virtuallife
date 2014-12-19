package physique;

import toolbox.Numerotable;

public class Point implements Comparable<Point>, Numerotable{
/*
 * Un point d'un univers, representant un lieu absolu et reel.
 * Non mutable, car destine a etre dans un Set.
 */
	private final int indice;
	
	public Point(int s){
		indice = s;
	}
	
	public int hashCode(){
		return indice;
	}
	
	public boolean equals(Point s){//si c'est bien fait, ne devrait meme pas etre necessaire
		return s.hashCode()==indice;
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
		return "P".concat(Integer.toString(indice));
	}

	@Override
	public int compareTo(Point arg0) {
		if(this.indice < arg0.getIndice()) return 1;
		else{
			if(this.indice > arg0.getIndice()) return -1;
			else return 0;
		}
	}

	@Override
	public int indice() {
		// TODO Auto-generated method stub
		return indice;
	}
	
	
}
