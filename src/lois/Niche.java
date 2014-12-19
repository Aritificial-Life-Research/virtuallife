package lois;

import physique.Point;
import physique.Univers;

import java.util.Map;
import java.util.Set;


import phenotypique.Emplacement;
import phenotypique.EtreVivant;
import phenotypique.Phenotype;
import physique.PointVirtuel;
import toolbox.Numerotable;

public abstract class Niche implements Numerotable, Comparable{
	
	protected Univers U;

	private int indice;
	
	protected Set<Point> points;
	
	public Niche(Univers univers, Set<Point> points, int indice){
		this.U = univers;
		this.indice = indice;
		this.points = points;
	}
	
	public abstract Emplacement emplacementNouveauNe(Phenotype ph, EtreVivant original);
	
	public int indice(){
		return indice;
	}
	
	public int hashCode(){
		return indice;
	}
	
	public int compareTo(Object arg0){
		return (this.indice-((Niche)arg0).indice);
	}
}
