package temporel;

import evenements.*;

import java.util.*;

import phenotypique.EtreVivant;
import physique.Univers;
import toolbox.LeProgrammeurEstUnGrosFruitException;

public class FriseChronologique implements Attentif{
	/*
	 *   Cet Objet sert a repertorier un ensemble d'Evenements programmes pour arriver dans le futur.
	 * On y trouve donc la date du present, puis un calendrier qui associe a des dates ulterieures des Evenements.
	 * 
	 *   Puisque l'on peut imaginer que plusieurs Evenements soient (par chance) simultanes, les Evenements a une
	 * date donnes sont stockes par ordre d'arrivee. C'est purement arbitraire : ce modele de temps est pense pour que
	 * des Evenement soient (tres) rarement simultanes, auquel cas ils ne sont pas censes interferer : l'ordre dans lequel ils
	 * se produisent ne compte pas.
	 * 
	 *   Certains Evenements (Reproduction, ProgressionMot, ChoixMot) sont a la fois differables et susceptibles
	 * de dependre de la survie d'un etre vivant : le survenue n'est donc pas en theorie garantie, mais il faut les
	 * programmer tout de meme, tout en etant pret a les retirer du calendrier en cas de mort de l'etreVivant dont
	 * ils dependent. C'est la vocation du champ projetsEtreVivants et c'est ce qui explique que FriseChronologique
	 * soit un objet Attentif.
	 * 
	 *   FriseChronologique est aussi attentif aux Evenements de type EcoulementduTemps, qui lui disent essentiellement
	 * que la date du present doit prendre une valeur ulterieure. On s'interdit de sauter des Evenements programmes : ainsi,
	 * chaque Evenement doit etre ecoule avant que dateActuelle prenne une valeur ulterieure. Ainsi, on garantit en pratique
	 * que les evenements programmes dans le calendrier n'appartiennent qu'au futur. 
	 */

	public SortedMap<Double, Queue<EvenementHistorique>> calendrier;//TODO
	
	private double dateActuelle;
	
	private Map<EtreVivant, Set<EvenementHistorique>> projetsEtresVivants;

	public FriseChronologique(double dateInitiale){//cree une frise sans aucun evenenement historique.
		calendrier = new TreeMap<Double, Queue<EvenementHistorique>>(); 
		projetsEtresVivants = new HashMap<EtreVivant, Set<EvenementHistorique>>();
	}
	
	public double dateDuProchainEvenement(){
		if(calendrier.isEmpty()) throw new LeProgrammeurEstUnGrosFruitException("pas de prochain evenement programme");
		else return calendrier.firstKey();
	}
	
	
	
	private void auxPrendEnComte(MortEtreVivant mEV){
		//assure que tous les Evenements futurs dependant de la survie de cet EtreVivant soient retires de la frise;
		EtreVivant decede = mEV.getEtreVivant();
		if(projetsEtresVivants.containsKey(decede)){
			Set<EvenementHistorique> projetsFuturs = projetsEtresVivants.get(decede);
			
			for(EvenementHistorique eH : projetsFuturs){
				this.retirerEvenementHistorique(eH);
				Evenement e = eH.getEvenement();
			}
			
			projetsEtresVivants.remove(decede);
		}
		else{
			
		}
	}
	public void arrive(){
		if(!this.calendrier.containsKey(this.dateActuelle)) throw new LeProgrammeurEstUnGrosFruitException("il n'y a pas d'Evenement a faire arriver en la date actuelle.");
		else{
			Queue<EvenementHistorique> auProgrammeEnCetteDate = calendrier.get(this.dateActuelle);
			while(!auProgrammeEnCetteDate.isEmpty()){//on fait arriver les evenements dans l'ordre
				
				EvenementHistorique evH = auProgrammeEnCetteDate.poll();//un evenement est retire du calendrier
				Evenement ev = evH.getEvenement();
				Univers U = ev.getUnivers();
				
				if(ev.dependDunEtreVivant()){//on verifie si cet Evenement ne fait pas partie des projets d'un EtreVivant
					EtreVivant v = ev.dependance();
					if(projetsEtresVivants.containsKey(v)){
						Set<EvenementHistorique> projetsDeV = projetsEtresVivants.get(v);
						projetsDeV.remove(evH);//auquel cas on retire cet Evenement des projets de l'EtreVivant
						
						//si apres cela l'EtreVivant n'a plus de projets, on retire l'EtreVivant de la Map
						if(projetsDeV.isEmpty()) projetsEtresVivants.remove(v);
					}
					
				}
				
				U.gestionnaireGlobal.prendEnCompte(ev);//on fait arriver l'Evenement

			}
			calendrier.remove(this.dateActuelle);
		}
		
	}
	
	private void auxPrendEnCompte(EcoulementDuTemps eDT){
		//TODO a discuter.
		if(eDT.dateInitiale!=this.dateActuelle) throw new LeProgrammeurEstUnGrosFruitException("on ne peut ecouler le temps depuis une autre date que la date actuelle.");
		if(eDT.dateFinale>this.dateDuProchainEvenement()) throw new LeProgrammeurEstUnGrosFruitException("on ne doit pas sauter des evenements.");
		
		this.dateActuelle = eDT.dateFinale;
	}

	public void ajouteEvenementHistorique(EvenementHistorique evH){
		if(calendrier.containsKey(evH.getDate())){
			if(calendrier.get(evH.getDate()).contains(evH)) throw new LeProgrammeurEstUnGrosFruitException("cette evenement historique est deja dans la frise");
			calendrier.get(evH.getDate()).add(evH);
		}
		else{
			LinkedList<EvenementHistorique> singleton = new LinkedList<EvenementHistorique>();
			singleton.add(evH);
			calendrier.put(evH.getDate(), singleton);
		}
		
		this.noteEvenementEtreVivants(evH.getEvenement(), evH);//permet de retenir si un Etrevivant est implique dans la realisation future de cet evenement.
	}
	
	private void retirerEvenementHistorique(EvenementHistorique evH){
		//retire un EvenementHistorique de du calendrier
		if(!calendrier.containsKey(evH.getDate())) {
			throw new LeProgrammeurEstUnGrosFruitException("impossible de retirer un Evenement qui n'est pas dans la frise." + evH.getDate() +";"+ this.dateActuelle);
		}
		
		Queue<EvenementHistorique> conjonction = calendrier.get(evH.getDate());
		
		boolean succes = conjonction.remove(evH);
		if(!succes) throw new LeProgrammeurEstUnGrosFruitException("cet Evenement historique ne figure pas dans la frise a cette date.");
	}
	
	/*
	 *   La methode suivante sert a retenir les evenements historiques futurs associes a un EtreVivant,
	 * et dependant donc de sa survie. Ce sera utile lorsque la mort de cette etre vivant arrivera, pour qu'il
	 * ne reste pas des evenements "fantomes" residuels.
	 */
	private void noteEvenementEtreVivants(Evenement evVivant, EvenementHistorique projet){
		if(evVivant.dependDunEtreVivant()){
			EtreVivant eVEnDanger = evVivant.dependance();
			if(projetsEtresVivants.containsKey(eVEnDanger)){
				projetsEtresVivants.get(eVEnDanger).add(projet);
			}
			else{
				Set<EvenementHistorique> projetsFuturs = new HashSet<EvenementHistorique>();
				projetsFuturs.add(projet);
				projetsEtresVivants.put(eVEnDanger, projetsFuturs);
			}
		}
	}

	public double getDateActuelle() {
		return dateActuelle;
	}

	public void setDateActuelle(double dateActuelle) {
		this.dateActuelle = dateActuelle;
	}

	@Override
	public void prendEnCompte(Evenement e) {
		switch(e.getGenre()){
		case MORT:
			this.auxPrendEnComte((MortEtreVivant)e);
			break;
		case ECOULEMENT_DU_TEMPS:
			this.auxPrendEnCompte((EcoulementDuTemps)e);
			break;
		default :
			break;
		}
		
	}

}
