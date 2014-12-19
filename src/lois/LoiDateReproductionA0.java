package lois;

import org.apache.commons.math3.distribution.ExponentialDistribution;

import phenotypique.EtreVivant;

public class LoiDateReproductionA0 implements LoiDateReproduction {
/*
 * Cette Loi calcule la date de prochaine reprodution d'un EtreVivant de la maniere suivante : 
 * La duree jusqu'à la prochaine reproduction est choisie selon une loi exponentielle.
 * Le parametre de cette loi exponentielle est choisi de maniere a ce que la duree de reproduction moyenne
 * d'un etre vivant soit proportionnel a sa taille, selon un coefficient de proportionnalite fixe.
 */
	
	public LoiDateReproductionA0(double tempsCaracteristique) {
		this.tempsCaracteristique = tempsCaracteristique;
		this.loiDuree = new ExponentialDistribution(tempsCaracteristique);
	}

	private double tempsCaracteristique;
	private ExponentialDistribution loiDuree;
	
	@Override
	public double dateProchaineReproduction(EtreVivant etrevivant,
			double dateActuelle) {
		/*
		 *   On utilise le fait (facilement demontrable) que si T suit une Loi Exponentielle de temps moyen t,
		 * alors D = n*T suit une loi Exponentielle de temps moyen n*d.
		 *   Ainsi, on evite d'avoir a creer une ExponentialDitribution a chaque calcul.
		 */
		return (dateActuelle + (loiDuree.sample()*etrevivant.getPh().getAnatomie().getSommets().size()));
	}
	
}
