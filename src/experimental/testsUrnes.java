package experimental;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.util.FastMath;

import aleatoire.Urne;
import aleatoire.UrneSeijin;

import physique.Point;

public class testsUrnes {

	/**
	 * @param args
	 */
	static Set<Point> points;
	
	public static void main(String[] args) {
		points = new HashSet<Point>();
		int n = 100;
		Urne<Point> urne = new UrneSeijin<Point>();
		for(int i = 0; i < n; i++){
			Point p = new Point(i);
			points.add(p);
			urne.ajouteBoule(p,1);
		}
		Map<Point, Double> ponderation = new Map<Point, Double>() {

			@Override
			public void clear() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean containsKey(Object arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean containsValue(Object arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Set<java.util.Map.Entry<Point, Double>> entrySet() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Double get(Object arg0) {
				// TODO Auto-generated method stub
				return (double) ((Point)arg0).hashCode();
			}

			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Set<Point> keySet() {
				// TODO Auto-generated method stub
				return points;
			}

			@Override
			public Double put(Point arg0, Double arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void putAll(Map<? extends Point, ? extends Double> arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Double remove(Object arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int size() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Collection<Double> values() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		urne.modifiePoidsToutesBoules(ponderation);
		
		int[] tab = new int[n];
		for(int i = 0; i < 100000; i++){
			Point tire = urne.tireAvecRemise();
			int num = tire.getIndice();
			tab[num]++;
		}
		for(int i = 0; i <tab.length; i++){
			System.out.print(tab[i] + " ");
		}

		System.out.println();
	}

}
