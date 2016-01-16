package controller;

import java.util.ArrayList;

import model.Graph;
import model.Vertex;

/**
 * Classe qui lit une Matrice et g�n�re le graphe associ�
 * 
 * @author Groupe 3
 * @version 2016-01-08
 */

public class ReaderMatrix {
	private ArrayList<String> alStr;
	private boolean bDirected;
	private boolean bValued;
	private int[][] tMatrix;
	private Graph graph;

	/**
	 * Constructeur permettant de g�n�rer le graphe
	 * 
	 * @param alStr
	 *            Une ArrayList de String correspondant aux lignes d'un fichier
	 * @param bDirected
	 *            true si le graphe est orient�
	 * @param bValued
	 *            true si le graphe est valu�
	 */
	public ReaderMatrix(ArrayList<String> alStr, boolean bDirected, boolean bValued) {
		this.alStr = alStr;
		this.bDirected = bDirected;
		this.bValued = bValued;

		graph = new Graph(bDirected, bValued);

		generateMatrix();

		graph = createGraph();
	}

	public Graph getGraph() {
		return graph;
	}

	/**
	 * M�thode cr�ant le graphe selon le fait qu'il soit valu� ou orient�
	 * 
	 * @return null si le format d'�criture du fichier est erron�
	 */
	private Graph createGraph() {
		try {
			if (bDirected && bValued) {
				return createDirectedValuedGraph();
			}

			else if (bDirected && !bValued) {
				return createDirectedNotValuedGraph();
			}

			else if (!bDirected && bValued) {
				return createNotDirectedValuedGraph();
			}

			else if (!bDirected && !bValued) {
				return createNotDirectedNotValuedGraph();
			}
		} catch (Exception e) {
		}

		return null;
	}

	/**
	 * M�thode permettant lire la matrice contenue dans le fichier
	 */
	private void generateMatrix() {

		int cpt = 0;
		for (int i = 0; i < alStr.size() && alStr.get(i).indexOf("Coordonn") < 0; i++) {
			cpt++;
		}

		tMatrix = new int[cpt][cpt];

		for (int i = 0; i < cpt; i++) {
			String tValuesMatrix[] = alStr.get(i).split(" ");

			for (int j = 0; j < tValuesMatrix.length; j++) {
				tMatrix[i][j] = Integer.parseInt(tValuesMatrix[j]);
			}
		}
	}

	/**
	 * M�thode permettant de lire un graphe orient� et valu�
	 * 
	 * @return null si le graphe n'a pas �t� correctement cr��.
	 */
	private Graph createDirectedValuedGraph() {
		Graph graph = new Graph(true, true);

		String strVertexName = "A";

		for (int i = 0; i < tMatrix.length; i++) {
			graph.addVertex(strVertexName);
			strVertexName = incrementeName(strVertexName);
		}

		for (int i = 0; i < tMatrix.length; i++) {
			Vertex v = graph.getVertex(i);

			for (int j = 0; j < tMatrix[i].length; j++) {
				if (tMatrix[i][j] != -1) {
					Vertex vBis = graph.getVertex(j);

					graph.addArc(vBis, v, tMatrix[i][j]);
				}
			}
		}

		return graph;
	}

	/**
	 * M�thode permettant de lire un graphe orient� non valu�
	 * 
	 * @return null si le graphe n'a pas �t� correctement cr��.
	 */
	private Graph createDirectedNotValuedGraph() {
		Graph graph = new Graph(true, false);

		String strVertexName = "A";

		for (int i = 0; i < tMatrix.length; i++) {
			graph.addVertex(strVertexName);
			strVertexName = incrementeName(strVertexName);
		}

		for (int i = 0; i < tMatrix.length; i++) {
			Vertex v = graph.getVertex(i);

			for (int j = 0; j < tMatrix[i].length; j++) {
				if (tMatrix[i][j] == 1) {
					Vertex vBis = graph.getVertex(j);

					graph.addArc(vBis, v);
				}
			}
		}

		return graph;
	}

	/**
	 * M�thode permettant de lire un graphe non orient� et non valu�
	 * 
	 * @return null si le graphe n'a pas �t� correctement cr��.
	 */
	private Graph createNotDirectedNotValuedGraph() {
		Graph graph = new Graph(true, false);

		String strVertexName = "A";

		for (int i = 0; i < tMatrix.length; i++) {
			graph.addVertex(strVertexName);
			strVertexName = incrementeName(strVertexName);
		}

		int iColStart = 0;

		for (int i = 0; i < tMatrix.length; i++) {
			Vertex v = graph.getVertex(i);

			for (int j = iColStart; j < tMatrix[i].length; j++) {
				if (tMatrix[i][j] == 1) {
					Vertex vBis = graph.getVertex(j);

					graph.addArc(vBis, v, tMatrix[i][j]);
				}
			}

			iColStart++;
		}

		return graph;
	}

	/**
	 * M�thode permettant de lire un graphe non orient� et valu�
	 * 
	 * @return null si le graphe n'a pas �t� correctement cr��.
	 */
	private Graph createNotDirectedValuedGraph() {
		Graph graph = new Graph(false, false);

		String strVertexName = "A";

		for (int i = 0; i < tMatrix.length; i++) {
			graph.addVertex(strVertexName);
			strVertexName = incrementeName(strVertexName);
		}

		int iColStart = 0;

		for (int i = 0; i < tMatrix.length; i++) {
			Vertex v = graph.getVertex(i);

			for (int j = iColStart; j < tMatrix[i].length; j++) {
				if (tMatrix[i][j] == 1) {
					Vertex vBis = graph.getVertex(j);

					graph.addArc(v, vBis);
				}
			}

			iColStart++;
		}

		return graph;
	}

	/**
	 * M�thode qui incr�mente le nom des sommets
	 * 
	 * @param str
	 *            le nom du dernier sommet
	 * @return le nom du prochain sommet
	 */
	private static String incrementeName(String str) {
		String strNew = "";

		if (isOnlyComposedOfZ(str)) {
			for (int i = 0; i < str.length(); i++) {
				strNew += "A";
			}

			strNew += "A";
		} else if (str.charAt(str.length() - 1) == 'Z') {
			for (int i = str.length() - 1; i >= 0; i--) {
				if (str.charAt(i) != 'Z') {
					strNew = str.substring(0, i) + (char) (str.charAt(i) + 1);

					for (int j = i + 1; j < str.length(); j++) {
						strNew += "A";
					}
				}
			}
		} else {
			strNew = str.substring(0, str.length() - 1) + (char) (str.charAt(str.length() - 1) + 1);
		}

		return strNew;
	}

	/**
	 * M�htode qui v�rifie si le nom est �gal � Z
	 * 
	 * @param str
	 *            nom du sommet
	 * @return true ou false
	 */
	private static boolean isOnlyComposedOfZ(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != 'Z') {
				return false;
			}
		}

		return true;
	}
}