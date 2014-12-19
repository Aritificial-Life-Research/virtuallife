package evenements;

import phenotypique.*;
import physique.Univers;
import toolbox.Recyclable;

public class VariationEnergieAsymptotique extends EvenementVivant  implements Recyclable{
	/*
	 * Cet Evenement indique que l'energie asymptotique de etreVivant varie pour valoir a present nouvelleEnergie.
	 */
	
	private double nouvelleEnergie;

	public VariationEnergieAsymptotique(Univers univers, EtreVivant etreVivant, double nouvelleEnergie) {
		super(univers, GenreEvenement.VARIATION_ENERGIE_ASYMPTOTIQUE, etreVivant);
		this.setNouvelleEnergie(nouvelleEnergie);
	}

	public double getNouvelleEnergie() {
		return nouvelleEnergie;
	}

	@Override
	public void nettoie() {
	}

	public void setNouvelleEnergie(double nouvelleEnergie) {
		this.nouvelleEnergie = nouvelleEnergie;
	}

}
