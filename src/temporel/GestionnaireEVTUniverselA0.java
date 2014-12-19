package temporel;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.util.FastMath;

import lois.LoiEnergieA0;
import lois.SelecteurChemin;
import phenotypique.Emplacement;
import phenotypique.EtreVivant;
import phenotypique.Phenotype;
import physique.Cout;
import physique.Lettre;
import physique.Mot;
import physique.Point;
import physique.PointVirtuel;
import physique.Univers;
import toolbox.Doublet;
import toolbox.LeProgrammeurEstUnGrosFruitException;
import chemins.Chemin;
import chemins.Chemin.RecycleurQueueChemin;
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
import experimental.MesureurDeDuree;
import genomique.Genome;

public class GestionnaireEVTUniverselA0 extends GestionnairedEvenements {

	private final Univers U;
	private final FriseChronologique friseDuFutur;
	
	public GestionnaireEVTUniverselA0(Univers U) {
		super();
		this.U = U;
		this.friseDuFutur = U.friseDuFutur;
	}
	
	protected void auxPrendEnCompte(MortEtreVivant mEV){
		U.getFaune().remove(mEV.getEtreVivant());//l'etre vivant n'est plus compte parmi les etres vivants de l'Univers
		for(PointVirtuel pv : mEV.getEtreVivant().getEmplacement().keySet()){//les points que l'etre vivant occupait sont liberes.
			U.getOccupation().remove(mEV.getEtreVivant().getEmplacement().get(pv));
		}
		//brassage des ressources prelevees par l'EtreVivant.
		U.rendRessourcesPrelevees(mEV.getEtreVivant());
		
		//recyclage de l'Etre vivant
		mEV.getEtreVivant().getPh().recycle(mEV.getEtreVivant());
	}
	protected void auxPrendEnCompte(NaissanceEtreVivant nEV){
		EtreVivant ev = nEV.getEtreVivant();
		Map<PointVirtuel, Point> emplacement = ev.getEmplacement();
		
		for(PointVirtuel pv : emplacement.keySet()){
			Point p = emplacement.get(pv);
			if(U.getOccupation().containsKey(p)){
				EtreVivant condamne = U.getOccupation().get(p);
				this.prendEnCompte(new MortEtreVivant(U, condamne)); //(new MortEtreVivant(U, condamne));//les etres vivants qui occupent un point de l'emplacement du nouveau-ne sont tues
			}
			U.getOccupation().put(p, ev);//les points de l'emplacement sont declares occuppes par le nouvel EtreVivant
		}
		
		U.getFaune().add(ev);//le nouveau-ne est declare appartenir a la Faune de L'Univers
		ev.setEnergie(nEV.getEnergieDeNaissance());//le nouveau-ne perçoit son energie de naissance.
		
		//la date de reproduction du nouveau-ne est fixee;
		double dateProchaineReproduction = U.getLoiDateReproduction().dateProchaineReproduction(ev, U.friseDuFutur.getDateActuelle());
		EvenementHistorique prochaineReproduction = new EvenementHistorique(dateProchaineReproduction, new ReproductionEtreVivant(U, ev));
		friseDuFutur.ajouteEvenementHistorique(prochaineReproduction);
		
		//le nouveau-ne choisit les mots qu'il doit former, immediatement
		//les mots sont choisis simultanement depuis tous les sommets d'entree
		PointVirtuel[] sommetsEntrees = ev.getPh().getAnatomie().getSommetsEntree().toArray(new PointVirtuel[0]);
		this.prendEnCompte(new ChoixMot(U, ev, sommetsEntrees));
	}
	
	protected void auxPrendEnCompte(CompletionMot CM){
		EtreVivant eV = CM.getEtreVivant();
		
		//l'etre vivant perd l'energie asymptotique associee a ce mot
		this.prendEnCompte(new VariationEnergieAsymptotique(U, eV, eV.getEnergieAsymptotique()-(U.loiEnergie.energieMot(CM.getMot())/eV.getPh().getAnatomie().getSe())));
		
		//l'etre vivant choisit un nouveau mot a former, immediatement
		PointVirtuel sEntree = CM.getSommetEntree();
		ChoixMot chM = new ChoixMot(U, CM.getEtreVivant(), sEntree);
		this.prendEnCompte(chM);
		
	}
	
	protected void auxPrendEnCompte(ChoixMot CM){
		
		EtreVivant etreVivant = CM.getEtreVivant();
		PointVirtuel[] sommetsEntree = CM.getSommetsEntree();
		
		//formation des mots, et prelevement des ressources pour les mots dont la formation a reussi
		Chemin[] cheminsChoisis = etreVivant.getPh().getSelChemin().choisitChemins(etreVivant, sommetsEntree);
		
		for(int i = 0; i < cheminsChoisis.length; i++){
			Chemin c = cheminsChoisis[i];//le chemin qui a ete forme depuis ce point virtuel
			Mot m = c.motInduit();//le mot forme
			
			PointVirtuel sommetEntree = sommetsEntree[i];
			
			etreVivant.getCheminsRestants().put(sommetEntree, c);
			etreVivant.getMotsAFormer().put(sommetEntree, m);
			
			//recompense energetique pour la formation du mot
			this.prendEnCompte(new VariationEnergieAsymptotique(U, etreVivant, etreVivant.getEnergieAsymptotique()+(U.loiEnergie.energieMot(m)/etreVivant.getPh().getAnatomie().getSe())));
			
			if(m.isEmpty()){
				//System.out.println("echec mot");
				//on a echoue a former le mot : la date du prochain choix est fixe selon la meme loi que pour progresser dans la formation d'un mot (TODO : autre loi?)
				double dateProchaineTentative = U.loiDateProgressionMot.dateProchaineProgression(etreVivant, U.friseDuFutur.getDateActuelle());
				EvenementHistorique prochaineTentative = new EvenementHistorique(dateProchaineTentative, new ChoixMot(U, etreVivant, sommetEntree));
				friseDuFutur.ajouteEvenementHistorique(prochaineTentative);
			}
			else{
				//System.out.println(m);
				//on a reussi a choisir le mot : on donne la date de la prochaine progression sur ce mot
				double dateProchaineProgression = U.loiDateProgressionMot.dateProchaineProgression(etreVivant, U.friseDuFutur.getDateActuelle());
				EvenementHistorique prochaineProgression = new EvenementHistorique(dateProchaineProgression, new ProgressionMot(U, etreVivant, sommetEntree));
				friseDuFutur.ajouteEvenementHistorique(prochaineProgression);
			}
		}
	}
	
	//il ne se passe essentiellement rien lors d'une progression, on ne fait qu'incrementer et verifier si le mot a est complete
	protected void auxPrendEnCompte(ProgressionMot CP){
		PointVirtuel sommetEntree = CP.getSommetEntree();
		EtreVivant etreVivant = CP.getEtreVivant();
		
		Chemin aSuivre = etreVivant.getCheminsRestants().get(sommetEntree);
		if(aSuivre.isEmpty()) {
			throw new LeProgrammeurEstUnGrosFruitException("on ne doit pas progresser sur un Chemin vide");
		}
		else{
			Point destination = etreVivant.getEmplacement().get(aSuivre.getFirst().getSommet());
			Lettre l = aSuivre.enleveLaTete();
		
			if(aSuivre.isEmpty()){//on a fini de former le mot
				this.prendEnCompte(new CompletionMot(U, etreVivant, sommetEntree, etreVivant.getMotsAFormer().get(sommetEntree)));
			}
			else{
				//on continue a former le mot en fixant la date de la prochaine progression sur ce mot
				double dateProchaineProgression = U.loiDateProgressionMot.dateProchaineProgression(etreVivant, U.friseDuFutur.getDateActuelle());
				EvenementHistorique prochaineProgression = new EvenementHistorique(dateProchaineProgression, new ProgressionMot(U, etreVivant, sommetEntree));
				friseDuFutur.ajouteEvenementHistorique(prochaineProgression);
			}
		}

	}
	
	protected void auxPrendEnCompte(ReproductionEtreVivant R){
		EtreVivant original = R.getEtreVivant();
		
		//c'est ici qu'ont lieu des mutations eventuelles
		Doublet<Phenotype, Genome> mutes = original.getGen().mute(original.getPh());

		Phenotype phenotypeNouveauNe = mutes.getFst();
		Genome genomeNouveauNe = mutes.getSnd();
		
		if(!(phenotypeNouveauNe==null)){//ce serait le cas d'un avortement : le genome mute n'est pas viable
		
			Emplacement emplacementNouveauNe = U.getDuplicateur().emplacementNouveauNe(phenotypeNouveauNe, original);
		
			if(!emplacementNouveauNe.isEmpty()){
			
				EtreVivant nouveauNe = phenotypeNouveauNe.fournitEtreVivant(U, emplacementNouveauNe, genomeNouveauNe);//new EtreVivant(U, phenotypeNouveauNe, emplacementNouveauNe);
				//le nouveu-ne nait avec la meme energie que l'original. On evite ainsi de rendre les nuveaux-ne vulnerables
				this.prendEnCompte(new NaissanceEtreVivant(U, nouveauNe, original.getEnergie()));
			}
		
		}
		double dateProchaineReproduction = U.loiDateReproduction.dateProchaineReproduction(original, U.friseDuFutur.getDateActuelle());
		
		friseDuFutur.ajouteEvenementHistorique(new EvenementHistorique(dateProchaineReproduction, new ReproductionEtreVivant(U, original)));
		
	}
	
	//fait simplement varier l'energie asymptotique d'un etre vivant
	protected void auxPrendEnCompte(VariationEnergieAsymptotique vE){
		vE.getEtreVivant().getEnergie();//assure que la valeur de l'energie de l'etre vivant evolue correctement
		vE.getEtreVivant().setEnergieAsymptotique(FastMath.max(vE.getNouvelleEnergie(),0.0));//pour eviter des energies negatives dues a des erreurs arithmetiques
	}
	
	protected void auxPrendEnCompte(EcoulementDuTemps eT){
		if(friseDuFutur.getDateActuelle()!=eT.dateInitiale) throw new LeProgrammeurEstUnGrosFruitException("cet ecoulement du temps n'est pas a l'heure.");
		
		if(friseDuFutur.dateDuProchainEvenement()<eT.dateFinale) throw new LeProgrammeurEstUnGrosFruitException("il est interdit de sauter des evenements programmes en ecoulant le temps");
		//la date actuelle de l'univers est modifiee
		//U.friseDuFutur.setDateActuelle(eT.dateFinale);
		
		//l'energie des etres vivants est modifiee selon la loi decrite par loiEnergie
		/*
		for(EtreVivant eV : U.getFaune()){
			this.prendEnCompte(new VariationDEnergie(U, eV, U.loiEnergie.decroissanceEnergetique(eV, eT.dateInitiale, eT.dateFinale)));
		}
		*/
	}

	@Override
	protected void auxPrendEnCompte(SignalExperimental sE) {
	}

	
}
