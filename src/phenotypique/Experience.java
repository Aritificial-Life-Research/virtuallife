package phenotypique;

import physique.Point;
import physique.PointVirtuel;


public class Experience {

	/*
	 *Cette classe est charg�e d'orchestrer le fonctionnement de tous les repr�sentants de toutes les classes.
	 *On y trouve, � un instant t:
	 *	-un Univers
	 *	-une liste d'�tres vivants
	 *	-les listes des phenotypes et genomes associ�s;
	 *	-�ventuellement d'autres donn�es d'int�r�t exp�rimental ou algorithmique.
	 *
	 *LES REGLES DU JEU:
	 *  L'exp�rience se d�roule en temps discret. A chaque intervalle de temps, chaque etre vivant effectue un mouvement elementaire (i.e "deplacement" d'une lettre le long d'une arete)
	 *  sur chacun de ses sommets d'entr�e. D�s qu'un �tre vivant dispose de suffisamment d'�nergie pour se reproduire, il le fait autant de fois que possible en �mettant une requ�te
	 *  de reproduction. Les requ�tes sont trait�s par l'exp�rience dans un ordre � d�terminer.
	 *  
	 *	Modalit�s d'un mouvement �l�mentaire : un curseur est translat� d'un sommet A vers un sommet B, �tiquet� par la lettre x, ayant pour origine un sommet d'entr�e E. 
	 *A ce moment l�, le sommet E perd 1 unit� de x, et le sommet B gagne une unit� de x.
	 *  Un mot est form� quand un curseur arrive sur le sommet de sortie S. A ce moment l�, l'�tre vivant gagne l'�nergie du mot form�, le curseur est replac� en E, et le pr�mot
	 *  correspondant est r�initialis� au vide.
	 *  
	 *  Modalit�s de choix du mot form� par un �tre vivant : il se fait en deux �tapes.
	 *  	1) On choisit al�atoirement un chemin basique, court, du sommet de d�part vers le sommet d'arriv�e,  r�alisable d'apr�s les ressources de ce sommet d'entr�e.
	 *  	2) On enrichit al�atoirement ce chemin en rajoutant, tant qu'on peut d'un point de vue de co�t en ressources, des circuits �l�mentaires partant des points par lesquels on est d�ja pass�.
	 *  
	 *  Remarquer que les sommets d'entr�e 
	 *  
	 *  
	 *  En principe, les �tre vivants simples se reproduisent plus vite, mais gagne moins d'�nergie que les �tres vivants compliqu�s.
	 *
	 *
	 *
	 *
	 *
	 */
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test();
	}

	static void Test(){
		Point p1 = new Point(0);
		Point p2 = new Point(0);
		System.out.println(p1.equals(p2));
		
		Point p3 = new Point(0);
		PointVirtuel p4 = new PointVirtuel(0);
		System.out.println(p3.equals(p4));
		
	}
	
}
