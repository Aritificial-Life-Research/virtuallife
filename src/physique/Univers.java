package physique;

import java.util.*;


import lois.Duplicateur;
import lois.DuplicateurA0;
import lois.LoiDateProgressionMot;
import lois.LoiDateProgressionMotA0;
import lois.LoiDateReproduction;
import lois.LoiDateReproductionA0;
import lois.LoiEnergie;
import lois.LoiEnergieA0;
import lois.SelecteurChemin.RecycleurCout;


import phenotypique.EtreVivant;

import evenements.EcoulementDuTemps;
import evenements.Evenement;
import evenements.GenreEvenement;
import evenements.MortEtreVivant;
import experimental.Observation;
import temporel.*;
import toolbox.LeProgrammeurEstUnGrosFruitException;

public class Univers {

	/*
	 * Un Univers est la donnee a un instant fixe d'un ensemble de lieux, de ressources sur ces lieux, et d'etres vivants qui s'y etendent.
	 */


	private final int P;//nombre des points
	private final Set<Point> points;
	
	private int L;//nombre de lettres, i.e la taille de l'alphabet
	private final Set<Lettre> alphabet;
	
	public LoiEnergie loiEnergie;
	public Duplicateur duplicateur;
	public LoiDateReproduction loiDateReproduction;
	public LoiDateProgressionMot loiDateProgressionMot;
	
	private Map<Point,Map<Lettre,Integer>> ressources;// dans le cas de ressources, on a une fonction complete : on autorise les valeurs nulles.
	//ATTENTION donc a ne pas les memes habitudes de manipulation que pour Cout.
	
	private Set<EtreVivant> Faune;//l'ensemble des etres vivants presents dans l'experience
	private Map<Point, EtreVivant> occupation;//associe a un point l'etre vivant qui l'occupe s'il y en a un.
	
	
	
	private Observation observation;
	
	public GestionnairedEvenements gestionnaireGlobal;
	public FriseChronologique friseDuFutur;

	public Univers(int M, int Q){
		P=M;
		points = new HashSet<Point>();
		for(int i = 0; i < P; i++){
			points.add(new Point(i));
		}
		
		L=Q;
		alphabet = new HashSet<Lettre>();
		for(int i = 0; i < L; i++){
			alphabet.add(new Lettre(i));
		}
		ressources = new HashMap<Point,Map<Lettre,Integer>>();
		for(Point i :points){
			Map<Lettre, Integer> tmp = new HashMap<Lettre, Integer>(4);
			for(Lettre l : alphabet){
				tmp.put(l, new Integer(0));
			}
			ressources.put(i, tmp);
		}
		
		this.Faune = new HashSet<EtreVivant>();
		this.occupation = new HashMap<Point, EtreVivant>();
		
		this.friseDuFutur = new FriseChronologique(0.0);
		this.gestionnaireGlobal = new GestionnaireEVTUniverselA0(this);//TODO ca marche ca?
		this.gestionnaireGlobal.ajouteAttentif(friseDuFutur);
		
		this.duplicateur = new DuplicateurA0(this);
		this.loiEnergie = new LoiEnergieA0(100.0);//TODO temps caracteristique a decider
		this.loiDateProgressionMot = new LoiDateProgressionMotA0(0.1);//TODO petit rayon de temps a decider
		this.loiDateReproduction = new LoiDateReproductionA0(20.0);//TODO temps caracteristique a decider
		
		indexLettres = alphabet.toArray(new Lettre[0]);//utilise pour le brassage de ressources
	}
	public Univers(Set<Point> points, Set<Lettre> alphabet) {
		super();
		this.points = points;
		this.alphabet = alphabet;
		
		P = points.size();
		L = points.size();
		
		ressources = new HashMap<Point,Map<Lettre,Integer>>();
		for(Point i :points){
			Map<Lettre, Integer> tmp = new HashMap<Lettre, Integer>(4);
			for(Lettre l : alphabet){
				tmp.put(l, new Integer(0));
			}
			ressources.put(i, tmp);
		}
		
		this.Faune = new HashSet<EtreVivant>();
		this.occupation = new HashMap<Point, EtreVivant>();
		
		this.friseDuFutur = new FriseChronologique(0.0);
		
		indexLettres = alphabet.toArray(new Lettre[0]);//utilise pour le brassage de ressources
		
	}
	
	public void setLois(GestionnairedEvenements ges, Duplicateur dup, LoiEnergie lEn, LoiDateProgressionMot lDPM, LoiDateReproduction lDR){
		this.gestionnaireGlobal = ges;
		this.gestionnaireGlobal.ajouteAttentif(friseDuFutur);
		
		this.duplicateur = dup;
		this.loiEnergie = lEn;
		this.loiDateProgressionMot = lDPM;//TODO petit rayon de temps a decider
		this.loiDateReproduction = lDR;

	}

	public void ecouleLeTemps(double dateFinale){
		//fait s'ecouler le temps depuis la date actuelle jusque date finale.
		//attention, de nouveaux evenements peuvent etres programmes en cours de route...
		while(friseDuFutur.getDateActuelle()<dateFinale){
			double prochaineEtape = friseDuFutur.dateDuProchainEvenement();
			//on fait s'ecouler le temps jusqu'au prochain evenement programme.
			gestionnaireGlobal.prendEnCompte(new EcoulementDuTemps(this, friseDuFutur.getDateActuelle(), prochaineEtape));
			//on fait arriver tous les evenements programmes a cet date.
			friseDuFutur.arrive();
		}
	}
	
	public void transloqueRessource(Point depart, Point destination, Lettre l){
		if(!ressources.containsKey(depart)) throw new LeProgrammeurEstUnGrosFruitException("Point de depart inexistant!");
		Map<Lettre, Integer> mdep = ressources.get(depart);
		if(!ressources.containsKey(destination)) throw new LeProgrammeurEstUnGrosFruitException("Point de destination inexistant!");
		Map<Lettre, Integer> mdest = ressources.get(destination);
		
		if(!mdep.containsKey(l)) throw new LeProgrammeurEstUnGrosFruitException("ressource a transloquer absente du point de depart");
		Integer qdep = mdep.get(l);
		if(qdep==0)  throw new LeProgrammeurEstUnGrosFruitException("ressource a transloquer en quantite 0");
		else mdep.put(l, qdep-1);
		
		mdest.put(l, mdest.get(l)+1);
	}
	
	public Integer getRessources(Point p, Lettre l){
		return ressources.get(p).get(l);
	}
	
	public void setRessources(Point p, Lettre l, int q){
		ressources.get(p).put(l,new Integer(q));
	}
	
	public Cout ressourcesToCout(Point p){//exprime les ressources en le point p en Cout
		Cout c = this.recycleurCouts.fournit();
		for(Lettre l : alphabet){
			if(getRessources(p, l) != 0) c.put(l, this.ressources.get(p).get(l));
		}
		
		return c;
	}

	public Set<Lettre> getAlphabet() {
		return alphabet;
	}

	public LoiEnergie getLoiEnergie() {
		return loiEnergie;
	}

	public void setLoiEnergie(LoiEnergie loiEnergie) {
		this.loiEnergie = loiEnergie;
	}

	public Map<Point, Map<Lettre, Integer>> getRessources() {
		return ressources;
	}

	public void setRessources(Map<Point, Map<Lettre, Integer>> ressources) {
		this.ressources = ressources;
	}

	public Set<Point> getPoints() {
		return points;
	}

	public Set<EtreVivant> getFaune() {
		return Faune;
	}

	public void setFaune(Set<EtreVivant> faune) {
		Faune = faune;
	}

	public Map<Point, EtreVivant> getOccupation() {
		return occupation;
	}

	public void setOccupation(Map<Point, EtreVivant> occupation) {
		this.occupation = occupation;
	}
	
	public double energieDelOccupant(Point p){
		//renvoie l'energie de l'etre vivnt qui occupe ce point s'il y en a un, et 0 sinon
		if(!points.contains(p)) throw new LeProgrammeurEstUnGrosFruitException("ce point n'est pas dans l'Univers");
		if(occupation.containsKey(p)){
			return occupation.get(p).getEnergie();
		}
		else return 0.0;
	}
	
	public boolean PointOccupePar(Point p, EtreVivant ev){
		//renvoie true ssi le point p est occupe par l'etrevivant ev
		if(!points.contains(p)) throw new LeProgrammeurEstUnGrosFruitException("point inexistant dans l'Univers");//TODO juste une securite.
		if(occupation.containsKey(p)) return occupation.get(p).equals(ev);
		else return false;

	}
	public Duplicateur getDuplicateur() {
		return duplicateur;
	}
	public void setDuplicateur(Duplicateur duplicateur) {
		this.duplicateur = duplicateur;
	}
	public LoiDateReproduction getLoiDateReproduction() {
		return loiDateReproduction;
	}
	public void setLoiDateReproduction(LoiDateReproduction loiDateReproduction) {
		this.loiDateReproduction = loiDateReproduction;
	}
	public LoiDateProgressionMot getLoiDateProgressionMot() {
		return loiDateProgressionMot;
	}
	public void setLoiDateProgressionMot(LoiDateProgressionMot loiDateProgressionMot) {
		this.loiDateProgressionMot = loiDateProgressionMot;
	}	
	
	private Lettre[] indexLettres;
	public void rendRessourcesPrelevees(EtreVivant ev){//restitue aleatoirement les ressources prelevees par l'EtreVivant aux points qu'il occupe(ait)
		Set<Point> ouBrasser = ev.etendue;
		
		Point[] indexPoints = ouBrasser.toArray(new Point[0]);
		int[][] tabReDistribution = new int[indexPoints.length][indexLettres.length];
		
		
		int n = indexPoints.length;
		Random r = new Random();
		
		for(PointVirtuel pv : ev.ressourcesPrelevees.keySet()){
			Cout aReDistribuer = ev.ressourcesPrelevees.get(pv);
			for(int j = 0; j < indexLettres.length; j++){
				int quantite = 0;
				if(aReDistribuer.containsKey(indexLettres[j])) quantite = aReDistribuer.get(indexLettres[j]);
				for(int k = 0; k < quantite; k++){
					int choisi = r.nextInt(n);//on choisit un point destination
					tabReDistribution[choisi][j]++;
				}
				aReDistribuer.put(indexLettres[j], 0);//le cout est vide de cette Lettre.
			}
		}
		for(int i = 0; i < n; i++){
			for(int j = 0; j < indexLettres.length; j++){
				ressources.get(indexPoints[i]).put(indexLettres[j], ressources.get(indexPoints[i]).get(indexLettres[j]) + tabReDistribution[i][j]);
			}
		}
	}
	/*
	 * RECYCLAGE D'OBJETS
	 */
	public final RecycleurCout recycleurCouts = new RecycleurCout();
	
}
