package lois;

import org.apache.commons.math3.distribution.ExponentialDistribution;

import phenotypique.EtreVivant;

public class LoiDateReproductionC0 implements LoiDateReproduction {
	/*
	 * Cette Loi calcule la date de prochaine reprodution d'un EtreVivant de la maniere suivante : 
	 * La duree jusqu'à la prochaine reproduction est choisie selon une loi exponentielle, 
	 * de moyenne le temps caracteristique en parametres
 	 */
		
		public LoiDateReproductionC0(double tempsCaracteristique) {
			this.loiDuree = new ExponentialDistribution(tempsCaracteristique);
		}

		private ExponentialDistribution loiDuree;
		
		@Override
		public double dateProchaineReproduction(EtreVivant etrevivant,
				double dateActuelle) {
			
			return (dateActuelle + loiDuree.sample());
		}

}
