package lois;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;

import phenotypique.EtreVivant;

/*
 * Comme A0, sauf que la dependance selon la taille est differente.
 * On utilise cette fois une loi Gamma(taille, tempsCaracteristique).
 * Le temps moyen de reproduction vaut donc toujours taille*tempscaracteristique.
 * Neanmoins, plus un EtreVivant sera gros, plus sa date de reproductin sera "deterministe".
 */
public class LoiDateReproductionB0 implements LoiDateReproduction {

	public LoiDateReproductionB0(double tempsCaracteristique) {
		this.tempsCaracteristique = tempsCaracteristique;
		variablesAleatoires = new HashMap<Integer, GammaDistribution>();
	}

	private double tempsCaracteristique;
	private Map<Integer, GammaDistribution> variablesAleatoires;
	
	@Override
	public double dateProchaineReproduction(EtreVivant etrevivant,
			double dateActuelle) {
		return (dateActuelle + variableGamma(etrevivant.getPh().getAnatomie().getSommets().size()).sample());
	}
	
	private GammaDistribution variableGamma(int nombre){
		if(variablesAleatoires.containsKey(nombre)) return variablesAleatoires.get(nombre);
		else{
			GammaDistribution g = new GammaDistribution(1.0*nombre, tempsCaracteristique);
			variablesAleatoires.put(nombre, g);
			return g;
		}
	}
}
