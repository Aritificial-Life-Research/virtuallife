package phenotypique;

import genomique.Genome;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

import physique.PointVirtuel;
import physique.Univers;

import toolbox.Recycleur;
import lois.SelecteurChemin;
import lois.SelecteurCheminECECB;

public class Phenotype {
	
	/*
	 * Un phenotype est la donnee d'un graphe orient�, �tiquet� par les lettres.
	 * 
	 * Certains sommets sont distingu�s : un/des sommets d'entr�e, un sommet de sortie.
	 * 
	 * De tout point, il y a un chemin vers le sommet de sortie (pas d'ar�tes inutiles).
	 * 
	 * Aucune ar�te ne part du sommet de sortie, aucune ar�te n'arrive sur un sommet d'entr�e.
	 * 
	 * L'automate est deterministe : d'un sommet, il part au plus une ar�te portant une lettre donn�e.
	 */	
	private Anatomie anatomie;//l'automate deterministe decrivant le phenotype.
	
	private SelecteurChemin selChemin;
	//on doit pouvoir calculer le co�t de reproduction d'un �tre vivant. On peut � priori consid�rer que ce co�t ne d�pend que du ph�notype.

	
	public Phenotype(Anatomie anatomie){
		this.anatomie = anatomie;
		this.selChemin = new SelecteurCheminECECB(anatomie, 0.6);//TODO choisir ces parametres.
	}
	
	public Phenotype(Anatomie anatomie, double pEnrichissement){
		this.anatomie = anatomie;
		this.selChemin = new SelecteurCheminECECB(anatomie, pEnrichissement);//TODO choisir ces parametres.
	}

	public Phenotype(Anatomie anatomie, SelecteurChemin selChemin) {
		this.anatomie = anatomie;
		this.selChemin = selChemin;
	}


	public float coutReproduction(){// TODO
		return 0;
	}


	public Anatomie getAnatomie() {
		return anatomie;
	}


	public void setAnatomie(Anatomie anatomie) {
		this.anatomie = anatomie;
	}


	public SelecteurChemin getSelChemin() {
		return selChemin;
	}


	public void setSelChemin(SelecteurChemin selChemin) {
		this.selChemin = selChemin;
	}

	private class RecycleurEmplacements extends Recycleur<Emplacement>{
		
		public RecycleurEmplacements(){
			super();
		}
		
		protected Emplacement construitNeuf() {
			return new Emplacement();
		}		
	}
	
	/*
	 * RECYCLAGE
	 * 
	 * On recycle ici deux choses : les Emplacement et les EtreVivant
	 */

	private Queue<EtreVivant> etresVivantsRecycles = new ArrayDeque<EtreVivant>();
	private Queue<Emplacement> emplacementsRecycles = new ArrayDeque<Emplacement>();
	
	public void recycle(EtreVivant eVARecycler){
		this.recycle(eVARecycler.getEmplacement());
		eVARecycler.setEmplacement(null);
		
		etresVivantsRecycles.add(eVARecycler);
	}
	private void recycle(Emplacement empARecycler){
		emplacementsRecycles.add(empARecycler);
	}
	
	public Emplacement fournitEmplacement(){
		if(emplacementsRecycles.isEmpty()) return new Emplacement();
		else{
			return emplacementsRecycles.poll();
		}
	}
	//methode voue a remplacer un cnstructeur de maniere avantageuse
	public EtreVivant fournitEtreVivant(Univers univers, Emplacement emplacement, Genome gen){
		if(etresVivantsRecycles.isEmpty()){
			return new EtreVivant(univers, this, emplacement, gen);
		}
		else{
			EtreVivant res = etresVivantsRecycles.poll();
			
			res.U = univers;
			res.emplacement = emplacement;
			res.genome = gen;
			
			res.etendue.clear();
			res.etendue.addAll(emplacement.values());
			
			res.setEnergieAsymptotique(0.0);
			
			return res;
		}
	}
}
