package lois;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.math3.util.FastMath;


import phenotypique.EtreVivant;
import physique.Lettre;
import physique.Mot;
import toolbox.LeProgrammeurEstUnGrosFruitException;

public class LoiEnergieA0 implements LoiEnergie {
	
	private double tempsCaracteristique = 20.0; //TODO PARAMETRE cf "decroissanceEnergetique(EtreVivant E)"
	
	public LoiEnergieA0(double tempsCaracteristique) {
		this.tempsCaracteristique = tempsCaracteristique;
		
		this.table = new HashMap<Integer, Double>();
		this.table.put(0, 0.0);
		this.plafond = 0;
	}

	@Override
	public double energieMot(Mot m) {//TODO PARAMETRE a calibrer
		//La formule choisie ici est (longueur du mot)*(1+ln(nombre de mots qu'on peut former avec les lettres du mot)).
		Map<Lettre, Integer> compteur = new TreeMap<Lettre,Integer>();
		int longueur = 0;
		for(Lettre l : m){
			if(compteur.containsKey(l)) compteur.put(l, compteur.get(l)+ 1);
			else compteur.put(l, 1);
			
			longueur++;
		}
		double e = 1.0;
		int l2 = longueur;
		for(Lettre l : compteur.keySet()){
			e=e+this.logFactorielle(l2)-this.logFactorielle(compteur.get(l))-this.logFactorielle(l2-compteur.get(l));
			l2 = l2 - compteur.get(l);
		}
		e = longueur*e;
		
		return e;
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
		return (energieAsymptotique + ((e.getAncienneEnergie()-energieAsymptotique)*Math.exp((dateInitiale-dateFinale)/this.tempsCaracteristique)));

	}

	private double logFactorielle(int n){//calcule ln(n!). Stocke les valeurs dans table pour reutilisation.
		if(table.containsKey(n)) return table.get(n);
		else{
			for(int i = plafond; i < n; i++) table.put(i+1, (table.get(i) + FastMath.log(i+1)));
			
			plafond = n;
			return table.get(n);
		}
	}
	
	private int plafond;//le plus haut entier >=1 jusqu'auquel on a stocke la valeur de ln(n!) dans table.
	private HashMap<Integer, Double> table;
}
	

