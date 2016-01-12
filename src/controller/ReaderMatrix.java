package controller;

import java.util.ArrayList;

import model.Graph;
import model.Vertex;

public class ReaderMatrix {
	private ArrayList<String> alStr;
	private boolean bDirected;
	private boolean bValued;
	private int[][] tMatrix;
	private Graph graph;

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
	
	private Graph createGraph() {
		try {
			if (bDirected && bValued) {
				return createDirectedValuedGraph();
			}

			if (bDirected && !bValued) {
				return createDirectedNotValuedGraph();
			}

			if (!bDirected && bValued) {
				return createNotDirectedValuedGraph();
			}

			if (!bDirected && !bValued) {
				return createNotDirectedNotValuedGraph();
			}
		} catch (Exception e) {
		}

		return null;
	}

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

					graph.addArc(vBis, v);
				}
			}

			iColStart++;
		}

		return graph;
	}

	/**
	 * Méthode qui incrémente le nom des sommets
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
	 * Méhtode qui vérifie si le nom est égal à Z
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