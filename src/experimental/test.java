package experimental;
import physique.*;
import phenotypique.*;
import toolbox.Doublet;
import toolbox.LeProgrammeurEstUnGrosFruitException;

import java.util.*;

import org.apache.commons.math3.distribution.GammaDistribution;

import lois.LoiEnergie;
import lois.LoiEnergieA0;
import lois.SelecteurCheminECECB;
import lois.SelecteurCheminECECB.ArbreChemins;

import chemins.Chemin;
import evenements.Evenement;
import evenements.ReproductionEtreVivant;
import genomique.Genome;
import genomique.GenomeElementaire;


public class test {

	static int nombrePointsVirtuels = 0;
	static Lettre[] alphabet;
	static Set<Lettre> ensLettres= new HashSet<Lettre>();
	static Lettre L0, L1, L2, L3, L4, L5, L6, L7, L8, L9;
	
	static int nombrePoints = 0;
	static Set<Point> lieux = new HashSet<Point>();
	
	static Map<PointVirtuel, Cout> ressources = new HashMap<PointVirtuel, Cout>();
	
	public static void main(String[] args){
		testeGenomesElementaires();
	}
	
	public static void testeGenomesElementaires(){
		Bestiaire.initialiseBestiaire();
		Bestiaire bes = new Bestiaire();
		
		Phenotype phenOriginal = bes.pVar;
		Genome genOriginal = bes.gVar;
		
		GenomeElementaire gen = new GenomeElementaire(bes.aVar, bes.getAlphabet());
		System.out.println(gen.testeViabilite(bes.aVar));
		
		int nombreAvortements = 0;
		int nombreCopiesConformes = 0;
		int nombreIterations = 10000;
		for(int i = 0; i < nombreIterations; i++){
			Doublet<Phenotype, Genome> res = genOriginal.mute(phenOriginal);
			if(res.getFst()==null) nombreAvortements++;
			else{
				if(res.getFst().equals(phenOriginal)) nombreCopiesConformes++;
			}
		}
		System.out.println("copies conformes : " + nombreCopiesConformes*100.0/nombreIterations + "%");
		System.out.println("avortements : " + nombreAvortements*100.0/nombreIterations + "%");
	}
	
	/*
	 * les variables qui suivent servent a constituer un petit bestiaire,
	 * permettant de fournir des cobayes pour les tests :
	 * Minimal, DoubleArete, EnV, Etape, EtapeReflex, EtapeBiReflex, CycleEtape, SortPatL0, et Varie,
	 * par ordre de complexite croissante.
	 * Il faut les initialiser avant experience.
	 * 
	 */
	static PointVirtuel mE0, mS;
	static Anatomie aMin;
	static Phenotype pMin;
	static EtreVivant evMin;
	
	static PointVirtuel daE0, daS;
	static Anatomie aDA;
	static Phenotype pDA;
	static EtreVivant evDA;
	
	static PointVirtuel envE0, envE1, envS;
	static Anatomie aEnv;
	static Phenotype pEnv;
	static EtreVivant evEnv;
	
	static PointVirtuel etpE0, etpI0, etpS;
	static Anatomie aEtp;
	static Phenotype pEtp;
	static EtreVivant evEtp;
	
	static PointVirtuel etrE0, etrI0, etrS;
	static Anatomie aEtR;
	static Phenotype pEtR;
	static EtreVivant evEtR;
	
	static PointVirtuel etbrE0, etbrI0, etbrS;
	static Anatomie aEtBR;
	static Phenotype pEtBR;
	static EtreVivant evEtBR;
	
	static PointVirtuel cycEtpE0, cycEtpI0, cycEtpI1, cycEtpI2, cycEtpI3, cycEtpS;
	static Anatomie aCycEtp;
	static Phenotype pCycEtp;
	static EtreVivant evCycEtp;
	
	static PointVirtuel sortParL0E0, sortParL0I0, sortParL0I1, sortParL0I2, sortParL0S;
	static Anatomie aSortParL0;
	static Phenotype pSortParL0;
	static EtreVivant evSortParL0;
	
	static PointVirtuel varE0, varE1, varE2;//sommets d'entree
	static PointVirtuel varI0, varI1, varI2, varI3, varI4, varI5, varI6, varI7, varI8;//sommets intermediaires
	static PointVirtuel varS;//sommet de sortie
	static Anatomie aVar;
	static Phenotype pVar;
	static EtreVivant evVar;
	
	
	public static void lirecout(Cout c){
		System.out.print(c.toString());
	}

	public static PointVirtuel nouveauPointVirtuel(){
		PointVirtuel P = new PointVirtuel(nombrePointsVirtuels);
		nombrePointsVirtuels++;
		approvisionnePoint(P);
		return P;
	}
	public static void approvisionnePoint(PointVirtuel pv) {
		Cout c = new Cout();
		for (int i = 0; i < alphabet.length; i++) {
			c.put(alphabet[i], 100);
		}
		ressources.put(pv, c);
	}
	public static void inventeLettres(int n){
		alphabet = new Lettre[n];
		for(int i = 0; i < n; i++){
			Lettre l = new Lettre(i);
			alphabet[i] = l;
			ensLettres.add(l);
		}
		L0 = alphabet[0];
		L1 = alphabet[1];
		L2 = alphabet[2];
		L3 = alphabet[3];
		L4 = alphabet[4];
		L5 = alphabet[5];
		L6 = alphabet[6];
		L7 = alphabet[7];
		L8 = alphabet[8];
		L9 = alphabet[9];
	}
	
	
	public static Cout fabriqueCout(int... valeurs){
		/*
		 * fabrique un cout a partir d'un tableau de nombre.
		 * Attention :
		 * 	- il n'y doit pas y avoir plus de nombres que de lettres dans l'alphabet
		 * 	- bien penser a mettre les valeurs nulles, meme si elles n'apparaitront pas dans le Cout retourne.
		 */
		Cout c = new Cout();
		if(valeurs.length > alphabet.length) throw new LeProgrammeurEstUnGrosFruitException("Il n'y a pas assez de lettres!");
		
		for (int i = 0; i < valeurs.length; i++) {
			int q = valeurs[i];
			if(q!=0) c.put(alphabet[i], q);
		}
		return c;
	}
	public static Mot fabriqueMot(Lettre...lettres){
		Mot m = new Mot();
		for (int i = 0; i < lettres.length; i++) {
			m.add(lettres[i]);
		}
		return m;
	}
	
	public static void fabriquePoints(int n){//cree n Points et les stocke dans lieux
		for(int i = 0; i < n; i++){
			lieux.add(new Point(nombrePoints));
			nombrePoints++;
		}
	}
	
	
	public static void initialiseMinimal(){
		mE0 = nouveauPointVirtuel();
		mS = nouveauPointVirtuel();
		
		aMin = new Anatomie();
		
		aMin.ajouteSommetsEntree(mE0);
		
		aMin.ajouteSommetsIntermediaires(mS);
		aMin.setSommetSortie(mS);
		
		aMin.ajouteArete(mE0, L0, mS);
		
		pMin = new Phenotype(aMin);
	}
	public static void initialiseDoubleArete(){
		daE0 = nouveauPointVirtuel();
		daS = nouveauPointVirtuel();
		
		aDA = new Anatomie();
		
		aDA.ajouteSommetsEntree(daE0);
		
		aDA.ajouteSommetsIntermediaires(daS);
		aDA.setSommetSortie(daS);
		
		aDA.ajouteArete(daE0, L0, daS);
		aDA.ajouteArete(daE0, L1, daS);
		
		pDA = new Phenotype(aDA);
	}
	public static void initialiseEnV(){
		envE0 = nouveauPointVirtuel();
		envE1 = nouveauPointVirtuel();
		envS = nouveauPointVirtuel();
		
		aEnv = new Anatomie();
		
		aEnv.ajouteSommetsEntree(envE0, envE1);
		
		aEnv.ajouteSommetsIntermediaires(envS);
		aEnv.setSommetSortie(envS);
		
		aEnv.ajouteArete(envE0, L0, envS);
		aEnv.ajouteArete(envE1, L1, envS);
		
		pEnv = new Phenotype(aEnv);
	}
	public static void initialiseEtape(){
		etpE0 = nouveauPointVirtuel();
		etpI0 = nouveauPointVirtuel();
		etpS = nouveauPointVirtuel();
		
		aEtp = new Anatomie();
		
		aEtp.ajouteSommetsEntree(etpE0);
		
		aEtp.ajouteSommetsIntermediaires(etpI0);
		
		aEtp.ajouteSommetsIntermediaires(etpS);
		aEtp.setSommetSortie(etpS);
		
		aEtp.ajouteArete(etpE0, L0, etpI0);
		aEtp.ajouteArete(etpI0, L1, etpS);
		
		pEtp = new Phenotype(aEtp);
	}
	public static void initialiseEtapeReflex(){
		etrE0 = nouveauPointVirtuel();
		etrI0 = nouveauPointVirtuel();
		etrS = nouveauPointVirtuel();
		
		aEtR = new Anatomie();
		
		aEtR.ajouteSommetsEntree(etrE0);
		
		aEtR.ajouteSommetsIntermediaires(etrI0);
		
		aEtR.ajouteSommetsIntermediaires(etrS);
		aEtR.setSommetSortie(etrS);
		
		aEtR.ajouteArete(etrE0, L0, etrI0);
		aEtR.ajouteArete(etrI0, L1, etrS);
		aEtR.ajouteArete(etrI0, L2, etrI0);
		
		pEtR = new Phenotype(aEtR);
	}
	public static void initialiseEtapeBiReflex(){
		etbrE0 = nouveauPointVirtuel();
		etbrI0 = nouveauPointVirtuel();
		etbrS = nouveauPointVirtuel();
		
		aEtBR = new Anatomie();
		
		aEtBR.ajouteSommetsEntree(etbrE0);
		
		aEtBR.ajouteSommetsIntermediaires(etbrI0);
		
		aEtBR.ajouteSommetsIntermediaires(etbrS);
		aEtBR.setSommetSortie(etbrS);
		
		aEtBR.ajouteArete(etbrE0, L0, etbrI0);
		aEtBR.ajouteArete(etbrI0, L1, etbrS);
		aEtBR.ajouteArete(etbrI0, L2, etbrI0);
		aEtBR.ajouteArete(etbrI0, L0, etbrI0);
		
		pEtBR = new Phenotype(aEtBR);
	}
	public static void initialiseCycleEtape(){
		cycEtpE0 = nouveauPointVirtuel();
		cycEtpI0 = nouveauPointVirtuel();
		cycEtpI1 = nouveauPointVirtuel();
		cycEtpI2 = nouveauPointVirtuel();
		cycEtpI3 = nouveauPointVirtuel();
		cycEtpS = nouveauPointVirtuel();
		
		aCycEtp = new Anatomie();
		
		aCycEtp.ajouteSommetsEntree(cycEtpE0);
		
		aCycEtp.ajouteSommetsIntermediaires(cycEtpI0);
		aCycEtp.ajouteSommetsIntermediaires(cycEtpI1);
		aCycEtp.ajouteSommetsIntermediaires(cycEtpI2);
		aCycEtp.ajouteSommetsIntermediaires(cycEtpI3);
		
		aCycEtp.ajouteSommetsIntermediaires(cycEtpS);
		aCycEtp.setSommetSortie(cycEtpS);
		
		aCycEtp.ajouteArete(cycEtpE0, L0, cycEtpI0);
		aCycEtp.ajouteArete(cycEtpI0, L1, cycEtpI1);
		aCycEtp.ajouteArete(cycEtpI1, L1, cycEtpI2);
		aCycEtp.ajouteArete(cycEtpI2, L1, cycEtpI3);
		aCycEtp.ajouteArete(cycEtpI3, L1, cycEtpI0);
		aCycEtp.ajouteArete(cycEtpI2, L0, cycEtpS);

		pCycEtp = new Phenotype(aCycEtp);
	}
	public static void initialiseSortPartL0(){
		sortParL0E0 = nouveauPointVirtuel();
		sortParL0I0 = nouveauPointVirtuel();
		sortParL0I1 = nouveauPointVirtuel();
		sortParL0I2 = nouveauPointVirtuel();
		sortParL0S = nouveauPointVirtuel();
		
		aSortParL0 = new Anatomie();
		
		aSortParL0.ajouteSommetsEntree(sortParL0E0);
		
		aSortParL0.ajouteSommetsIntermediaires(sortParL0I0, sortParL0I1, sortParL0I2);
		
		aSortParL0.ajouteSommetsIntermediaires(sortParL0S);
		aSortParL0.setSommetSortie(sortParL0S);
		
		aSortParL0.ajouteArete(sortParL0E0, L0, sortParL0S);
		aSortParL0.ajouteArete(sortParL0I0, L0, sortParL0S);
		aSortParL0.ajouteArete(sortParL0I1, L0, sortParL0S);
		aSortParL0.ajouteArete(sortParL0I2, L0, sortParL0S);
		aSortParL0.ajouteArete(sortParL0E0, L1, sortParL0I0);
		aSortParL0.ajouteArete(sortParL0I0, L1, sortParL0I0);
		aSortParL0.ajouteArete(sortParL0I1, L1, sortParL0I0);
		aSortParL0.ajouteArete(sortParL0I2, L1, sortParL0I0);
		aSortParL0.ajouteArete(sortParL0E0, L2, sortParL0I1);
		aSortParL0.ajouteArete(sortParL0I0, L2, sortParL0I1);
		aSortParL0.ajouteArete(sortParL0I1, L2, sortParL0I1);
		aSortParL0.ajouteArete(sortParL0I2, L2, sortParL0I1);
		aSortParL0.ajouteArete(sortParL0E0, L3, sortParL0I2);
		aSortParL0.ajouteArete(sortParL0I0, L3, sortParL0I2);
		aSortParL0.ajouteArete(sortParL0I1, L3, sortParL0I2);
		aSortParL0.ajouteArete(sortParL0I2, L3, sortParL0I2);
		
		pSortParL0 = new Phenotype(aSortParL0);
	}
	public static void initialiseVarie(){
		varE0 = nouveauPointVirtuel();
		varE1 = nouveauPointVirtuel();
		varE2 = nouveauPointVirtuel();
		varI0 = nouveauPointVirtuel();
		varI1 = nouveauPointVirtuel();
		varI2 = nouveauPointVirtuel();
		varI3 = nouveauPointVirtuel();
		varI4 = nouveauPointVirtuel();
		varI5 = nouveauPointVirtuel();
		varI6 = nouveauPointVirtuel();
		varI7 = nouveauPointVirtuel();
		varI8 = nouveauPointVirtuel();
		varS = nouveauPointVirtuel();
		
		aVar = new Anatomie();
		
		aVar.ajouteSommetsEntree(varE0, varE1, varE2);
		
		aVar.ajouteSommetsIntermediaires(varI0, varI0, varI1, varI2, varI3, varI4, varI5, varI6, varI7, varI8);
		
		aVar.ajouteSommetsIntermediaires(varS);
		aVar.setSommetSortie(varS);
		
		aVar.ajouteArete(varE0, L0, varI0);
		aVar.ajouteArete(varE0, L1, varI0);
		aVar.ajouteArete(varE0, L2, varI6);
		
		aVar.ajouteArete(varE1, L0, varI0);
		aVar.ajouteArete(varE1, L2, varS);
		
		aVar.ajouteArete(varE2, L0, varS);
		
		aVar.ajouteArete(varI0, L0, varI1);
		aVar.ajouteArete(varI0, L1, varI0);
		
		aVar.ajouteArete(varI1, L0, varS);
		aVar.ajouteArete(varI1, L1, varI2);
		aVar.ajouteArete(varI1, L2, varI4);
		
		aVar.ajouteArete(varI2, L1, varI3);
		aVar.ajouteArete(varI2, L0, varS);
		
		aVar.ajouteArete(varI3, L1, varI1);
		
		aVar.ajouteArete(varI4, L1, varI1);
		aVar.ajouteArete(varI4, L2, varI5);
		
		aVar.ajouteArete(varI5, L0, varI4);
		aVar.ajouteArete(varI5, L1, varI5);
		aVar.ajouteArete(varI5, L2, varI1);
		
		aVar.ajouteArete(varI6, L2, varI7);
		aVar.ajouteArete(varI7, L2, varI8);
		aVar.ajouteArete(varI8, L2, varS);
		
		pVar = new Phenotype(aVar);
	}
	/*
	public static void testsCouts(){
		//on teste ici la validite des operations sur les Couts.
		Cout c0 = fabriqueCout(0,0,0,0);
		Cout c1 = fabriqueCout(1,0,0,0);
		Cout c2 = fabriqueCout(0,0,1,0);
		Cout c3 = fabriqueCout(2,5,1,0);
		Cout c4 = fabriqueCout(1,5,3,2);
		
		System.out.println("test de fabriquation :");
		System.out.println("c0 = " + c0);
		System.out.println("c1 = " + c1);
		System.out.println("c2 = " + c2);
		System.out.println("c3 = " + c3);
		System.out.println("c4 = " + c4);
		
		Cout c5 = Cout.ajouter(c0, c0);
		Cout c6 = Cout.ajouter(c0, c2);
		Cout c7 = Cout.ajouter(c1, c2);
		Cout c8 = Cout.ajouter(c3, c4);
		
		System.out.println("test d'addition :");
		System.out.println("c5 = c0 + c0 = " + c5);
		System.out.println("c6 = c0 + c2 = " + c6);
		System.out.println("c7 = c1 + c2 = " + c7);
		System.out.println("c8 = c3 + c4 = " + c8);	
		System.out.println("c0 = " + c0);
		System.out.println("c1 = " + c1);
		System.out.println("c2 = " + c2);
		System.out.println("c3 = " + c3);
		System.out.println("c4 = " + c4);
		
		System.out.println("test de retranche ;");
		Cout c9 = Cout.retranche(c0, c0);
		Cout c10 = Cout.retranche(c1, c0);
		Cout c11 = Cout.retranche(c3, c3);
		Cout c12 = Cout.retranche(c8, c3);
		System.out.println("c9 = c0 - c0 = " + c9);
		System.out.println("c10 = c1 - c0 = " + c10);
		System.out.println("c11 = c3 - c3 = " + c11);
		System.out.println("c12 = c8 - c3 = " + c12);
		
		System.out.println("test de ajoute :");
		c0.ajoute(c0);
		System.out.println("c0 := c0+(c0) = " + c0);
		c0.ajoute(c1);
		System.out.println("c0 := c0+(c1) = " + c0);
		c2.ajoute(new Cout());
		System.out.println("c2 := c2+(0) = " + c2);
		c2.ajoute(c1);
		System.out.println("c2 := c2+(c1) = " + c2);
		c4.ajoute(c3);
		System.out.println("c4 := c4+(c3) = " + c4);
		
		System.out.println("test de moinscher : ");
		Cout c13 = new Cout();
		Cout c14 = fabriqueCout(0,0,0,5);
		Cout c15 = fabriqueCout(0,0,0,1);
		c3 = fabriqueCout(2,5,1,0);
		c4 = fabriqueCout(1,5,3,2);
		System.out.println("c3 = " + c3);
		System.out.println("c4 = " + c4);
		System.out.println("c13 = " + c13);
		System.out.println("c14 = " + c14);
		System.out.println("c15 = " + c15);
		System.out.println("c13 mc c14 : " + c13.moinsCher(c14));
		System.out.println("c14 mc c13 : " +  c14.moinsCher(c13));
		System.out.println("c14 mc c15 : " +  c14.moinsCher(c15));
		System.out.println("c15 mc c14 : " +  c15.moinsCher(c14));
		System.out.println("c3 mc c4 : " +  c3.moinsCher(c4));
		System.out.println("c4 mc c3 : " +  c4.moinsCher(c3));
		System.out.println("c15 mc c4 : " +  c15.moinsCher(c4));
		System.out.println("c4 mc c15 : " +  c4.moinsCher(c15));
		System.out.println("c4 mc c4 : " +  c4.moinsCher(c4));
		System.out.println("c13 mc c13 : " +  c13.moinsCher(c13));
		
		System.out.println("test de borneInf");
		Cout c20 = fabriqueCout(5,5,5,5);
		Cout c21 = fabriqueCout(5,5,5,5);
		Cout c22 = fabriqueCout(4,6,0,5);
		Cout c23 = fabriqueCout(17,0,9,2);
		Set<Cout> ens = new HashSet<Cout>(4);
		ens.add(c20);
		ens.add(c21);
		ens.add(c22);
		ens.add(c23);
		System.out.println("ensemble de Couts : " + ens);
		System.out.println(Cout.borneInf(ens));

		
		System.out.println("test sur monteLettre");
		c0 = fabriqueCout(0,0,0,0);
		c1 = fabriqueCout(1,0,0,0);
		c2 = fabriqueCout(0,0,1,0);
		c3 = fabriqueCout(2,5,1,0);
		c4 = fabriqueCout(1,5,3,2);
		System.out.println("c0 = " + c0);
		System.out.println("c1 = " + c1);
		System.out.println("c2 = " + c2);
		System.out.println("c3 = " + c3);
		System.out.println("c4 = " + c4);
		System.out.println("c0 + L0 = " + c0.monteLettre(L0));
		System.out.println("c0 + L2 = " + c0.monteLettre(L2));
		System.out.println("c1 + L0 = " + c1.monteLettre(L0));
		System.out.println("c1 + L2 = " + c1.monteLettre(L2));
		System.out.println("c3 + L3 = " + c3.monteLettre(L3));
		System.out.println("c4 + L3 = " + c4.monteLettre(L3));
		System.out.println("c0 = " + c0);
		System.out.println("c1 = " + c1);
		System.out.println("c2 = " + c2);
		System.out.println("c3 = " + c3);
		System.out.println("c4 = " + c4);
		
		System.out.println("test sur descendLettre");
		c0 = fabriqueCout(0,0,0,0);
		c1 = fabriqueCout(1,0,0,0);
		c2 = fabriqueCout(0,0,1,0);
		c3 = fabriqueCout(2,5,1,0);
		c4 = fabriqueCout(1,5,3,2);
		System.out.println("c0 = " + c0);
		System.out.println("c1 = " + c1);
		System.out.println("c2 = " + c2);
		System.out.println("c3 = " + c3);
		System.out.println("c4 = " + c4);
		System.out.println("c1 - L0 = " + c1.descendLettre(L0));
		System.out.println("c3 - L2 = " + c3.descendLettre(L2));
		System.out.println("c4 - L0 = " + c4.descendLettre(L0));
		System.out.println("c4 - L3 = " + c4.descendLettre(L3));
		System.out.println("c0 = " + c0);
		System.out.println("c1 = " + c1);
		System.out.println("c2 = " + c2);
		System.out.println("c3 = " + c3);
		System.out.println("c4 = " + c4);
	}
	
	
	public static void testsGenerationMots(){
		inventeLettres(10);
		//cree minimal
		initialiseMinimal();
		//cree doubleArete
		initialiseDoubleArete();		
		//cree enV
		initialiseEnV();
		//cree etape
		initialiseEtape();
		//cree etapeReflex
		initialiseEtapeReflex();
		//cree etapeBiReflex
		initialiseEtapeBiReflex();
		//cree cycleEtape
		initialiseCycleEtape();
		//cree sortParL0
		initialiseSortPartL0();
		//cree varie
		initialiseVarie();
		
		SelecteurCheminECECB scEnV = (SelecteurCheminECECB) pEnv.getSelChemin();
		ArbreChemins a = scEnV.getArbresCheminsElementaires().get(envE0).get(envS);
		ArbreChemins b = scEnV.getArbresCheminsElementaires().get(envE1).get(envS);
		/*		
		Chemin c = scEnV.choisitChemin(envE0, ressources.get(envE0));
		System.out.println(c);
		System.out.println(c.getCout());
		
		
	}
*/
	public static void testEnergies(){
		LoiEnergie LE = new LoiEnergieA0(100.0);
		
		Mot m0 = fabriqueMot();
		System.out.println("m0 = " + m0);
		System.out.println("energie = " + LE.energieMot(m0));
		
		Mot m1 = fabriqueMot(L0);
		System.out.println("m1 = " + m1);
		System.out.println("energie = " + LE.energieMot(m1));
		
		Mot m2 = fabriqueMot(L0,L0,L0,L0,L0);
		System.out.println("m2 = " + m2);
		System.out.println("energie = " + LE.energieMot(m2));
		
		Mot m3 = fabriqueMot(L0,L1,L0,L1,L0);
		System.out.println("m3 = " + m3);
		System.out.println("energie = " + LE.energieMot(m3));
		
		Mot m4 = fabriqueMot(L0,L0,L0,L0,L1);
		System.out.println("m4 = " + m4);
		System.out.println("energie = " + LE.energieMot(m4));
		
		Mot m5 = fabriqueMot(L0,L1,L2,L1,L0);
		System.out.println("m5 = " + m5);
		System.out.println("energie = " + LE.energieMot(m5));
		
		Mot m6 = fabriqueMot(L0,L1,L2,L3,L4);
		System.out.println("m6 = " + m6);
		System.out.println("energie = " + LE.energieMot(m6));
		
		Mot m7 = fabriqueMot(L2, L5, L6, L9, L7, L8, L2, L4, L1, L9, L6, L0, L4, L5, L7, L4, L8, L2, L7, L9, L7, L2, L5, L6, L6, L5);
		System.out.println("m7 = " + m7);
		System.out.println("energie = " + LE.energieMot(m7));
		
		Mot m8 = fabriqueMot(L0, L0, L1, L2, L3, L4, L5, L6, L7, L8);
		System.out.println("m8 = " + m8);
		System.out.println("energie = " + LE.energieMot(m8));
	}
	
	public static void remplitUnivers(Univers U, Cout c){
		//remplit de maniere homogene les ressources de l'univers grace a c
		for(Point p : U.getPoints()){
			U.getRessources().get(p).putAll(c);
		}
	}
	
	public static EtreVivant fabriqueEtreVivant(Phenotype ph, Set<Point> Petri, Univers U){
		/*
		 * cree un nouvel etre vivant a partir du Phenotype en parametres, sur des points choisis arbitrairement dans Petri.
		 * Retire les points en question de l'ensemble.
		 */
		Emplacement emplacement = new Emplacement();
		
		Set<PointVirtuel> pvs = ph.getAnatomie().getSommets();
		
		Iterator<Point> I = Petri.iterator();
		Set<Point> poubelle = new HashSet<Point>();
		for(PointVirtuel pv : pvs){
			if(!I.hasNext()) throw new LeProgrammeurEstUnGrosFruitException("plus de points a prelever.");
			Point p = I.next();
			emplacement.put(pv, p);
			poubelle.add(p);
		}
		Petri.removeAll(poubelle);
		return new EtreVivant(U, ph, emplacement, genomeTrivial);
	}

	private static Genome genomeTrivial = new Genome() {
		
		@Override
		public Doublet<Phenotype, Genome> mute(Phenotype original) {
			return new Doublet<Phenotype, Genome>(original, this);
		}
	};



}
