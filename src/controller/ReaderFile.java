package controller;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import model.Graph;

public class ReaderFile {
	private ArrayList<String> alStr;
	private Graph graph;
	private ReaderMatrix rm;
	private ReaderAdjacencyList ral;
	private boolean bIsMatrix;
	private boolean bValued;
	private boolean bDirected;
	private boolean bHaveCoord;
	private Point[] tPoints;
	
	public ReaderFile(String strFilePath) {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(strFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Scanner sc = new Scanner(br);
		
		alStr = new ArrayList<String>();
		
		while (sc.hasNextLine()) {
			alStr.add(sc.nextLine());
		}
		
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String strFirstLine = alStr.get(0);
		alStr.remove(0);
		
		bDirected = checkDirection(alStr.get(0));
		alStr.remove(0);
		
		bValued = checkDirection(alStr.get(0));
		alStr.remove(0);
		
		for (int i = 0; i < alStr.size(); i++) {
			if (alStr.get(i).equals("")) {
				alStr.remove(i);
			}
		}
		
		alStr.remove(0);
		
		if (bIsMatrix = isMatrix(strFirstLine)) {
			rm = new ReaderMatrix(alStr,bDirected,bValued);
			graph = rm.getGraph();
		}
		else {
			ral = new ReaderAdjacencyList(alStr,bDirected,bValued);
			graph = ral.getGraph();
		}
		
		generateTabPoints();
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	public Point[] getPoints() {
		return tPoints;
	}
	
	public boolean hasCoord() {
		return bHaveCoord;
	}
	
	private void generateTabPoints() {
		int iIndiceCoord = getCoordinatesIndice();
		
		bHaveCoord = (iIndiceCoord != -1);
		
		if (bHaveCoord) {
			int iNbVertex = graph.getAlVertex().size();
			tPoints = new Point[iNbVertex];
			
			alStr.remove(iIndiceCoord);
			
			String str = alStr.get(iIndiceCoord).replaceAll("\\[", "");
			str = str.replaceAll("\\]", "");
			
			String[] tStrPoints = str.split(";");
			
			for (int i = 0; i < tPoints.length; i++) {
				String[] tCoordonnates = tStrPoints[i].split(",");

				double iX;
				double iY;
				
				if (tCoordonnates.length == 2) {
					try {
						iX = Double.parseDouble(tCoordonnates[0]);
						iY = Double.parseDouble(tCoordonnates[1]);
					} catch (Exception e) {
						iX = 0.0;
						iY = 0.0;
					}

					if (iX < 0) {
						iX = 0.0;
					}

					if (iY < 0) {
						iX = 0.0;
					}

					tPoints[i] = new Point((int)iX, (int)iY);
				}
				else {
					iX = 0.0;
					iY = 0.0;
				}
			}
		}
	}
	
	private int getCoordinatesIndice() {
		for (int i = 0; i < alStr.size(); i++) {
			if (alStr.get(i).indexOf("Coordonnées") >= 0) {
				return i;
			}
		}
		
		return -1;
	}
	
	private boolean isMatrix(String str) {
		str = str.toLowerCase().replaceAll("ismatrix=", "");

		return (str.toLowerCase().equals("true"));
	}
	
	private boolean checkDirection(String str) {
		str = str.toLowerCase().replace("directed=", "");
		
		return (str.toLowerCase().equals("true"));
	}
	
	private boolean checkValue(String str) {
		str = str.toLowerCase().replace("valued=", "");

		return (str.toLowerCase().equals("true"));
	}
}
