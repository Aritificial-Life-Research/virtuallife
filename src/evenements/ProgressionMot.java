package evenements;

import phenotypique.EtreVivant;
import physique.PointVirtuel;
import physique.Univers;
import toolbox.Recyclable;

public class ProgressionMot extends EvenementVivant implements Recyclable{
	/*
	 * Cet Evenement indique que etreVivant poursuit la formation du Mot entamee depuis sommetEntree
	 * En pratique, il ne se passe rien lors de la progression, il ne s'agit que d'une incrementation
	 */

	private PointVirtuel sommetEntree;


	public ProgressionMot(Univers univers, EtreVivant etreVivant, PointVirtuel sommetEntree) {
		super(univers, GenreEvenement.PROGRESSION_MOT, etreVivant);
		this.setSommetEntree(sommetEntree);
	}


	public PointVirtuel getSommetEntree() {
		return sommetEntree;
	}


	@Override
	public void nettoie() {
	}


	public void setSommetEntree(PointVirtuel sommetEntree) {
		this.sommetEntree = sommetEntree;
	}

}
