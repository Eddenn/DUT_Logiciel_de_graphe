package controller;

import java.util.ArrayList;

import model.Graph;
import model.Vertex;

/**
 * Classe qui g�n�re le graphe correspondant � la liste d'adjacent contenue dans
 * le fichier � lire
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
	 * Constructeur permettant de g�n�rer le graphe
	 * @param alStr Une ArrayList de String correspondant aux lignes d'un fichier
	 * @param bDirected true si le graphe est orient�
	 * @param bValued true si le graphe est valu�
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
	 * M�thode permettant d'initialiser les sommets du graphe
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
	 * M�thode cr�ant le graphe selon le fait qu'il soit valu� ou orient�
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
		} catch (Exception e) {}
		
		return null;
	}

	/**
	 * M�thode permettant de lire un graphe orient� et valu�
	 * @return null si le graphe n'a pas �t� correctement cr��.
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

						// On v�rifie s'il est bien valu�.
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
	 * M�thode permettant de lire un graphe orient� non valu�
	 * @return null si le graphe n'a pas �t� correctement cr��.
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

						// On v�rifie s'il est bien valu�.
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
	 * M�thode permettant de lire un graphe non orient� et valu�
	 * @return null si le graphe n'a pas �t� correctement cr��.
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

						// On v�rifie s'il est bien valu�.
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
	 * M�thode permettant de lire un graphe non orient� et non valu�
	 * @return null si le graphe n'a pas �t� correctement cr��.
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

						// On v�rifie s'il est bien valu�.
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
