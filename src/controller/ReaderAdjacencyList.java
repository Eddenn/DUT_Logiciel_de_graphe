package controller;

import java.util.ArrayList;
import model.Graph;
import model.Vertex;

/**
 * Classe qui génère le graphe correspondant à la liste d'adjacent contenue dans le fichier à lire
 * @author Groupe 3
 * @version 2016-01-08
 */
public class ReaderAdjacencyList extends Reader {
	
	/**
	 * Méthode qui crée un graphe correspondant à la liste d'adjacent du fichier
	 * @param alStr Contient la liste des adjacents
	 * @return un graphe correspondant à la liste d'adjacence
	 */
	public static Graph ReadAdjacencyList(ArrayList<String> alStr) {

		/* Vérification des paramètres du graphe*/
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
			/*Création d'un graphe avec les paramètres enregistrés*/
			Graph graph = new Graph(bIsDirected, bIsValued);	
			
			String[] tVertexName = new String[alStr.size()];	//Tableau contenant les noms des sommets

			/*Extraction des noms des sommets et ajout des sommets au graphe*/
			for (int i = 0; i < alStr.size(); i++) {
				String[] tStr = alStr.get(i).split("=");

				graph.addVertex(tStr[0]);
				tVertexName[i] = tStr[0];
			}

			/*---Appelle une méthode pour ajouter les arcs selon les paramètres du graphe--*/
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
	 * Méthode qui crée les arcs d'un graphe orienté et valué
	 * @param graph le graphe en création contenant seulement les sommets
	 * @param alStr la liste d'adjacence
	 * @param tVertexName le tableau contenant le nom des sommets
	 * @return le graphe généré
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

					// On vérifie s'il est bien valué.
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
	 * Méthode qui crée les arcs d'un graphe orienté et non valué
	 * @param graph le graphe en création contenant seulement les sommets
	 * @param alStr la liste d'adjacence
	 * @param tVertexName le tableau contenant le nom des sommets
	 * @return le graphe généré
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

					// On fait un test pour vérifier si la liste est bien non-valuée
					String[] tStrVertexValue = strVertexValue.split(",");
					
					// Si c'est valué, on retourne avec la méthode valué
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
	 * Méthode qui crée les arcs d'un graphe non orienté et valué
	 * @param graph le graphe en création contenant seulement les sommets
	 * @param alStr la liste d'adjacence
	 * @param tVertexName le tableau contenant le nom des sommets
	 * @return le graphe généré
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

					// On vérifie s'il est bien valué.
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
	 * Méthode qui crée les arcs d'un graphe non orienté et non valué
	 * @param graph le graphe en création contenant seulement les sommets
	 * @param alStr la liste d'adjacence
	 * @param tVertexName le tableau contenant le nom des sommets
	 * @return le graphe généré
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

					// On fait un test pour vérifier si la liste est bien non-valuée
					String[] tStrVertexValue = strVertexValue.split(",");
					
					// Si c'est valué, on retourne avec la méthode valué
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
