package lois;

import java.util.HashSet;
import java.util.Set;

import phenotypique.EtreVivant;
import physique.Mot;
import physique.Lettre;

/*
 * Meme decroissance energetique que LoiEnergieA0.
 * 
 * une loi tres simple pour l'energie des mots.
 */
public class LoiEnergieB0 implements LoiEnergie {

	private double tempsCaracteristique = 20.0; //TODO PARAMETRE cf "decroissanceEnergetique(EtreVivant E)"
	
	public LoiEnergieB0(double tempsCaracteristique) {
		this.tempsCaracteristique = tempsCaracteristique;
	}
	
	public double energieMot(Mot m) {
		//tres simple : longueur du mot au carre fois le nombre de lettres.
		Set<Lettre> lettres = new HashSet<Lettre>();
		int longueur=0;
		for(Lettre l : m){
			lettres.add(l);
			longueur++;
		}
		return 1.0*longueur*longueur*lettres.size();
	}

	@Override	
	/*
	 * La loi d'energie est la suivante :
	 * -l'energie asymptotique de l'etre vivant est la somme des energies des mots qu'il est en train de former.
	 * -l'energie de l'etre vivant tend de maniere exponentielle vers l'energie asymptotique
	 * selon le temps caracteristique qui est un parametre de la loi.
	 */
	public double variationEnergetique(EtreVivant e, double dateInitiale, double dateFinale) {
		double energieAsymptotique = e.getEnergieAsymptotique();
		return energieAsymptotique + ((e.getAncienneEnergie()-e.getEnergieAsymptotique())*Math.exp((dateInitiale-dateFinale)/this.tempsCaracteristique));	
	}

}
