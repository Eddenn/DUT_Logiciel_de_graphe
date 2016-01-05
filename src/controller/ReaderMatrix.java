package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import model.Graph;
import model.Vertex;

public class ReaderMatrix extends Reader {
	public static Graph readMatrix(String strFile) {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(strFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		@SuppressWarnings("resource")
		Scanner sc = new Scanner(br);

		boolean bIsDirected = false;
		boolean bIsValuated = false;

		ArrayList<String> alStr = new ArrayList<String>();

		while (sc.hasNextLine()) {
			alStr.add(sc.nextLine());
		}

		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (alStr.size() < 4) {
			return null;
		}

		bIsDirected = checkDirection(alStr.get(0));
		bIsValuated = checkValue(alStr.get(1));

		alStr.remove(0);
		alStr.remove(0);

		for (int i = 0; i < alStr.size() && alStr.get(i).equals(""); i++) {
			alStr.remove(i);
		}

		int[][] tMatrix = new int[alStr.size()][alStr.size()];

		for (int i = 0; i < alStr.size(); i++) {
			String tValuesMatrix[] = alStr.get(i).split(" ");

			for (int j = 0; j < tValuesMatrix.length; j++) {
				tMatrix[i][j] = Integer.parseInt(tValuesMatrix[j]);
			}
		}

		if (bIsDirected && bIsValuated) {
			return createDirectedValuatedGraph(tMatrix);
		}

		if (bIsDirected && !bIsValuated) {
			return createDirectedNotValuatedGraph(tMatrix);
		}

		if (!bIsDirected && bIsValuated) {
			return createNotDirectedValuatedGraph(tMatrix);
		}

		return createNotDirectedNotValuatedGraph(tMatrix);
	}

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

					graph.addArc(v, vBis, tMatrix[i][j]);
				}
			}
		}

		return graph;
	}

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

					graph.addArc(v, vBis);
				}
			}
		}

		return graph;
	}

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

					graph.addArc(v, vBis, tMatrix[i][j]);
				}
			}

			iColStart++;
		}

		return graph;
	}

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

					graph.addArc(v, vBis);
				}
			}

			iColStart++;
		}

		return graph;
	}

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
	
	private static boolean isOnlyComposedOfZ(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != 'Z') {
				return false;
			}
		}

		return true;
	}
}
