package toolbox;

/*
 *   Cet interface est implemente par des objets susceptibles d'etres recycles par
 * un objet Recycleur.
 *   On ne demande a de tels objets que de pouvoir etre "nettoyes", c'est a dire rendus
 * vierges pour leur prochaine utilisation, un peu comme des CD regravables par exemple.
 *   L'enjeu de cet interface est de gagner en performances temporelles et spatiales,
 * en evitant d'instancier et d'initialiser puis de laisser au Garbage-Collector des objets
 * ephemeres.
 */

public interface Recyclable {

	public void nettoie();//nettoie l'objet, le rendant ainsi pret pour reutilisation.
}
