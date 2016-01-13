package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Classe permettant de gérer le graphe dans son ensemble
 * @author Groupe 3
 * @version 2016-01-11
 */
public class Graph {

	private int[][] tMatrix;
	private ArrayList<Vertex> alVertex;
	private boolean bDirected;
	private boolean bValued;

	/**
	 * Constructeur qui instancie un graphe avec les paramètres désirés 
	 * @param bDirected true si le graphe est orienté, false sinon
	 * @param bValued true si le graphe est valué, false sinon
	 */
	public Graph(boolean bDirected, boolean bValued) {
		alVertex = new ArrayList<Vertex>(); //récupération de l'ArrayList de sommet
		this.bDirected = bDirected;
		this.bValued = bValued;

	}

	/**
	 * Méthoque qui indique si le graphe est orienté
	 * @return true s'il est orienté, false sinon
	 */
	public boolean isDirected() {
		return bDirected;
	}

	/**
	 * Méthode qui indique si le graphe est valué
	 * @return true s'il est valué, false sinon
	 */
	public boolean isValued() {
		return bValued;
	}


	/**
	 * Méthode permettant de générer la matrice du graphe
	 * @return un tableau représentant la matrice
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
					// Si l'un des arcs de i est reliÃ© Ã  j
					if (a.getVertex() == alVertex.get(j)) {
						// Si la matrice est valuÃ©, on affiche la valeur de
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
	 * Méthode permettant de générer la liste d'adjacence du graphe
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
	 * Méthode permettant d'ajouter un sommet
	 * @param strName le nom du sommet
	 */
	public void addVertex(String strName) {
		alVertex.add(new Vertex(strName));
	}

	/**
	 * Méthode permettant d'ajouter un sommet avec une couleur particulière
	 * @param strName le nom du sommet
	 * @param strColor la couleur du sommet
	 */
	public void addVertex(String strName, String strColor) {
		alVertex.add(new Vertex(strName, strColor));
	}

	/**
	 * Methode permettant d'ajouter des arcs et arêtes non valués
	 * @param v le nom du premier sommet
	 * @param vBis le nom du deuxième sommet
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
	 * Méthode permettant de supprimer un sommet
	 * Supprimer aussi tous les arcs associés
	 * @param v le nom du sommet à supprimer
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
	 * Méthode permettant de supprimer un arc
	 * @param a l'arc à supprimer
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
	 * Methode permettant d'ajouter des arcs et arêtes valuées
	 * @param v le nom du premier sommet
	 * @param vBis le nom du deuxième sommet
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
	 * Méthode qui retourner la liste des noms des sommets
	 * @return tableau de caractères
	 */
	public char[] getListVertex() {
		char[] tChar = new char[alVertex.size()];
		
		for (int i = 0; i < alVertex.size(); i++) {
			tChar[i] = alVertex.get(i).getName().charAt(0);
		}
		
		return tChar;
	}

	/**
	 * Méthode qui retourne tous les sommets
	 * @return une arraylist de sommet
	 */
	public ArrayList<Vertex> getAlVertex() {
		return this.alVertex;
	}

	/**
	 * Méthode qui retourne le sommet à l'indice donné
	 * @param i l'indice du sommet dans l'arrayList de sommet
	 * @return le sommet correspondant
	 */
	public Vertex getVertex(int i) {
		return alVertex.get(i);
	}

	/**
	 * Méthode qui retourne le sommet correspondant au nom de sommet donné
	 * @param strVertexName le nom du sommet à retourner
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
	 * Méthode permettant d'obtenir la liste d'adjacence du graphe
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
	 * Méthode permettant d'obtenir la liste d'adjacence du graphe
	 * @return Une ArrayList de String donc chaque élément correspont à un sommet
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
	 * Méthode permettant de mettre à jour un sommet
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
	 * Méthode permettant de modifier la valeur d'un arc
	 * @param a L'arc à modifier
	 * @param value la nouvelle valeur de l'arc
	 */
	public void updateArc(Arc a, int value) {
		a.setValue(value);
	}
	
	

	
}
