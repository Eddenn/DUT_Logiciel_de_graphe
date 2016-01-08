package controller;

import java.util.ArrayList;
import model.Graph;
import model.Vertex;

/**
 * Classe qui g�n�re le graphe correspondant � la liste d'adjacent contenue dans le fichier � lire
 * @author Groupe 3
 * @version 2016-01-08
 */
public class ReaderAdjacencyList extends Reader {
	
	/**
	 * M�thode qui cr�e un graphe correspondant � la liste d'adjacent du fichier
	 * @param alStr Contient la liste des adjacents
	 * @return un graphe correspondant � la liste d'adjacence
	 */
	public static Graph ReadAdjacencyList(ArrayList<String> alStr) {

		/* V�rification des param�tres du graphe*/
		boolean bIsDirected = checkDirection(alStr.get(0));
		boolean bIsValued = checkValue(alStr.get(1));

		/*Suppression des informations sur le graphe*/
		alStr.remove(0);
		alStr.remove(0);

		/*Suppression des lignes vides*/
		for (int i = 0; i < alStr.size() && alStr.get(i).equals(""); i++) {
			alStr.remove(i);
		}

		try {
			/*Cr�ation d'un graphe avec les param�tres enregistr�s*/
			Graph graph = new Graph(bIsDirected, bIsValued);	
			
			String[] tVertexName = new String[alStr.size()];	//Tableau contenant les noms des sommets

			/*Extraction des noms des sommets et ajout des sommets au graphe*/
			for (int i = 0; i < alStr.size(); i++) {
				String[] tStr = alStr.get(i).split("=");

				graph.addVertex(tStr[0]);
				tVertexName[i] = tStr[0];
			}

			/*---Appelle une m�thode pour ajouter les arcs selon les param�tres du graphe--*/
			if (bIsDirected && bIsValued) {
				return createDirectedValuedGraph(graph, alStr, tVertexName);
			}

			if (bIsDirected && !bIsValued) {
				return createDirectedNotValuedGraph(graph, alStr, tVertexName);
			}

			if (!bIsDirected && bIsValued) {
				return createNotDirectedValuedGraph(graph, alStr, tVertexName);
			}

			return createNotDirectedNotValuedGraph(graph, alStr, tVertexName);
			/*-----------------*/
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * M�thode qui cr�e les arcs d'un graphe orient� et valu�
	 * @param graph le graphe en cr�ation contenant seulement les sommets
	 * @param alStr la liste d'adjacence
	 * @param tVertexName le tableau contenant le nom des sommets
	 * @return le graphe g�n�r�
	 */
	private static Graph createDirectedValuedGraph(Graph graph, ArrayList<String> alStr, String[] tVertexName) {
		/*On filtre les informations en retirant le surplus de symbole*/
		for (String str : alStr) {
			str = str.replaceAll("\\{", "");
			str = str.replaceAll("\\}", "");

			String[] tStr = str.split("=");

			if (tStr.length > 1) {
				String[] tLinkedVertex = tStr[1].split("\\),\\(");

				for (int i = 0; i < tLinkedVertex.length; i++) {
					String strVertexValue = tLinkedVertex[i].replaceAll("\\(", "");
					strVertexValue = strVertexValue.replaceAll("\\)", "");

					String[] tStrVertexValue = strVertexValue.split(",");

					// On v�rifie s'il est bien valu�.
					if (tStrVertexValue.length < 2) {
						graph.setValued(false);
						
						return createDirectedNotValuedGraph(graph,alStr,tVertexName);
					}
					
					Vertex v = graph.getVertex(tStr[0]);

					Vertex vBis = graph.getVertex(tStrVertexValue[0]);

					graph.addArc(v, vBis, Integer.parseInt(tStrVertexValue[1]));

				}
			}

		}

		return graph;
	}
	
	/**
	 * M�thode qui cr�e les arcs d'un graphe orient� et non valu�
	 * @param graph le graphe en cr�ation contenant seulement les sommets
	 * @param alStr la liste d'adjacence
	 * @param tVertexName le tableau contenant le nom des sommets
	 * @return le graphe g�n�r�
	 */
	private static Graph createDirectedNotValuedGraph(Graph graph, ArrayList<String> alStr, String[] tVertexName) {
		/*On filtre les informations en retirant le surplus de symbole*/
		for (String str : alStr) {
			str = str.replaceAll("\\{", "");
			str = str.replaceAll("\\}", "");

			String[] tStr = str.split("=");

			if (tStr.length > 1) {
				String[] tLinkedVertex = tStr[1].split("\\),\\(");

				for (int i = 0; i < tLinkedVertex.length; i++) {
					String strVertexValue = tLinkedVertex[i].replaceAll("\\(", "");
					strVertexValue = strVertexValue.replaceAll("\\)", "");

					// On fait un test pour v�rifier si la liste est bien non-valu�e
					String[] tStrVertexValue = strVertexValue.split(",");
					
					// Si c'est valu�, on retourne avec la m�thode valu�
					if (tStrVertexValue.length > 1) {
						graph.setValued(true);
						
						return createDirectedValuedGraph(graph, alStr, tVertexName);
					}
					
					Vertex v = graph.getVertex(tStr[0]);

					Vertex vBis = graph.getVertex(strVertexValue);

					graph.addArc(v, vBis);
				}
			}
		}

		return graph;
	}
	
	/**
	 * M�thode qui cr�e les arcs d'un graphe non orient� et valu�
	 * @param graph le graphe en cr�ation contenant seulement les sommets
	 * @param alStr la liste d'adjacence
	 * @param tVertexName le tableau contenant le nom des sommets
	 * @return le graphe g�n�r�
	 */
	private static Graph createNotDirectedValuedGraph(Graph graph, ArrayList<String> alStr, String[] tVertexName) {
		ArrayList<String> alVertexAlreadyProcessed = new ArrayList<String>();
		/*On filtre les informations en retirant le surplus de symbole*/
		for (String str : alStr) {
			str = str.replaceAll("\\{", "");
			str = str.replaceAll("\\}", "");

			String[] tStr = str.split("=");

			if (tStr.length > 1) {
				String[] tLinkedVertex = tStr[1].split("\\),\\(");

				for (int i = 0; i < tLinkedVertex.length; i++) {
					String strVertexValue = tLinkedVertex[i].replaceAll("\\(", "");
					strVertexValue = strVertexValue.replaceAll("\\)", "");

					String[] tStrVertexValue = strVertexValue.split(",");

					// On v�rifie s'il est bien valu�.
					if (tStrVertexValue.length < 2) {
						graph.setValued(false);
						
						return createNotDirectedNotValuedGraph(graph,alStr,tVertexName);
					}
					
					if (!alVertexAlreadyProcessed.contains(tStrVertexValue[0])) {
						Vertex v = graph.getVertex(tStr[0]);

						Vertex vBis = graph.getVertex(tStrVertexValue[0]);

						graph.addArc(v, vBis, Integer.parseInt(tStrVertexValue[1]));
					}
				}
			}

			alVertexAlreadyProcessed.add(tStr[0]);
		}

		return graph;
	}
	
	/**
	 * M�thode qui cr�e les arcs d'un graphe non orient� et non valu�
	 * @param graph le graphe en cr�ation contenant seulement les sommets
	 * @param alStr la liste d'adjacence
	 * @param tVertexName le tableau contenant le nom des sommets
	 * @return le graphe g�n�r�
	 */
	private static Graph createNotDirectedNotValuedGraph(Graph graph, ArrayList<String> alStr, String[] tVertexName) {
		ArrayList<String> alVertexAlreadyProcessed = new ArrayList<String>();
		/*On filtre les informations en retirant le surplus de symbole*/
		for (String str : alStr) {
			str = str.replaceAll("\\{", "");
			str = str.replaceAll("\\}", "");

			String[] tStr = str.split("=");

			if (tStr.length > 1) {
				String[] tLinkedVertex = tStr[1].split("\\),\\(");

				for (int i = 0; i < tLinkedVertex.length; i++) {
					String strVertexValue = tLinkedVertex[i].replaceAll("\\(", "");
					strVertexValue = strVertexValue.replaceAll("\\)", "");

					// On fait un test pour v�rifier si la liste est bien non-valu�e
					String[] tStrVertexValue = strVertexValue.split(",");
					
					// Si c'est valu�, on retourne avec la m�thode valu�
					if (tStrVertexValue.length > 1) {
						graph.setValued(true);

						return createNotDirectedValuedGraph(graph, alStr, tVertexName);
					}
					
					if (!alVertexAlreadyProcessed.contains(strVertexValue)) {
						Vertex v = graph.getVertex(tStr[0]);

						Vertex vBis = graph.getVertex(strVertexValue);

						graph.addArc(v, vBis);
					}
				}
			}

			alVertexAlreadyProcessed.add(tStr[0]);
		}

		return graph;
	}
}
