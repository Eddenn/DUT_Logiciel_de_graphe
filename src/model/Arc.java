package model;

/**
 * Classe permettant de g�rer les arcs entre les sommets
 * 
 * @author Les grosses bites
 * 
 */

public class Arc {

	private Vertex vertex;
	private Integer iValue;

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
	
	public void setValue(int a){
		this.iValue = a;
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
