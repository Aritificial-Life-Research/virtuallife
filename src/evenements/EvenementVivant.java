package evenements;

import phenotypique.EtreVivant;
import physique.Univers;
import toolbox.Recyclable;

public abstract class EvenementVivant extends Evenement  implements Recyclable{

	public EvenementVivant(Univers univers, GenreEvenement type, EtreVivant etreVivant) {
		super(univers, type);
		this.setEtreVivant(etreVivant);
		// TODO Auto-generated constructor stub
	}

	private EtreVivant etreVivant;
	
	public EtreVivant getEtreVivant() {
		return etreVivant;
	}

	@Override
	public boolean dependDunEtreVivant() {
		return true;
	}

	@Override
	public EtreVivant dependance() {
		return this.getEtreVivant();
	}

	public void setEtreVivant(EtreVivant etreVivant) {
		this.etreVivant = etreVivant;
	}
	
}
