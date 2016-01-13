package controller;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import model.Graph;
import view.GraphStyle;

/**
 * Classe qui lit un fichier texte et génère le graphe contenu à l'intérieur
 * 
 * @author Groupe 3
 * @version 2016-01-08
 */

public class ReaderFile {
	private ArrayList<String> alStr;
	private Graph graph;
	private ReaderMatrix rm;
	private ReaderAdjacencyList ral;
	private boolean bValued;
	private boolean bDirected;
	private boolean bHaveCoord;
	private Point[] tPoints;
	private GraphStyle style;
	private Scanner sc;
	
	/**
	 * Constructeur permettant de charger un fichier à partir de son chemin d'accès
	 * @param strFilePath Le nom du fichier à charger
	 */
	public ReaderFile(String strFilePath) {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(strFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		sc = new Scanner(br);
		
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
		
		bValued = checkValue(alStr.get(0));
		alStr.remove(0);
		
		for (int i = 0; i < alStr.size(); i++) {
			if (alStr.get(i).equals("")) {
				alStr.remove(i);
			}
		}
		
		alStr.remove(0);
		
		
		if (isMatrix(strFirstLine)) {
			rm = new ReaderMatrix(alStr,bDirected,bValued);
			graph = rm.getGraph();
		}
		else {
			ral = new ReaderAdjacencyList(alStr,bDirected,bValued);
			graph = ral.getGraph();
		}
		
		generateTabPoints();
		generateStyle();
	}
	
	/**
	 * Constructeur permettant de charger un graphe à partir de ses informations contenues dans une ArrayList de String (Utilisé pour la sauvegarde provisoire)
	 * @param alStr ArrayList de String contenant les données du graphe ainsi que son style
	 * @param bDirected true si le graphe est orienté
	 * @param bValued true si le graphe est valué
	 */
	public ReaderFile(ArrayList<String> alStr, boolean bDirected, boolean bValued) {
		this.alStr = alStr;
		this.bDirected = bDirected;
		this.bValued = bValued;

		alStr.remove(0);
		
		for (int i = 0; i < alStr.size(); i++) {
			if (alStr.get(i).equals("")) {
				alStr.remove(i);
			}
		}
		
		ral = new ReaderAdjacencyList(alStr,bDirected,bValued);
		graph = ral.getGraph();
		
		generateTabPoints();
	}
	
	  //----------------------------//
	 //---- Getters and Setters ---//
	//----------------------------//
	public Graph getGraph() {
		return graph;
	}
	
	public Point[] getPoints() {
		return tPoints;
	}
	
	public GraphStyle getStyle() {
		return style;
	}
	
	public boolean hasCoord() {
		return bHaveCoord;
	}
	
	/**
	 * Méthode permettant de lire les coordonnées des points du graphe
	 */
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
	
	/**
	 * Méthode permettant de connaitre l'indice de la ligne où figure les coordonnées
	 * @return -1 si la ArrayList ne comporte pas de coordonnées
	 */
	private int getCoordinatesIndice() {
		for (int i = 0; i < alStr.size(); i++) {
			if (alStr.get(i).indexOf("Coordonn") >= 0) {
				return i;
			}
		}
		
		return -1;
	}
	
	/*----- Style -----*/
	/**
	 * Méthode permettant de générer le style associé au graphe
	 */
	private void generateStyle() {
		int iIndiceStyle = getStyleIndice();
		
		style.Personnalise.setEdgeBorder(Color.BLACK);
		style.Personnalise.setEdgeBackground(Color.WHITE);
		style.Personnalise.setEdgeText(Color.BLACK);
		style.Personnalise.setArcLine(Color.GRAY);
		style.Personnalise.setArcText(Color.BLACK);
		style.Personnalise.setBackground(new Color(238,238,238));
		
		if (iIndiceStyle != -1) {
			alStr.remove(iIndiceStyle);
			
			String str = alStr.get(iIndiceStyle);
			
			String[] tStrColor = str.split(";");
			Color edgeBackground = Color.WHITE;
			Color edgeBorder = Color.WHITE;
			Color edgeText = Color.WHITE;
			Color arcLine = Color.WHITE;
			Color arcText = Color.WHITE;
			Color background = Color.WHITE;
			
			for (int i = 0; i < tStrColor.length; i++) {
				String[] tRGB = tStrColor[i].split(",");

				if(i==0)
					edgeBackground = new Color(Integer.parseInt(tRGB[0]),Integer.parseInt(tRGB[1]),Integer.parseInt(tRGB[2]));
				if(i==1)
					edgeBorder = new Color(Integer.parseInt(tRGB[0]),Integer.parseInt(tRGB[1]),Integer.parseInt(tRGB[2]));
				if(i==2)
					edgeText = new Color(Integer.parseInt(tRGB[0]),Integer.parseInt(tRGB[1]),Integer.parseInt(tRGB[2]));
				if(i==3)
					arcLine = new Color(Integer.parseInt(tRGB[0]),Integer.parseInt(tRGB[1]),Integer.parseInt(tRGB[2]));
				if(i==4)
					arcText = new Color(Integer.parseInt(tRGB[0]),Integer.parseInt(tRGB[1]),Integer.parseInt(tRGB[2]));
				if(i==5)
					background = new Color(Integer.parseInt(tRGB[0]),Integer.parseInt(tRGB[1]),Integer.parseInt(tRGB[2]));
			}
			GraphStyle.Personnalise.setEdgeBackground(edgeBackground);
			GraphStyle.Personnalise.setEdgeBorder(edgeBorder);
			GraphStyle.Personnalise.setEdgeText(edgeText);
			GraphStyle.Personnalise.setArcLine(arcLine);
			GraphStyle.Personnalise.setArcText(arcText);
			GraphStyle.Personnalise.setBackground(background);
			style = GraphStyle.Personnalise;
		} else {
			style = GraphStyle.Basique;
		}
	}
	
	/**
	 * Méthode permettant de connaitre l'indice de la ligne où figure le style
	 * @return -1 si la ArrayList ne comporte pas de style
	 */
	private int getStyleIndice() {
		for (int i = 0; i < alStr.size(); i++) {
			if (alStr.get(i).indexOf("Style") >= 0) {
				return i;
			}
		}
		
		return -1;
	}
	/*----------------*/
	
	
	/**
	 * Methode permettant de savoir si le contenu du fichier correspond à une matrice
	 * @param str La première ligne du fichier
	 * @return false si le contenu est une liste d'adjacence
	 */
	private boolean isMatrix(String str) {
		str = str.toLowerCase().replaceAll("ismatrix=", "");

		return (str.toLowerCase().equals("true"));
	}
	
	/**
	 * Méthode permettant de savoir si le graphe est orienté ou non
	 * @param str La deuxième ligne du fichier
	 * @return true si le graphe est orienté
	 */
	private boolean checkDirection(String str) {
		str = str.toLowerCase().replace("directed=", "");
		
		return (str.toLowerCase().equals("true"));
	}
	
	/**
	 * Méthode permettant de savoir si le graphe est valué ou non
	 * @param str La troisième ligne du fichier
	 * @return true si le graphe est valué
	 */
	private boolean checkValue(String str) {
		str = str.toLowerCase().replace("valued=", "");

		return (str.toLowerCase().equals("true"));
	}
}
