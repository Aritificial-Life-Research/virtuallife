package phenotypique;

import genomique.Genome;

import java.util.*;

import lois.SelecteurChemin;

import physique.*;
import toolbox.LeProgrammeurEstUnGrosFruitException;
import toolbox.Recyclable;

import chemins.*;

public class EtreVivant implements Recyclable{


	Univers U;
	
	Phenotype ph;
	
	Genome genome;
	
	Emplacement emplacement;//Ceci associe les points reels de l'Univers aux points virtuels de l'anatomie.
	//l'ensemble de depart de emplacement se veut etre l'ensemble des points de Anatomie. Doit etre INJECTIVE.
	
	//retient les ressources prelevees en chaque point pour former des chemins.
	public Map<PointVirtuel, Cout> ressourcesPrelevees;
	
	public Map<PointVirtuel, Mot> motsAFormer;
	/* Ceci associe, a chaque point d'ENTREE de l'anatomie, le mot que forme l'etre vivant en le point reel associe.
	 * Le domaine de definition se veut donc l'ensemble des points d'entree de Anatomie.
	 */
	public Map<PointVirtuel, Chemin> cheminsRestants;
	/*
	 * Ceci associe, a chaque point d'ENTREE de l'anatomie, le reste du chemin a faire parcourir aux ressources de ce sommet pour former le Mot choisi.
	 * Le domaine de definition se veut donc l'ensemble des points d'entree de Anatomie.
	 */
	
	private double energieAsymptotique;
	private double ancienneEnergie;//la derniere valeur conneue de l'energie de l'etre vivant
	private double dateDerniereEnergie;//la date la plus recente a laquelle l'energie de l'etre vivant etait connue
	
	public EtreVivant(Univers u, Phenotype ph, Emplacement emplacement, Genome gen) {//(fait naitre l'etre vivant) TODO : obsolete depuis paradigme des Evenements
		
		U = u;
		this.ph = ph;
		this.emplacement = emplacement;
		this.genome = gen;
		
		this.motsAFormer = new HashMap<PointVirtuel, Mot>(4);
		this.cheminsRestants = new HashMap<PointVirtuel, Chemin>(4);
		
		//initialisation de ressourcesPrelevees
		this.ressourcesPrelevees = new HashMap<PointVirtuel, Cout>();
		for(PointVirtuel pv : this.emplacement.keySet()){
			this.ressourcesPrelevees.put(pv, new Cout());
		}
		
		//initialise etendue
		etendue = new HashSet<Point>();
		etendue.addAll(emplacement.values());
		
		this.energieAsymptotique = 0.0;

	}

	public Phenotype getPh() {
		return ph;
	}

	public void setPh(Phenotype ph) {
		this.ph = ph;
	}
	
	public double getEnergie() {
		double dateActuelle = this.U.friseDuFutur.getDateActuelle();
		if(this.dateDerniereEnergie==dateActuelle) return this.ancienneEnergie;
		else{
			this.ancienneEnergie = this.U.loiEnergie.variationEnergetique(this, dateDerniereEnergie, dateActuelle);
			this.dateDerniereEnergie = dateActuelle;
			return this.ancienneEnergie;
		}
	}

	public void setEnergie(double energie) {
		this.ancienneEnergie = energie;
		this.dateDerniereEnergie = this.U.friseDuFutur.getDateActuelle();
	}

	public Genome getGen() {
		return genome;
	}

	public void setGen(Genome gen) {
		this.genome = gen;
	}

	public Emplacement getEmplacement() {
		return emplacement;
	}

	public void setEmplacement(Emplacement emplacement) {
		this.emplacement = emplacement;
	}


	public Univers getU() {
		return U;
	}


	public void setU(Univers u) {
		U = u;
	}


	public Map<PointVirtuel, Mot> getMotsAFormer() {
		return motsAFormer;
	}


	public void setMotsAFormer(Map<PointVirtuel, Mot> motsAFormer) {
		this.motsAFormer = motsAFormer;
	}


	public Map<PointVirtuel, Chemin> getCheminsRestants() {
		return cheminsRestants;
	}


	public void setCheminsRestants(Map<PointVirtuel, Chemin> cheminsRestants) {
		this.cheminsRestants = cheminsRestants;
	}
	
	public final Set<Point> etendue;//l'ensemble des Point sur lesquels s'entend l'Etre Vivant.

	public double getEnergieAsymptotique() {
		return energieAsymptotique;
	}

	public void setEnergieAsymptotique(double energieAsymptotique) {
		this.energieAsymptotique = energieAsymptotique;
	}

	public double getAncienneEnergie() {
		return ancienneEnergie;
	}

	public boolean ressourceDisponible(Lettre l, PointVirtuel lieu){
		return U.getRessources(this.emplacement.get(lieu), l)>0;
	}
	
	/*
	 *   Preleve la ressource l en parametre en le PointVirtuel lieu;
	 *   Cette ressource sera retenue par l'EtreVivant jusqu'a sa mort, et
	 * donc absente des ressources minerales de l'Univers courant.
	 *   Attention : cette methode ne declenche pas d'Evenement jusqu'a nouvel ordre.
	 * Elle est appelee depuis le SelecteurChemin associe a cet EtreVivant, au cours
	 * du processus de formation de mot.
	 */
	public void preleveRessource(Lettre prelevee, PointVirtuel lieu){
		Point pointReel = this.emplacement.get(lieu);
		if(U.getRessources(pointReel, prelevee)<1){//la ressource est absente, ce qui est un cas degenere
			throw new LeProgrammeurEstUnGrosFruitException("il est interdit de prelever une ressource absente de l'Univers");
		}
		U.getRessources().get(pointReel).put(prelevee, U.getRessources(pointReel, prelevee)-1);//ressource enlevee du point concerne
		this.ressourcesPrelevees.get(lieu).CoutPlusPlus(prelevee);//la ressource est comptee comme retenue par l'EtreVivant
	}
	
	@Override
	public void nettoie() {
	}
}
