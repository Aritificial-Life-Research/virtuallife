package physique;

import java.util.*;
import java.util.*;

import toolbox.Fournisseur;
import toolbox.LeProgrammeurEstUnGrosFruitException;
import toolbox.Recyclable;
import toolbox.Recycleur;



public class Cout extends HashMap<Lettre, Integer> implements Recyclable{//Tout teste!

	/*
	 * Un Cout est une fonction qui a une lettre associe associe une quantite.
	 * Si cette quantite est 0,  on ne fait pas figurer la lettre dans le Map, pour des raisons d'economie.
	 */
	
	
	public Cout(){
		super(8);
	}
	
	public static Cout ajouter(Cout c1, Cout c2, Fournisseur<Cout> fournisseurC){//ajoute deux Couts, typiquement de 2 chemins concatenes
		Cout somme = fournisseurC.fournit();
		somme.putAll(c1);
		for(Lettre p : c2.keySet()){
			if(somme.containsKey(p)) somme.put(p, new Integer(c1.get(p).intValue()+c2.get(p).intValue()));
			else somme.put(p, c2.get(p));
		}
		
		return somme;
	}
	
	public static Cout retranche(Cout c1, Cout c2, Fournisseur<Cout> fournisseurC){//on doit avoir c2 <= c1 !
		// une erreur de type NullPointerException indique que l'on n'a pas c2 <= c1. 
		Cout diff = fournisseurC.fournit();
		
		diff.putAll(c1);//pour les lettres que c1 contient et pas c2.
		
		for(Lettre l : c2.keySet()){
			if(c1.containsKey(l)){
				if(c2.get(l)>c1.get(l)) throw new LeProgrammeurEstUnGrosFruitException("retranchement illicite");
				else diff.put(l, c1.get(l)-c2.get(l));
			}
			else{
				if(c2.get(l)>0) throw new LeProgrammeurEstUnGrosFruitException("retranchement illicite");
			}
		}
	
		return diff;
	}
	
	public void ajoute(Cout c1){
		/*
		 * modifie le Cout courant en y ajoutant le Cout c1.
		 * ATTENTION! modification du Cout courant.
		 */
		for(Lettre l : c1.keySet()){
			if(this.containsKey(l)){
				this.put(l, this.get(l)+ c1.get(l));
			}
			else{
				this.put(l, c1.get(l));
			}
		}
	}
	
	public static boolean pasMoinsCher(Cout ct1, Cout ct2){
		/*
		 * renvoie true ssi une lettre est STRICTEMENT plus abondantes dans c1 que dans c2
		 * 
		 * ATTENTION : ne permet pas de savoir si un Cout est plus cher qu'un autre.
		 */
		for(Lettre p : ct1.keySet()){
			if(ct2.containsKey(p)){
				if(ct2.get(p).intValue()<ct1.get(p).intValue()) return true;
			}
			else if(ct1.get(p)>0) return true;
		}
		
		return false;
	}
	
	public boolean moinsCher(Cout ct2){
		//renvoie true ssi toutes les lettres de this sont moins abondantes dans this que dans ct2.
		//ATTENTION : ct1.moinsCher(ct2) n'est PAS  la negation de ct2.moinsCher(ct1)
		return !pasMoinsCher(this, ct2);
	}

	public boolean plusCher(Cout ct2){
		//renvoie true ssi toutes les lettres de this sont plus abondantes dans this que dans ct2.
		//ATTENTION : cf moinsCher.
		return !pasMoinsCher(ct2, this);
	}
	
	public static Cout borneInf(Cout ct1, Cout ct2, Fournisseur<Cout> fournisseurC){
		//renvoie la borne inferieure de 2 Couts, ie le cout dans lequel chaque quantite correspond au minimum des quantites dans les deux couts ct1 et ct2
		Cout b = fournisseurC.fournit();
		for(Lettre p : ct1.keySet()){
			if(ct2.containsKey(p)){
				b.put(p, new Integer(java.lang.Math.min(ct1.get(p).intValue(), ct2.get(p).intValue())));
			}
		}
		return b;
	}
	
	public static Cout borneInf(Set<Cout> couts, Fournisseur<Cout> fournisseurC){
		Cout c0 = choisitUnElement(couts);
		
		Cout c = fournisseurC.fournit();
		c.putAll(c0);
		
		Set<Lettre> tmp = new HashSet<Lettre>();
		for(Cout ct : couts){
			for(Lettre l : c.keySet()){
				if(ct.containsKey(l)) c.put(l, Math.min(ct.get(l), c.get(l)));
				else tmp.add(l);
			}
			for(Lettre l : tmp){
				c.remove(l);
			}
			tmp.clear();
		}
		return c;
		}
	
	public static Cout choisitUnElement(Set<Cout> couts){
		Iterator<Cout> i = couts.iterator();
		if(i.hasNext()) return i.next();
		else throw new LeProgrammeurEstUnGrosFruitException("ensemble vide, impossible de choisir.");
	}

	public Cout descendLettre(Lettre l, Fournisseur<Cout> fournisseurC){
		//renvoie le Cout dans lequel la quantite de l est diminuee de 1. Renvoie un AUTRE Cout.
		Cout c = fournisseurC.fournit();
		c.putAll(this);
		if(this.containsKey(l)){
			if(this.get(l)==0) throw new LeProgrammeurEstUnGrosFruitException("essaie d'enlever une lettre qui n'est pas la.");
			c.put(l, this.get(l)-1);
		}
		else{
			throw new LeProgrammeurEstUnGrosFruitException("essaie d'enlever une lettre qui n'est pas la.");
		}
		return c;
	}
	
	public Cout monteLettre(Lettre p, Fournisseur<Cout> fournisseurC){
		//renvoie le Cout dans lequel la quantite de p est agmentee de 1. Renvoie un AUTRE Cout.
		Cout c = fournisseurC.fournit();
		c.putAll(this);
		
		if(c.containsKey(p)) c.put(p, c.get(p)+1);
		else c.put(p, 1);
		
		return c;
		
	}

	public void CoutPlusPlus(Lettre l){//analogue de monteLettre(), mais en modifiant le Cout.
		if(this.containsKey(l)) this.put(l, this.get(l)+1);
		else this.put(l, 1);
	}
	
	public void CoutMoinsMoins(Lettre l){
		if(this.containsKey(l)){
			
			if(this.get(l).intValue()==0) throw new LeProgrammeurEstUnGrosFruitException("essaie d'enlever une lettre qui n'est pas la.");
			
			this.put(l, this.get(l)-1);
		}
		else{
			throw new LeProgrammeurEstUnGrosFruitException("essaie d'enlever une lettre qui n'est pas la.");		
		}
	}
	
	public String toString(){
		String s = "[-";
		for(Lettre l : this.keySet()){
			s = s.concat(l.toString()).concat("x").concat(this.get(l).toString()).concat("-");
		}
		s = s.concat("]");
		return s;
	}

	@Override
	public void nettoie() {// le nettoyage est fait en assignant a 0 
		for(Map.Entry<Lettre, Integer> kv : this.entrySet()) kv.setValue(0);
	}
	
	public boolean contientLettre(Lettre l){
		if(this.containsKey(l)) return this.get(l)>0;
		return false;
	}
}
