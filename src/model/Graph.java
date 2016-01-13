package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Classe permettant de g�rer le graphe dans son ensemble
 * @author Groupe 3
 * @version 2016-01-11
 */
public class Graph {

	private int[][] tMatrix;
	private ArrayList<Vertex> alVertex;
	private boolean bDirected;
	private boolean bValued;

	/**
	 * Constructeur qui instancie un graphe avec les param�tres d�sir�s 
	 * @param bDirected true si le graphe est orient�, false sinon
	 * @param bValued true si le graphe est valu�, false sinon
	 */
	public Graph(boolean bDirected, boolean bValued) {
		alVertex = new ArrayList<Vertex>(); //r�cup�ration de l'ArrayList de sommet
		this.bDirected = bDirected;
		this.bValued = bValued;

	}

	/**
	 * M�thoque qui indique si le graphe est orient�
	 * @return true s'il est orient�, false sinon
	 */
	public boolean isDirected() {
		return bDirected;
	}

	/**
	 * M�thode qui indique si le graphe est valu�
	 * @return true s'il est valu�, false sinon
	 */
	public boolean isValued() {
		return bValued;
	}


	/**
	 * M�thode permettant de g�n�rer la matrice du graphe
	 * @return un tableau repr�sentant la matrice
	 */
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
	
	/**
	 * M�thode permettant de g�n�rer la liste d'adjacence du graphe
	 * @return une hashMap contenant le nom des sommets et leurs arcs
	 */
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

	/**
	 * M�thode permettant d'ajouter un sommet
	 * @param strName le nom du sommet
	 */
	public void addVertex(String strName) {
		alVertex.add(new Vertex(strName));
	}

	/**
	 * M�thode permettant d'ajouter un sommet avec une couleur particuli�re
	 * @param strName le nom du sommet
	 * @param strColor la couleur du sommet
	 */
	public void addVertex(String strName, String strColor) {
		alVertex.add(new Vertex(strName, strColor));
	}

	/**
	 * Methode permettant d'ajouter des arcs et ar�tes non valu�s
	 * @param v le nom du premier sommet
	 * @param vBis le nom du deuxi�me sommet
	 */
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

	/**
	 * M�thode permettant de supprimer un sommet
	 * Supprimer aussi tous les arcs associ�s
	 * @param v le nom du sommet � supprimer
	 */
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

	/**
	 * M�thode permettant de supprimer un arc
	 * @param a l'arc � supprimer
	 */
	public boolean deleteArc(Vertex v, Vertex vBis)  {
		for (int i = 0; i < v.getAlArcs().size(); i++) {
			if (v.getAlArcs().get(i).getVertex() == vBis) {
				v.getAlArcs().remove(v.getAlArcs().get(i));
				return true;
			}
		}
		return false;
	}

	/**
	 * Methode permettant d'ajouter des arcs et ar�tes valu�es
	 * @param v le nom du premier sommet
	 * @param vBis le nom du deuxi�me sommet
	 * @param iValue la valeur de l'arc
	 */
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

	/*---------------------
	 * Getters et Setters
	 *--------------------*/
	public int[][] getTMatrix() {
		return this.tMatrix;
	}
	
	/**
	 * M�thode qui retourner la liste des noms des sommets
	 * @return tableau de caract�res
	 */
	public char[] getListVertex() {
		char[] tChar = new char[alVertex.size()];
		
		for (int i = 0; i < alVertex.size(); i++) {
			tChar[i] = alVertex.get(i).getName().charAt(0);
		}
		
		return tChar;
	}

	/**
	 * M�thode qui retourne tous les sommets
	 * @return une arraylist de sommet
	 */
	public ArrayList<Vertex> getAlVertex() {
		return this.alVertex;
	}

	/**
	 * M�thode qui retourne le sommet � l'indice donn�
	 * @param i l'indice du sommet dans l'arrayList de sommet
	 * @return le sommet correspondant
	 */
	public Vertex getVertex(int i) {
		return alVertex.get(i);
	}

	/**
	 * M�thode qui retourne le sommet correspondant au nom de sommet donn�
	 * @param strVertexName le nom du sommet � retourner
	 * @return le sommet correspondant
	 */
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

	/**
	 * M�thode permettant d'obtenir la liste d'adjacence du graphe
	 * @return Une string sous la forme "A:{B(6)}...."
	 */
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

	/**
	 * M�thode permettant d'obtenir la liste d'adjacence du graphe
	 * @return Une ArrayList de String donc chaque �l�ment correspont � un sommet
	 */
	public ArrayList<String> getFormattedListAlString() {

		HashMap<String, ArrayList<String>> hm = generateAdjacencyList();

		ArrayList<String> alRet = new ArrayList<String>();
		alRet.add("-- Liste d'adjacence :\n");
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

	/*-------------
	 * Affichage
	 * ----------*/
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
	
	/**
	 * M�thode permettant de mettre � jour un sommet
	 * @param oldName l'ancien nom du sommet
	 * @param newName le nouveau nom du sommet
	 */
	public void updateVertex(String oldName, String newName) {
		for (Vertex v : alVertex) {
			if (v.getName().equals(oldName)) {
				v.setName(newName);
			}
		}
	}
	
	/**
	 * M�thode permettant de modifier la valeur d'un arc
	 * @param a L'arc � modifier
	 * @param value la nouvelle valeur de l'arc
	 */
	public void updateArc(Arc a, int value) {
		a.setValue(value);
	}
	
	

	
}
