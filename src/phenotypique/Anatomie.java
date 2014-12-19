package phenotypique;



import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import physique.Lettre;
import physique.PointVirtuel;
import toolbox.LeProgrammeurEstUnGrosFruitException;

public class Anatomie {

	/*
	 * Decrit l'automate deterministe a pusieurs sommets d'entrees et un de sortie qui
	 * est une propriete d'un etre vivant.
	 * 
	 * Puisque l'automate est deterministe, on a une fonction partielle ("aretes") associant 
	 * a un sommet et a une lettre un sommet.
	 * 
	 * Quelques remarques importantes sur la structure de tels automates :
	 * 	- on peut avoir plusieurs aretes (d'etiquettes differentes) qui menent d'un sommet a un autre.
	 * 	- une arete peut mener d'un sommet a lui-meme.
	 * 
	 * Pour qu'une Anatomie decrive un etre vivant "viable" selon le modele, il est essentiel que :
	 * 	- un sommet d'entree ait une valence d'entree nulle (i.e aucune arete n'arrive dessus)
	 * 	- le sommet de sortie ait une valence de sortie nulle (i.e aucune arete n'en part)
	 * 	- d'un sommet (autre que celui de sortie), il existe toujours un chemin jusqu'au sommet de sortie.
	 * (Ainsi, les sommets d'entree permettent toujours de former des mots, et aucun point ni aucune arete n'est inutilisable).
	 */
	
	private int S;//nombre de sommets de l'automate
	private Set<PointVirtuel> sommets;
	
	private int Se;//le nombre de sommets d'entree, Se < S.
	private Set<PointVirtuel> sommetsEntree; //sous-ensemble de "sommets", ne contenant pas sommet sortie. 
	private PointVirtuel sommetSortie;

	public Anatomie(){//cree un graphe vide
		S = 0;
		sommets = new HashSet<PointVirtuel>();
		
		Se = 0;
		sommetsEntree = new HashSet<PointVirtuel>();
		sommetSortie = null;
	}
	//methodes permettant de construire manuellement une anatomie
	public void ajouteSommetsEntree(PointVirtuel ... E){
		for (int i = 0; i < E.length; i++) {
			sommets.add(E[i]);
			S++;
			sommetsEntree.add(E[i]);
			Se++;
		}
	}
	
	public void ajouteSommetsIntermediaires(PointVirtuel ... I){
		for (int j = 0; j < I.length; j++) {
			sommets.add(I[j]);
			S++;
		}
	}
	
	public void ajouteArete(PointVirtuel depart, Lettre l, PointVirtuel arrivee) throws LeProgrammeurEstUnGrosFruitException{
		if((depart==null)||(l==null)||(arrivee==null)) {
			throw new LeProgrammeurEstUnGrosFruitException("parametre null : depart=" + depart + " l = " + l + " arrivee = " + arrivee);
		}
		if(!this.sommets.contains(depart) || !this.sommets.contains(arrivee)){
			throw new LeProgrammeurEstUnGrosFruitException("Impossible d'ajouter une aretes entre deux sommets qui ne sont pas tous les deux dans l'Anatomie.");
		}
		if(depart.equals(this.sommetSortie)||this.sommetsEntree.contains(arrivee)){//interdit d'ajouter des aretes interdites
			throw new LeProgrammeurEstUnGrosFruitException("arete interdite");
		}
		depart.successeurs.put(l, arrivee);
	}
	
	//les getters et setters, classique.
	public int getS() {
		return S;
	}

	public void setS(int s) {
		S = s;
	}

	public Set<PointVirtuel> getSommets() {
		return sommets;
	}

	public void setSommets(Set<PointVirtuel> sommets) {
		this.sommets = sommets;
	}

	public int getSe() {
		return Se;
	}

	public void setSe(int se) {
		Se = se;
	}

	public Set<PointVirtuel> getSommetsEntree() {
		return sommetsEntree;
	}

	public void setSommetsEntree(Set<PointVirtuel> sommetsEntree) {
		this.sommetsEntree = sommetsEntree;
	}

	public PointVirtuel getSommetSortie() {
		return sommetSortie;
	}

	public void setSommetSortie(PointVirtuel sommetSortie) {
		this.sommetSortie = sommetSortie;
	}
	/*
	 *   renvoie une anatomie qui est un clone de celle-ci en s'assurant
	 * que les indices des PointVirtuels representes resultent en une fonction
	 * de hachage plus efficace.
	 */
	public Anatomie recopieProprement(){
		int cpt = 0;
		Map<PointVirtuel, PointVirtuel> remplacants = new HashMap<PointVirtuel, PointVirtuel>(S, 1.0F);
		
		Set<PointVirtuel> sommets1 = new HashSet<PointVirtuel>(S, 1.0F);
		Set<PointVirtuel> sommetsEntree1 = new HashSet<PointVirtuel>(Se, 1.0F);
		
		for(PointVirtuel pv : this.sommets){
			PointVirtuel pv1 = new PointVirtuel(cpt++);
			remplacants.put(pv, pv1);
			
			sommets1.add(pv1);
			if(this.sommetsEntree.contains(pv)) sommetsEntree1.add(pv1);
		}
		
		for(PointVirtuel pv : this.sommets){
			PointVirtuel pv1 = remplacants.get(pv);
			for(Lettre l : pv.successeurs.keySet()){
				pv1.successeurs.put(l, remplacants.get(pv.successeurs.get(l)));
			}
		}
		
		return new Anatomie(S, sommets1, Se, sommetsEntree1, remplacants.get(sommetSortie));
	}
	public Anatomie(int s, Set<PointVirtuel> sommets, int se,
			Set<PointVirtuel> sommetsEntree, PointVirtuel sommetSortie) {
		S = s;
		this.sommets = sommets;
		Se = se;
		this.sommetsEntree = sommetsEntree;
		this.sommetSortie = sommetSortie;
	}
}
