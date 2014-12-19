package evenements;

import phenotypique.EtreVivant;

public interface SurvieDependant {
	/*
	 * cet interface sert a savoir si un Evenement depend de la survie d'un etre vivant.
	 */
	public boolean dependDunEtreVivant();
	
	public EtreVivant dependance();
	
}
