package lois;
import java.util.*;

import phenotypique.Anatomie;
import phenotypique.EtreVivant;
import physique.*;
import toolbox.Fournisseur;
import toolbox.Recupereur;
import toolbox.Recycleur;

import chemins.*;
import chemins.Chemin.QueueChemin;

public abstract class SelecteurChemin {
	/*
	 *  Le travail d'un objet de type SelecteurChemin est de choisir (aleatoirement)
	 * quels sont les chemins formes par un Etre Vivant pour aller d'en sommet d'entree
	 * vers le sommet de sortie.
	 */
	
	
	
	
	public SelecteurChemin(Anatomie anatomie) {
		super();
		this.anatomie = anatomie;
	}
	//le graphe dont on va choisir les chemins.
	protected Anatomie anatomie;

	/*
	 * Prend en parametres un Etre Vivant et un n-uplet de points d'entree de son anatomie.
	 * Renvoie le n-uplet de Chemins formes aleatoirement correspondants.
	 * NB : au cours de ce processus, l'Etre Vivant preleve les ressources des Chemins qu'il forme.
	 */
	public abstract Chemin[] choisitChemins(EtreVivant eV, PointVirtuel[] sommetsEntrees);
	
	
	public Anatomie getAnatomie() {
		return anatomie;
	}
	public void setAnatomie(Anatomie anatomie) {
		this.anatomie = anatomie;
	}
/*
	protected Fournisseur<Chemin> fournisseurC = new Fournisseur<Chemin>() {

		@Override
		public Chemin fournit() {
			return new Chemin();
		}
	};
	protected Fournisseur<QueueChemin> fournisseurQC = new Fournisseur<Chemin.QueueChemin>() {

		@Override
		public Chemin.QueueChemin fournit() {
			return new QueueChemin();
		}
	};
	protected Fournisseur<Chemin> fournisseurCSC = new Fournisseur<Chemin>() {

		@Override
		public Chemin fournit() {
			return new Chemin();
		}
		
	};
	protected Recupereur<Chemin> recupereurCSC = new Recupereur<Chemin>() {

		@Override
		public void recupere(Chemin aRecycler) {
			
		}
	};
	protected Recupereur<Chemin> recupereurC = new Recupereur<Chemin>() {

		@Override
		public void recupere(Chemin aRecycler) {
			// TODO Auto-generated method stub
			
		}
	};

	public void setFournisseurC(Fournisseur<Chemin> fournisseurC) {
		this.fournisseurC = fournisseurC;
	}


	public void setFournisseurQC(Fournisseur<QueueChemin> fournisseurQC) {
		this.fournisseurQC = fournisseurQC;
	}


	public void setRecupereurCSC(Recupereur<Chemin> recupereurCSC) {
		this.recupereurCSC = recupereurCSC;
	}


	public void setRecupereurC(Recupereur<Chemin> recupereurC) {
		this.recupereurC = recupereurC;
	}


	public void setFournisseurCSC(Fournisseur<Chemin> fournisseurCSC) {
		this.fournisseurCSC = fournisseurCSC;
	}
*/
	public static class RecycleurCout extends Recycleur<Cout>{
		//private int cpt;
		@Override
		protected Cout construitNeuf() {
			//System.out.println("nouveau Cout" + cpt);
			//cpt++;
			
			return new Cout();
		}
		
	}

	protected Recycleur<Cout> recycleurCouts = new RecycleurCout();

	public void setRecycleurCouts(Recycleur<Cout> recycleurCouts) {
		this.recycleurCouts = recycleurCouts;
	}
}
