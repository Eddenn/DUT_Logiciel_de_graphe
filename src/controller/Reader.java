package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import model.Graph;

/**
 * Classe qui va déterminer si un fichier texte contient une matrice ou une liste d'adjacence et retourner le graphe correspondant.
 * @author Groupe 3
 * @version 2016-01-08
 */
public abstract class Reader {
	
	/**
	 * Méthode qui va lire le fichier passé en paramètre et extraire un graphe.
	 * @param strFile le nom du fichier à lire
	 * @return le graphe correspondant à la matrice ou la liste d'adjacence contenu dans le fichier
	 */
	public static Graph read(String strFile) {
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(strFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Scanner sc = new Scanner(br);
		
		ArrayList<String> alStr = new ArrayList<String>(); //alStr va contenir les différentes lignes du fichier
		
		while (sc.hasNextLine()) {
			alStr.add(sc.nextLine());
		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String strFirstLine = alStr.get(0);	//La première ligne détermine s'il s'agit d'une matrice ou d'une liste d'adjacent
		alStr.remove(0);
		
		if (isMatrix(strFirstLine)) {
			return ReaderMatrix.readMatrix(alStr);
		}
				
		return ReaderAdjacencyList.ReadAdjacencyList(alStr);
	}
	
	/**
	 * Méthode qui détermine si le fichier lu contient une matrice.
	 * @param str Chaine de caractère correspondant à la première ligne du fichier lu.
	 * @return true s'il s'agit d'une matrice, false sinon.
	 */
	private static boolean isMatrix(String str) {
		str = str.toLowerCase().replaceAll("ismatrix=", "");

		return (str.toLowerCase().equals("true"));
	}
	
	/**
	 * Méthode qui détermine si le graphe contenu dans le fichier est orienté.
	 * @param str Chaine de caractère correspondant à la deuxième ligne du fichier lu.
	 * @return true s'il le graphe est orienté, false sinon.
	 */
	protected static boolean checkDirection(String str) {
		str = str.toLowerCase().replace("directed=", "");
		
		return (str.toLowerCase().equals("true"));
	}

	/**
	 * Méthode qui détermine si le graphe contenu dans le fichier est valué.
	 * @param str Chaine de caractère correspondant à la troisième ligne du fichier lu.
	 * @return true s'il le graphe est valué, false sinon.
	 */
	protected static boolean checkValue(String str) {
		str = str.toLowerCase().replace("valued=", "");

		return (str.toLowerCase().equals("true"));
	}
}
