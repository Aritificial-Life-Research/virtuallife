package experimental;

import evenements.Evenement;
import physique.*;

public interface Observation {
/*
 * Une Observation est un ensemble de donnees qui sont stockees a un instant donne,
 * concernant le deroulement d'une experience.
 * Une Observation est vouee a etre mise a jour a chaque fois qu'un Evenement arrive.
 * Une Observation N'EST PAS necessaire a l'ecoulement du temps.
 * En principe, une observation n'est susceptible d'etre modifiee que par l'arrivee d'un evenement.
 */
	
	public void metAJour(Evenement evn);
	
}
