package lois;

import phenotypique.EtreVivant;
import toolbox.LeProgrammeurEstUnGrosFruitException;

public class LoiDateProgressionMotA0 implements LoiDateProgressionMot {
/*
 * La duree jusqu'a la prochaine progression dans la formation d'un mot est voisine de 1.
 * C'est d'ailleurs ce qui donne l'echelle de temps de l'Univers.
 * 
 * On choisit ici que cette duree suit une loi uniforme sur [1-epsilon ; 1 + epsilon] ou epsilon est un
 * petit parametre.
 */
	
	private double epsilon;
	
	public LoiDateProgressionMotA0(double epsilon) {
		super();
		if(epsilon >=1) throw new LeProgrammeurEstUnGrosFruitException("le parametre epsilon se doit d'etre positif et petit devant 1.");
		
		this.epsilon = epsilon;
	}

	@Override
	public double dateProchaineProgression(EtreVivant etreVivant, double dateActuelle) {
		return (dateActuelle + 1.0 + this.epsilon*Math.random());
	}

}
