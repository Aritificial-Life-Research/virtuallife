package evenements;

import phenotypique.EtreVivant;
import physique.PointVirtuel;
import physique.Univers;
import toolbox.Recyclable;

public class ChoixMot extends EvenementVivant implements Recyclable{
	
	/*
	 * Cet Evenement dit que etreVivant tente de choisir un Mot a former depuis
	 * un ou plusieurs sommets d'entree.
	 */

	private PointVirtuel[] sommetsEntree;
	
	public PointVirtuel[] getSommetsEntree() {
		return sommetsEntree;
	}

	public void setSommetsEntree(PointVirtuel[] sommetsEntree) {
		this.sommetsEntree = sommetsEntree;
	}



	public ChoixMot(Univers univers, EtreVivant etreVivant,
			PointVirtuel sommetEntree) {
		super(univers, GenreEvenement.CHOIX_MOTS, etreVivant);
		PointVirtuel[] sE = {sommetEntree};
		this.sommetsEntree = sE;
	}

	public ChoixMot(Univers univers, EtreVivant etreVivant, PointVirtuel[] sommetEntree) {
		super(univers, GenreEvenement.CHOIX_MOTS, etreVivant);
		this.sommetsEntree = sommetEntree;
	}

	@Override
	public void nettoie() {
	}




}
