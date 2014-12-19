package aleatoire;
import org.apache.commons.math3.distribution.*;

import java.util.*;

import physique.Lettre;


public class Aleatoire extends Random{
	
	private static final Random r = new Random();
	
	public static int entierAleatoire(int N){
		return r.nextInt(N);
	}
	
	public static Lettre lettreAleatoire(Set<Lettre> ens, int taille){
		int i = r.nextInt(taille);
		Iterator<Lettre> t = ens.iterator();
		Lettre elu = null;
		for(int j = 0; j <= i; j++){
			elu = t.next();
		}
		return elu;
	}
	
	public static Lettre lettreAleatoire(Set<Lettre> ens){
		return lettreAleatoire(ens, ens.size());
	}
	
	public static boolean Bernouilli(double p){
		/*
		 * tire un double d au hasard dans [0;1[ et renvoie true ssi d<p
		 */
		double d = r.nextDouble();
		return (d<p);
	}
	
	public static Object choixBarycentrique(SortedMap<Object, Double> ponderation){
		//effectue un tirage barycentrique avec remise des objets ponderes par ponderation
		
		TreeMap<Double, Object> cumulative = new TreeMap<Double, Object>();
		TreeSet<Double> intervalles = new TreeSet<Double>();
		
		Double somme = 0.0;
		
		for(Object o : ponderation.keySet()){
			cumulative.put(somme, o);
			intervalles.add(somme);
			somme = somme + ponderation.get(o);
		}
		
		Double d = r.nextDouble()*somme;
		Double d1 = intervalles.lower(d);
		Object o = cumulative.get(d1);
		
		return o;
	}
	
	public static Map<Object,Object> tiragesSuccessifsBarycentriques(Set<Object> indices, Map<Object, Double> ponderation){
		//effectue un tirage sans remise selon la meme loi que choixBarycentrique()
		
		TreeMap<Double, Object> cumulative = new TreeMap<Double, Object>();
		TreeSet<Double> intervalles = new TreeSet<Double>();
	
		Map<Object, Object> tirages = new HashMap<Object, Object>();
		
		for(Object i : indices){
			
			Double somme = 0.0;
			
			cumulative.clear();
			intervalles.clear();
			
			for(Object o : ponderation.keySet()){
				cumulative.put(somme, o);
				intervalles.add(somme);
				somme = somme + ponderation.get(o);
			}
			
			Double d = r.nextDouble()*somme;
			Double d1 = intervalles.lower(d);
			Object o = cumulative.get(d1);
		
			tirages.put(i, o);
			
			ponderation.remove(o);
		}
		return tirages;
	}
	
	
}
