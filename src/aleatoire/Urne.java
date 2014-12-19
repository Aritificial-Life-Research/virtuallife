package aleatoire;

import java.util.*;

public interface Urne<B> {
	/*
	 * Cet interface sert a faire un tirage aleatoire de type barycentrique.
	 * 
	 * TIRAGE DE TYPE BARYCENTRIQUE : on tire des boules de type B. A chaque boule Bi est associe un reel positif Xi
	 * (de type double) appele son coefficient. La probabilite de tirer la boule Bi vaut Xi/(somme Xj).
	 * On impose qu'il soit rigoureusement(et non quasiment) impossible de tirer une boule de coefficient 0.
	 * 
	 * De maniere minimale, un Objet qui implemente Urne<B> doit donc necessairement garder une trace de la fonction de ponderation,
	 * qui a une boule associe son coefficient.
	 */
	
	//ajoute une boule a l'urne. Si cette boule est deja dans l'urne, change le coefficient de cette boule.
	//il semble judicieux de ne pas ajouter une boule de coefficient nul.
	public void ajouteBoule(B boule, double coefficient);
	
	//tire une boule de l'urne selon la loi barycentrique, et la retire de l'urne
	public B tireAvecRemise();
	
	//tire une boule de l'urne selon la loi barycentrique, et la remet dans l'urne apres, sans modifier l'urne donc.
	public B tireSansRemise();
	
	//enleve une boule de l'urne. Ne fait rien si elle n'est pas dans l'Urne.
	public void retireBoule(B boule);
	
	public void modifiePoids(B boule, double nouveauPoids);
	
	//ajoute toutes les boules dans l'urne selon le coefficient qui leur est associe par la fonction ponderation.
	public void ajouteToutesBoules(Map<B, Double> ponderation);
	
	//modifie le poids de toutes les boules de l'urne figurant dans nouvellePonderation.
	public void modifiePoidsToutesBoules(Map<B, Double> nouvellePonderation);
	
	//reinitialise l'urne pour en faire une urne de tirage uniforme parmi l'ensemble de boules en parametre (tous les coefficients valent 1)
	public void initialiseUniforme(Set<B> boules);
	
	//renvoie true ssi l'urne ne contient pas de boule.
	public boolean isEmpty();

	//enleve toutes les boules de l'urne
	public void clear();
}
