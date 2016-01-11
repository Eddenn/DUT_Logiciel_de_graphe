package model;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Classe permettant de gérer les sommets
 * @author Groupe 3
 * @version 2016-01-11
 */
public class Vertex {

	private String strName;
	private ArrayList<Arc> alArcs;
	@SuppressWarnings("unused")
	private Color color;
	@SuppressWarnings("unused")
	private Graph graph;

	/**
	 * Constructeur qui instancie un sommet 
	 * @param strName nom du sommet
	 * @param graph graphe associé
	 */
	public Vertex(String strName, Graph graph) {
		this.alArcs = new ArrayList<Arc>();
		this.strName = strName;
		this.graph = graph;
	}

	/**
	 * Constructeur qui instancie un sommet
	 * @param strName nom du sommet
	 * @param strColor couleur du sommet
	 * @param graph graphe associé
	 */
	public Vertex(String strName, String strColor, Graph graph) {
		this.alArcs = new ArrayList<Arc>();
		this.strName = strName;
		this.graph = graph;
	}

	/*---------------------
	 * Getters et Setters
	 *--------------------*/
	public String getName() {
		return this.strName;
	}
	public void setName(String s) {
		this.strName = s;
	}

	public ArrayList<Arc> getAlArcs() {
		return this.alArcs;
	}

	/*------------
	 * Affichage
	 *-----------*/
	public String toString() {
		String sRet = this.strName + " : ";
		for (Arc a : alArcs) {
			sRet += a.displayVertex(this) +  " ";
		}
		return sRet;
	}
}
