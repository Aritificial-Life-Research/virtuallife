package experimental;

import physique.Univers;
import evenements.SignalExperimental;

/*
 * Cet evenement sera utile pour mesurer le temps reel (pour la machine) que prennent
 * certains processus.
 * 
 * La methode pour s'en servir : incruster dans le code a l'endroit qui nous interesse
 * 
 * long tInit = System.currentTimeMillis();
 * ... le processus dont on se demande quel temps il prend...
 * long tFinal = System.currentTimeMillis();
 * U.gestionnaireGlobal.prendEnCompte(new MesureurDeDuree(U, tFinal - tInitial));
 */
public class MesureurDeDuree extends SignalExperimental {

	private String etiquette;//optionnel
	private final long duree;//la duree mesuree en nanosecondes

	public MesureurDeDuree(Univers univers, long duree) {
		super(univers);
		this.duree = duree;
		this.setEtiquette("");
		// TODO Auto-generated constructor stub
	}

	public MesureurDeDuree(Univers univers, String etiquette, long duree) {
		super(univers);
		this.setEtiquette(etiquette);
		this.duree = duree;
	}

	public String getEtiquette() {
		return etiquette;
	}

	public void setEtiquette(String etiquette) {
		this.etiquette = etiquette;
	}

	public long getDuree() {
		return duree;
	}
	


}
