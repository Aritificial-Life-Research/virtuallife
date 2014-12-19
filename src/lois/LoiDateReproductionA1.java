package lois;

import org.apache.commons.math3.distribution.GammaDistribution;

import phenotypique.EtreVivant;
//Pareil que la version A0, mais avec une loi Gamma(2,tempsCaracteristique*taille/2), pour des temps de reproduction plus piques.
public class LoiDateReproductionA1 implements LoiDateReproduction {
	/*
	 * Cette Loi calcule la date de prochaine reprodution d'un EtreVivant de la maniere suivante : 
	 * La duree jusqu'à la prochaine reproduction est choisie selon une loi exponentielle.
	 * Le parametre de cette loi exponentielle est choisi de maniere a ce que la duree de reproduction moyenne
	 * d'un etre vivant soit proportionnel a sa taille, selon un coefficient de proportionnalite fixe.
	 */
		
		public LoiDateReproductionA1(double tempsCaracteristique) {
			this.tempsCaracteristique = tempsCaracteristique;
			this.loiDuree = new GammaDistribution(2, tempsCaracteristique/2);
		}

		private double tempsCaracteristique;
		private GammaDistribution loiDuree;
		
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
