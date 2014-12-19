package lois;

import java.util.*;

import experimental.*;
import phenotypique.Emplacement;
import phenotypique.EtreVivant;
import phenotypique.Phenotype;
import physique.*;
import chemins.*;

public abstract class Duplicateur {
	/*
	 *   Un objet de type Duplicateur sert a assurer la reproduction d'etres vivants particuliers,
	 * en creant une copie de cette etre vivant qui s'etend sur d'autres points de l'univers.
	 *   Cette reproduction peut se faire au prix de la mort d'autres etres vivants.
	 */
	
	protected Univers U;
	
	public Duplicateur(Univers U) {
		this.U = U;
	}

	public Phenotype phenotypeNouveauNe(EtreVivant original){// TODO a modifier quand les mutations seront incorporees
		return original.getPh();
	}
	
	public abstract Set<Point> lieuxNouveauNe(Phenotype ph, EtreVivant original);
	
	public abstract Emplacement emplacementNouveauNe(Phenotype ph, EtreVivant original);
	
	
}
