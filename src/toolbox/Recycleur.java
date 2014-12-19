package toolbox;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/*
 *  Un Recycleur contient un ensemble d'objets Recyclables prets a etre reutilises.
 *  Un Recycleur sert a fournir des objets vierges, prets-a-l'emploi sans avoir eu si possible
 * a les instancier et a les initialiser.
 */
public abstract class Recycleur<R extends Recyclable> implements Fournisseur<R>, Recupereur<R>{
	
	//la liste des objets prets a etre reutilises.
	private Queue<R> reserve;

	public Recycleur() {
		//reserve = new QueueReserve<R>();
		reserve = new ArrayDeque<R>();
	}
	
	public void recupere(R aRecycler){//nettoie puis ajoute a la reserve l'objet a recycler en parametres
		aRecycler.nettoie();
		reserve.add(aRecycler);
	}
	
	public R fournit(){//fournit un Recyclable pret-a-l'emploi
		if(reserve.isEmpty()) return this.construitNeuf();
		else return reserve.poll();
	}
	
	public void clear(){
		reserve.clear();
	}
	
	//fabrique un objet pret-a-l'emploi.
	protected abstract R construitNeuf();

	private class QueueReserve<R> implements Queue<R>{
		
		private Object[] tab;//tableau ou sont stockees les valeurs
		private int index;//plus haut indice contenant une valeur
		
		public QueueReserve(){
			tab = new Object[1];
			index = -1;
		}
		
		private void agranditTab(){
				Object[] nTab = new Object[2*tab.length];
				
				for(int i = 0; i <= index; i++) nTab[i] = tab[i];
				
				tab = nTab;
				
				//System.out.println("agrandissement " + tab.length);
		}
		
		@Override
		public boolean addAll(Collection<? extends R> arg0) {
			for(R e : arg0) this.add(e);
			return true;
		}
		@Override
		public void clear() {
			tab = new Object[1];
			index = -1;
		}
		@Override
		public boolean contains(Object arg0) {
			for(int i = 0; i <= index; i++){
				if(tab[i].equals(arg0)) return true;
			}
			return false;
		}
		@Override
		public boolean containsAll(Collection<?> arg0) {
			for(Object e : arg0){
				if(!contains(e)) return false;
			}
			return true;
		}
		@Override
		public boolean isEmpty() {
			return index == -1;
		}
		@Override
		public Iterator<R> iterator() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public boolean remove(Object arg0) {
			if(index==-1) return false;
			for(int i = index; i >=0; i--){
				if(tab[i].equals(arg0)){
					tab[i] = tab[index];
					tab[index] = null;
					index--;
					return true;
				}
			}
			return false;
		}
		@Override
		public boolean removeAll(Collection<?> arg0) {
			boolean res = true;
			for(Object e : arg0){
				res = res&&remove(e);
			}
			return res;
		}
		@Override
		public boolean retainAll(Collection<?> arg0) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public int size() {
			return index+1;
		}
		@Override
		public Object[] toArray() {
			Object[] res = new Object[index+1];
			for(int i = 0; i <= index; i++) res[i] = tab[i];
			return res;
		}
		@Override
		public <T> T[] toArray(T[] arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public boolean add(R arg0) {
			if(index+1==tab.length) agranditTab();
			index++;
			tab[index] = arg0;
			return true;
		}
		@Override
		public R element() {
			if(index==-1) throw new NoSuchElementException();
			return (R) tab[index];
		}
		@Override
		public boolean offer(R arg0) {
			index++;
			if(index==tab.length) agranditTab();
			tab[index] = arg0;
			return true;
		}
		@Override
		public R peek() {
			if(index==-1) return null;
			return (R) tab[index];
		}
		@Override
		public R poll() {
			if(index==-1) return null;
			index--;
			return (R) tab[index +1];
		}
		@Override
		public R remove() {
			if(index==-1) throw new NoSuchElementException();
			index--;
			return (R) tab[index +1];
		}
	}

		public boolean isEmpty(){
			return reserve.isEmpty();
		}

}
