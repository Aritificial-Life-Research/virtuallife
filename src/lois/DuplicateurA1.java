package lois;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import phenotypique.Emplacement;
import phenotypique.EtreVivant;
import phenotypique.Phenotype;
import physique.Point;
import physique.PointVirtuel;
import physique.Univers;
import aleatoire.Urne;
import aleatoire.UrneSeijin;

/*
 * Tres similaire a DuplicateurA1.
 * Cependant, ici, le test de comparaisons d'energie est fait
 * apres l'optimisation de l'emplacement.
 */
public class DuplicateurA1 extends Duplicateur{


	private static double parametreBernouilli01 = 0.9;//celui qui decide si on continue a rearranger les points de destination d'un etre vivant
	private Urne<Point> urnePoints;
	
	public DuplicateurA1(Univers univers) {
		super(univers);
		urnePoints = new UrneSeijin<Point>();
		urnePoints.initialiseUniforme(U.getPoints());//ponderation arbitraire, c'est juste pour ajouter les points
		
		ponderation = new Map<Point,Double>(){
			@Override
			public void clear() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean containsKey(Object arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean containsValue(Object arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Set<java.util.Map.Entry<Point, Double>> entrySet() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Double get(Object arg0) {
				// TODO Auto-generated method stub
				return poids(U.energieDelOccupant((Point)arg0));
			}

			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Set<Point> keySet() {
				// TODO Auto-generated method stub
				return U.getPoints();
			}

			@Override
			public Double put(Point arg0, Double arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void putAll(Map<? extends Point, ? extends Double> arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Double remove(Object arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int size() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Collection<Double> values() {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		
		PointsExterieursImpliques = new HashSet<Point>();
		PointsImpliquesLibres = new HashSet<Point>();
		
		EtresVivantsImpliques = new HashSet<EtreVivant>();
		
		blocs = new HashSet<Set<Point>>();
		cardinalDuBloc = new HashMap<Set<Point>, Integer>();
		nombreCiblesDansBloc = new HashMap<Set<Point>, Integer>();
		saturationDuBloc = new HashMap<Set<Point>, Double>();
		
		pointsDepart = new UrneSeijin<PointVirtuel>();
		pointsCiblesLibres = new UrneSeijin<Point>();
		
		blocDuPoint = new HashMap<Point, Set<Point>>();
		
	}
	public DuplicateurA1(Univers univers, double p1) {
		super(univers);
		this.parametreBernouilli01 = p1;
		urnePoints = new UrneSeijin<Point>();
		urnePoints.initialiseUniforme(univers.getPoints());//ponderation arbitraire, c'est juste pour ajouter les points
		
		ponderation = new Map<Point,Double>(){
			@Override
			public void clear() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean containsKey(Object arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean containsValue(Object arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Set<java.util.Map.Entry<Point, Double>> entrySet() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Double get(Object arg0) {
				// TODO Auto-generated method stub
				return poids(U.energieDelOccupant((Point)arg0));
			}

			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Set<Point> keySet() {
				// TODO Auto-generated method stub
				return U.getPoints();
			}

			@Override
			public Double put(Point arg0, Double arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void putAll(Map<? extends Point, ? extends Double> arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Double remove(Object arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int size() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Collection<Double> values() {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		
		PointsExterieursImpliques = new HashSet<Point>();
		PointsImpliquesLibres = new HashSet<Point>();
		
		EtresVivantsImpliques = new HashSet<EtreVivant>();
		
		blocs = new HashSet<Set<Point>>();
		cardinalDuBloc = new HashMap<Set<Point>, Integer>();
		nombreCiblesDansBloc = new HashMap<Set<Point>, Integer>();
		saturationDuBloc = new HashMap<Set<Point>, Double>();
		
		pointsDepart = new UrneSeijin<PointVirtuel>();
		pointsCiblesLibres = new UrneSeijin<Point>();
		
		blocDuPoint = new HashMap<Point, Set<Point>>();
		

	}

	@Override
	public Set<Point> lieuxNouveauNe(Phenotype ph, EtreVivant original) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Set<Point> PointsExterieursImpliques;
	private Set<Point> PointsImpliquesLibres;//les points n'etant pas des cibles de pointeurs, a ne pas confondre avec des points non occuppes par des etres vivants.
	
	private Set<EtreVivant> EtresVivantsImpliques;
	
	private Set<Set<Point>> blocs;//un bloc correspond soit a l'ensembles des points sur lesquels s'etend un etre vivant, soit a un singleton d'un etre vivant non occupe
	private Map<Set<Point>, Integer> cardinalDuBloc;//associe, a chaque bloc, le nombre de ses points.
	private Map<Set<Point>, Integer> nombreCiblesDansBloc;//associe, a chaque bloc, le nombre de ses points qui est cible d'un PoinVirtuel du phenotype, via emplacement.
	private Map<Set<Point>, Double> saturationDuBloc;//le rapport (nmbre cibles)/cardinal
	
	private Urne<PointVirtuel> pointsDepart;
	private Urne<Point> pointsCiblesLibres;
	
	Map<Point, Double> ponderation;

	private Map<Point, Set<Point>> blocDuPoint;//permet, etant donne un point, de remonter a son bloc.
	
	private Emplacement emplacementVide = new Emplacement();
	@Override
	public Emplacement emplacementNouveauNe(Phenotype ph, EtreVivant original) {

		Set<PointVirtuel> PVDepart = ph.getAnatomie().getSommets();//les sommets du phenotype
		
		Emplacement emplacement = ph.fournitEmplacement();//l'emplacement final du nouveau ne, a remplir.
		

		//etape 1 : tirage barycentrique de points cibles initiaux en fonction de l'energie.
	
		//initialisation de l'urne avec des cefficients dependant de l'energie. ATTENTION : on pressuppose que tous les points de l'Univers sont deja dans l'Urne.

		/*for(Point ext : this.U.getPoints()){//initialisation de l'urne avec des cefficients dependant de l'energie. ATTENTION : on pressuppose que tous les points de l'Univers sont deja dans l'Urne.
			double coefficient = poids(U.energieDelOccupant(ext));
			urnePoints.modifiePoids(ext, coefficient);
		}*/
		

		urnePoints.modifiePoidsToutesBoules(ponderation);
		
		for(PointVirtuel pv : PVDepart){//on retire de l'urne les points ou s'etend l'etre vivant original
			urnePoints.retireBoule(original.getEmplacement().get(pv));
		}
		
		boolean echec = false;
		for(PointVirtuel depart : PVDepart){//on initialise emplacement
			if(urnePoints.isEmpty()) echec = true;//reproduction echouee faute de points ou se reproduire.
			else{
				Point cibleInitiale = urnePoints.tireSansRemise();
				emplacement.put(depart, cibleInitiale);
			}
		}
		for(PointVirtuel pv: emplacement.keySet()){
			Point cible = emplacement.get(pv);
			urnePoints.ajouteBoule(cible, poids(U.energieDelOccupant(cible)));
		}
		
		for(PointVirtuel pv : PVDepart){// on remet les points de l'etre vivant original dans l'urne.
			Point p = original.getEmplacement().get(pv);
			urnePoints.ajouteBoule(p, poids(U.energieDelOccupant(p)));
		}
		if(echec) {
			System.out.println("echec");//TODO
			return this.emplacementVide;
		}
		
		//etape 2 : on rearrange les points cibles pour "eviter les morts inutiles"
		
		PointsExterieursImpliques.clear();
		PointsImpliquesLibres.clear();
		
		EtresVivantsImpliques.clear();
		
		blocs.clear();
		cardinalDuBloc.clear();
		nombreCiblesDansBloc.clear();
		saturationDuBloc.clear();
		
		for(PointVirtuel pv : emplacement.keySet()){//les points de chaque etre vivant dont l'un des points est un point cible sont impliques
			Point cible = emplacement.get(pv);
			if(U.getOccupation().containsKey(cible)){//si le point est occupe par un EtreVivant
				EtreVivant concerne = U.getOccupation().get(cible);
				for(PointVirtuel nouveauPV : concerne.getEmplacement().keySet()){
					Point nouveauP = concerne.getEmplacement().get(nouveauPV);
					PointsExterieursImpliques.add(nouveauP);
					PointsImpliquesLibres.add(nouveauP);
				}
				
				EtresVivantsImpliques.add(concerne);
			}
			else{//si le point n'est pas occuppe
				PointsExterieursImpliques.add(cible);
				
				Set<Point> bloc = Collections.singleton(cible);//new HashSet<Point>();
				//bloc.add(cible);
				blocs.add(bloc);
				
				cardinalDuBloc.put(bloc, 1);
				nombreCiblesDansBloc.put(bloc, 1);
				
				blocDuPoint.put(cible, bloc);
			}
		}
		for(EtreVivant ev : EtresVivantsImpliques){//on forme ici les blocs correspondant aux etres vivants impliques
			Set<Point> bloc = ev.etendue;
			
			for(Point p : bloc){
				blocDuPoint.put(p, bloc);
			}
			//inutile
			blocs.add(bloc);
			
			cardinalDuBloc.put(bloc, bloc.size());
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
		
		pointsDepart.clear();
		pointsCiblesLibres.clear();
		
		for(PointVirtuel pv : emplacement.keySet()){//initialise l'urne pointsDepart
			pointsDepart.ajouteBoule(pv, 1-saturationDuBloc.get(blocDuPoint.get(emplacement.get(pv))));
		}
		
		pointsCiblesLibres.initialiseUniforme(PointsImpliquesLibres);//initialise l'urne pointsCiblesLibres
		
		while(Math.random() < parametreBernouilli01){//TODO ca marche ca? On decide aleatoirement si on continue de rearranger les points cibles
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
		
		//etape 3 : on verifie si un des points choisis n'a pas une energie trop grande pour l'etre vivant, auquel cas la reproduction a echoue (on renvoie une emplacement vide).
		
		for(PointVirtuel pv  : emplacement.keySet()){
			if(U.energieDelOccupant(emplacement.get(pv))>original.getEnergie()){//cas ou la reproduction echoue
				return this.emplacementVide;
			}
		}
		
		return emplacement;
	}

	public static double poids(double energie){//TODO a choisir. Attention : ne pas renvoyer 0.
		//renvoie le poids affecte a un Point d'energie "energie". Cette fonction se doit d'etre decroissante
		// on choisit ici la formule p = 1/(1+e^2) 
		return (1.0/(1.0 + 100.0*energie*energie));
	}
}
