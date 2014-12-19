package evenements;

import phenotypique.EtreVivant;
import physique.Univers;
import toolbox.LeProgrammeurEstUnGrosFruitException;
import toolbox.Recyclable;

public class EcoulementDuTemps extends Evenement implements Recyclable {



	public double dateInitiale;
	public double dateFinale;
	
	
	
	@Override
	public boolean dependDunEtreVivant() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EtreVivant dependance() {
		// TODO Auto-generated method stub
		return null;
	}

	public EcoulementDuTemps(Univers univers, double dateInitiale, double dateFinale) {
		super(univers, GenreEvenement.ECOULEMENT_DU_TEMPS);
		this.dateInitiale = dateInitiale;
		this.dateFinale = dateFinale;
	}

	@Override
	public void nettoie() {
	}

}
