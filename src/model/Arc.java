package model;

/**
 * Classe permettant de gérer les arcs entre les sommets
 * 
 * @author Les grosses bites
 * 
 */

public class Arc {
/*test*/
	protected Vertex vertex;
	protected Integer iValue;

	public Arc(Vertex vertex) {
		this.vertex = vertex;
	}

	public Arc(Vertex vertex, int iValue) {
		this.vertex = vertex;
		this.iValue = new Integer(iValue);
	}

	// Getters and Setters
	public Vertex getVertex() {
		return this.vertex;
	}
	
	public void setVertex(Vertex v){
		this.vertex = v;
	}

	public int getIValue() {
		return this.iValue;
	}

	// Equivalent toString
	public String displayVertex(Vertex vertex) {
		String str = this.vertex.getName();

		if (this.iValue != null) {
			str += "(" + this.iValue.intValue() + ")";
		}

		return str;
	}

}
