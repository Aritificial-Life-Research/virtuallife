package temporel;

import evenements.*;

public class EvenementHistorique {

/*
 * Un Evenement marque par une date.
 */
	private double date;
	private Evenement evenement;
	
	public EvenementHistorique(double date, Evenement evenement) {
		super();
		this.date = date;
		this.evenement = evenement;
	}

	public double getDate() {
		return date;
	}

	public void setDate(double date) {
		this.date = date;
	}

	public Evenement getEvenement() {
		return evenement;
	}

	public void setEvenement(Evenement evenement) {
		this.evenement = evenement;
	}
}
