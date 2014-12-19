package experimental;

import genomique.Genome;
import genomique.GenomeElementaire;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import phenotypique.Anatomie;
import phenotypique.Phenotype;
import physique.Lettre;
import physique.PointVirtuel;

/*
 * Cet classe sert a fabriquer les phenotypes d'etres vivants modeles pour les experiences.
 * 
 * Utilisation de cette classe : initialiser avec intialiserBestiaire().
 * Puis, utiliser les phenotypes ainsi cree en les appelant depuis une autre classe.
 * (les noms de phenotypes commencent par un p).
 * 
 * Exemple : si je veux me servir du phenotype de Minimal, je tape "Bestiaire.pMin".
 * 
 * 
 * 
 * Pour une description des etres vivants : voir a la fin.
 */
public class Bestiaire {
	//prametres de phenotypes, utilisees pour former des mots.

	//parametre de Bernouilli si on tente d'enrichir un mot en un point d'insertion donne.
	private static double pEnrichissementChemin;
	
	
	private int nombrePointsVirtuels = 0;
	
	private static Lettre[] alphabet;
	public static Set<Lettre> ensLettres= new HashSet<Lettre>();
	private static Lettre L0, L1, L2, L3, L4, L5, L6, L7, L8, L9;
	
	public Bestiaire(){
		
		nombrePointsVirtuels = 0;
		
		initialiseMinimal();
		initialiseDoubleArete();
		initialiseEnV();
		initialiseEtape();
		initialiseEtapeReflex();
		initialiseEtapeBiReflex();
		initialiseCycleEtape();
		initialiseSortPartL3();
		initialiseVarie();
		
		
	}
	/*
	 * cree les lettres puis initialise tous les phenotypes du bestiaire.
	 * donne des valeurs par defaut aux parametres.
	 */
	public static void initialiseBestiaire(){
		pEnrichissementChemin = 0.6;

		
		inventeLettres(10);
	}
	//meme chose mais on choisit cette fois les valeurs des parametres de phenotypes.
	public static void initialiseBestiaire(double pFM1){
		pEnrichissementChemin = pFM1;
		
		inventeLettres(10);
	}
	/*
	 * Le bloc de methodes suivant initialise tous les phenotypes.
	 * fait appel a nouveauPointVirtuel() et utilise les parametres de phenotype.
	 */
	private void initialiseMinimal(){
		mE0 = nouveauPointVirtuel();
		mS = nouveauPointVirtuel();
		
		aMin = new Anatomie();
		
		aMin.ajouteSommetsEntree(mE0);
		
		aMin.ajouteSommetsIntermediaires(mS);
		aMin.setSommetSortie(mS);
		
		aMin.ajouteArete(mE0, L0, mS);
		
		gMin = new GenomeElementaire(aMin, ensLettres);
		
		pMin = new Phenotype(aMin, pEnrichissementChemin);
		
		specimenDe.put(espMin, pMin);
		genotypeDe.put(espMin, gMin);
	}
	private void initialiseDoubleArete(){
		daE0 = nouveauPointVirtuel();
		daS = nouveauPointVirtuel();
		
		aDA = new Anatomie();
		
		aDA.ajouteSommetsEntree(daE0);
		
		aDA.ajouteSommetsIntermediaires(daS);
		aDA.setSommetSortie(daS);
		
		aDA.ajouteArete(daE0, L0, daS);
		aDA.ajouteArete(daE0, L1, daS);
		
		gDA = new GenomeElementaire(aDA, ensLettres);
		
		pDA = new Phenotype(aDA, pEnrichissementChemin);
		
		specimenDe.put(espDA, pDA);
		genotypeDe.put(espDA, gDA);
	}
	private void initialiseEnV(){
		envE0 = nouveauPointVirtuel();
		envE1 = nouveauPointVirtuel();
		envS = nouveauPointVirtuel();
		
		aEnV = new Anatomie();
		
		aEnV.ajouteSommetsEntree(envE0, envE1);
		
		aEnV.ajouteSommetsIntermediaires(envS);
		aEnV.setSommetSortie(envS);
		
		aEnV.ajouteArete(envE0, L0, envS);
		aEnV.ajouteArete(envE1, L1, envS);
		
		gEnV = new GenomeElementaire(aEnV, ensLettres);
		
		pEnV = new Phenotype(aEnV, pEnrichissementChemin);
		
		specimenDe.put(espEnV, pEnV);
		genotypeDe.put(espEnV, gEnV);
	}
	private void initialiseEtape(){
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
		
		gEtp = new GenomeElementaire(aEtp, ensLettres);
		
		pEtp = new Phenotype(aEtp, pEnrichissementChemin);
		
		specimenDe.put(espEtp, pEtp);
		genotypeDe.put(espEtp, gEtp);
	}
	private void initialiseEtapeReflex(){
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
		
		gEtR = new GenomeElementaire(aEtR, ensLettres);
		
		pEtR = new Phenotype(aEtR, pEnrichissementChemin);
		
		specimenDe.put(espEtR, pEtR);
		genotypeDe.put(espEtR, gEtR);
	}
	private void initialiseEtapeBiReflex(){
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
		
		gEtBR = new GenomeElementaire(aEtBR, ensLettres);
		
		pEtBR = new Phenotype(aEtBR, pEnrichissementChemin);
		
		specimenDe.put(espEtBR, pEtBR);
		genotypeDe.put(espEtBR, gEtBR);
	}
	private void initialiseCycleEtape(){
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
		
		gCycEtp = new GenomeElementaire(aCycEtp, ensLettres);

		pCycEtp = new Phenotype(aCycEtp, pEnrichissementChemin);
		
		specimenDe.put(espCycEtp, pCycEtp);
		genotypeDe.put(espCycEtp, gCycEtp);
	}
	private void initialiseSortPartL3(){
		sortParL3E0 = nouveauPointVirtuel();
		sortParL3I0 = nouveauPointVirtuel();
		sortParL3I1 = nouveauPointVirtuel();
		sortParL3I2 = nouveauPointVirtuel();
		sortParL3S = nouveauPointVirtuel();
		
		aSortParL3 = new Anatomie();
		
		aSortParL3.ajouteSommetsEntree(sortParL3E0);
		
		aSortParL3.ajouteSommetsIntermediaires(sortParL3I0, sortParL3I1, sortParL3I2);
		
		aSortParL3.ajouteSommetsIntermediaires(sortParL3S);
		aSortParL3.setSommetSortie(sortParL3S);
		
		aSortParL3.ajouteArete(sortParL3E0, L3, sortParL3S);
		aSortParL3.ajouteArete(sortParL3I0, L3, sortParL3S);
		aSortParL3.ajouteArete(sortParL3I1, L3, sortParL3S);
		aSortParL3.ajouteArete(sortParL3I2, L3, sortParL3S);
		aSortParL3.ajouteArete(sortParL3E0, L1, sortParL3I1);
		aSortParL3.ajouteArete(sortParL3I0, L1, sortParL3I1);
		aSortParL3.ajouteArete(sortParL3I1, L1, sortParL3I1);
		aSortParL3.ajouteArete(sortParL3I2, L1, sortParL3I1);
		aSortParL3.ajouteArete(sortParL3E0, L2, sortParL3I2);
		aSortParL3.ajouteArete(sortParL3I0, L2, sortParL3I2);
		aSortParL3.ajouteArete(sortParL3I1, L2, sortParL3I2);
		aSortParL3.ajouteArete(sortParL3I2, L2, sortParL3I2);
		aSortParL3.ajouteArete(sortParL3E0, L0, sortParL3I0);
		aSortParL3.ajouteArete(sortParL3I0, L0, sortParL3I0);
		aSortParL3.ajouteArete(sortParL3I1, L0, sortParL3I0);
		aSortParL3.ajouteArete(sortParL3I2, L0, sortParL3I0);
		
		gSortParL3 = new GenomeElementaire(aSortParL3, ensLettres);
		
		pSortParL3 = new Phenotype(aSortParL3, pEnrichissementChemin);
		
		specimenDe.put(espSortParL3, pSortParL3);
		genotypeDe.put(espSortParL3, gSortParL3);
	}
	private void initialiseVarie(){
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
		
		gVar = new GenomeElementaire(aVar, ensLettres);
		
		pVar = new Phenotype(aVar, pEnrichissementChemin);
		
		specimenDe.put(espVar, pVar);
		genotypeDe.put(espVar, gVar);
	}
	
	//cette methode cree n lettres (n>10) et les ajoutes a l'ensemble ensLettres
	private static void inventeLettres(int n){
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
	//cette methode sert a fabriquer un nouveau PointVirtuel
	private PointVirtuel nouveauPointVirtuel(){
		PointVirtuel P = new PointVirtuel(nombrePointsVirtuels);
		nombrePointsVirtuels++;
		return P;
	}
	/*
	 * Les champs suivants sont des constantes concernant des phenotypes modeles.
	 */
	//associe a une espece le phenotype qui la represente dans ce bestiaire precis.
	Map<Espece, Phenotype> specimenDe = new HashMap<Espece, Phenotype>();
	//associe a une espece le genome qui la decrit dans ce bestiaire particulier
	Map<Espece, Genome> genotypeDe = new HashMap<Espece, Genome>();
	
	//"Minimal" : decrit un etre vivant "minimaliste" : un point d'entree, une arete, un point de sortie.
	private  PointVirtuel mE0, mS;
	 Anatomie aMin;
	public  Phenotype pMin;
	public Genome gMin;
	public static Espece espMin = new Espece("Minimal");
	
	//"Double Arete" : comme Minimal, mais avec une arete en plus.
	private  PointVirtuel daE0, daS;
	 Anatomie aDA;
	public  Phenotype pDA;
	public Genome gDA;
	public static Espece espDA = new Espece("Double Arete");
	// EtreVivant evDA;
	
	//"En V" : un etre vivant avec deux sommets d'entree, minimaliste (deux aretes, de lettres differentes)
	private  PointVirtuel envE0, envE1, envS;
	 Anatomie aEnV;
	public  Phenotype pEnV;
	public Genome gEnV;
	public static Espece espEnV = new Espece("En V");
	// EtreVivant evEnv;
	
	//"Etape" : un etre vivant avec un sommet d'entree, un sommet intermediaire, et un sommet de sortie.
	private  PointVirtuel etpE0, etpI0, etpS;
	 Anatomie aEtp;
	public  Phenotype pEtp;
	public Genome gEtp;
	public static Espece espEtp = new Espece("Etape");
	// EtreVivant evEtp;
	
	//"Etape Reflexive" : comme Etape, mais le sommet intermediaire a une arete de lui-meme vers lui-meme (cycles possibles)
	PointVirtuel etrE0;
	PointVirtuel etrI0;
	PointVirtuel etrS;
	 Anatomie aEtR;
	public  Phenotype pEtR;
	public Genome gEtR;
	public static Espece espEtR = new Espece("Etape Reflexive");
	// EtreVivant evEtR;
	
	//"Etape BiReflexive" : comme Etape Reflexive, mais avec deux aretes reflexives.
	private  PointVirtuel etbrE0, etbrI0, etbrS;
	 Anatomie aEtBR;
	public  Phenotype pEtBR;
	public Genome gEtBR;
	public static Espece espEtBR = new Espece("Etape BiReflexive");
	// EtreVivant evEtBR;
	
	//"Cycle-Etape" : les sommets intermediaires forment un cycle.
	private  PointVirtuel cycEtpE0, cycEtpI0, cycEtpI1, cycEtpI2, cycEtpI3, cycEtpS;
	 Anatomie aCycEtp;
	public  Phenotype pCycEtp;
	public Genome gCycEtp;
	public static Espece espCycEtp = new Espece("Cycle-Etape");
	// EtreVivant evCycEtp;
	
	/* 
	 * "Sort par L3" : celui-ci est plus complique. Il a 1 entree, 3 intermediaires (et 1 sortie).
	 * Il est tres connecte.
	 * Le princpe du graphe est simple. 4 lettres sont concernees : L0, L1, L2, L3.
	 * De chaque point (autre que la sortie) par une arete de chaque lettre.
	 * Les L0-aretes menent a I0.
	 * Les L1-aretes menent a I1;
	 * Les L2-aretes menent a I2;
	 * Les L3-aretes menent a la sortie S.
	 */
	private  PointVirtuel sortParL3E0, sortParL3I0, sortParL3I1, sortParL3I2, sortParL3S;
	 Anatomie aSortParL3;
	public  Phenotype pSortParL3;
	public Genome gSortParL3;
	public static Espece espSortParL3 = new Espece("Sort-Par-L3");
	
	/*
	 *   "Varie" : Decrit un etre vivant sans symetrie particuliere, dont l'anatomie presente toutes sortes
	 * de configurations.
	 *   3 entrees, 9 intermediaires. 3 lettres utilisees.
	 */
	PointVirtuel varE0;//sommets d'entree


	private PointVirtuel varE1;


	private PointVirtuel varE2;
	PointVirtuel varI0;//sommets intermediaires


	PointVirtuel varI1;


	private PointVirtuel varI2;


	private PointVirtuel varI3;


	private PointVirtuel varI4;


	private PointVirtuel varI5;


	private PointVirtuel varI6;


	private PointVirtuel varI7;


	private PointVirtuel varI8;
	PointVirtuel varS;//sommet de sortie
	 Anatomie aVar;
	public  Phenotype pVar;
	public Genome gVar;
	public static Espece espVar = new Espece("Varie");
	// EtreVivant evVar;

	public static Lettre[] getAlphabet() {
		return alphabet;
	}
}
