package lois;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.distribution.ExponentialDistribution;

import phenotypique.Emplacement;
import phenotypique.EtreVivant;
import phenotypique.Phenotype;
import physique.Point;
import physique.PointVirtuel;
import physique.Univers;
import aleatoire.Urne;
import aleatoire.UrneSeijin;

/*
 * Pareil que DuplicateurA1, sauf que la comparaison d'energie est assouplie, grace
 * a une loi exponentielle.
 */
public class DuplicateurC0 extends Duplicateur{


	private double parametreBernouilli01 = 0.9;//celui qui decide si on continue a rearranger les points de destination d'un etre vivant
	private Urne<Point> urnePoints;
	
	private ExponentialDistribution ed = new ExponentialDistribution(1);
	
	public DuplicateurC0(Univers U) {
		super(U);
		urnePoints = new UrneSeijin<Point>();
		urnePoints.initialiseUniforme(U.getPoints());//ponderation arbitraire, c'est juste pour ajouter les points
	}
	public DuplicateurC0(Univers U, double p1) {
		super(U);
		this.parametreBernouilli01 = p1;
		urnePoints = new UrneSeijin<Point>();
		urnePoints.initialiseUniforme(U.getPoints());//ponderation arbitraire, c'est juste pour ajouter les points
	}

	@Override
	public Set<Point> lieuxNouveauNe(Phenotype ph, EtreVivant original) {
		// TODO Auto-generated method stub
		return null;
	}

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
		
		Map<Point, Double> ponderation = new Map<Point,Double>(){
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
			return new Emplacement();
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
				
				Set<Point> bloc = Collections.singleton(cible);//new HashSet<Point>();
				//bloc.add(cible);
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
		
		//etape 3 : ceci augmente le risque d'echec de la reproduction si un des etres vivants cibles a une energie trop grande.
		EtresVivantsImpliques.clear();
		for(PointVirtuel pv : emplacement.keySet()){
			Point pt = emplacement.get(pv);
			if(U.getOccupation().containsKey(pt)) EtresVivantsImpliques.add(U.getOccupation().get(pt));
		}
		for(EtreVivant etr : EtresVivantsImpliques){
			if(etr.getEnergie()>(original.getEnergie()*ed.sample()*10.0)){//cas ou la reproduction echoue
				
				return new Emplacement();
			}
		}
		
		return emplacement;
	}

	public static double poids(double energie){//TODO a choisir. Attention : ne pas renvoyer 0.
		//renvoie le poids affecte a un Point d'energie "energie". Cette fonction se doit d'etre decroissante
		// on choisit ici la formule p = 1/(1+e) 
		return (1.0/(1.0 + 100.0*energie*energie));
	}

}
