package aleatoire;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import toolbox.LeProgrammeurEstUnGrosFruitException;
import toolbox.Recyclable;
import toolbox.Recycleur;


public class UrneSeijin<B> implements Urne<B> {

	private ArbreBinaire arbre;
	private Map<B, Feuille> positionBoules;
	
	public UrneSeijin() {
		this.arbre = null;
		this.positionBoules = new HashMap<B, Feuille>();
	}
	
	public void ajouteBoule(B boule, double poids){
		if(positionBoules.containsKey(boule)){
			if(poids==0){// on retire la boule de l'urne
				auxRetireBoule(positionBoules.get(boule));
				positionBoules.remove(boule);
			}
			else{
				Feuille position = positionBoules.get(boule);
				position.poids = poids;
				miseAJourRemontante(position);
			}
		}
		else{
			if(poids!=0){
				positionBoules.put(boule, auxAjouteBoule(boule, poids));
			}
		}
	}
	
	private Feuille auxAjouteBoule(B boule, double p){//ajoute une boule a arbre et modifie positionBoules en consequence
		//on met a jour a mesure
		if(arbre==null){
			arbre = fournitFeuille(null, false,  p, boule);
			return (Feuille) arbre;
		}
		else{
			if(arbre.estUneFeuille()){
				NoeudInterne b = fournitNoeudInterne(null, false, arbre, fournitFeuille(null, true, p, boule));
				b.droite.setPere(b);
				b.gauche.setPere(b);
				b.gauche.cote = false;
				
				arbre = b;
				
				miseAJourRemontante(b.droite);
				return (Feuille) b.droite;
			}
			else{
				Feuille f = arbre.ajouteBoule(boule, p);
				this.miseAJourRemontante(f);
				return f;
			}
		}
	}
	//mets a jour en remontant les informations sur le poids et la hauteur.
	public void miseAJourRemontante(ArbreBinaire a){
		if(a==null) throw new LeProgrammeurEstUnGrosFruitException("mise a jour demandee sur un arbre vide");
		else{
			a.metsAJourLocalement();
			if(a.aUnPere()) miseAJourRemontante(a.pere);
		}
	}
	
	public B tireAvecRemise(){
		if (!isEmpty()){ 
			double d=arbre.poids*Math.random();
			return arbre.tirer(d).boule;
		}
		else throw new LeProgrammeurEstUnGrosFruitException("impossible de tirer dans une Urne vide");
	}
	
	public B tireSansRemise(){
		if (!isEmpty()){
			if(arbre==null) throw new LeProgrammeurEstUnGrosFruitException();
			double d=arbre.poids*Math.random();//on trouve la position en descendant dans l'arbre
			
			Feuille position = arbre.tirer(d);//on retire la boule associee
			B boule = position.boule;
			
			auxRetireBoule(position);
			
			positionBoules.remove(boule);
			
			return boule;
		}
		else throw new LeProgrammeurEstUnGrosFruitException("impossible de tirer dans une Urne vide");
	}
	
	//enleve une boule de l'urne.
	public void retireBoule(B boule){
		if(positionBoules.containsKey(boule)){
			Feuille position = this.positionBoules.get(boule);
			auxRetireBoule(position);
			positionBoules.remove(boule);
		}
		else;
	}
	
	//retire une feuille de l'arbre, et resorbe automatiquement l'arbre, puis effectue une mise a jour remontante depuis le lieu de resorbation.
	private void auxRetireBoule(Feuille position){
		if(position.aUnPere()){
			NoeudInterne papa = position.pere;//le noeud devenu inutile qui va etre "remplace" par un de ses fils
			if(papa.aUnPere()){//resorbation
				NoeudInterne grandPapa = papa.pere;
				ArbreBinaire adopte = papa.getFils(!position.cote);
				grandPapa.setFils(papa.cote, adopte);
				adopte.pere = grandPapa;
				adopte.cote = papa.cote;
				
				miseAJourRemontante(grandPapa);
			}
			else{
				arbre = papa.getFils(!position.cote);
				arbre.pere = null;
				arbre.metsAJourLocalement();
			}
			
			recycleurFeuille.recupere(position);
			recycleurNoeuds.recupere(papa);
		}
		else{//arbre est une Feuille
			recycleurFeuille.recupere((Feuille) arbre);
			arbre = null;
		}
	}
	//ajoute toutes les boules dans l'urne selon le coefficient qui leur est associe par la fonction ponderation.
	public void ajouteToutesBoules(Map<B, Double> ponderation){
		for(B i: ponderation.keySet()){
			ajouteBoule(i,ponderation.get(i));
		}
	}
	
	//reinitialise l'urne pour en faire une urne de tirage uniforme parmi l'ensemble de boules en parametre (tous les coefficients valent 1)
	public void initialiseUniforme(Set<B> boules){
		arbre= null;
		for(B i: boules){
			this.ajouteBoule(i, 1.0);
		}
	}
	
	//renvoie true ssi l'urne ne contient pas de boule.
	public boolean isEmpty(){
		return positionBoules.keySet().isEmpty();
	}

	//enleve toutes les boules de l'urne
	public void clear(){
		this.arbre= null;
		this.positionBoules.clear();
		//discutable
		//this.recycleurFeuille.clear();
		//this.recycleurNoeuds.clear();
	}
	
	public void modifiePoids(B boule, double poids){
		if(positionBoules.containsKey(boule)){
			if(poids==0){// on retire la boule de l'urne
				auxRetireBoule(positionBoules.get(boule));
				positionBoules.remove(boule);
			}
			else{
				Feuille position = positionBoules.get(boule);
				position.poids = poids;
				miseAJourRemontante(position);
			}
		}
		else{
			throw new LeProgrammeurEstUnGrosFruitException("cette boule n'est pas dans l'Urne!");
		}
	}

	@Override
	public void modifiePoidsToutesBoules(Map<B, Double> nouvellePonderation) {
		/*
		 * Ne devrait etre appellee que si le nombre de poids a modifier est de l'ordre du nombre de boules.
		 * contrairement a modifiePoids, ajoute les boules qui ne seraient pas dans l'Urne.
		 */
		for(B boule : nouvellePonderation.keySet()){
			double nouveauPoids = nouvellePonderation.get(boule);
			if(this.positionBoules.containsKey(boule)){
				Feuille position = positionBoules.get(boule);
				if(nouveauPoids==0){//reprend la methode de auxRetireBoule, mais sans la mise a jour remontante.
					if(position.aUnPere()){
						NoeudInterne papa = position.pere;//le noeud devenu inutile qui va etre "remplace" par un de ses fils
						if(papa.aUnPere()){//resorbation
							NoeudInterne grandPapa = papa.pere;
							ArbreBinaire adopte = papa.getFils(!position.cote);
							grandPapa.setFils(papa.cote, adopte);
							adopte.pere = grandPapa;
							adopte.cote = papa.cote;
						}
						else{//position est un fils de arbre
							arbre = papa.getFils(!position.cote);
							
							recycleurNoeuds.recupere(arbre.pere);
							arbre.pere = null;
						}
						recycleurNoeuds.recupere(papa);
					}
					else{//arbre est une Feuille
						recycleurFeuille.recupere((Feuille) arbre);
						arbre = null;
					}
					
					positionBoules.remove(boule);
				}
				else{
					position.poids = nouveauPoids;
				}
			}
			else{//il vaut mieux que ce cas ne soit pas trop frequent.
				Feuille position = positionBoules.get(boule);
				position.poids = nouveauPoids;
				miseAJourRemontante(position);
			}
		}
		
		//on met a jour l'information sur la hauteur et le poids.
		if(arbre!=null) arbre.miseAJourRecursiveDescendante();
	}

	private class RecycleurNoeuds extends Recycleur<NoeudInterne>{

		@Override
		protected NoeudInterne construitNeuf() {
			return new NoeudInterne(null, false, null, null);
		}
		
	}
	private class RecycleurFeuille extends Recycleur<Feuille>{

		@Override
		protected Feuille construitNeuf() {
			return new Feuille(null, false, 0, null);
		}
		
	}
	
	private Recycleur<NoeudInterne> recycleurNoeuds = new RecycleurNoeuds();
	private Recycleur<Feuille> recycleurFeuille = new RecycleurFeuille();
	
	private NoeudInterne fournitNoeudInterne(NoeudInterne pere, boolean cote, ArbreBinaire gauche, ArbreBinaire droite){
		NoeudInterne res = recycleurNoeuds.fournit();
		
		res.pere = pere;
		res.cote = cote;
		res.poids = 0.0;
		res.hauteur = 1;
		
		res.gauche = gauche;
		res.droite = droite;
		
		return res;
	}
	private Feuille fournitFeuille(NoeudInterne pere, boolean cote, double poids, B boule){
		Feuille res = recycleurFeuille.fournit();
		
		res.pere = pere;
		res.cote = cote;
		res.poids = poids;
		res.hauteur = 1;
		
		res.boule = boule;
		
		return res;
	}
	
	private abstract class ArbreBinaire implements Recyclable{

		protected NoeudInterne pere;
		public boolean cote;//true pour droite, false pour gauche.
		public double poids; // chaque noeud possede la valeur �gale � la somme de toutes les valeurs des feuilles qui en resultent
		int hauteur;
		
		protected abstract void metsAJourLocalement();
		public abstract void miseAJourRecursiveDescendante();

		final boolean aUnPere(){
			return this.pere!=null;
		}

		public abstract Feuille ajouteBoule(B boule, double p);
		
		protected abstract boolean estUneFeuille();
		
		public abstract Feuille tirer(double r);
		
		protected abstract void equilibrer();

		public ArbreBinaire(NoeudInterne pere, boolean cote, double poids, int hauteur) {
			this.pere = pere;
			this.cote = cote;
			this.poids = poids;
			this.hauteur = hauteur;
		}

		public void setPere(NoeudInterne pere) {
			this.pere = pere;
		}

		public void setPoids(double poids) {
			this.poids = poids;
		}

		public void setHauteur(int hauteur) {
			this.hauteur = hauteur;
		}
		
		//a n'utiliser que pour des restrucutrations ponctuelles;
		public void restructurationRemontante(){
			equilibrer();
			this.metsAJourLocalement();
			if(this.aUnPere()) this.pere.restructurationRemontante();
		}
	}
	private class Feuille extends ArbreBinaire {

		public B boule;

		public Feuille(NoeudInterne pere, boolean cote, double poids, B boule) {
			super(pere, cote, poids, 1);
			this.boule = boule;
		}

		protected void metsAJourLocalement() {
		}

		@Override
		protected boolean estUneFeuille() {
			return true;
		}

		@Override
		public Feuille ajouteBoule(B boule, double p) {
			throw new LeProgrammeurEstUnGrosFruitException("on ne peut demander a une Feuille d'ajouter une boule.");
		}

		@Override
		public Feuille tirer(double r) {
			return this;
		}

		@Override
		public void miseAJourRecursiveDescendante() {
		}

		@Override
		protected void equilibrer() {
			// rien a faire
			
		}

		@Override
		public void nettoie() {
			this.boule = null;
			
			this.pere = null;
			
			this.cote = false;
			this.poids = 1.0;
			
			this.hauteur = 1;
		}
	}
	private class NoeudInterne extends ArbreBinaire{
		/*
		 * Un arbre est vide s'il n'a pas de fils gauche et pas de fils droit, et si ce n'est pas une feuille.
		 * Une Feuille est un arbre qui contient une boule, et n'a pas de fils.
		 * On garantit l'invariant suivant : tout arbre qui n'est ni vide, ni une Feuille, a un fils gauche et un fils droit.
		 * Autrement dit : un des fils d'un Arbre est null si et seulement si ses deux le sont.
		 */

		ArbreBinaire gauche;
		ArbreBinaire droite;//feuille est non null si et seulement si l'element est bien une feuille.
			

		
		public NoeudInterne(NoeudInterne pere, boolean cote, ArbreBinaire gauche, ArbreBinaire droite) {
			super(pere, cote, 0.0, 1);
			this.gauche = gauche;
			this.droite = droite;
		}

		protected void metsAJourLocalement(){//met localement a jour le poids et la hauteur en considerant ceux des descendants
			if(gauche==null||droite==null) throw new LeProgrammeurEstUnGrosFruitException("arbre a structure degeneree.");
			else{
				this.hauteur = 1 + Math.max(gauche.hauteur, droite.hauteur);
				this.poids = gauche.poids+droite.poids;
			}
		}

		public void miseAJourRecursiveDescendante(){
			/*
			 * Une alternative a la mise a jour montante en cas de modification simultanee de beaucoup
			 * de feuilles. On met cette fois tout a jour, restructuration comprise, avec un cout lineaire,
			 * par un parcours en profondeur.
			 */
			if(gauche==null||droite==null) throw new LeProgrammeurEstUnGrosFruitException("arbre degenere : ce noeud interne a un fils null");
			
			gauche.miseAJourRecursiveDescendante();
			droite.miseAJourRecursiveDescendante();
			
			while(Math.abs(gauche.hauteur-droite.hauteur)>1) this.equilibrer();
			this.metsAJourLocalement();
		}
		
		public Feuille tirer(double d){//principe: un curseur muni d'une valeur se balade dans l'arbre. si la valeur du fils gauche du noeud ou se trouve le curseur est plus eleve que celle du curseur, ce dernier va a gauche. sinon, on retire la valeur au curseur et il va a droite.
			//TODO if (val=0) --> exception arbre vide. mais enfait le filtrage est effectu� lorsque la fonction est appelee
			if(gauche==null||droite==null) throw new LeProgrammeurEstUnGrosFruitException("arbre degenere : un noeud interne a un fils null.");
			
			if (gauche.poids>d) return gauche.tirer(d);
			else return droite.tirer(d-gauche.poids);
		}

		@Override
		public Feuille ajouteBoule(B boule, double p) {
			if(gauche==null||droite==null) throw new LeProgrammeurEstUnGrosFruitException("un noeud interne n'a pas le droit d'avoir des fils null.");
			
			if(gauche.hauteur<droite.hauteur){//on insere a gauche
				if(gauche.estUneFeuille()){
					Feuille f = fournitFeuille(null, true, p, boule);
					
					NoeudInterne a = fournitNoeudInterne(this, gauche.cote, this.gauche, f);
					f.setPere(a);
					gauche.cote = false; gauche.setPere(a);
					
					this.gauche = a;
					return f;
				}
				else{
					return gauche.ajouteBoule(boule, p);
				}
			}
			else{
				if(droite.estUneFeuille()){
					Feuille f = fournitFeuille(null, true, p, boule);
					
					NoeudInterne a = fournitNoeudInterne(this, droite.cote, this.droite, f);
					f.setPere(a);
					droite.cote = false; droite.setPere(a);
					
					this.droite = a;
					return f;
				}
				else{
					return droite.ajouteBoule(boule, p);
				}
			}
		}
		
		public ArbreBinaire getFils(boolean cote){
			if(cote) return droite;
			else return gauche;
		}
		
		public void setFils(boolean cote, ArbreBinaire a){
			if(cote){ 
				droite = a;
			}
			else gauche = a;
		}

		@Override
		protected boolean estUneFeuille() {
			return false;
		}

		@Override
		protected void equilibrer() {
			if(gauche==null||droite==null) throw new LeProgrammeurEstUnGrosFruitException("arbre degenere");
			
			if(gauche.hauteur>droite.hauteur+1){
				NoeudInterne a = (NoeudInterne) gauche;
				boolean coteH = (a.droite.hauteur>droite.hauteur);
				
				ArbreBinaire traversant = a.getFils(!coteH);
				ArbreBinaire remontant = a.getFils(coteH);
				
				a.setPere(this);
				a.setFils(false, traversant);
				a.setFils(true, droite);
				a.cote = true;
				//NoeudInterne naissant = fournitNoeudInterne(this, true, traversant, droite);pour ne pas recreer inutilement un noeud
				droite.setPere(a);
				traversant.setPere(a); traversant.cote=false;
				
				droite = a;
				
				gauche = remontant;
				gauche.setPere(this); gauche.cote=false;
				
				a.metsAJourLocalement();
				//this.metsAJourLocalement();
			}
			if(droite.hauteur>gauche.hauteur+1){
				NoeudInterne a = (NoeudInterne) droite;
				boolean coteH = (a.droite.hauteur>gauche.hauteur);
				
				ArbreBinaire traversant = a.getFils(!coteH);
				ArbreBinaire remontant = a.getFils(coteH);
				
				a.setPere(this);
				a.cote = false;
				a.gauche = this.gauche;
				a.droite = traversant;
				//NoeudInterne naissant = fournitNoeudInterne(this, false, gauche, traversant);
				gauche.setPere(a);
				traversant.setPere(a); traversant.cote=true;
				
				gauche = a;
				
				droite = remontant;
				droite.setPere(this); droite.cote=true;
				
				a.metsAJourLocalement();
				//this.metsAJourLocalement();
			}
		}

		@Override
		public void nettoie() {
			this.pere = null;
			
			this.cote = false;
			this.poids = 1.0;
			
			this.hauteur = 0;
			
			this.gauche = null;
			this.droite = null;
		}
	}
}
