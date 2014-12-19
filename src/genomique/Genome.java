package genomique;

import phenotypique.Phenotype;
import toolbox.Doublet;

/*
 * Un objet de type genome sert a contenir l'information genetique d'un etre vivant
 */
public abstract class Genome {

	/*
	 *   Fournit un nouveau genome et un nouveau phenotype avec eventuellement des
	 * mutations par rapport au genome original.
	 *   Si ces mutations sont silencieuses, pour des raisons d'economie,
	 * la methode renvoie le Phenotype original.
	 *   Si il n'y a pas de mutations, renvoie le Genome original
	 */
	public abstract Doublet<Phenotype, Genome> mute(Phenotype original);
}
