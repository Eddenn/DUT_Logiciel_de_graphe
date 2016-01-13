package controller;

import java.util.ArrayList;

import model.Graph;
import model.Vertex;

/**
 * Classe qui génère le graphe correspondant à la liste d'adjacent contenue dans
 * le fichier à lire
 * 
 * @author Groupe 3
 * @version 2016-01-08
 */
public class ReaderAdjacencyList {
	private ArrayList<String> alStr;
	private boolean bDirected;
	private boolean bValued;
	private Graph graph;
	private ArrayList<String> alVertexName;

	
	/**
	 * Constructeur permettant de générer le graphe
	 * @param alStr Une ArrayList de String correspondant aux lignes d'un fichier
	 * @param bDirected true si le graphe est orienté
	 * @param bValued true si le graphe est valué
	 */
	public ReaderAdjacencyList(ArrayList<String> alStr, boolean bDirected, boolean bValued) {
		this.alStr = alStr;
		this.bDirected = bDirected;
		this.bValued = bValued;

		graph = new Graph(bDirected, bValued);
		generateVertex();

		graph = createGraph();
	}

	
	public Graph getGraph() {
		return graph;
	}
	
	/**
	 * Méthode permettant d'initialiser les sommets du graphe
	 */
	private void generateVertex() {
		alVertexName = new ArrayList<String>();

		for (int i = 0; i < alStr.size() && alStr.get(i).indexOf("Coordonn") < 0; i++) {
			if (alStr.get(i).indexOf("Coordonn") < 0) {
				String str = alStr.get(i).substring(0, alStr.get(i).indexOf("="));

				graph.addVertex(str);
				alVertexName.add(str);
			}
		}
	}

	/**
	 * Méthode créant le graphe selon le fait qu'il soit valué ou orienté
	 * @return null si le format d'écriture du fichier est erroné
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
		} catch (Exception e) {}
		
		return null;
	}

	/**
	 * Méthode permettant de lire un graphe orienté et valué
	 * @return null si le graphe n'a pas été correctement créé.
	 */
	private Graph createDirectedValuedGraph() {
		boolean bContinue = true;

		for (int i = 0; i < alStr.size() && bContinue; i++) {
			if (!alStr.get(i).equals("")) {
				String str = alStr.get(i).replaceAll("\\{", "");
				str = str.replaceAll("\\}", "");

				String[] tStr = str.split("=");

				if (tStr.length > 1) {
					String[] tLinkedVertex = tStr[1].split("\\),\\(");

					for (int j = 0; j < tLinkedVertex.length; j++) {
						String strVertexValue = tLinkedVertex[j].replaceAll("\\(", "");
						strVertexValue = strVertexValue.replaceAll("\\)", "");

						String[] tStrVertexValue = strVertexValue.split(",");

						// On vérifie s'il est bien valué.
						if (tStrVertexValue.length < 2) {
							graph.setValued(false);

							return createDirectedNotValuedGraph();
						}

						Vertex v = graph.getVertex(tStr[0]);

						Vertex vBis = graph.getVertex(tStrVertexValue[0]);

						graph.addArc(v, vBis, Integer.parseInt(tStrVertexValue[1]));
					}
				}
			} else {
				bContinue = false;
			}
		}
		
		return graph;
	}

	/**
	 * Méthode permettant de lire un graphe orienté non valué
	 * @return null si le graphe n'a pas été correctement créé.
	 */
	private Graph createDirectedNotValuedGraph() {
		boolean bContinue = true;

		for (int i = 0; i < alStr.size() && bContinue; i++) {
			if (!alStr.get(i).equals("")) {
				String str = alStr.get(i).replaceAll("\\{", "");
				str = str.replaceAll("\\}", "");

				String[] tStr = str.split("=");

				if (tStr.length > 1) {
					String[] tLinkedVertex = tStr[1].split("\\),\\(");

					for (int j = 0; j < tLinkedVertex.length; j++) {
						String strVertexValue = tLinkedVertex[j].replaceAll("\\(", "");
						strVertexValue = strVertexValue.replaceAll("\\)", "");

						String[] tStrVertexValue = strVertexValue.split(",");

						// On vérifie s'il est bien valué.
						if (tStrVertexValue.length > 1) {
							graph.setValued(true);

							return createDirectedValuedGraph();
						}

						Vertex v = graph.getVertex(tStr[0]);

						Vertex vBis = graph.getVertex(strVertexValue);

						graph.addArc(v, vBis);
					}
				}
			} else {
				bContinue = false;
			}
		}
		
		return graph;
	}

	/**
	 * Méthode permettant de lire un graphe non orienté et valué
	 * @return null si le graphe n'a pas été correctement créé.
	 */
	private Graph createNotDirectedValuedGraph() {
		ArrayList<String> alVertexAlreadyProcessed = new ArrayList<String>();
		boolean bContinue = true;

		for (int i = 0; i < alStr.size() && bContinue; i++) {
			if (!alStr.get(i).equals("")) {
				String str = alStr.get(i).replaceAll("\\{", "");
				str = str.replaceAll("\\}", "");

				String[] tStr = str.split("=");

				if (tStr.length > 1) {
					String[] tLinkedVertex = tStr[1].split("\\),\\(");

					for (int j = 0; j < tLinkedVertex.length; j++) {
						String strVertexValue = tLinkedVertex[j].replaceAll("\\(", "");
						strVertexValue = strVertexValue.replaceAll("\\)", "");

						String[] tStrVertexValue = strVertexValue.split(",");

						// On vérifie s'il est bien valué.
						if (tStrVertexValue.length < 2) {
							graph.setValued(false);

							return createNotDirectedNotValuedGraph();
						}

						if (!alVertexAlreadyProcessed.contains(tStrVertexValue[0])) {
							Vertex v = graph.getVertex(tStr[0]);

							Vertex vBis = graph.getVertex(tStrVertexValue[0]);

							graph.addArc(v, vBis, Integer.parseInt(tStrVertexValue[1]));
						}
					}
				}
				
				alVertexAlreadyProcessed.add(tStr[0]);
			} else {
				bContinue = false;
			}
		}
		
		return graph;
	}

	/**
	 * Méthode permettant de lire un graphe non orienté et non valué
	 * @return null si le graphe n'a pas été correctement créé.
	 */
	private Graph createNotDirectedNotValuedGraph() {
		ArrayList<String> alVertexAlreadyProcessed = new ArrayList<String>();
		boolean bContinue = true;

		for (int i = 0; i < alStr.size() && bContinue; i++) {
			if (!alStr.get(i).equals("")) {
				String str = alStr.get(i).replaceAll("\\{", "");
				str = str.replaceAll("\\}", "");

				String[] tStr = str.split("=");

				if (tStr.length > 1) {
					String[] tLinkedVertex = tStr[1].split("\\),\\(");

					for (int j = 0; j < tLinkedVertex.length; j++) {
						String strVertexValue = tLinkedVertex[j].replaceAll("\\(", "");
						strVertexValue = strVertexValue.replaceAll("\\)", "");

						String[] tStrVertexValue = strVertexValue.split(",");

						// On vérifie s'il est bien valué.
						if (tStrVertexValue.length > 1) {
							graph.setValued(true);

							return createNotDirectedValuedGraph();
						}

						if (!alVertexAlreadyProcessed.contains(strVertexValue)) {
							Vertex v = graph.getVertex(tStr[0]);

							Vertex vBis = graph.getVertex(strVertexValue);

							graph.addArc(v, vBis);
						}
					}
				}
				
				alVertexAlreadyProcessed.add(tStr[0]);
			} else {
				bContinue = false;
			}
		}
		
		return graph;
	}
}
