package evenements;

import phenotypique.EtreVivant;
import physique.Univers;
import toolbox.Recyclable;

public class NaissanceEtreVivant extends EvenementVivant  implements Recyclable{

	/*
	 * Cet Evenement dit que l'etreVivant "s'implante" dans l'Univers.
	 * 
	 * parmi les consequences, il y a la mort des autres EtreVivants qui occupent au moins un de ses points.
	 * 
	 * TODO : il serait peut-etre plus juste de ne pas se donner d'emblee un EtreVivant, mais plutot un phenotype et un emplacement.
	 */
	
	private double energieDeNaissance;
	


	public NaissanceEtreVivant(Univers univers, EtreVivant etreVivant, double energieDeNaissance) {
		super(univers, GenreEvenement.NAISSANCE, etreVivant);
		this.setEnergieDeNaissance(energieDeNaissance);
	}



	public double getEnergieDeNaissance() {
		return energieDeNaissance;
	}



	@Override
	public void nettoie() {
	}



	public void setEnergieDeNaissance(double energieDeNaissance) {
		this.energieDeNaissance = energieDeNaissance;
	}

}
