package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Classe permettant de gérer le graphe dans son ensemble
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

	// Getters and Setters
	public boolean isDirected() {
		return bDirected;
	}

	public boolean isValued() {
		return bValued;
	}

	// Méthode permettant de générer la matrice du graphe
	public int[][] generateMatrix() {
		int alVertexSize = alVertex.size();
		tMatrix = new int[alVertexSize][alVertexSize];

		// Initialise i -1 les cases de la matrice
		for (int i = 0; i < alVertexSize; i++) {
			for (int j = 0; j < alVertexSize; j++) {
				if (this.bValued)
					tMatrix[j][i] = -1;
				else
					tMatrix[j][i] = 0;
			}
		}

		// On parcours le graphe
		for (int i = 0; i < alVertexSize; i++) {
			for (int j = 0; j < alVertexSize; j++) {
				// On parcours les arcs du sommet i
				for (Arc a : alVertex.get(i).getAlArcs()) {
					// Si l'un des arcs de i est relié à j
					if (a.getVertex() == alVertex.get(j)) {
						// Si la matrice est valué, on affiche la valeur de
						// l'arc
						if (bValued)
							tMatrix[j][i] = a.getIValue();
						// Sinon, on note l'existence de l'arc avec 1
						else
							tMatrix[j][i] = 1;
						break;
					}
				}
			}
		}
		return tMatrix;
	}

	@SuppressWarnings("rawtypes")
	public HashMap<String, ArrayList<String>> generateAdjacencyList() {
		HashMap<String, ArrayList<String>> hm = new HashMap<String, ArrayList<String>>();

		int qVertex = this.alVertex.size();
		String curVertex = "";
		ArrayList<String> list = new ArrayList<String>();

		for (int i = 0; i < qVertex; i++) {
			curVertex = alVertex.get(i).getName();
			list.add("{");
			for (int j = 0; j < alVertex.get(i).getAlArcs().size(); j++) {
				String s = "(";
				s += alVertex.get(i).getAlArcs().get(j).getVertex().getName();
				if (this.isValued()) {
					s += ",";
					s += alVertex.get(i).getAlArcs().get(j).getIValue();
				}
				s += ")";

				if (j != alVertex.get(i).getAlArcs().size() - 1)
					s += ",";

				list.add(s);
			}

			list.add("}");
			hm.put(curVertex, list);

			list = new ArrayList<String>();
		}

		return hm;
	}

	public void addVertex(String strName) {
		alVertex.add(new Vertex(strName, this));
	}

	public void addVertex(String strName, String strColor) {
		alVertex.add(new Vertex(strName, strColor, this));
	}

	// Methode permettant d'ajouter des arcs et arètes non valués
	public void addArc(Vertex v, Vertex vBis) {
		if (this.bDirected) {
			Arc arc = new Arc(vBis);
			v.getAlArcs().add(arc);
		} else {
			Arc a1 = new Arc(v);
			Arc a2 = new Arc(vBis);
			v.getAlArcs().add(a2);
			vBis.getAlArcs().add(a1);
		}
	}

	public void deleteVertex(Vertex v) {
		for (int i = 0; i < alVertex.size(); i++) {
			for (int j = 0; j < alVertex.get(i).getAlArcs().size(); j++) {
				if (alVertex.get(i).getAlArcs().get(j).getVertex() == v) {
					alVertex.get(i).getAlArcs().remove(j);
				}
			}
		}
		this.alVertex.remove(v);
	}

	public boolean deleteArc(Vertex v, Vertex vBis)  {
		for (int i = 0; i < v.getAlArcs().size(); i++) {
			if (v.getAlArcs().get(i).getVertex() == vBis) {
				v.getAlArcs().remove(v.getAlArcs().get(i));
				return true;
			}
		}
		return false;
	}

	// Methode permettant d'ajouter des arcs et arètes valuées
	public void addArc(Vertex v, Vertex vBis, int iValue) {
		if (this.bDirected) {
			Arc a = new Arc(vBis, iValue);
			v.getAlArcs().add(a);
		} else {
			Arc a1 = new Arc(v, iValue);
			Arc a2 = new Arc(vBis, iValue);
			v.getAlArcs().add(a2);
			vBis.getAlArcs().add(a1);
		}
	}

	// Getters and Setters
	public int[][] getTMatrix() {
		return this.tMatrix;
	}
	
	public char[] getListVertex() {
		char[] tChar = new char[alVertex.size()];
		
		for (int i = 0; i < alVertex.size(); i++) {
			tChar[i] = alVertex.get(i).getName().charAt(0);
		}
		
		return tChar;
	}

	public ArrayList<Vertex> getAlVertex() {
		return this.alVertex;
	}

	public Vertex getVertex(int i) {
		return alVertex.get(i);
	}

	public Vertex getVertex(String strVertexName) {
		for (Vertex vertex : alVertex) {
			if (vertex.getName().equals(strVertexName)) {
				return vertex;
			}
		}

		return null;
	}
	
	public void setValued(boolean bValued) {
		this.bValued = bValued;
	}

	// Display
	public String toString() {
		String sRet = "";

		for (Vertex v : alVertex) {
			sRet += v + "\n";
		}
		return sRet;
	}

	public String displayMatrix() {
		this.generateMatrix();
		String sRet = "";

		for (int i = 0; i < this.tMatrix.length; i++) {
			for (int j = 0; j < this.tMatrix.length; j++) {
				sRet += tMatrix[i][j] + " ";
			}
			sRet += "\n";
		}

		return sRet;
	}

	public String getFormattedList() {

		HashMap<String, ArrayList<String>> hm = generateAdjacencyList();

		String sRet = "";

		Set<String> setKey = hm.keySet();
		java.util.Iterator<String> it = setKey.iterator();

		while (it.hasNext()) {
			String strKey = it.next();
			sRet += strKey + "=";

			for (String s : hm.get(strKey)) {
				sRet += s;
			}

			sRet += "\n";
		}

		return sRet;
	}
	
	public ArrayList<String> getFormattedListAlString() {

		HashMap<String, ArrayList<String>> hm = generateAdjacencyList();

		ArrayList<String> alRet = new ArrayList<String>();
		String sProv = "";
		
		Set<String> setKey = hm.keySet();
		java.util.Iterator<String> it = setKey.iterator();

		while (it.hasNext()) {
			String strKey = it.next();
			sProv += strKey + "=";

			for (String s : hm.get(strKey)) {
				sProv += s;
			}
			alRet.add(sProv);
			sProv = "";
		}

		return alRet;
	}

	public String displayMatrix2() {
		this.generateMatrix();
		String sRet = "";

		for (int i = 0; i < tMatrix.length; i++) {
			sRet += "[";
			for (int j = 0; j < tMatrix[0].length; j++) {
				sRet += String.format("%4d", tMatrix[i][j]);
				if (j < tMatrix.length - 1)
					sRet += ", ";
			}
			sRet += "]";
			if (i < tMatrix[0].length - 1)
				sRet += ",\n";
		}

		return sRet;
	}

	
}
