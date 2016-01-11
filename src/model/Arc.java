package model;

/**
 * Classe permettant de gérer les arcs et arrête entre les sommets
 * @author Groupe 3
 * @version 2016-01-11
 */
public class Arc {

	private Vertex vertex;
	private Integer iValue;

	/**
	 * Constructeur qui crée un arc
	 * @param vertex le sommet que pointe l'arc
	 */
	public Arc(Vertex vertex) {
		this.vertex = vertex;
	}

	/**
	 * Constructeur qui crée un arc valué
	 * @param vertex le sommet que pointe l'arc
	 * @param iValue la valeur de l'arc
	 */
	public Arc(Vertex vertex, int iValue) {
		this.vertex = vertex;
		this.iValue = new Integer(iValue);
	}

	/*----------------------
	 * Getters et Setters
	 *---------------------*/
	public Vertex getVertex() {
		return this.vertex;
	}
	
	public void setVertex(Vertex v){
		this.vertex = v;
	}

	public int getIValue() {
		return this.iValue;
	}
	
	public void setValue(int a){
		this.iValue = a;
	}

	/**
	 *  Méthode équivalente au toString
	 *  @param vertex le nom du sommet pour lequel on veut écrire les arcs
	 */
	public String displayVertex(Vertex vertex) {
		String str = this.vertex.getName();

		if (this.iValue != null) {
			str += "(" + this.iValue.intValue() + ")";
		}

		return str;
	}


}
