package evenements;

import phenotypique.EtreVivant;
import physique.Univers;
import toolbox.LeProgrammeurEstUnGrosFruitException;
import toolbox.Recyclable;

public class MortEtreVivant extends EvenementVivant  implements Recyclable{

	/*
	 * Cet Evenement dit que etreVivant meurt, i.e disparait de l'Univers englobant.
	 */
	

	public boolean dependDunEtreVivant(){
		return false;
	}
	public EtreVivant dependance(){
		throw new LeProgrammeurEstUnGrosFruitException("ne depend pas d'un etre vivant");
	}
	public MortEtreVivant(Univers univers, EtreVivant etreVivant) {
		super(univers, GenreEvenement.MORT, etreVivant);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void nettoie() {
	}
}
