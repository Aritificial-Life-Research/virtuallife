package evenements;

import phenotypique.EtreVivant;
import physique.Univers;
import toolbox.Recyclable;

public abstract class Evenement implements SurvieDependant{
	/*
	 * Un Evenement est "quelque chose" qui peut se passer dans un Univers.
	 * Par exemple, la naissance d'un etre vivant, sa mort, sa reproduction...
	 * La raison d'etre de tels objets est d'assurer qu'on a une trace de tout ce qui arrive dans un Univers.
	 * 
	 * Il s'agit surtout d'assurer que, quand "quelque chose" arrive, toutes ses consequences soient immediatement prises en compte.
	 * 
	 */


	private Univers U;
	protected GenreEvenement genre;
	
	public Evenement(Univers univers, GenreEvenement type){
		this.setU(univers);
		this.genre = type;
	}
	
	public Univers getUnivers() {
		return getU();
	}
	
	public GenreEvenement getGenre(){
		return genre;
	}
	//public abstract boolean dependDunEtreVivant();
	//public abstract EtreVivant dependance();

	public Univers getU() {
		return U;
	}

	public void setU(Univers u) {
		U = u;
	}
}
