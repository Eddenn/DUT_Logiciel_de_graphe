package model;

/**
 * Classe permettant de gérer les arêtes entre les sommets
 * @author Les grosses bites
 *
 */

public class Edge extends Arc {

	private Vertex vertexBis;
	
	public Edge(Vertex vertex, Vertex vertexBis) {
		super(vertex);
		this.vertexBis = vertexBis;
	}
	
	public Edge(Vertex vertex, Vertex vertexBis, int iValue) {
		super(vertex,iValue);
		this.vertexBis = vertexBis;
	}
	
	
	// Getters and Setters
	public Vertex getVertexBis() {	return this.vertexBis; }
	
	// Equivalent toString
	public String displayVertex(Vertex vertex) {
		String str;
		
		if (super.vertex != vertex) {
			str = super.vertex.getName();
		}
		else {
			str = this.vertexBis.getName();
		}
		
		if (super.iValue != null) {
			str += "(" + super.iValue.intValue() + ")"; 
		}
		
		return str;
	}
	
}
