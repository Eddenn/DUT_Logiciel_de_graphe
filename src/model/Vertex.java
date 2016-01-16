package model;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Classe permettant de gérer les sommets
 * 
 * @author Groupe 3
 * @version 2016-01-13
 */

public class Vertex {

	private String strName;
	private ArrayList<Arc> alArcs;
	private Color color;

	/**
	 * Constructeur permettant d'instancier un sommet
	 * 
	 * @param strName
	 *            Le nom du sommet
	 */
	public Vertex(String strName) {
		this.alArcs = new ArrayList<Arc>();
		this.strName = strName;
	}

	/**
	 * Constructeur permettant d'instancier un sommet
	 * 
	 * @param strName
	 *            Le nom du sommet
	 * @param strColor
	 *            La couleur du sommet
	 */
	public Vertex(String strName, String strColor) {
		this.alArcs = new ArrayList<Arc>();
		this.strName = strName;
	}

	// Getters and Setters
	public String getName() {
		return this.strName;
	}

	public void setName(String s) {
		this.strName = s;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public ArrayList<Arc> getAlArcs() {
		return this.alArcs;
	}

	// Display
	public String toString() {
		String sRet = this.strName + " : ";
		for (Arc a : alArcs) {
			sRet += a.displayVertex(this) + " ";
		}
		return sRet;
	}

}
