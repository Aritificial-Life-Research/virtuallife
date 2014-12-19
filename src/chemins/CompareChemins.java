package chemins;

import java.util.Comparator;
//sert uniquement a creer le Comparator<Chemin> compareChemin
public class CompareChemins{
	public static final Comparator<Chemin.QueueChemin> compareChemins = new Comparator<Chemin.QueueChemin>(){
		public int compare(Chemin.QueueChemin ch1, Chemin.QueueChemin ch2){
			if(ch1==null){
				if(ch2==null) return 0;
				else return -1;
			}
			else{
				if(ch2==null) return 1;
				else{
					if(ch1.getLettre().compareTo(ch2.getLettre())==0) return compare(ch1.getTail(),ch2.getTail());
					else return ch1.getLettre().compareTo(ch2.getLettre());
				}
			}
		}
	};
}
