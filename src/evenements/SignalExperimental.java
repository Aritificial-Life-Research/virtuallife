package evenements;

import phenotypique.EtreVivant;
import physique.Univers;
import toolbox.LeProgrammeurEstUnGrosFruitException;

public class SignalExperimental extends Evenement {
	/*
	 * Un evenement utilise a des fins experimentales, pour signaler quand quelque chose
	 * se produit.
	 * Typiquement, inserer son arrivee dans une methode.
	 */

	public SignalExperimental(Univers univers) {
		super(univers, GenreEvenement.SIGNAL_EXPERIMENTAL);
	}

	@Override
	public boolean dependDunEtreVivant() {
		return false;
	}

	@Override
	public EtreVivant dependance() {
		throw new LeProgrammeurEstUnGrosFruitException("on ne doit pas demander de quel EtreVivant depend unEvenement qui ne depend d'aucun.");
	}

}
