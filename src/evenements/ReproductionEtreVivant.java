package evenements;

import phenotypique.EtreVivant;
import physique.Univers;
import toolbox.Recyclable;

public class ReproductionEtreVivant extends EvenementVivant implements Recyclable{

	public ReproductionEtreVivant(Univers univers, EtreVivant etreVivant) {
		super(univers, GenreEvenement.REPRODUCTION, etreVivant);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void nettoie() {
	}



}
