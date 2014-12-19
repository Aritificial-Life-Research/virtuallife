package lois;

import phenotypique.EtreVivant;
import phenotypique.Phenotype;
import physique.Point;
import physique.Univers;
import java.util.*;

/*
 *   Dans ce duplicateur, chaque niche est contigue a un certain ensemble d'autres niches.
 * Pour choisir une niche, un EtreVivant choisit une niche contigue a la sienne de maniere
 * equiprobable.
 *   Attention, rien n'est fait ici pour garantir que la relation de contiguite soit symetrique
 *   
 */
public class DuplicateurANichesIsotrope extends DuplicateurANiches{

	private Map<Niche, Niche[]> nichesContiguesA;
	private Random r;
	
	public DuplicateurANichesIsotrope(Univers U, Collection<Niche> niches) {
		super(U, niches);
		r = new Random();
		this.nichesContiguesA = new HashMap<Niche, Niche[]>();
		
	}

	/*
	 * 
	 */
	public void setContiguites(Niche... niches){
		Niche nEmission = niches[0];
		Niche[] voisines = new Niche[niches.length-1];
		for(int i = 1; i < niches.length; i++){
			voisines[i-1] = niches[i];
		}
		nichesContiguesA.put(nEmission, voisines);
	}
	@Override
	protected Niche choisitNiche(Phenotype ph, EtreVivant original) {
		Niche nicheEmission = super.nicheDeLEtreVivant(original);
		Niche[] voisines = nichesContiguesA.get(nicheEmission);
		return voisines[r.nextInt(voisines.length)];
	}

}
