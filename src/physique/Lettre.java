package physique;

import toolbox.Numerotable;

public class Lettre implements Comparable<Lettre>, Numerotable{
	/*
	 * Une lettre d'un alphabet. Non mutable, car destine a etre dans un Set.
	 */
	private final int indice;
	
	public Lettre(int s){
		indice = s;
	}
	
	public int hashCode(){
		return indice;
	}
	
	public boolean equals(Lettre s){//TODO si c'est bien fait, ne devrait meme pas etre necessaire
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
	
	public int compareTo(Lettre l) {
		//permet d'ordonner les lettres par indice
		if(indice<l.getIndice()) return -1;
		else{
			if(indice>l.getIndice()) return 1;
			else return 0;
		}
	}
	
	public String toString(){
		return "L".concat(Integer.toString(indice));
	}

	@Override
	public int indice() {
		// TODO Auto-generated method stub
		return indice;
	}
}
