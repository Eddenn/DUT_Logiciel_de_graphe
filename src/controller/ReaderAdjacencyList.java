package controller;

import java.util.ArrayList;
import model.Graph;
import model.Vertex;

public class ReaderAdjacencyList extends Reader {
	public static Graph ReadAdjacencyList(ArrayList<String> alStr) {
		boolean bIsDirected = checkDirection(alStr.get(0));
		boolean bIsValued = checkValue(alStr.get(1));

		alStr.remove(0);
		alStr.remove(0);

		for (int i = 0; i < alStr.size() && alStr.get(i).equals(""); i++) {
			alStr.remove(i);
		}

		try {
			Graph graph = new Graph(bIsDirected, bIsValued);
			
			String[] tVertexName = new String[alStr.size()];

			for (int i = 0; i < alStr.size(); i++) {
				String[] tStr = alStr.get(i).split("=");

				graph.addVertex(tStr[0]);
				tVertexName[i] = tStr[0];
			}
			
			for (String str : alStr) {
				System.out.println(str);
			}
		
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
		} catch (Exception e) {
			return null;
		}
	}

	private static Graph createDirectedValuedGraph(Graph graph, ArrayList<String> alStr, String[] tVertexName) {
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

					Vertex v = graph.getVertex(tStr[0]);

					Vertex vBis = graph.getVertex(tStrVertexValue[0]);

					graph.addArc(v, vBis, Integer.parseInt(tStrVertexValue[1]));

				}
			}

		}

		return graph;
	}

	private static Graph createDirectedNotValuedGraph(Graph graph, ArrayList<String> alStr, String[] tVertexName) {
		for (String str : alStr) {
			str = str.replaceAll("\\{", "");
			str = str.replaceAll("\\}", "");

			String[] tStr = str.split("=");

			if (tStr.length > 1) {
				String[] tLinkedVertex = tStr[1].split("\\),\\(");

				for (int i = 0; i < tLinkedVertex.length; i++) {
					String strVertexValue = tLinkedVertex[i].replaceAll("\\(", "");
					strVertexValue = strVertexValue.replaceAll("\\)", "");

					Vertex v = graph.getVertex(tStr[0]);

					Vertex vBis = graph.getVertex(strVertexValue);

					graph.addArc(v, vBis);
				}
			}
		}

		return graph;
	}

	private static Graph createNotDirectedValuedGraph(Graph graph, ArrayList<String> alStr, String[] tVertexName) {
		ArrayList<String> alVertexAlreadyProcessed = new ArrayList<String>();

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

	private static Graph createNotDirectedNotValuedGraph(Graph graph, ArrayList<String> alStr, String[] tVertexName) {
		ArrayList<String> alVertexAlreadyProcessed = new ArrayList<String>();

		for (String str : alStr) {
			str = str.replaceAll("\\{", "");
			str = str.replaceAll("\\}", "");

			String[] tStr = str.split("=");

			if (tStr.length > 1) {
				String[] tLinkedVertex = tStr[1].split("\\),\\(");

				for (int i = 0; i < tLinkedVertex.length; i++) {
					String strVertexValue = tLinkedVertex[i].replaceAll("\\(", "");
					strVertexValue = strVertexValue.replaceAll("\\)", "");

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
