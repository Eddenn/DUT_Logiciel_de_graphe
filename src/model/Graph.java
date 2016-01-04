package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe permettant de gérer le graphe dans son ensemble
 * 
 * @author Les grosses bites
 * 
 */

public class Graph {

	private int[][] tMatrix;
	private ArrayList<Vertex> alVertex;
	private boolean bDirected;
	private boolean bValued;

	public Graph(boolean bDirected, boolean bValued) {
		alVertex = new ArrayList<Vertex>();
		this.bDirected = bDirected;
		this.bValued = bValued;

	}
	
	//Getters and Setters
	public boolean isbDirected() {return bDirected;}
	public boolean isbValued() 	 {return bValued;}

	// Méthode permettant de générer la matrice du graphe
	public int[][] generateMatrix() {
		int alVertexSize = alVertex.size();
		tMatrix = new int[alVertexSize][alVertexSize];

		// On parcours le graphe
		for (int i = 0; i < alVertexSize; i++) {
			for (int j = 0; j < alVertexSize; j++) {
				// On parcours les arcs du sommet i
				for (Arc a : alVertex.get(i).getAlArcs()) {
					// Si l'un des arcs de i est relié à j
					if (a.getVertex() == alVertex.get(j)) {
						// Si la matrice est valué, on affiche la valeur de l'arc
						if (bValued)
							tMatrix[i][j] = a.getIValue();
						// Sinon, on note l'existence de l'arc avec 1
						else
							tMatrix[i][j] = 1;
						break;
					} 
					// Si aucun arc de i n'est relié à j, on place un 0 dans la matrice
					else
						tMatrix[i][j] = 0;
				}
			}
		}
		return tMatrix;
	}

	public HashMap generateAdjacencyList() {
		return new HashMap();
	}

	public void addVertex(String strName) {
		alVertex.add(new Vertex(strName, this));
	}

	public void addVertex(String strName, String strColor) {
		alVertex.add(new Vertex(strName, strColor, this));
	}

	// Methode permettant d'ajouter des arcs et arêtes non valués
	public void addArc(Vertex v, Vertex vBis) {
		if (this.bDirected) {
			Arc arc = new Arc(vBis);
			v.getAlArcs().add(arc);
		} else {
			Arc a1 = new Arc(v);
			Arc a2 = new Arc(vBis);
			v.getAlArcs().add(a2);
			vBis.getAlArcs().add(a1);
			/*Edge e = new Edge(v, vBis);
			v.getAlArcs().add(e);
			vBis.getAlArcs().add(e);*/
		}
	}
	
	
	public void deleteVertex(Vertex v) {
		for (int i = 0; i < alVertex.size(); i++ ){
			for (int j = 0; j < alVertex.get(i).getAlArcs().size(); j++){
				if (alVertex.get(i).getAlArcs().get(j).getVertex() == v){
					alVertex.get(i).getAlArcs().remove(j);
				}
			}
		}
		this.alVertex.remove(v);
	}
	
	public void deleteArc(Arc a) {
		for (int i = 0; i < alVertex.size(); i++) {
			ArrayList<Arc> alArcRef = alVertex.get(i).getAlArcs();
			for (int j = 0; j < alArcRef.size() ; i++) {
				if (alArcRef.get(j) == a) {
					alArcRef.remove(j);
				}
			}
		}
			
	}

	// Methode permettant d'ajouter des arcs et arêtes valués
	public void addArc(Vertex v, Vertex vBis, int iValue) {
		if (this.bDirected) {
			Arc a = new Arc(vBis, iValue);
			v.getAlArcs().add(a);
		} else {
			Arc a1 = new Arc(v, iValue);
			Arc a2 = new Arc(vBis, iValue);
			v.getAlArcs().add(a2);
			vBis.getAlArcs().add(a1);
			/*Edge e = new Edge(v, vBis, iValue);
			v.getAlArcs().add(e);
			vBis.getAlArcs().add(e);*/
		}
	}

	// Getters and Setters
	public int[][] getTMatrix() {
		return this.tMatrix;
	}
	public ArrayList<Vertex> getAlVertex() {
		return this.alVertex;
	}

	// Display
	public String toString() {
		String sRet = "";

		for (Vertex v : alVertex) {
			sRet += v + "\n";
		}
		return sRet;
	}

	public static void main(String[] args) {
		Graph a = new Graph(false, false);

		a.addVertex("A");
		a.addVertex("B");
		a.addVertex("C");

		a.addArc(a.getAlVertex().get(0), a.getAlVertex().get(1));
		a.addArc(a.getAlVertex().get(1), a.getAlVertex().get(2));
		a.addArc(a.getAlVertex().get(1), a.getAlVertex().get(0));

		System.out.println(a);
		System.out.println();

		Graph b = new Graph(true, false);

		b.addVertex("A");
		b.addVertex("B");
		b.addVertex("C");

		b.addArc(b.getAlVertex().get(0), b.getAlVertex().get(1));
		b.addArc(b.getAlVertex().get(1), b.getAlVertex().get(2));
		a.addArc(b.getAlVertex().get(1), b.getAlVertex().get(0));

		System.out.println(b);
		System.out.println();

		Graph c = new Graph(true, true);

		c.addVertex("A");
		c.addVertex("B");
		c.addVertex("C");

		c.addArc(c.getAlVertex().get(0), c.getAlVertex().get(1),8);
		c.addArc(c.getAlVertex().get(1), c.getAlVertex().get(2),5);
		a.addArc(c.getAlVertex().get(1), c.getAlVertex().get(0),3);

		System.out.println(c);
		System.out.println();

		Graph d = new Graph(false, true);

		d.addVertex("A");
		d.addVertex("B");
		d.addVertex("C");

		d.addArc(d.getAlVertex().get(0), d.getAlVertex().get(1),8);
		d.addArc(d.getAlVertex().get(1), d.getAlVertex().get(2),5);
		a.addArc(d.getAlVertex().get(1), d.getAlVertex().get(0),3);

		System.out.println(d);
		
		d.generateMatrix();
		b.generateMatrix();
		c.generateMatrix();
		a.generateMatrix();
		
		System.out.println("Oriente : "+ a.bDirected + " Value : " + a.bValued);
		for (int i = 0 ; i < a.tMatrix.length ; i++) {
			for (int j = 0; j < a.tMatrix.length ; j++) {
				System.out.print(a.tMatrix[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		
		System.out.println("Oriente : "+ b.bDirected + " Value : " + b.bValued);
		for (int i = 0 ; i < b.tMatrix.length ; i++) {
			for (int j = 0; j < b.tMatrix.length ; j++) {
				System.out.print(b.tMatrix[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		
		System.out.println("Oriente : "+ c.bDirected + " Value : " + c.bValued);
		for (int i = 0 ; i < c.tMatrix.length ; i++) {
			for (int j = 0; j < c.tMatrix.length ; j++) {
				System.out.print(c.tMatrix[i][j] + " ");
			}
			System.out.println();
		}
		
		
		System.out.println();
		
		System.out.println("Oriente : "+ d.bDirected + " Value : " + d.bValued);
		for (int i = 0 ; i < d.tMatrix.length ; i++) {
			for (int j = 0; j < d.tMatrix.length ; j++) {
				System.out.print(d.tMatrix[i][j] + " ");
			}
			System.out.println();
		}
		
		System.out.println();
		
		
		d.deleteVertex(d.getAlVertex().get(2));
		d.generateMatrix();
		
		System.out.println("Oriente : "+ d.bDirected + " Value : " + d.bValued);
		for (int i = 0 ; i < d.tMatrix.length ; i++) {
			for (int j = 0; j < d.tMatrix.length ; j++) {
				System.out.print(d.tMatrix[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		
		System.out.println("Oriente : "+ c.bDirected + " Value : " + c.bValued);
		for (int i = 0 ; i < c.tMatrix.length ; i++) {
			for (int j = 0; j < c.tMatrix.length ; j++) {
				System.out.print(c.tMatrix[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		c.deleteArc(d.getAlVertex().get(1).getAlArcs().get(1));//a corriger
		c.generateMatrix();
		
		System.out.println("Oriente : "+ c.bDirected + " Value : " + c.bValued);
		for (int i = 0 ; i < c.tMatrix.length ; i++) {
			for (int j = 0; j < c.tMatrix.length ; j++) {
				System.out.print(c.tMatrix[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

}
