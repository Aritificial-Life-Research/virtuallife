package genomique;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import phenotypique.Anatomie;
import phenotypique.Phenotype;
import physique.Lettre;
import physique.PointVirtuel;
import toolbox.Doublet;
import toolbox.LeProgrammeurEstUnGrosFruitException;

/*
 * Un Genome qui calque l'anatomie qu'il decrit.
 * Pas d'information silencieuse.
 * 
 * Les mutations possibles sont :
 * -destruction d'un point, et donc de toutes les aretes qui y arrivent ou en partent
 * -destruction d'une arete
 * -redirection d'une arete vers n'importe que autre point, uniformement
 * -apparition d'une nouvelle arete vers n'importe quel autre point, uniformement
 * -Dilatation d'une arete : -L->P devient -L->P'-L->P' ou P' est un nouveau point et L' une Lettre au hasard
 * -ajout d'un nouveau sommet d'entree pointant vers un point quelconque deja existant
 */
public class GenomeElementaire extends Genome {

	private double probabiliteDestructionPV = 0.001;//.01 ;
	private double probabiliteDestructionLettre = 0.001;//0.01;
	private double pRedirectionLettre = 0.001;
	private double pApparitionArete = 0.001;
	private double pDilatationArete = 0.001;
	private double pApparitionEntree = 0.001;
	
	@Override
	public Doublet<Phenotype, Genome> mute(Phenotype original) {
		/*
		 *   La tactique est la suivante :
		 *   
		 *   Des mutations peuvent apparaitre a certains endroits de l'anatomie.
		 *   On inspecte ces endroits sur l'anatomie de l'etre vivant original.
		 *   
		 *   Des qu'une mutation a lieu, on cree une copie de l'anatomie originale (qui est elle en LECTURE SEULE)
		 * et qui est modifiee au fur et a mesure que des mutations apparaissent.
		 * 
		 *   
		 */
		//permet de savoir si une mutation a eu lieu
		boolean aMute = false;
		boolean mutationNonSilencieuse = false;
		//les champs suivant ne seront utilises qu'en cas de mutation
		//associe a chaque point de l'Anatomie original le point correspondant dans la nouvelle anatomie en cas de mutation
		Map<PointVirtuel, PointVirtuel> correspondance = null;
		
		Set<PointVirtuel> pointsDetruits = null;
		List<PointVirtuel> pointsChoisissables = null;
		List<PointVirtuel> nouveauxPointsVirtuels = null;
		List<PointVirtuel> nouvellesEntrees = null;
		int cpt = 0;
		
		Anatomie aOriginal = original.getAnatomie();
		
		//on itere sur tous les points du l'EtreVivant et on ajoute des mutations eventuelles
		for(PointVirtuel pv : aOriginal.getSommets()){
			//mutation : dilatation d'une arete
			for(Lettre l1 : pv.successeurs.keySet()){
				if(Math.random()<this.pDilatationArete){
					if(!aMute){//initialisation de correspondance
						correspondance = new HashMap<PointVirtuel, PointVirtuel>();
						for(PointVirtuel pv1 : aOriginal.getSommets()){
							correspondance.put(pv1, new PointVirtuel(cpt++));
						}
						for(PointVirtuel pv1 : aOriginal.getSommets()){
							for(Lettre l : pv1.successeurs.keySet()){
								correspondance.get(pv1).successeurs.put(l, correspondance.get(pv1.successeurs.get(l)));
							}
						}
						
						//initialisation de PointDetruit
						pointsDetruits = new HashSet<PointVirtuel>(4);
						pointsChoisissables = new ArrayList<PointVirtuel>(aOriginal.getSommets());
						nouveauxPointsVirtuels = new LinkedList<PointVirtuel>();
						nouvellesEntrees = new LinkedList<PointVirtuel>();
						
						aMute = true;
					}
					PointVirtuel nouveau = new PointVirtuel(cpt++);
					nouveauxPointsVirtuels.add(nouveau);
					
					Lettre nouvelleLettre = this.choixDeLettre[this.r.nextInt(this.choixDeLettre.length)];
					
					nouveau.successeurs.put(nouvelleLettre, correspondance.get(pv.successeurs.get(l1)));
					
					correspondance.get(pv).successeurs.put(l1, nouveau);
				}
			}						
			//mutation : apparition d'une arete
			if(Math.random()<pApparitionArete){
				if(!aMute){//initialisation de correspondance
					correspondance = new HashMap<PointVirtuel, PointVirtuel>();
					for(PointVirtuel pv1 : aOriginal.getSommets()){
						correspondance.put(pv1, new PointVirtuel(cpt++));
					}
					for(PointVirtuel pv1 : aOriginal.getSommets()){
						for(Lettre l : pv1.successeurs.keySet()){
							correspondance.get(pv1).successeurs.put(l, correspondance.get(pv1.successeurs.get(l)));
						}
					}
					
					//initialisation de PointDetruit
					pointsDetruits = new HashSet<PointVirtuel>(4);
					pointsChoisissables = new ArrayList<PointVirtuel>(aOriginal.getSommets());
					nouveauxPointsVirtuels = new LinkedList<PointVirtuel>();
					nouvellesEntrees = new LinkedList<PointVirtuel>();
					
					aMute = true;
				}
				Lettre nouvelleLettre = this.choixDeLettre[this.r.nextInt(this.choixDeLettre.length)];
				PointVirtuel cible = this.choisitPV(pointsChoisissables);
				
				correspondance.get(pv).successeurs.put(nouvelleLettre, correspondance.get(cible));
			}
			//mutation : redirection d'une arete
			for(Lettre l1 : pv.successeurs.keySet()){
				if(Math.random()<this.pRedirectionLettre){
					if(!aMute){//initialisation de correspondance
						correspondance = new HashMap<PointVirtuel, PointVirtuel>();
						for(PointVirtuel pv1 : aOriginal.getSommets()){
							correspondance.put(pv1, new PointVirtuel(cpt++));
						}
						for(PointVirtuel pv1 : aOriginal.getSommets()){
							for(Lettre l : pv1.successeurs.keySet()){
								correspondance.get(pv1).successeurs.put(l, correspondance.get(pv1.successeurs.get(l)));
							}
						}
						
						//initialisation de PointDetruit
						pointsDetruits = new HashSet<PointVirtuel>(4);
						pointsChoisissables = new ArrayList<PointVirtuel>(aOriginal.getSommets());
						nouveauxPointsVirtuels = new LinkedList<PointVirtuel>();
						nouvellesEntrees = new LinkedList<PointVirtuel>();
						
						aMute = true;
					}
					correspondance.get(pv).successeurs.put(l1, correspondance.get(this.choisitPV(pointsChoisissables)));
				}
			}			
			//mutation : destruction d'une arete
			for(Lettre l1 : pv.successeurs.keySet()){
				if(Math.random()<this.probabiliteDestructionLettre){
					if(!aMute){//initialisation de correspondance
						correspondance = new HashMap<PointVirtuel, PointVirtuel>();
						for(PointVirtuel pv1 : aOriginal.getSommets()){
							correspondance.put(pv1, new PointVirtuel(cpt++));
						}
						for(PointVirtuel pv1 : aOriginal.getSommets()){
							for(Lettre l : pv1.successeurs.keySet()){
								correspondance.get(pv1).successeurs.put(l, correspondance.get(pv1.successeurs.get(l)));
							}
						}
						
						//initialisation de PointDetruit
						pointsDetruits = new HashSet<PointVirtuel>(4);
						pointsChoisissables = new ArrayList<PointVirtuel>(aOriginal.getSommets());
						nouveauxPointsVirtuels = new LinkedList<PointVirtuel>();
						nouvellesEntrees = new LinkedList<PointVirtuel>();
						
						aMute = true;
					}
					
					correspondance.get(pv).successeurs.remove(l1);
				}
			}
			//mutation : DESTRUCTION D'UN POINT
			if(Math.random() < probabiliteDestructionPV){
				if(!aMute){//initialisation de correspondance
					correspondance = new HashMap<PointVirtuel, PointVirtuel>();
					for(PointVirtuel pv1 : aOriginal.getSommets()){
						correspondance.put(pv1, new PointVirtuel(cpt++));
					}
					for(PointVirtuel pv1 : aOriginal.getSommets()){
						for(Lettre l : pv1.successeurs.keySet()){
							correspondance.get(pv1).successeurs.put(l, correspondance.get(pv1.successeurs.get(l)));
						}
					}
					
					//initialisation de PointDetruit
					pointsDetruits = new HashSet<PointVirtuel>(4);
					pointsChoisissables = new ArrayList<PointVirtuel>(aOriginal.getSommets());
					nouveauxPointsVirtuels = new LinkedList<PointVirtuel>();
					nouvellesEntrees = new LinkedList<PointVirtuel>();
					
					aMute = true;
				}
				pointsDetruits.add(correspondance.get(pv));
			}
			//mutation : apparition d'un point d'entree qui pointe sur le PointVirtuel courant.
			if(Math.random()<this.pApparitionEntree){
				if(!aMute){//initialisation de correspondance
					correspondance = new HashMap<PointVirtuel, PointVirtuel>();
					for(PointVirtuel pv1 : aOriginal.getSommets()){
						correspondance.put(pv1, new PointVirtuel(cpt++));
					}
					for(PointVirtuel pv1 : aOriginal.getSommets()){
						for(Lettre l : pv1.successeurs.keySet()){
							correspondance.get(pv1).successeurs.put(l, correspondance.get(pv1.successeurs.get(l)));
						}
					}
				
					//initialisation de PointDetruit
					pointsDetruits = new HashSet<PointVirtuel>(4);
					pointsChoisissables = new ArrayList<PointVirtuel>(aOriginal.getSommets());
					nouveauxPointsVirtuels = new LinkedList<PointVirtuel>();
					nouvellesEntrees = new LinkedList<PointVirtuel>();
					
					aMute = true;
				}
				PointVirtuel nouveau = new PointVirtuel(cpt++);
			
				Lettre nouvelleLettre = this.choixDeLettre[this.r.nextInt(this.choixDeLettre.length)];
			
				nouveau.successeurs.put(nouvelleLettre, correspondance.get(pv));
				
				nouveauxPointsVirtuels.add(nouveau);
				nouvellesEntrees.add(nouveau);
			}
		}
		
		Genome genRes = this;
		Phenotype phenRes = original;
		
		if(aMute){//construction d'un nouveau genome et d'un nouveau phenotype
			Anatomie aGen = new Anatomie();
			//ajout des sommets d'entree
			for(PointVirtuel pve : aOriginal.getSommetsEntree()){
				if(!pointsDetruits.contains(correspondance.get(pve))){
					aGen.getSommetsEntree().add(correspondance.get(pve));
					aGen.setSe(aGen.getSe()+1);
				}
			}
			for(PointVirtuel pve : nouvellesEntrees){
				aGen.getSommetsEntree().add(pve);
				aGen.setSe(aGen.getSe()+1);
			}
			
			//ajout du sommet de sortie
			if(!pointsDetruits.contains(correspondance.get(aOriginal.getSommetSortie()))){
				aGen.setSommetSortie(correspondance.get(aOriginal.getSommetSortie()));
			}
			//ajout des autres sommets
			for(PointVirtuel pv : aOriginal.getSommets()){
				if(!pointsDetruits.contains(correspondance.get(pv))){
					aGen.getSommets().add(correspondance.get(pv));
					aGen.setS(aGen.getS()+1);
					
					//suppression des aretes qui menent a des Points detruits.
					Iterator<Map.Entry<Lettre, PointVirtuel>> iter = correspondance.get(pv).successeurs.entrySet().iterator();
					while(iter.hasNext()){
						if(pointsDetruits.contains(iter.next().getValue())){
							iter.remove();
						}
					}
				}
			}
			//ajouts des nouveaux sommets crees
			for(PointVirtuel pv : nouveauxPointsVirtuels){
				aGen.getSommets().add(pv);
				aGen.setS(aGen.getS()+1);
				
				//suppression des aretes qui menent a des Points detruits.
				Iterator<Map.Entry<Lettre, PointVirtuel>> iter = pv.successeurs.entrySet().iterator();
				while(iter.hasNext()){
					if(pointsDetruits.contains(iter.next().getValue())){
						iter.remove();
					}
				}
			}
			aGen = aGen.recopieProprement();//une forme de clonage qui augmente l'efficacite algorithmique
			
			//on verifie que le nouveau genome candidat est viable
			if(this.testeViabilite(aGen)){
				genRes = new GenomeElementaire(aGen, choixDeLettre);
				phenRes = new Phenotype(aGen.recopieProprement(), 0.6);
			}
			else{
				genRes = null;
				phenRes = null;
			}
		}
		
		return new Doublet<Phenotype, Genome>(phenRes, genRes);
	}
	

	//l'anatomie en partie calquee de l'EtreVivant original
	private Anatomie anatomie;
	private Lettre[] choixDeLettre;
	
	private Random r = new Random();

	public GenomeElementaire(Anatomie anatomie, Lettre[] choixDeLettres) {
		this.anatomie = anatomie;
	}

	public GenomeElementaire(Anatomie anatomie, Set<Lettre> lettresAccessibles){
		this.anatomie = anatomie;
		this.choixDeLettre = lettresAccessibles.toArray(new Lettre[0]);
	}
	
	//choisit un point au hasard parmi un ensemble de points donne (couteux!)
	private PointVirtuel choisitPV(Set<PointVirtuel> possibilites){
		PointVirtuel[] tab = possibilites.toArray(new PointVirtuel[0]);
		
		int indice = r.nextInt(tab.length);
		
		return tab[indice];
	}
	private PointVirtuel choisitPV(List<PointVirtuel> possibilites){
		int indice = r.nextInt(possibilites.size());
		return possibilites.get(indice);
	}
	/*
	 * Teste si l'anatomie passee en parametres verifie toutes les conditions requises
	 * pour etre une anatomie d'etre vivant.
	 */
	public boolean testeViabilite(Anatomie nouvelleAnatomie){
		if(nouvelleAnatomie == null) return false;
		
		//on verifie qu'il y a bien un sommet de sortie
		if(nouvelleAnatomie.getSommetSortie() == null) return false;
		
		//on verifie qu'il y a au moins un sommet d'entree
		if(nouvelleAnatomie.getSommetsEntree() == null) return false;
		if(nouvelleAnatomie.getSommetsEntree().isEmpty()) return false;
		
		//on verifie que la sortie ne mene nulle part
		PointVirtuel sortie = nouvelleAnatomie.getSommetSortie();
		if(!(sortie.successeurs.isEmpty())) return false;
		
		//on verifie qu'aucune arete n'arrive sur un sommet d'entree
		Set<PointVirtuel> entrees = nouvelleAnatomie.getSommetsEntree();
		for(PointVirtuel qqc : nouvelleAnatomie.getSommets()){
			if(arriveSurEntree(qqc, entrees)) return false;
		}
		
		//on verifie que de tout sommet il existe un chemin jusqu'a la sortie (parcours de graphe)
		Set<PointVirtuel> atteignentLaSortie = new HashSet<PointVirtuel>();
		atteignentLaSortie.add(sortie);
		Set<PointVirtuel> dejaVus = new HashSet<PointVirtuel>(4);
		for(PointVirtuel pv : nouvelleAnatomie.getSommets()){
			if(!this.peutRejoindreSortie(pv, atteignentLaSortie, dejaVus)) return false;
		}
		
		//on verifie que tout point est l'extremite d'un chemin partant d'un sommet d'entree (parcours de graphe)
		Set<PointVirtuel> entreeAtteignables = this.atteignablesDepuisEntree(nouvelleAnatomie.getSommetsEntree());
		for(PointVirtuel qqc : nouvelleAnatomie.getSommets()){
			if(!entreeAtteignables.contains(qqc)) return false;
		}
		
		return true;
	}
	
	private boolean arriveSurEntree(PointVirtuel examine, Set<PointVirtuel> entrees){
		for(PointVirtuel voisin : examine.successeurs.values()){
			if(entrees.contains(voisin)) return true;
		}
		return false;
	}
	
	private Set<PointVirtuel> atteignablesDepuisEntree(Set<PointVirtuel> entrees){
		Set<PointVirtuel> res = new HashSet<PointVirtuel>();
		for(PointVirtuel entree : entrees) parcoursDepuisEntree(entree, res);
		return res;
	}
	private void parcoursDepuisEntree(PointVirtuel examine, Set<PointVirtuel> atteignables){
		if(!atteignables.contains(examine)){
			atteignables.add(examine);
			for(PointVirtuel voisin : examine.successeurs.values()){
				parcoursDepuisEntree(voisin, atteignables);
			}
		}
	}
	
	private boolean peutRejoindreSortie(PointVirtuel examine, Set<PointVirtuel> rejoignentSortie, Set<PointVirtuel> dejaVus){
		if(rejoignentSortie.contains(examine)) return true;
		else{
			dejaVus.add(examine);
			for(Lettre l : examine.successeurs.keySet()){
				PointVirtuel voisin = examine.successeurs.get(l);
				if(!dejaVus.contains(voisin)){
					if(this.peutRejoindreSortie(voisin, rejoignentSortie, dejaVus)){
						rejoignentSortie.add(examine);
						dejaVus.remove(examine);
						return true;
					}
				}
			}
			return false;
		}
	}
}
