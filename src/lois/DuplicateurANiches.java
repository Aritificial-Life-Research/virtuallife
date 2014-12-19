package lois;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import phenotypique.Emplacement;
import phenotypique.EtreVivant;
import phenotypique.Phenotype;
import physique.Point;
import physique.PointVirtuel;
import physique.Univers;

public abstract class DuplicateurANiches extends Duplicateur{

	public DuplicateurANiches(Univers U, Collection<Niche> niches) {
		super(U);
		this.nicheDuPoint = new HashMap<Point, Niche>();
		
		for(Niche niche : niches){
			for(Point p : niche.points){
				this.nicheDuPoint.put(p, niche);
			}
		}
		// TODO Auto-generated constructor stub
	}

	@Override
	public Set<Point> lieuxNouveauNe(Phenotype ph, EtreVivant original) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Emplacement emplacementNouveauNe(Phenotype ph,
			EtreVivant original) {
		return this.choisitNiche(ph, original).emplacementNouveauNe(ph, original);
	}

	protected abstract Niche choisitNiche(Phenotype ph, EtreVivant original);

	protected Map<Point, Niche> nicheDuPoint;
	
	public Niche nicheDeLEtreVivant(EtreVivant eV){
		Point p = null;
		for(Point p1 : eV.etendue){//choisit un point quelconque dans l'etendue de eV
			p = p1;
			break;
		}
		return nicheDuPoint.get(p);
	}
	
	
}
