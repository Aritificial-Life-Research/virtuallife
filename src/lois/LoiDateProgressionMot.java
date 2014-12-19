package lois;

import phenotypique.EtreVivant;

public interface LoiDateProgressionMot {
/*
 * Cette Loi sert a dire dans combien de temps un etreVivant doit avancer dans la formation d'un Mot.
 * (duree jusque la prochaine translocation de ressources)
 */
	
	public abstract double dateProchaineProgression(EtreVivant etreVivant,double dateActuelle);
}
