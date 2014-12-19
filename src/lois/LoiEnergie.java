package lois;

import phenotypique.EtreVivant;
import physique.Mot;

public interface LoiEnergie {
/*
 * Cet interface a pour objectif de determiner l'evolution energetique des etres vivants.
 * Deux phenomenes sont a prendre en compte :
 * 	- les etres vivants sont recompenses par de l'energie quand ils forment des mots;
 * 	- les etres vivants perdent de l'energie avec le temps.
 */
	
	
	public double energieMot(Mot m);//decide de l'energie dont est recompense un etre vivant pour avoir forme un mot
	
	public abstract double variationEnergetique(EtreVivant e, double dateInitiale, double dateFinale);//decide de l'evolution de l'energie d'un etre vivant independamment de la formation de mots.
}
