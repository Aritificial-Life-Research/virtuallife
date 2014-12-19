package evenements;

import phenotypique.EtreVivant;
import physique.Mot;
import physique.PointVirtuel;
import physique.Univers;
import toolbox.Recyclable;

public class CompletionMot extends EvenementVivant implements Recyclable {

	/*
	 * Cet Evenement dit que etreVivant a reussi a former le mot Mot depuis le pointVirtuel SommetEntree
	 */

	



	private PointVirtuel sommetEntree;
	private Mot mot;


	public PointVirtuel getSommetEntree() {
		return sommetEntree;
	}

	public Mot getMot() {
		return mot;
	}

	public CompletionMot(Univers univers, EtreVivant etreVivant, PointVirtuel sommetEntree, Mot mot) {
		super(univers, GenreEvenement.COMPLETION_MOT, etreVivant);
		this.setSommetEntree(sommetEntree);
		this.setMot(mot);
	}

	@Override
	public void nettoie() {
	}

	public void setSommetEntree(PointVirtuel sommetEntree) {
		this.sommetEntree = sommetEntree;
	}

	public void setMot(Mot mot) {
		this.mot = mot;
	}

}
