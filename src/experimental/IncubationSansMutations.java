package experimental;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.math3.distribution.ExponentialDistribution;

import chemins.Chemin.RecycleurChemin;
import chemins.Chemin.RecycleurChemin;

import evenements.Evenement;
import evenements.GenreEvenement;
import evenements.MortEtreVivant;
import evenements.NaissanceEtreVivant;
import evenements.VariationEnergieAsymptotique;
import genomique.Genome;

import aleatoire.Urne;
import aleatoire.UrneSeijin;

import lois.Duplicateur;
import lois.DuplicateurA0;
import lois.DuplicateurA1;
import lois.DuplicateurANichesIsotrope;
import lois.DuplicateurB0;
import lois.DuplicateurC0;
import lois.LoiDateProgressionMot;
import lois.LoiDateProgressionMotA0;
import lois.LoiDateReproduction;
import lois.LoiDateReproductionA0;
import lois.LoiDateReproductionA1;
import lois.LoiDateReproductionB0;
import lois.LoiDateReproductionC0;
import lois.LoiEnergie;
import lois.LoiEnergieA0;
import lois.LoiEnergieB0;
import lois.Niche;
import lois.NicheA1;
import lois.SelecteurCheminECECB;

import phenotypique.Emplacement;
import phenotypique.EtreVivant;
import phenotypique.Phenotype;
import physique.Cout;
import physique.PointVirtuel;
import physique.Univers;
import physique.Point;
import temporel.Attentif;
import temporel.GestionnaireEVTUniverselA0;
import temporel.GestionnairedEvenements;
import toolbox.Doublet;
import toolbox.LeProgrammeurEstUnGrosFruitException;
import physique.Lettre;


public class IncubationSansMutations {
	
	//l'univers de travail
	public Univers U;
	public Bestiaire bestiaire;
	//parametres experimentaux : TODO a explorer
	
	//parametre de Bernouilli si on tente d'enrichir un mot en un point d'insertion donne.
	private static double pEnrichissementMot=0.6;
	
	//parametre de Bernouilli pour choisir comment est optimise le lieu de reproduction :
	private static double pReproduction=0.9;
	
	//temps caracteristique employe pour la loi de reproduction :
	private static double tempsReproduction=20.0;
	//temps caracteristique de decroissance energetique :
	private static double tempsVariationEnergetique = 100.0;
	
	//ressources attribuees a chaque Point de l'Univers pendant son initialisation.
	private static Cout ressourcesInitiales;
	
	public static void main(String[] args){
		Bestiaire.initialiseBestiaire(pEnrichissementMot);//obligatoire avant toute incubation
		//fabriqueFichierCourbesOccupationTypes(9, 100_000);
		
		long tInit = System.currentTimeMillis();
		evolutionsSimuleesSuccessives();
		long tFinal = System.currentTimeMillis();
		System.out.println("temps total : " + (tFinal-tInit)/1000.0 + "s");
	}
	/*
	 * EXPERIENCES
	 */
	public IncubationSansMutations(){
		this.bestiaire = new Bestiaire();
		ressourcesInitiales = fabriqueCout(50,50,50,50,50);//ce n'est pas un probleme.
	}
	
	
	/*
	 * PROTOCOLES EXPERIMENTAUX
	 */
	
	/* EXPERIENCE A :
	 * Cette experience est la plus elementaires des experiences de selection naturelle.
	 * On dispose d'un ensemble d' organismes modeles : on veut savoir si un organisme o1
	 * est plus adapte qu'un organisme o2.
	 * Pour cela, on implante o1 et o2 dans un Univers vierge, et on regarde quelles especes
	 * sont encore presentes a la fin de l'experience.
	 * On effectue cela 100 fois de suite (statistiques obligent) et on obtient des pourcentages de survie a la fin.
	 */
	public boolean[] experienceA1(Espece o1, Espece o2){
		Phenotype ph1 = this.bestiaire.specimenDe.get(o1);
		Phenotype ph2 = this.bestiaire.specimenDe.get(o2);
			
		this.initialiseUnivers(1000);
		this.suitEvolutionPopulations();
			
		this.implanteEtreVivant(ph1, 10.0);
		this.implanteEtreVivant(ph2, 10.0);
			
		this.U.ecouleLeTemps(10000.0);
		
		boolean survie1 = this.populationsEnVie.containsKey(ph1)&&this.populationsEnVie.get(ph1)>0;
		boolean survie2 = this.populationsEnVie.containsKey(ph2)&&this.populationsEnVie.get(ph2)>0;
			
		boolean[] res = new boolean[2];
		res[0] = survie1;
		res[1] = survie2;
			
		return res;	
}
	
	public void experienceA(Espece o1, Espece o2){
				
			System.out.println(o1.nom() + " versus " + o2.nom() + " : ");
				
			int cptSurvie1 = 0;
			int cptSurvie2 = 0;
			int cptSurvie1_2 = 0;
				
			for(int i = 0; i < 100; i++){
				Phenotype ph1 = this.bestiaire.specimenDe.get(o1);
				Phenotype ph2 = this.bestiaire.specimenDe.get(o2);
					
				this.initialiseUnivers(1000);
				this.suitEvolutionPopulations();
					
				this.implanteEtreVivant(ph1, 10.0);
				this.implanteEtreVivant(ph2, 10.0);
					
				this.U.ecouleLeTemps(10000.0);
				
				boolean survie1 = this.populationsEnVie.containsKey(ph1)&&this.populationsEnVie.get(ph1)>0;
				boolean survie2 = this.populationsEnVie.containsKey(ph2)&&this.populationsEnVie.get(ph2)>0;
					
				if(survie1) cptSurvie1++;
				if(survie2) cptSurvie2++;
				if(survie1&&survie2) cptSurvie1_2++;
			}
			System.out.println("survie de " + o1.nom() + " : " + cptSurvie1 + "%");
			System.out.println("survie de " + o2.nom() + " : " + cptSurvie2 + "%");
			System.out.println("survie de " + o1.nom() + " et " + o2.nom() + " : " + cptSurvie1_2 + "%");
			System.out.println();
	}
	/*
	 * Cette experience implante un specimen des organismes en parametres,
	 * et compare les population des deux especes a la fin.
	 */
	public static void experiencePerformances(){
		IncubationSansMutations exp = new IncubationSansMutations();
		exp.initialiseUnivers(2000, exp.ressourcesInitiales);
		
		exp.implanteEtreVivant(exp.bestiaire.pVar, 20.0);
		
		long tInit = System.currentTimeMillis();
		
		exp.U.ecouleLeTemps(300000);
		
		long tFinal = System.currentTimeMillis();
		System.out.println("temps total : " + (tFinal-tInit)/1000.0 + " s");
	}
	public static void experienceExemple(){
		long tInit = System.currentTimeMillis();
		//premiere experience : incubation d'une espece seule;
		System.out.println("***************************************************");
		System.out.println("******************EXPERIENCE 1*********************");
		System.out.println("***************************************************");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IncubationSansMutations exp1 = new IncubationSansMutations();
		exp1.initialiseUnivers(1000, ressourcesInitiales);
		
		exp1.suitEvolutionPopulations();
		exp1.echantillonneDureesDeVieParEspece();
		
		exp1.implanteEtreVivant(exp1.bestiaire.pSortParL3, 10.0);
		
		exp1.U.ecouleLeTemps(20000);

		exp1.ecritFichierTexte("ressources1", exp1.printDistributionRessources());
		exp1.ecritFichierTexte("Exp1_courbes_occupation", exp1.fabriqueCourbesOccupation(Bestiaire.espSortParL3));
		exp1.ecritFichierTexte("Exp1_durees_de_vie", exp1.fabriqueCumulativesDureesDeVie(Bestiaire.espSortParL3));
		System.out.println("plus haute energie" + exp1.plusHauteEnergie());
		System.out.println("energie moyenne" + exp1.energieMoyenne());
		//deuxieme experience : colonisations concurrentes
		System.out.println("***************************************************");
		System.out.println("******************EXPERIENCE 2*********************");
		System.out.println("***************************************************");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IncubationSansMutations exp2 = new IncubationSansMutations();
		exp2.initialiseUnivers(1000, exp2.ressourcesInitiales);
		
		exp2.suitEvolutionPopulations();
		
		exp2.implanteEtreVivant(exp2.bestiaire.pVar, 10.0);
		exp2.implanteEtreVivant(exp2.bestiaire.pMin, 10.0);
		
		exp2.U.ecouleLeTemps(8000);
		
		exp2.ecritFichierTexte("Exp2_courbes_occupation", exp2.fabriqueCourbesOccupation(Bestiaire.espMin, Bestiaire.espVar));
		
		//troisieme experience : evolutions simulees
		System.out.println("***************************************************");
		System.out.println("******************EXPERIENCE 3*********************");
		System.out.println("***************************************************");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IncubationSansMutations exp3 = new IncubationSansMutations();
		
		exp3.initialiseUnivers(1000, exp3.fabriqueCout(200,200,200));
		
		exp3.suitEvolutionPopulations();
		exp3.echantillonneDureesDeVieParEspece();
		
		exp3.implanteEtreVivant(exp3.bestiaire.pMin, 50.0);
		exp3.U.ecouleLeTemps(2000.0);
		
		exp3.implanteEtreVivant(exp3.bestiaire.pDA, exp3.plusHauteEnergie());
		exp3.U.ecouleLeTemps(5000.0);
		
		exp3.implanteEtreVivant(exp3.bestiaire.pEnV, exp3.plusHauteEnergie());
		exp3.U.ecouleLeTemps(8000.0);
		
		exp3.implanteEtreVivant(exp3.bestiaire.pEtp, exp3.plusHauteEnergie());
		exp3.U.ecouleLeTemps(15000.0);
		
		exp3.implanteEtreVivant(exp3.bestiaire.pCycEtp, exp3.plusHauteEnergie());
		exp3.U.ecouleLeTemps(20000.0);
		
		exp3.implanteEtreVivant(exp3.bestiaire.pVar, exp3.plusHauteEnergie());
		exp3.U.ecouleLeTemps(30000.0);
		exp3.ecritFichierTexte("Exp3_Courbes_Occupation", exp3.fabriqueCourbesOccupation(Bestiaire.espMin,Bestiaire.espDA,Bestiaire.espEnV,Bestiaire.espEtp,Bestiaire.espEtR,Bestiaire.espEtBR,Bestiaire.espCycEtp, Bestiaire.espVar));
		exp3.ecritFichierTexte("Exp3_durees_de_vie", exp3.fabriqueCumulativesDureesDeVie(Bestiaire.espMin,Bestiaire.espDA,Bestiaire.espEnV,Bestiaire.espEtp,Bestiaire.espEtR,Bestiaire.espEtBR,Bestiaire.espCycEtp, Bestiaire.espVar));
		
		long tFinal = System.currentTimeMillis();
		System.out.println("temps total : " + (tFinal-tInit)/1000.0 + " s");
	}
	
	public void colonisationsConcurrentes(Espece... colons){
		initialiseUnivers(1000);
		this.echantillonneDureesDeVieParEspece();
		
		suitEvolutionPopulations();
		
		for(int i=0; i<colons.length; i++) implanteEtreVivant(bestiaire.specimenDe.get(colons[i]), 100.0);
		
		U.ecouleLeTemps(10000);
		
	}
	
	public static void evolutionsSimuleesSuccessives(){
		
		IncubationSansMutations exp = new IncubationSansMutations();
		
		exp.initialiseUnivers(1000, exp.fabriqueCout(200,200,200));
		
		exp.suitEvolutionPopulations();
		exp.echantillonneDureesDeVieParEspece();
		
		exp.implanteEtreVivant(exp.bestiaire.pMin, 50.0);
		exp.U.ecouleLeTemps(1000.0);
		
		exp.implanteEtreVivant(exp.bestiaire.pDA, exp.plusHauteEnergie());
		exp.U.ecouleLeTemps(5000.0);
		
		exp.implanteEtreVivant(exp.bestiaire.pEnV, exp.plusHauteEnergie());
		exp.U.ecouleLeTemps(10000.0);
		
		exp.implanteEtreVivant(exp.bestiaire.pEtp, exp.plusHauteEnergie());
		exp.U.ecouleLeTemps(15000.0);
		
		exp.implanteEtreVivant(exp.bestiaire.pEtR, exp.plusHauteEnergie());
		exp.U.ecouleLeTemps(20000.0);
		
		exp.implanteEtreVivant(exp.bestiaire.pEtBR, exp.plusHauteEnergie());
		exp.U.ecouleLeTemps(25000.0);
		
		exp.implanteEtreVivant(exp.bestiaire.pCycEtp, exp.plusHauteEnergie());
		exp.U.ecouleLeTemps(40000.0);
		
		exp.implanteEtreVivant(exp.bestiaire.pVar, exp.plusHauteEnergie());
		exp.U.ecouleLeTemps(80000.0);
		exp.ecritFichierTexte("variations-populations", exp.fabriqueCourbesOccupation(Bestiaire.espMin,Bestiaire.espDA,Bestiaire.espEnV,Bestiaire.espEtp,Bestiaire.espEtR,Bestiaire.espEtBR,Bestiaire.espCycEtp, Bestiaire.espVar));
		exp.ecritFichierTexte("durees_de_vie", exp.fabriqueCumulativesDureesDeVie(Bestiaire.espMin,Bestiaire.espDA,Bestiaire.espEnV,Bestiaire.espEtp,Bestiaire.espEtR,Bestiaire.espEtBR,Bestiaire.espCycEtp, Bestiaire.espVar));
		exp.ecritFichierTexte("ressources", exp.printDistributionRessources());
	}
	/*
	 * OUTILS D'INITIALISATION.
	 */
	//initialise l'Univers avec les parametres d'experience.
 	public void initialiseUnivers(int nombrePoints){
		
		Set<Point> points = new HashSet<Point>(nombrePoints, (float) 1.0);
		
		for(int i=0; i<nombrePoints;i++){
			points.add(new Point(i));
		}
		
		U = new Univers(points, Bestiaire.ensLettres);
		
		LoiEnergie loiEnergie = new LoiEnergieA0(tempsVariationEnergetique);
		Duplicateur duplicateur = new DuplicateurC0(U, pReproduction);
		LoiDateReproduction loiDateReproduction = new LoiDateReproductionB0(tempsReproduction);
		LoiDateProgressionMot loiDateProgressionMot = new LoiDateProgressionMotA0(0.1);
		
		GestionnairedEvenements gestionnaireGlobal = new GestionnaireEVTUniverselA0(U);

		U.setLois(gestionnaireGlobal, duplicateur, loiEnergie, loiDateProgressionMot, loiDateReproduction);
		
		remplitUnivers(ressourcesInitiales);
		
	}
 	public void initialiseUnivers(int nombrePoints, Cout ressInitiales){
		
		Set<Point> points = new HashSet<Point>(nombrePoints, (float) 1.0);
		
		for(int i=0; i<nombrePoints;i++){
			points.add(new Point(i));
		}
		
		U = new Univers(points, Bestiaire.ensLettres);
		
		LoiEnergie loiEnergie = new LoiEnergieA0(tempsVariationEnergetique);
		Duplicateur duplicateur = new DuplicateurC0(U, pReproduction);
		LoiDateReproduction loiDateReproduction = new LoiDateReproductionB0(tempsReproduction);
		LoiDateProgressionMot loiDateProgressionMot = new LoiDateProgressionMotA0(0.1);
		
		GestionnairedEvenements gestionnaireGlobal = new GestionnaireEVTUniverselA0(U);

		U.setLois(gestionnaireGlobal, duplicateur, loiEnergie, loiDateProgressionMot, loiDateReproduction);
		
		remplitUnivers(ressInitiales);
		
	}
 	/*
 	 * fabrique univers avec un duplicateur a niches isotrope qui se presente comme un carre.
 	 * Ressources homogenes.
 	 */
 	public void initialiseUniversMaille(int cote, int nbPointsParNiche, Cout ressInitiales){
 		//compteurs
 		int cPts = 0;
 		Set<Point> points = new HashSet<Point>();
 		Point[] numsP = new Point[cote*cote*nbPointsParNiche]; 
 		
 		for(int x = 0; x < cote; x++){//creation de l'ensemble des points
 			for(int y = 0; y < cote; y++){
 				for(int i = 0; i < nbPointsParNiche; i++){
 					Point p = new Point(cPts);
 					points.add(p);
 					numsP[cPts] = p;
 					cPts++;
 				}
 			}
 		}
 		U = new Univers(points, Bestiaire.ensLettres);
 		
 		int cNiches = 0;
 		Niche[][] tabNiches = new Niche[cote][cote];
 		Collection<Niche> niches = new LinkedList<Niche>();
 		
 		cPts = 0;
 		for(int x = 0; x < cote; x++){//creation des niches
 			for(int y = 0; y < cote; y++){
 				Set<Point> pts = new HashSet<Point>();
 				for(int i = 0; i < nbPointsParNiche; i++){
 					pts.add(numsP[cPts]);
 					cPts++;
 				}
 				Niche niche = new NicheA1(U, pts, cNiches);
 				tabNiches[x][y] = niche;
 				niches.add(niche);
 				cNiches++;
 			}
 		}
 		
 		DuplicateurANichesIsotrope duplicateur = new DuplicateurANichesIsotrope(U, niches);
 		
 		//les 4 coins
 		duplicateur.setContiguites(tabNiches[0][0],tabNiches[0][1],tabNiches[1][0],tabNiches[0][0]);
 		duplicateur.setContiguites(tabNiches[0][cote-1],tabNiches[0][cote-2],tabNiches[1][cote-1],tabNiches[0][cote-1]);
 		duplicateur.setContiguites(tabNiches[cote-1][0],tabNiches[cote-1][1],tabNiches[cote-2][0],tabNiches[cote-1][0]);
 		duplicateur.setContiguites(tabNiches[cote-1][cote-1],tabNiches[cote-1][cote-2],tabNiches[cote-2][cote-1],tabNiches[cote-1][cote-1]);
 		
 		//les 4 bords
 		for(int y = 1; y < cote-1; y++){
 			duplicateur.setContiguites(tabNiches[0][y],tabNiches[0][y+1],tabNiches[0][y-1], tabNiches[1][y],tabNiches[0][y]);
 			duplicateur.setContiguites(tabNiches[cote-1][y],tabNiches[cote-1][y+1],tabNiches[cote-1][y-1], tabNiches[cote-1][y],tabNiches[cote-1][y]); 		
 		}
 		for(int x = 1; x < cote-1; x++){
 			duplicateur.setContiguites(tabNiches[x][0],tabNiches[x+1][0],tabNiches[x-1][0],tabNiches[x][1],tabNiches[x][0]);
 			duplicateur.setContiguites(tabNiches[x][cote-1],tabNiches[x+1][cote-1],tabNiches[x-1][cote-1],tabNiches[x][cote-2],tabNiches[x][cote-1]);
 		}
 		
 		//l'interieur
 		for(int x = 1; x < cote-1; x++){
 			for(int y = 1; y < cote-1; y++){
 				duplicateur.setContiguites(tabNiches[x][y],tabNiches[x+1][y],tabNiches[x-1][y],tabNiches[x][y+1],tabNiches[x][y-1],tabNiches[x][y]);
 			}
 		}
 		
 		//fin de l'initialisation de l'univers
		LoiEnergie loiEnergie = new LoiEnergieA0(tempsVariationEnergetique);
		LoiDateReproduction loiDateReproduction = new LoiDateReproductionC0(tempsReproduction);
		LoiDateProgressionMot loiDateProgressionMot = new LoiDateProgressionMotA0(0.1);
		
		GestionnairedEvenements gestionnaireGlobal = new GestionnaireEVTUniverselA0(U);

		U.setLois(gestionnaireGlobal, duplicateur, loiEnergie, loiDateProgressionMot, loiDateReproduction);
		
		remplitUnivers(ressInitiales);
 	}
 	
 	public void initialiseUniversTore(int largeur, int hauteur, int nbPointsParNiche, Cout ressInitiales){
 		//compteurs
 		int cPts = 0;
 		Set<Point> points = new HashSet<Point>();
 		Point[] numsP = new Point[largeur*hauteur*nbPointsParNiche]; 
 		
 		for(int x = 0; x < largeur; x++){//creation de l'ensemble des points
 			for(int y = 0; y < hauteur; y++){
 				for(int i = 0; i < nbPointsParNiche; i++){
 					Point p = new Point(cPts);
 					points.add(p);
 					numsP[cPts] = p;
 					cPts++;
 				}
 			}
 		}
 		U = new Univers(points, Bestiaire.ensLettres);
 		
 		int cNiches = 0;
 		Niche[][] tabNiches = new Niche[largeur][hauteur];
 		Collection<Niche> niches = new LinkedList<Niche>();
 		
 		cPts = 0;
 		for(int x = 0; x < largeur; x++){//creation des niches
 			for(int y = 0; y < hauteur; y++){
 				Set<Point> pts = new HashSet<Point>();
 				for(int i = 0; i < nbPointsParNiche; i++){
 					pts.add(numsP[cPts]);
 					cPts++;
 				}
 				Niche niche = new NicheA1(U, pts, cNiches);
 				tabNiches[x][y] = niche;
 				niches.add(niche);
 				cNiches++;
 			}
 		}
 		
 		DuplicateurANichesIsotrope duplicateur = new DuplicateurANichesIsotrope(U, niches);
 		
 		//l'interieur
 		for(int x = 0; x < largeur; x++){
 			for(int y = 0; y < hauteur; y++){
 				duplicateur.setContiguites(tabNiches[x][y],tabNiches[(x+1) % largeur][y],tabNiches[(x-1+largeur) % largeur][y],tabNiches[x][(y+1) % hauteur],tabNiches[x][(y-1+hauteur) % hauteur],tabNiches[x][y]);
 			}
 		}
 		
 		//fin de l'initialisation de l'univers
		LoiEnergie loiEnergie = new LoiEnergieA0(tempsVariationEnergetique);
		LoiDateReproduction loiDateReproduction = new LoiDateReproductionC0(tempsReproduction);
		LoiDateProgressionMot loiDateProgressionMot = new LoiDateProgressionMotA0(0.1);
		
		GestionnairedEvenements gestionnaireGlobal = new GestionnaireEVTUniverselA0(U);

		U.setLois(gestionnaireGlobal, duplicateur, loiEnergie, loiDateProgressionMot, loiDateReproduction);
		
		remplitUnivers(ressInitiales);
 	}
	/*
	 * Fait naitre un EtreVivant du Phenotype specifie dans l'Univers.
	 * Pour choisir l'emplacement d'implantation, se base sur le meme principe que
	 * DuplicateurA0.
	 */
	private EtreVivant implanteEtreVivant(Phenotype aImplanter, double energieInitiale){
		
		aImplanter.getSelChemin().setRecycleurCouts(U.recycleurCouts);//TODO
		
		Set<PointVirtuel> PVDepart = aImplanter.getAnatomie().getSommets();//les sommets du phenotype
		
		Emplacement emplacement = new Emplacement();//l'emplacement final du nouveau ne, a remplir.
		

		//etape 1 : tirage barycentrique de points cibles initiaux en fonction de l'energie.
	
		//initialisation de l'urne avec des cefficients dependant de l'energie. ATTENTION : on pressuppose que tous les points de l'Univers sont deja dans l'Urne.

		/*for(Point ext : this.U.getPoints()){//initialisation de l'urne avec des cefficients dependant de l'energie. ATTENTION : on pressuppose que tous les points de l'Univers sont deja dans l'Urne.
			double coefficient = poids(U.energieDelOccupant(ext));
			urnePoints.modifiePoids(ext, coefficient);
		}*/
		Urne<Point> urnePoints = new UrneSeijin<Point>();
		urnePoints.initialiseUniforme(U.getPoints());
		
		
		Map<Point, Double> ponderation = new Map<Point,Double>(){
			@Override
			public void clear() {
				
			}

			@Override
			public boolean containsKey(Object arg0) {
				return false;
			}

			@Override
			public boolean containsValue(Object arg0) {
				return false;
			}

			@Override
			public Set<java.util.Map.Entry<Point, Double>> entrySet() {
				return null;
			}

			@Override
			public Double get(Object arg0) {
				return DuplicateurA0.poids(U.energieDelOccupant((Point)arg0));
			}

			@Override
			public boolean isEmpty() {
				return false;
			}

			@Override
			public Set<Point> keySet() {
				return U.getPoints();
			}

			@Override
			public Double put(Point arg0, Double arg1) {
				return null;
			}

			@Override
			public void putAll(Map<? extends Point, ? extends Double> arg0) {
				
			}

			@Override
			public Double remove(Object arg0) {
				return null;
			}

			@Override
			public int size() {
				return 0;
			}

			@Override
			public Collection<Double> values() {
				return null;
			}
			
		};
		urnePoints.modifiePoidsToutesBoules(ponderation);

		for(PointVirtuel depart : PVDepart){//on initialise emplacement
			if(urnePoints.isEmpty()) throw new LeProgrammeurEstUnGrosFruitException("L'Univers est trop petit pour y implanter un etreVivant de cette taille.");
			else{
				Point cibleInitiale = urnePoints.tireSansRemise();
				emplacement.put(depart, cibleInitiale);
			}
		}
		
		//etape 2 : on rearrange les points cibles pour "eviter les morts inutiles"
		
		Set<Point> PointsExterieursImpliques = new HashSet<Point>();
		Set<Point> PointsImpliquesLibres = new HashSet<Point>();//les points n'etant pas des cibles de pointeurs, a ne pas confondre avec des points non occuppes par des etres vivants.
		
		Set<EtreVivant> EtresVivantsImpliques = new HashSet<EtreVivant>();
		
		Set<Set<Point>> blocs = new HashSet<Set<Point>>();//un bloc correspond soit a l'ensembles des points sur lesquels s'etend un etre vivant, soit a un singleton d'un etre vivant non occupe
		Map<Set<Point>, Integer> cardinalDuBloc = new HashMap<Set<Point>, Integer>();//associe, a chaque bloc, le nombre de ses points.
		Map<Set<Point>, Integer> nombreCiblesDansBloc = new HashMap<Set<Point>, Integer>();//associe, a chaque bloc, le nombre de ses points qui est cible d'un PoinVirtuel du phenotype, via emplacement.
		Map<Set<Point>, Double> saturationDuBloc = new HashMap<Set<Point>, Double>();//le rapport (nmbre cibles)/cardinal

		Map<Point, Set<Point>> blocDuPoint = new HashMap<Point, Set<Point>>();//permet, etant donne un point, de remonter a son bloc.
		
		for(PointVirtuel pv : emplacement.keySet()){//les points de chaque etre vivant dont l'un des points est un point cible sont impliques
			Point cible = emplacement.get(pv);
			if(U.getOccupation().containsKey(cible)){//si le point est occupe par un EtreVivant
				EtreVivant concerne = U.getOccupation().get(cible);
				for(PointVirtuel nouveauPV : concerne.getEmplacement().keySet()){
					Point nouveauP = concerne.getEmplacement().get(nouveauPV);
					PointsExterieursImpliques.add(nouveauP);
					PointsImpliquesLibres.add(nouveauP);
				}
				//TODO c'est ce qui manquait... j'en ai marre d'etre con
				EtresVivantsImpliques.add(concerne);
			}
			else{//si le point n'est pas occuppe
				PointsExterieursImpliques.add(cible);
				
				Set<Point> bloc = new HashSet<Point>();
				bloc.add(cible);
				blocs.add(bloc);
				
				cardinalDuBloc.put(bloc, 1);
				nombreCiblesDansBloc.put(bloc, 1);
				
				blocDuPoint.put(cible, bloc);
			}
		}
		for(EtreVivant ev : EtresVivantsImpliques){//on forme ici les blocs correspondant aux etres vivants impliques
			Set<Point> bloc = new HashSet<Point>();
			int cpt = 0;
			for(PointVirtuel pv : ev.getEmplacement().keySet()){
				Point element = ev.getEmplacement().get(pv);
				bloc.add(element);
				blocDuPoint.put(element, bloc);
				cpt++;
			}
			blocs.add(bloc);
			
			cardinalDuBloc.put(bloc, cpt);
			nombreCiblesDansBloc.put(bloc, 0);//provisoirement, cf. boucle suivante
		}
		
		for(PointVirtuel pv : emplacement.keySet()){//on enleve les points cibles de PointsImpliquesLibres pour que ce dernier corresponde  sa definition
			//De meme, on remplit nombreCiblesDansBloc
			Point cible = emplacement.get(pv);
			PointsImpliquesLibres.remove(cible);
			
			Set<Point> bloc = blocDuPoint.get(cible);//deja initialise plus haut
			nombreCiblesDansBloc.put(bloc, nombreCiblesDansBloc.get(bloc)+1);
		}
		
		for(Set<Point> bloc : blocs){//initialise saturationDuBloc
			saturationDuBloc.put(bloc, (nombreCiblesDansBloc.get(bloc)*1.0)/(cardinalDuBloc.get(bloc)*1.0));
		}
		
		Urne<PointVirtuel> pointsDepart = new UrneSeijin<PointVirtuel>();
		Urne<Point> pointsCiblesLibres = new UrneSeijin<Point>();
		
		for(PointVirtuel pv : emplacement.keySet()){//initialise l'urne pointsDepart
			pointsDepart.ajouteBoule(pv, 1-saturationDuBloc.get(blocDuPoint.get(emplacement.get(pv))));
		}
		
		pointsCiblesLibres.initialiseUniforme(PointsImpliquesLibres);//initialise l'urne pointsCiblesLibres
		
		while(Math.random() < pReproduction){//TODO ca marche ca? On decide aleatoirement si on continue de rearranger les points cibles
			/*
			 * On "optimise aleatoirement" de la maniere suivante :
			 * On choisit un PointVirtuel de depart en fonction de 1-saturation de sa cible
			 * On choisit un Point implique libre uniformement parmi les points libres.
			 * Ce dernier Point devient la nouvelle cible du PointVirtuel de depart.
			 */
			if(!pointsDepart.isEmpty()){
				if(!pointsCiblesLibres.isEmpty()){
					PointVirtuel depart = pointsDepart.tireSansRemise();
					
					Point ancienneCible = emplacement.get(depart);
					Point nouvelleCible = pointsCiblesLibres.tireSansRemise();
					
					emplacement.put(depart, nouvelleCible);//la cible est changee
					
					//on met ensuite a jour les donnes sur les blocs
					PointsImpliquesLibres.remove(nouvelleCible);//nouvelleCible n'est plus libre
					PointsImpliquesLibres.add(ancienneCible);//ancienneCible est libre
					
					Set<Point> ancienBloc = blocDuPoint.get(ancienneCible);
					Set<Point> nouveauBloc = blocDuPoint.get(nouvelleCible);
					
					nombreCiblesDansBloc.put(ancienBloc, nombreCiblesDansBloc.get(ancienBloc) - 1);
					nombreCiblesDansBloc.put(nouveauBloc, nombreCiblesDansBloc.get(nouveauBloc) + 1);
					
					saturationDuBloc.put(ancienBloc, saturationDuBloc.get(ancienBloc) - (1.0/cardinalDuBloc.get(ancienBloc)));
					saturationDuBloc.put(nouveauBloc, saturationDuBloc.get(nouveauBloc) + (1.0/cardinalDuBloc.get(nouveauBloc)));
					
					//on remets les boules qui vont bien dans les urnes
					pointsDepart.ajouteBoule(depart, 1 - saturationDuBloc.get(nouveauBloc));
					pointsCiblesLibres.ajouteBoule(ancienneCible, 1.0);
				}
			}
		}
		EtreVivant implante = new EtreVivant(U, aImplanter, emplacement, genomeTrivial);
		U.gestionnaireGlobal.prendEnCompte(new NaissanceEtreVivant(U, implante, energieInitiale));
		
		return implante;
	}
	
	private Cout fabriqueCout(int... valeurs){
		/*
		 * fabrique un cout a partir d'un tableau de nombre.
		 * Attention :
		 * 	- il n'y doit pas y avoir plus de nombres que de lettres dans l'alphabet
		 * 	- bien penser a mettre les valeurs nulles, meme si elles n'apparaitront pas dans le Cout retourne.
		 */
		Cout c = new Cout();
		if(valeurs.length > Bestiaire.getAlphabet().length) throw new LeProgrammeurEstUnGrosFruitException("Il n'y a pas assez de lettres!");
		
		for (int i = 0; i < valeurs.length; i++) {
			int q = valeurs[i];
			if(q!=0) c.put(Bestiaire.getAlphabet()[i], q);
		}
		return c;
	}
	private void remplitUnivers(Cout c){
		for(Point p : U.getPoints()) U.getRessources().get(p).putAll(c);
	}
	/*
	 * OUTILS D'OBSERVATION
	 */
	
	/* 
	 *  Les blocs de methodes suivants servent a faire des Observations sur l'Univers.
	 * Y sont souvent associees des variables de classes.
	 */
	
	/*
	 * observation des variations de population de chaque espece repressentee dans l'Univers.
	 */
	private Map<Phenotype, Integer> populationsEnVie;
	private Map<Phenotype, Map<Double, Integer>> variationsPopulations;
	public void suitEvolutionPopulations(){
		populationsEnVie = new HashMap<Phenotype, Integer>();
		variationsPopulations = new HashMap<Phenotype, Map<Double,Integer>>();
		Attentif a = new Attentif(){
			public void prendEnCompte(Evenement e) {
				switch(e.getGenre()){
				
				case NAISSANCE :
					NaissanceEtreVivant nEV = (NaissanceEtreVivant) e;
					EtreVivant eV = nEV.getEtreVivant();
					Phenotype ph = eV.getPh();
					if(variationsPopulations.containsKey(ph)){
						int n = populationsEnVie.get(ph);
						populationsEnVie.put(ph, n+1);
						variationsPopulations.get(ph).put(U.friseDuFutur.getDateActuelle(), n+1);
					}
					else{
						Map<Double, Integer> population = new HashMap<Double, Integer>();
						populationsEnVie.put(ph, 1);
						population.put(U.friseDuFutur.getDateActuelle(), 1);
						variationsPopulations.put(ph, population);
					}
					break;
				
				case MORT :
					MortEtreVivant m = (MortEtreVivant) e;
					EtreVivant eVm = m.getEtreVivant();
					Phenotype phm = eVm.getPh();
					int n2 = populationsEnVie.get(phm);
					populationsEnVie.put(phm, n2-1);
					variationsPopulations.get(phm).put(U.friseDuFutur.getDateActuelle(), n2-1);
				default : 
					break;
				}
			}
		
		};
		U.gestionnaireGlobal.ajouteAttentif(a);
	}
	/*
	 *   Construit une chaine de caracteres representant le contenu de variationsPopulations, propice
	 * a etre importee dans un tableur.
	 *   L'algorithme manipule des LinkedList<Character> : c'est necessaire pour avoir une complexite lineaire
	 *   et non quadratique comme en utilisant String.concat();
	 */
	private String fabriqueCourbesVariationsPopulations(Espece... modeles){
		String res = "";
		
		Vector<LinkedList<String>> colonnes = new Vector<LinkedList<String>>(2*modeles.length);
		int numColonne = 0;
		
		for(int i = 0; i < modeles.length; i++){
			Espece o = modeles[i];
			
			if(variationsPopulations.containsKey(bestiaire.specimenDe.get(o))){
				
				TreeMap<Double, Integer> courbe = new TreeMap<Double, Integer>();
				courbe.putAll(variationsPopulations.get(bestiaire.specimenDe.get(o)));
				
				LinkedList<String> colonne1 = new LinkedList<String>();
				colonne1.add("date;");
				for(Double date : courbe.keySet()){
					colonne1.addLast(date.toString()+";");
				}
				colonnes.add(numColonne, colonne1);
				numColonne++;
				
				LinkedList<String> colonne2 = new LinkedList<String>();
				colonne2.addLast(o.nom() + ";");
				for(Double date : courbe.keySet()){
					colonne2.addLast(courbe.get(date).toString()+";");
				}
				colonnes.add(numColonne, colonne2);
				numColonne++;
			}
		}
		LinkedList<Character> tmp = new LinkedList<Character>();
		
		Set<Integer> numColonnesNonVides = new HashSet<Integer>();
		for(int i = 0; i < numColonne; i++){
			numColonnesNonVides.add(i);
		}
		while(!numColonnesNonVides.isEmpty()){
			for(int i = 0; i < numColonne; i++){//transpose
				if(numColonnesNonVides.contains(i)){
					tmp.addAll(stringToCharacterList(colonnes.get(i).poll()));
					if(colonnes.get(i).isEmpty()) numColonnesNonVides.remove(i);
				}
				else{
					tmp.addAll(stringToCharacterList(" ;"));
				}
			}
		tmp.addAll(stringToCharacterList("\n"));
		}
		res = listToString(tmp);
		
		return res;
	}
	private String fabriqueCourbesOccupation(Espece... modeles){//fabrique le texte pour decrire l'evolution du pourcentage d'occupation de chaque espece en parametres.
		String res = "";
		
		Vector<LinkedList<String>> colonnes = new Vector<LinkedList<String>>(2*modeles.length);
		int numColonne = 0;
		
		for(int i = 0; i < modeles.length; i++){
			Espece o = modeles[i];
			
			if(variationsPopulations.containsKey(bestiaire.specimenDe.get(o))){
				
				TreeMap<Double, Integer> courbe = new TreeMap<Double, Integer>();
				courbe.putAll(variationsPopulations.get(bestiaire.specimenDe.get(o)));
				
				LinkedList<String> colonne1 = new LinkedList<String>();
				colonne1.add("date;");
				for(Double date : courbe.keySet()){
					colonne1.addLast(date.toString()+";");
				}
				colonnes.add(numColonne, colonne1);
				numColonne++;
				
				LinkedList<String> colonne2 = new LinkedList<String>();
				colonne2.addLast(o.nom() + ";");
				for(Double date : courbe.keySet()){
					colonne2.addLast(String.valueOf(100.0*courbe.get(date)*(this.bestiaire.specimenDe.get(o).getAnatomie().getSommets().size())/(this.U.getPoints().size()))+";");				}
				colonnes.add(numColonne, colonne2);
				numColonne++;
			}
		}
		LinkedList<Character> tmp = new LinkedList<Character>();
		
		Set<Integer> numColonnesNonVides = new HashSet<Integer>();
		for(int i = 0; i < numColonne; i++){
			numColonnesNonVides.add(i);
		}
		while(!numColonnesNonVides.isEmpty()){
			for(int i = 0; i < numColonne; i++){//transpose
				if(numColonnesNonVides.contains(i)){
					tmp.addAll(stringToCharacterList(colonnes.get(i).poll()));
					if(colonnes.get(i).isEmpty()) numColonnesNonVides.remove(i);
				}
				else{
					tmp.addAll(stringToCharacterList(" ;"));
				}
			}
		tmp.addAll(stringToCharacterList("\n"));
		}
		res = listToString(tmp);
		
		return res;
	
		
	}
	private static void fabriqueFichierCourbesOccupationTypes(int largeur, int hauteur){
		File f = new File("receptacle_courbes_occupation.txt");
		
		PrintWriter pw = null;
		
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));
			for(int i = 0; i < largeur; i++){
				pw.print("date" + ";");
				pw.print("Espece" + i + ";");
			}
			pw.println();
			for(int j = 1; j < hauteur; j++){
				for(int i = 0; i < largeur; i++){
					pw.print(0.1*j + ";");
					pw.print((1.0*i/largeur) + ";");
				}
				pw.println();
			}
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(pw != null) pw.close();
		}
	}
	/*
	 * Observation des variations d'energies d'etres vivants individuels, tries selon leur phenotype.
	 */
	private Map<EtreVivant, Map<Double, Double>> variationsEnergiesVivants;
	private Map<Phenotype, Collection<Map<Double, Double>>> variationsEnergiesParPhenotype;
	public void suitEvolutionEnergiesIndividuellesParEspeces(){
		variationsEnergiesVivants = new HashMap<EtreVivant, Map<Double, Double>>();
		variationsEnergiesParPhenotype = new HashMap<Phenotype, Collection<Map<Double, Double>>>();
		
		
		Attentif a = new Attentif() {
			
			@Override
			public void prendEnCompte(Evenement e) {
				switch(e.getGenre()){
				
				case VARIATION_ENERGIE_ASYMPTOTIQUE:
					VariationEnergieAsymptotique vE = (VariationEnergieAsymptotique) e;
					EtreVivant eV = vE.getEtreVivant();
					if(variationsEnergiesVivants.containsKey(eV)){
						Map<Double, Double> varInd = variationsEnergiesVivants.get(eV);
						varInd.put(U.friseDuFutur.getDateActuelle(), vE.getNouvelleEnergie());
					}
					else{
						Map<Double, Double> varInd = new HashMap<Double, Double>();
						varInd.put(U.friseDuFutur.getDateActuelle(), vE.getNouvelleEnergie());
						if(variationsEnergiesParPhenotype.containsKey(eV.getPh())){
							variationsEnergiesParPhenotype.get(eV.getPh()).add(varInd);
						}
						else{
							LinkedList<Map<Double, Double>> evolutions = new LinkedList<Map<Double,Double>>();
							evolutions.add(varInd);
							variationsEnergiesParPhenotype.put(eV.getPh(), evolutions);
						}
						variationsEnergiesVivants.put(eV, varInd);
					}
					break;
					
				case MORT:
					MortEtreVivant mEV = (MortEtreVivant) e;
					variationsEnergiesVivants.remove(mEV.getEtreVivant());
					break;
					
				default:
					break;
				}
			}
		};

		U.gestionnaireGlobal.ajouteAttentif(a);
	}

	/*
	 * Echantillonne les durees de vie des etres vivants, triees selon le Phenotype
	 */
	private Map<EtreVivant, Double> datesNaissance;
	private Map<Phenotype, List<Double>> dureesDeVie;
	private void echantillonneDureesDeVieParEspece(){
		datesNaissance = new HashMap<EtreVivant, Double>();
		dureesDeVie = new HashMap<Phenotype, List<Double>>();
		
		Attentif a = new Attentif(){
			
			public void prendEnCompte(Evenement e) {
				switch(e.getGenre()){
				case NAISSANCE :
					NaissanceEtreVivant nEv = (NaissanceEtreVivant) e;
					datesNaissance.put(nEv.getEtreVivant(), U.friseDuFutur.getDateActuelle());
					break;
				case MORT :
					MortEtreVivant mEv = (MortEtreVivant) e;
					EtreVivant eV = mEv.getEtreVivant();
					if(datesNaissance.containsKey(eV)){
						double dateNaissance = datesNaissance.get(eV);
						double dateDeces = U.friseDuFutur.getDateActuelle();
						Phenotype ph = eV.getPh();
						if(dureesDeVie.containsKey(ph)){
							Collection<Double> durees = dureesDeVie.get(ph);
							durees.add(dateDeces-dateNaissance);
						}
						else{
							List<Double> durees = new LinkedList<Double>();
							durees.add(dateDeces-dateNaissance);
							dureesDeVie.put(ph, durees);
						}
						datesNaissance.remove(eV);
					}
					else{
						
					}
					break;
				default:
					break;
				}
			}
		};
		this.U.gestionnaireGlobal.ajouteAttentif(a);
	}
	private String fabriqueCumulativesDureesDeVie(Espece...especes){
		String res = "";
		
		for(EtreVivant eV : datesNaissance.keySet()){//on rajoute les donnes sur les EtresVivants encore en vie
			double dateNaissance = datesNaissance.get(eV);
			double dateDeces = U.friseDuFutur.getDateActuelle();
			Phenotype ph = eV.getPh();
			if(dureesDeVie.containsKey(ph)){
				List<Double> durees = dureesDeVie.get(ph);
				durees.add(dateDeces-dateNaissance);
			}
			else{
				List<Double> durees = new LinkedList<Double>();
				durees.add(dateDeces-dateNaissance);
				dureesDeVie.put(ph, durees);
			}
		}
		
		Vector<LinkedList<String>> colonnes = new Vector<LinkedList<String>>(2*especes.length);
		int numColonne = 0;
		
		for(int i = 0; i < especes.length; i++){
			Espece o = especes[i];
			Phenotype ph = bestiaire.specimenDe.get(o);
			
			if(dureesDeVie.containsKey(ph)){
				
				List<Double> durees = dureesDeVie.get(ph);
				Collections.sort(durees);
				
				LinkedList<String> colonne1 = new LinkedList<String>();
				colonne1.addLast(o.nom() + ";");
				for(Double duree : durees){
					colonne1.addLast(duree+";");
				}
				colonnes.add(numColonne, colonne1);
				numColonne++;
				
				LinkedList<String> colonne2 = new LinkedList<String>();
				colonne2.add("proportion cumulee;");
				int nombreDonnees = durees.size();
				for(int j = 0; j < nombreDonnees; j++){
					colonne2.addLast(String.valueOf(1.0*j/nombreDonnees)+";");
				}
				colonnes.add(numColonne, colonne2);
				numColonne++;
			}
		}
		LinkedList<Character> tmp = new LinkedList<Character>();
		
		Set<Integer> numColonnesNonVides = new HashSet<Integer>();
		for(int i = 0; i < numColonne; i++){
			numColonnesNonVides.add(i);
		}
		while(!numColonnesNonVides.isEmpty()){
			for(int i = 0; i < numColonne; i++){//transpose
				if(numColonnesNonVides.contains(i)){
					tmp.addAll(stringToCharacterList(colonnes.get(i).poll()));
					if(colonnes.get(i).isEmpty()) numColonnesNonVides.remove(i);
				}
				else{
					tmp.addAll(stringToCharacterList(" ;"));
				}
			}
		tmp.addAll(stringToCharacterList("\n"));
		}
		res = listToString(tmp);
		
		return res;
		
	}
	
	public double plusHauteEnergie(){
		Set<EtreVivant> f = this.U.getFaune();
		double res = 0.0;
		for(EtreVivant eV : f) res = Math.max(res, eV.getEnergie());
		return res;
	}
	public double energieMoyenne(){
		Set<EtreVivant> f = this.U.getFaune();
		double total = 0.0;
		for(EtreVivant eV : f) total = total + eV.getEnergie();
		double res = total/f.size();
		return res;
	}
	public double energieTotale(){
		Set<EtreVivant> f = this.U.getFaune();
		double total = 0.0;
		for(EtreVivant eV : f) total = total + eV.getEnergie();
		return total;
	}
	
	public String printDistributionRessources(){
		LinkedList<Character> l = new LinkedList<Character>();
		l.addAll(stringToCharacterList("Point;"));
		for(Lettre a : U.getAlphabet()) l.addAll(stringToCharacterList(a.toString()+";"));
		l.addLast('\n');
		for(Point p : this.U.getPoints()){
			l.addAll((stringToCharacterList(p.toString()+";")));
			for(Lettre a : U.getAlphabet()){
				l.addAll(stringToCharacterList((String.valueOf(U.getRessources(p, a))+";")));
			}
			l.add('\n');
		}
		return listToString(l);
	}
/*
 * OUTILS AUXILLIAIRES
 */
	private static LinkedList<Character> stringToCharacterList(String s){
		LinkedList<Character> res = new LinkedList<Character>();
		for(int i = 0; i < s.length(); i++){
			res.addLast(s.charAt(i));
		}
		return res;
	}
	private static String listToString(LinkedList<Character> l){
		char[] arr = new char[l.size()];
		int cpt = 0;
		for(Character c : l){
			arr[cpt] = c;
			cpt++;
		}
		String s = new String(arr);
		return s;
	}
	private void ecritFichierTexte(String nomFichier, String texte){
		//ecrit du texte dans un fichier dont le nom est specifie en parametres. Si ce fichier existe deja il sera prealablement efface.
		File fichier = new File(nomFichier + ".txt");
		BufferedWriter fw = null;

		try {
			fw = new BufferedWriter(new FileWriter(fichier, false));
			fw.write(texte, 0, texte.length());
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fw != null)
				try {
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	/*
	 * GENOMIQUE
	 * On definit ici une forme triviale de Genome, qui ne mute jamais.
	 */
	private Genome genomeTrivial = new Genome() {
		
		@Override
		public Doublet<Phenotype, Genome> mute(Phenotype original) {
			return new Doublet<Phenotype, Genome>(original, this);
		}
	};
 }
