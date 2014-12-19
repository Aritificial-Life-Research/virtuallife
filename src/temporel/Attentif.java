package temporel;

import evenements.Evenement;

public interface Attentif {
	/*
	 * qualifie des Objets qui sont sensibles au consequences de certains Evenements.
	 * Un objet attentif est fait pour se faire prevenir de l'arrivee d'un Evenement par un GestionnaireDEvenements.
	 */
	
	public void prendEnCompte(Evenement e);
}
