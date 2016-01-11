package controller;

import java.util.ArrayList;

import model.Graph;
import model.Vertex;

/**
 *  Classe qui g�n�re le graphe correspondant � la matrice contenue dans le fichier � lire
 * @author Groupe 3
 * @version 2016-01-08
 */
public class ReaderMatrix extends Reader {
	
	/**
	 * M�thode qui cr�e un graphe correspondant � la matrice du fichier
	 * @param alStr Contient les diff�rentes lignes de la matrice
	 * @return le graphe correspondant � la matrice
	 */
	public static Graph readMatrix(ArrayList<String> alStr) {
		/* V�rification des param�tres du graphe*/
		boolean bIsDirected = checkDirection(alStr.get(0));
		boolean bIsValued = checkValue(alStr.get(1));
		
		/*Suppression des informations sur le graphe*/
		alStr.remove(0);
		alStr.remove(0);

		/*Suppression des lignes vides*/
		try {
			for (int i = 0; i < alStr.size() && alStr.get(i).equals(""); i++) {
				alStr.remove(i);
			}
			
			/*Cr�ation d'un tableau contenant la matrice*/
			int[][] tMatrix = new int[alStr.size()][alStr.size()];

			for (int i = 0; i < alStr.size(); i++) {
				String tValuesMatrix[] = alStr.get(i).split(" ");

				for (int j = 0; j < tValuesMatrix.length; j++) {
					tMatrix[i][j] = Integer.parseInt(tValuesMatrix[j]);
				}
			}

			/*---Appelle une m�thode pour ajouter les arcs selon les param�tres du graphe--*/
			if (bIsDirected && bIsValued) {
				return createDirectedValuatedGraph(tMatrix);
			}

			if (bIsDirected && !bIsValued) {
				return createDirectedNotValuatedGraph(tMatrix);
			}

			if (!bIsDirected && bIsValued) {
				return createNotDirectedValuatedGraph(tMatrix);
			}

			return createNotDirectedNotValuatedGraph(tMatrix);
			/*----------------------*/
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * M�thode qui cr�e les arcs d'un graphe orient� et valu�
	 * @param tMatrix
	 * @return le graphe complet�
	 */
	private static Graph createDirectedValuatedGraph(int[][] tMatrix) {
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
	 * M�thode qui cr�e les arcs d'un graphe orient� et non valu�
	 * @param tMatrix
	 * @return  le graphe complet�
	 */
	private static Graph createDirectedNotValuatedGraph(int[][] tMatrix) {
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
	 * M�thode qui cr�e les arcs d'un graphe non orient� et valu�
	 * @param tMatrix
	 * @return le graphe complet�
	 */
	private static Graph createNotDirectedValuatedGraph(int[][] tMatrix) {
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
	 * M�thode qui cr�e les arcs d'un graphe non orient� et non valu�
	 * @param tMatrix
	 * @return  le graphe complet�
	 */
	private static Graph createNotDirectedNotValuatedGraph(int[][] tMatrix) {
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

					graph.addArc(vBis, v);
				}
			}

			iColStart++;
		}

		return graph;
	}

	/**
	 * M�thode qui incr�mente le nom des sommets
	 * @param str le nom du dernier sommet
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
	 * @param str nom du sommet
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
