package lois;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import lois.SelecteurCheminECECB.ArbreChemins.NoeudChemins;
import lois.SelecteurCheminECECB.ArbreChemins.NoeudInterneChemins;

import phenotypique.Anatomie;
import phenotypique.EtreVivant;
import physique.Cout;
import physique.Lettre;
import physique.PointVirtuel;
import toolbox.Fournisseur;
import toolbox.LeProgrammeurEstUnGrosFruitException;
import toolbox.Recupereur;
import toolbox.Recycleur;

import chemins.Chemin;
import chemins.Chemin;
import chemins.Chemin.QueueChemin;

public class SelecteurCheminECECB extends SelecteurChemin {
	/*
	 * Un selecteur de Chemin a Enrichissement de Chemin Elementaire par des Cycles avec loi de Bernouilli.
	 * 
	 * Voici comment sont formes les Chemins partant d'un ensemble de sommet d'entree.
	 * 1)Arbitrairement (i.e aleatoirement), on se donne un ordre de priorite parmi ces sommets d'entree.
	 * Cet ordre intervient, a un instant de la formation des mots, quand les chemins en formation se disputent une ressource.
	 * 2)Dans l'ordre de priorite evoque, les sommets d'entree forment chacun un premier Chemin acyclique.
	 * 3)Depuis chaque sommet d'entree, ces Chemins sont parcourus depuis le sommet d'entree de maniere synchronise, incrementalement
	 * et a meme vitesse. En chaque point ou c'est possible, on decide aleatoirement d'enrichir le chemin a l'aide un cycle choisi aleatoirement,
	 * et ce selon une loi de Bernouilli de parametre pEnrichissement.
	 * Le Chemin se rallonge donc dynamiquement au cours de sa formation.
	 * 
	 * A NOTER : les ressources sont prelevees au cours de la formation des Chemins quand celle-ci reussit.
	 * Il y a donc des interactions avec l'environnement.
	 */
	

	private double pEnrichissement;//pour choisir ou on insert TODO PARAMETRE
	
	public Map<PointVirtuel, Map<PointVirtuel, ArbreChemins>> arbresCheminsElementaires;
	/*
	 * Sera utile dans 2 situations :
	 * 	-calculer un chemin elementaire donc acyclique d'un sommet d'entree vers le sommet de sortie
	 * 	-ajouter des cycles elementaires pour enrichir un chemin
	 * 

	 * 
	 * La racine (i.e le champ depart) de arbreCheminsElementaires[i][j] est toujours i;
	 */
	
	public SelecteurCheminECECB(Anatomie anatomie, double p1) {
		super(anatomie);
		this.pEnrichissement = p1;
		
		this.calculeArbresCheminsElementaires();
		//this.paracheveArbresCheminsElementaires();
	}


	

	
	public static Cout choisitCoutPlafond(Cout r){
		//renvoie un Cout inferieur a r;
		//TODO renvoyer Cout plus petit(aleatoire)?
		return r;
	}
	
	public void calculeArbresCheminsElementaires(){
		arbresCheminsElementaires = new HashMap<PointVirtuel, Map<PointVirtuel, ArbreChemins>>();
		
		Set<PointVirtuel> sommets = this.anatomie.getSommets();
		
		for (PointVirtuel depart : sommets) {//on cree des arbres vides pour tous les couples (depart, destination)
			Map<PointVirtuel, ArbreChemins> m = new HashMap<PointVirtuel, ArbreChemins>();
			for(PointVirtuel destination : sommets){
				m.put(destination, new ArbreChemins(depart, destination));
			}
			arbresCheminsElementaires.put(depart, m);
		}
		
		for(PointVirtuel i : sommets){//on incorpore les aretes de l'anatomie
			for(Lettre l : i.successeurs.keySet()){
				PointVirtuel j = i.successeurs.get(l);
				this.arbresCheminsElementaires.get(i).get(j).ajouteAreteDirecte(l);
			}
		}	
		
		for(int k = 1; k <= this.anatomie.getS() ; k++){//contruction de l'arbre recursivement
			for(PointVirtuel dep : sommets){
				for(Lettre l : dep.successeurs.keySet()){
					PointVirtuel etape = dep.successeurs.get(l);
					for(PointVirtuel dest : sommets){//aux chemins de longueur <k  allant de dep a dest, on raoute ceux de longueur <= de la forme depart-l->inter-...->destination
						if(!etape.equals(dest)){//assure qu'on ajoute pas de cycles
							arbresCheminsElementaires.get(dep).get(dest).adopteSansCycles(l, arbresCheminsElementaires.get(etape).get(dest));
						}
					}
				}
			}
		}		
	}
	
	public void paracheveArbresCheminsElementaires(){
		/*
		 * Par souci d'economie de place, ne laisse dans arbresCheminsElementaires que les chemins allant d'un sommet d'entree a celui de sortie
		 * ou d'un sommet a lui-meme.
		 */
		
		Set<PointVirtuel> sommetsEntree = this.anatomie.getSommetsEntree();
		PointVirtuel sommetSortie = this.anatomie.getSommetSortie();
		
		Iterator<PointVirtuel> itDep = arbresCheminsElementaires.keySet().iterator();
		while(itDep.hasNext()){
			PointVirtuel dep = itDep.next();
			if(sommetsEntree.contains(dep)){
				Iterator<PointVirtuel> itDest = arbresCheminsElementaires.get(dep).keySet().iterator();
				while(itDest.hasNext()){// on enleve tous les arbres qui ne menent pas au sommet de sortie
					PointVirtuel dest = itDest.next();
					if(!dest.equals(sommetSortie)) itDest.remove();
				}
			}
			else{//il reste a conserver les cycles
				Map<PointVirtuel, ArbreChemins> destinations = arbresCheminsElementaires.get(dep);
				if(destinations.containsKey(dep)){
					ArbreChemins aGarder = destinations.get(dep);//les chemins menant de dep a lui meme
					if(aGarder.neVaNullePart()){//il n'existe pas de cycle de dep vers lui-meme
						itDep.remove();
					}
					else{
						destinations.clear();//on enleve tous les autres arbres inutiles
						destinations.put(dep, aGarder);//on garde celui qui contient les cycles dep-dep
					}
				}
				else{//rien a garder
					itDep.remove();
				}
			}
		}
	}

	public double getP1() {
		return pEnrichissement;
	}

	public void setP1(double p1) {
		this.pEnrichissement = p1;
	}

	public Map<PointVirtuel, Map<PointVirtuel, ArbreChemins>> getArbresCheminsElementaires() {
		return arbresCheminsElementaires;
	}

	public void setArbresCheminsElementaires(
			Map<PointVirtuel, Map<PointVirtuel, ArbreChemins>> arbresCheminsElementaires) {
		this.arbresCheminsElementaires = arbresCheminsElementaires;
	}

	//utilisee dans la methode suivante
	private ArrayList<Integer> priorites = new ArrayList<Integer>();
	@Override
	public Chemin[] choisitChemins(EtreVivant eV, PointVirtuel[] sommetsEntrees) {
		Chemin res[] = new Chemin[sommetsEntrees.length];
		
		//liste disant dans quel ordre il faudra traiter les chemins
		priorites.clear();
		for(int i = 0; i < sommetsEntrees.length; i++){//initialisation de priorites
			priorites.add(i, i);
		}
		Collections.shuffle(priorites);//on change aleatoirement l'ordre de priorites
		
		//premiere etape : formation de chemins elementaires
		
		for(int i : priorites){
			res[i] = this.formeCheminSansCycles(sommetsEntrees[i], eV);
		}
		
		//deuxieme etape : enrichissement des Chemins
		
		PointVirtuel sortie = eV.getPh().getAnatomie().getSommetSortie();
		//une liste FIFO qui indique, dans l'ordre de priorites, ou enrichir les chemins de travail.
		Queue<QueueChemin> pointsDEnrichissement = new LinkedList<QueueChemin>();
		for(int i : priorites){//initialisation de pointsDEnrichissement
			Chemin ch = res[i];
			//on n'ajoute que les chemins dont la pre-formation sans cycles a reussi
			if(!ch.isEmpty()) pointsDEnrichissement.add(ch.getFirst());
		}
		while(!pointsDEnrichissement.isEmpty()){
			QueueChemin ouEnrichir = pointsDEnrichissement.poll();
			PointVirtuel pv = ouEnrichir.getSommet();
			if(pv.equals(sortie)){
				// on a fini d'enrichir ce chemin, on ne remet rien dans la liste
			}
			else{//on n'est pas arrivee a la sortie : on tente d'enrichir en ce point
				if(arbresCheminsElementaires.containsKey(pv)){
					//TODO verifier que ce deuxieme test est bien inutile, d'apres la construction de l'arbre
					if(arbresCheminsElementaires.get(pv).containsKey(pv)){//on peut enrichir
						if(Math.random()<this.pEnrichissement){//dans ce cas, on enrichit
							this.cheminVide.insere(this.formeCycleElementaire(pv, eV), ouEnrichir);//il est possible que la tentative echoue faute de ressources
							//REMARQUE : le fait qu'on appelle cette methode sur cheminVide n'est pas un probleme, n'importe quel chemin ferait l'affaire. cf le code de insere
						}
					}
				}
				//ensuite, on peut continuer d'avancer dans la formation de ce Chemin
				pointsDEnrichissement.add(ouEnrichir.getTail());
			}
		}
		
		return res;
	}
	//un Chemin vide a renvoyer en cas d'echec de formation du mot.
	private final Chemin cheminVide = new Chemin();
	//forme un cycle elementaire depuis le point virtuel pv si c'est possible.
	//attention : ne verifie pas s'il existe de tels cycles
	private Chemin formeCycleElementaire(PointVirtuel pv, EtreVivant ev){
		//chemin a renvoyer en cas de succes.
		Chemin res = new Chemin(new QueueChemin(null, pv, null));
		NoeudInterneChemins noeudCourant = this.arbresCheminsElementaires.get(pv).get(pv).racine;
		if(this.auxFormeCheminSansCycles(res, noeudCourant, ev, pv)){//succes a la formation du chemin
			res.enleveLaTete();//artificiellement, on avait mis le point pv au debut
			return res;
		}
		else{//echec faute de ressources
			return cheminVide;
		}
	}
	//forme un Chemin sans cycles allant du point d'entree au point de sortie. Preleve les ressources en cas de succes.
	private Chemin formeCheminSansCycles(PointVirtuel sommetEntree, EtreVivant eV){
		//Chemin a renvoyer en cas de succes
		Chemin res = new Chemin(new QueueChemin(null, sommetEntree, null));
		PointVirtuel sortie = eV.getPh().getAnatomie().getSommetSortie();
		NoeudInterneChemins noeudCourant = this.arbresCheminsElementaires.get(sommetEntree).get(sortie).racine;
		if(this.auxFormeCheminSansCycles(res, noeudCourant, eV, sortie)){//succes de la formation du Chemin, les ressources sont prelevees
			res.enleveLaTete();//on avait ajoute artificiellement le sommet de depart au chemin
			return res;
		}
		else{//echec de la formation du Chemin, res est incompletement forme et aucune ressource n'a ete prelevee
			return cheminVide;
		}
	}
	/*
	 *   Une fonction recursive utilisee par le methode precedente.
	 *   Poursuit la formation du Chemin aFormer depuis le PointVirtuel porte par noeudCourant.
	 *   Renvoie false en cas d'echec (faute de ressource disponibles), auquel cas les ressources
	 * ne sont pas prelevees. On fait ainsi remonter l'information de succes de la formation du mot
	 * depuis le point final (succes) ou depuis le point ou les ressources font defaut (echec). Les ressources
	 * sont eventuellement prelevees apres que l'information soit remontee.
	 *   Attention : le parcours ne s'arrete que quand le PointVirtuel sortie en parametres est atteint.
	 * Cela ne veut pas dire qu'un noeud terminal a ete atteint dans le parcours de l'arbre.
	 */
	private boolean auxFormeCheminSansCycles(Chemin aFormer, NoeudInterneChemins noeudCourant, EtreVivant eV, PointVirtuel pointFinal){
		Lettre suivante = this.choisitSuivant(noeudCourant, eV);
		if(suivante==null){//echec de formation du Chemin : aucune ressource disponible.
			return false;
		}
		else{
			NoeudChemins noeudSuivant = noeudCourant.branches.get(suivante);
			aFormer.rallongeALaFin(suivante, noeudSuivant.sommet);//on poursuit la formation du Chemin, que ce soit un echec ou non
			if(noeudSuivant.estTerminal()){//on est arrive au bout du chemin, fin de la recursion
				if(!noeudSuivant.sommet.equals(pointFinal)) throw new LeProgrammeurEstUnGrosFruitException("parcours anormal de l'arbre");//juste une precaution
				
				eV.preleveRessource(suivante, noeudCourant.sommet);//ressource prelevee
				return true;//succes de la formation du Chemin.
			}
			else{//on n'a pas fini de former le Chemin, on devrait donc etre sur que noeudSuivant est interne
				//une erreur de type classCastException devrait indiquer un defaut de l'abre ou une erreur dans le parametre sortie
				if(this.auxFormeCheminSansCycles(aFormer, (NoeudInterneChemins) noeudSuivant, eV, pointFinal)){
					//succes de la formation du Chemin, on peut prelever la ressource
					eV.preleveRessource(suivante, noeudCourant.sommet);//ressource prelevee
					return true;//on fait remonter l'information de succes
				}
				else{//echec de la formation du chemin, on fait remonter le constat d'echec
					return false;
					//ainsi aucune ressource ne sera prelevee
				}
			}
		}
	}
	//choisit une lettre parmi celles qui sont disponibles et qui partent de pv, au sein de l'arbre.
	private Lettre choisitSuivant(NoeudInterneChemins nIC, EtreVivant eV){
		int cpt1 = 0;
		for(Lettre l : nIC.branches.keySet()){
			if(eV.ressourceDisponible(l, nIC.sommet)) cpt1++;
		}
		if(cpt1==0) return null;//il n'y a plus de lettre disponible
		else{
			int k = rdm.nextInt(cpt1);
			cpt1 = 0;
			for(Lettre l : nIC.branches.keySet()){
				if(eV.ressourceDisponible(l, nIC.sommet)){
					if(cpt1==k) return l;
					else cpt1++;
				}
			}
		}
		//logiquement, ne devrait pas arriver
		throw new LeProgrammeurEstUnGrosFruitException("echec de choix de la Lettre");
	}
	private Random rdm = new Random();
	/*
	 *   Une maniere arborescente de stocker un ensemble de Chemins
	 * allant d'un point a un autre.
	 *   On garantit les invariants suivants :
	 *  1) un ArbreChemin ne contient aucun chemin ssi sa racine est vide
	 *  2) un ArbreChemin ne contient pas de Chemins formant des cycles internes stricts
	 *(autrement dit, la seule redondance possible d'un PointVirtuel au sein d'un chemin est celle du depart et de l'arrivee).
	 *  3) Les feuilles d'un ArbreChemin sont toujours des NoeudsTerminaux, dont le sommet associe est la destination de l'ArbreChemin.
	 */
	public static class ArbreChemins {
		
		private PointVirtuel depart;
		private PointVirtuel destination;
		
		private NoeudInterneChemins racine;
		
		//cree un arbre ne contenant aucun Chemin;
		public ArbreChemins(PointVirtuel depart, PointVirtuel destination){
			this.depart = depart;
			this.destination = destination;
			
			this.racine = new NoeudInterneChemins(depart);
		}
		
		//cree un arbre contenant uniquement le chemin direct depart-l->destination
		public ArbreChemins(PointVirtuel depart, PointVirtuel destination, Lettre l){
			this.depart = depart;
			this.destination = destination;
			
			this.racine = new NoeudInterneChemins(depart);
			this.racine.branches.put(l, new NoeudTerminalChemins(destination));
		}
		
		static abstract class NoeudChemins{

			protected PointVirtuel sommet;
			
			public abstract void seFaitAdopter(NoeudInterneChemins adopteur, Lettre l, PointVirtuel indesirable);
			
			public abstract boolean estTerminal();
			
			abstract String auxToString(String alinea);
		}
		
		static class NoeudTerminalChemins extends NoeudChemins{
			
			public NoeudTerminalChemins(PointVirtuel sommet){
				this.sommet = sommet;
			}

			public void seFaitAdopter(NoeudInterneChemins adopteur, Lettre l,
					PointVirtuel indesirable) {
				
				adopteur.branches.put(l, this);
			}

			@Override
			public boolean estTerminal() {
				return true;
			}

			@Override
			String auxToString(String alinea) {
				return sommet.toString();
			}
			
		}
		
		static class NoeudInterneChemins extends NoeudChemins{
			
			private Map<Lettre, NoeudChemins> branches;
			
			public NoeudInterneChemins(PointVirtuel sommet){
				this.sommet = sommet;
				
				this.branches = new HashMap<Lettre, NoeudChemins>(4, 1.0F);
			}
			
			public PointVirtuel getSommet() {
				return sommet;
			}

			public void setSommet(PointVirtuel sommet) {
				this.sommet = sommet;
			}

			public Map<Lettre, NoeudChemins> getBranches() {
				return branches;
			}

			public void setBranches(Map<Lettre, NoeudChemins> branches) {
				this.branches = branches;
			}

			@Override
			/*
			 * ajoute recursivement le contenu de cet arbre sous une l-branche de adopteur,
			 * en eliminant les chemins qui passeraient par indesirable.
			 */
			public void seFaitAdopter(NoeudInterneChemins adopteur, Lettre l,
					PointVirtuel indesirable) {
				if(this.sommet.equals(indesirable)){
					//on filtre ici pour ne pas ajouter de cycles.
				}
				else{
					if(!adopteur.branches.containsKey(l)){
						adopteur.branches.put(l, new NoeudInterneChemins(this.sommet));
					}
					NoeudInterneChemins nIC;
					try{
						nIC = (NoeudInterneChemins) adopteur.branches.get(l);
					}catch(ClassCastException e){
						throw new LeProgrammeurEstUnGrosFruitException("adoption illegale");
					}
					
					for(Lettre l1 : this.branches.keySet()){//recursion
						this.branches.get(l1).seFaitAdopter(nIC, l1, indesirable);
					}
					
					if(nIC.branches.isEmpty()){//resorbation eventuelle pour ne pas avoir de branches qui ne menent nulle part
						adopteur.branches.remove(l);
					}
				}
			}

			@Override
			public boolean estTerminal() {
				return false;
			}

			@Override
			String auxToString(String alinea) {
				String res = sommet.toString();
				
				for(Lettre l : branches.keySet()){
					res = (res + "\n" + alinea + "-" + l.toString() + "->" + branches.get(l).auxToString(alinea+" "));
				}
				
				return res;
			}	
		}
		
		private boolean neVaNullePart(){
			return this.racine.branches.isEmpty();
		}
		
		public void adopteSansCycles(Lettre l, ArbreChemins b){
			if(!b.destination.equals(this.destination)){
				throw new LeProgrammeurEstUnGrosFruitException("adoption d'un arbre qui n'a pas la meme destination");
			}
			if(!b.neVaNullePart() && !b.depart.equals(this.destination)){//indispensable si on ne veut pas avoir des branches non finies et pas de cycles
				b.racine.seFaitAdopter(this.racine, l, this.depart);
			}
		}
		
		public void ajouteAreteDirecte(Lettre l){//ajoute a l'arbre le chemin direct depart-l->destination
			this.racine.branches.put(l, new NoeudTerminalChemins(this.destination));
		}

		@Override
		public String toString() {
			return this.racine.auxToString(" ");
		}
	}
}
