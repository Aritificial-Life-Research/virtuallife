package experimental;

public class Espece {//une classe servant a des fins de classification.

	private String nom;//optionnel
	
	public Espece(){
		super();
	}
	
	public Espece(String nom){
		this.nom = nom;
	}
	
	public String nom(){
		return this.nom;
	}
}
