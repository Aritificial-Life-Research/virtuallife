package physique;

import java.util.LinkedList;

public class Mot extends LinkedList<Lettre>{
	
	public String toString(){
		String s = "-";
		for(Lettre l : this){
			s = s.concat(l.toString()).concat("-");
		}
		return s;
	}
}
