package chemins;

import java.util.*;


import physique.*;
import toolbox.Fournisseur;
import toolbox.LeProgrammeurEstUnGrosFruitException;
import toolbox.Recyclable;
import toolbox.Recycleur;


public class Chemin implements Recyclable{
	/*
	 * Une liste de doublets (Lettre, PointVirtuel) representant un chemin,
	 * elaboree : on se donne un pointeur sur le debut et la fin de la liste,
	 * de maniere a faciliter les operations de concatenation.
	 * 
	 * En pratique, on enveloppe un objet QueueChemin.
	 */
	


	//un pointeur sur le premier element du chemin
	protected QueueChemin first;
	//un pointeur sur le dernier element du chemin : normalement, last==null OU last.tail==null
	protected QueueChemin last;
	
	
	public Chemin(){
		first = null;
		last = null;
	}
	
	public Chemin(QueueChemin qc){
		if(qc==null){
			first = null;
			last = null;
		}
		else{
			first = qc;
			last = qc.trouveDernier();
		}
	}
	
	public Chemin(QueueChemin first, QueueChemin last) {
		this.first = first;
		this.last = last;
	}
	
/*
	@Override
	public boolean addAll(Collection<? extends FlechePointVirtuel> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		first = null;
		last = null;
	}

	@Override
	public boolean contains(Object arg0) {
		//parcours lineaire
		if(first==null) return false;
		else return first.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		if(first==null) return false;
		else{
			Set<FlechePointVirtuel> flechesPoints = first.flechePointsDuChemin();
			return flechesPoints.containsAll(arg0);
		}
	}

	@Override
	public boolean isEmpty() {
		return (first==null);
	}

	@Override
	public Iterator<FlechePointVirtuel> iterator() {
		return new Iterator<FlechePointVirtuel>(){
			private QueueChemin pointeur = first;
			
			@Override
			public boolean hasNext() {
				return !(pointeur==null);
			}

			@Override
			public FlechePointVirtuel next() {
				FlechePointVirtuel tmp = pointeur.getHead();
				pointeur = pointeur.getTail();
				return tmp;
			}

			@Override
			public void remove() {
				//pas implemente, NE PAS UTILISER
				
			}
			
		};
	}

	@Override
	public boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		Iterator<FlechePointVirtuel> i = this.iterator();
		int cpt = 0;
		while(i.hasNext()) cpt++;
		return cpt;
	}

	@Override
	public Object[] toArray() {
		int l = this.size();
		FlechePointVirtuel[] array = new FlechePointVirtuel[l];
		Iterator<FlechePointVirtuel> i = this.iterator();
		int indice = 0;
		while(i.hasNext()){
			array[indice] = i.next();
			indice++;
		}
		return array;
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(FlechePointVirtuel arg0) {
		if(this.isEmpty()){
			last = new QueueChemin(arg0, null);
			first = last;
		}
		else{
			first = new QueueChemin(arg0, first);
		}
		return true;
	}

	@Override
	public FlechePointVirtuel element() {
		if(first==null) throw new NoSuchElementException("chemin vide");
		else{
			FlechePointVirtuel fpv = first.getHead();
			first = first.getTail();
			if(first==null) last = null;
			return fpv;
		}
	}

	@Override
	public boolean offer(FlechePointVirtuel arg0) {
		if(this.isEmpty()){
			last = new QueueChemin(arg0, null);
			first = last;
		}
		else{
			first = new QueueChemin(arg0, first);
		}
		return true;
	}

	@Override
	public FlechePointVirtuel peek() {
		if(first==null) return null;
		else{
			FlechePointVirtuel fpv = first.getHead();
			return fpv;
		}
	}

	@Override
	public FlechePointVirtuel poll() {
		if(first==null) return null;
		else{
			FlechePointVirtuel fpv = first.getHead();
			first = first.getTail();
			if(first==null) last = null;
			return fpv;
		}
	}

	@Override
	public FlechePointVirtuel remove() {
		if(first==null) throw new NoSuchElementException("chemin vide");
		else{
			FlechePointVirtuel fpv = first.getHead();
			first = first.getTail();
			if(first==null) last = null;
			return fpv;
		}
	}
*/
	public boolean isEmpty() {
		return (first==null);
	}
	
	public String toString(){
		if(first==null) return "";
		else return first.toString();
	}
	//transforme le Chemin -L->P-ch' en ch', et renvoie la premiere lettre incidente L.
	public Lettre enleveLaTete(){
		if(isEmpty()) throw new LeProgrammeurEstUnGrosFruitException("on ne peut enlever la tete d'un Chemin vide");
		Lettre res = first.lettreIncidente;
		first = first.getTail();
		if(first==null){//cas ou le Chemin ne comportait qu'un point
			last=null;
		}
		return res;
	}
	
	public Collection<Chemin> suffixes(Fournisseur<Chemin> fournisseurCSC){
		/*
		 * renvoie la liste des Chemins suffixes de ce chemin
		 * puisque c'est destine a donner des emplacements d'insertion, affecte une valeur arbitraire 
		 * aux couts de ces nouveaux chemins.
		 */
		if(this.isEmpty()) return new LinkedList<Chemin>();
		else{
			if(this.first.equals(this.last)){
				Collection<Chemin> cl = new LinkedList<Chemin>();
				cl.add(this);
				return cl;
			}
			else{
				//CheminSansCout c = new CheminSansCout(first.getTail(), last);
				Chemin c =fournisseurCSC.fournit();
				c.setFirst(first.getTail()); c.setLast(last);
				
				Collection<Chemin> cl = c.suffixes(fournisseurCSC);
				cl.add(this);
				return cl;
			}
		}
	}	
	public Collection<Chemin> suffixesStricts(Fournisseur<Chemin> fournisseurCSC){
		/*
		 * renvoie la liste des Chemins suffixes stricts de ce chemin
		 * 
		 */
		if(this.isEmpty()) return new LinkedList<Chemin>();
		else{
			if(this.first.equals(this.last)){
				return new LinkedList<Chemin>();
			}
			else{
				//CheminSansCout c = new CheminSansCout(first.getTail(), last);
				Chemin c =fournisseurCSC.fournit();
				c.setFirst(first.getTail()); c.setLast(last);
				
				Collection<Chemin> cl = c.suffixes(fournisseurCSC);
				return cl;
			}
		}
	}	
	public Chemin suffixeAt(int position){
		//renvoie le suffixe de this pris a la position en parametre (si position == 0, renvoie this)
		if(isEmpty()) throw new LeProgrammeurEstUnGrosFruitException("un chemin vide n'a pas de suffixe");
		else{
			QueueChemin qc = this.first;
			for(int i = 0; i < position; i++){
				if(qc==null) throw new LeProgrammeurEstUnGrosFruitException("indice trop eleve pour extraire un suffixe.");
				qc = qc.getTail();
			}
			return new Chemin(qc, last);
		}
	}
	
	public void insere(Chemin aInserer, QueueChemin ouInserer){//TODO
		/*
		 * modifie le chemin en y inserant le Chemin ouInserer a la suite du premier sommet de ouInserer.
		 * 
		 * typiquement, ouInserer est un "suffixe" de notre Chemin (this).
		 * 
		 * modifie ouInserer et aInserer.
		 * 
		 * le lieu d'insertion ouInserer est ensuite place a la suite de l'insert.
		 * 
		 * REMARQUE : en fait, le Chemin modifie n'est que celui qui contient ouInserer
		 */
		if(ouInserer.tail==null) throw new LeProgrammeurEstUnGrosFruitException("cette methode ne sait pas inserer derriere la fin du chemin");
		if(aInserer.isEmpty());// pas de modification
		else{
			aInserer.getLast().setTail(ouInserer.getTail());
			ouInserer.setTail(aInserer.getFirst());
		}
	}

	public Mot motInduit(){//renvoie le mot obtenu en lisant les lettres sur les aretes du chemin dans l'ordre. Parcours lineaire pour contruire le mot.
		Mot m = new Mot();
		if(!this.isEmpty()){
			QueueChemin pointeur = this.first;
			while(pointeur!=null){
				m.add(pointeur.getLettre());
				pointeur = pointeur.getTail();
			}
		}
		return m;
	}
	
	public QueueChemin getFirst() {
		return first;
	}

	public void setFirst(QueueChemin first) {
		this.first = first;
	}

	public QueueChemin getLast() {
		return last;
	}

	public void setLast(QueueChemin last) {
		this.last = last;
	}

	//ajoute l'element -l->pv a la fin de ce chemin
	public void rallongeALaFin(Lettre l, PointVirtuel pv){
		if(this.isEmpty()){
			this.first = new QueueChemin(l, pv, null);
			this.last = this.first;
		}
		else{
			this.last.setTail(new QueueChemin(l, pv, null));
			this.last = this.last.getTail();
		}
	}
	
	public static class RecycleurQueueChemin extends Recycleur<QueueChemin>{

		@Override
		protected QueueChemin construitNeuf() {
			return new QueueChemin();
		}
		
	}
	
	public static class QueueChemin implements Recyclable{
		/*
		 * Grosso merdo une liste de points avec des fleches etiquetees qui leur arrivent dessus..
		 * fuuuuuuuuuuuuuuuck
		 * 
		 * Moralement, le point de depart n'apparait pas dans un chemin
		 * 
		 * la liste vide est representee par null
		 */
			


			private Lettre lettreIncidente;// i.e. la lettre necessaire pour ARRIVER au sommet correspondant, dans [|0;P-1|]
			private PointVirtuel sommet;
			
			private QueueChemin tail;
			
			public QueueChemin(Lettre lIncidente, PointVirtuel s, QueueChemin tl){
				this.lettreIncidente = lIncidente;
				this.sommet = s;
				
				this.tail = tl;
			}
			
			public QueueChemin() {
			}
			
	

			public PointVirtuel getSommet() {
				return this.sommet;
			}

			public Lettre getLettre() {
				return lettreIncidente;
			}

			public void setLettre(Lettre lettre) {
				this.lettreIncidente = lettre;;
			}

			public QueueChemin getTail() {
				return tail;
			}

			public void setTail(QueueChemin tail) {
				this.tail = tail;
			}

			
			public String toString(){
				String s = "-"+lettreIncidente.toString()+"->"+sommet.toString();
				if(tail==null) return s;
				else return (s + tail.toString());
			}
			
			public QueueChemin trouveDernier(){
				if(tail==null) return this;
				else return tail.trouveDernier();
			}

			@Override
			public void nettoie() {
				this.tail = null;
				
				this.lettreIncidente = null;
				this.sommet = null;
			}
		}

	@Override
	public void nettoie() {
		this.first = null;
		this.last = null;
	}
	
	public static class RecycleurChemin extends Recycleur<Chemin>{

		@Override
		protected Chemin construitNeuf() {
			return new Chemin();
		}
		
	}
}
