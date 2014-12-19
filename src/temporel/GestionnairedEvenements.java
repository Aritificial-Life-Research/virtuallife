package temporel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import phenotypique.EtreVivant;
import physique.Lettre;
import physique.Mot;
import physique.Point;
import physique.PointVirtuel;
import physique.Univers;
import toolbox.Recupereur;
import toolbox.Recycleur;

import evenements.ChoixMot;
import evenements.CompletionMot;
import evenements.EcoulementDuTemps;
import evenements.Evenement;
import evenements.MortEtreVivant;
import evenements.NaissanceEtreVivant;
import evenements.ProgressionMot;
import evenements.ReproductionEtreVivant;
import evenements.SignalExperimental;
import evenements.VariationEnergieAsymptotique;

public abstract class GestionnairedEvenements implements Attentif{
/*
 * Un gestionnaire d'evenements sert à prendre en compte le fait qu'un Evenement a eu lieu,
 * et de le signaler à un certain ensemble d'Attentifs.
 */
	
	protected HashSet<Attentif> attentifsDependants;
	
	public GestionnairedEvenements(){
		attentifsDependants = new HashSet<Attentif>();
	}
	
	public final void ajouteAttentif(Attentif a){
		attentifsDependants.add(a);
	}
	
	public final void retireAttentif(Attentif b){
		attentifsDependants.remove(b);
	}

	public final void prendEnCompte(Evenement e){
		
		switch (e.getGenre()) {
		case CHOIX_MOTS:
			this.auxPrendEnCompte((ChoixMot) e);
			break;
		case COMPLETION_MOT:
			this.auxPrendEnCompte((CompletionMot)e);
			break;
		case ECOULEMENT_DU_TEMPS:
			this.auxPrendEnCompte((EcoulementDuTemps)e);
			break;
		case MORT:
			this.auxPrendEnCompte((MortEtreVivant)e);
			break;
		case NAISSANCE:
			this.auxPrendEnCompte((NaissanceEtreVivant)e);
			break;
		case PROGRESSION_MOT:
			this.auxPrendEnCompte((ProgressionMot)e);
			break;
		case REPRODUCTION:
			this.auxPrendEnCompte((ReproductionEtreVivant)e);
			break;
		case VARIATION_ENERGIE_ASYMPTOTIQUE:
			this.auxPrendEnCompte((VariationEnergieAsymptotique)e);
			break;
		case SIGNAL_EXPERIMENTAL:
			this.auxPrendEnCompte((SignalExperimental)e);
			break;
		default :
			throw new EvenementNonGereException();
		}
		
		this.avertit(e);
		
	}
	
	protected abstract void auxPrendEnCompte(ChoixMot cM);
	protected abstract void auxPrendEnCompte(CompletionMot cM);
	protected abstract void auxPrendEnCompte(EcoulementDuTemps eT);
	protected abstract void auxPrendEnCompte(MortEtreVivant mEV);
	protected abstract void auxPrendEnCompte(NaissanceEtreVivant mEV);
	protected abstract void auxPrendEnCompte(ProgressionMot pM);
	protected abstract void auxPrendEnCompte(ReproductionEtreVivant rEV);
	protected abstract void auxPrendEnCompte(VariationEnergieAsymptotique vE);
	protected abstract void auxPrendEnCompte(SignalExperimental sE);
	
	private final void avertit(Evenement e){
		for(Attentif a : attentifsDependants) a.prendEnCompte(e);
	}

}
