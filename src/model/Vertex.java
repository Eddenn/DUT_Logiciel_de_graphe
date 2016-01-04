package model;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Classe permettant de gérer les sommets
 * 
 * @author Les grosses bites
 * 
 */

public class Vertex {

	private String strName;
	private ArrayList<Arc> alArcs;
	private Color color;
	private Graph graph;

	public Vertex(String strName, Graph graph) {
		this.alArcs = new ArrayList<Arc>();
		this.strName = strName;
		this.graph = graph;
	}

	public Vertex(String strName, String strColor, Graph graph) {
		this.alArcs = new ArrayList<Arc>();
		this.strName = strName;
		this.graph = graph;
	}
	
	
	

	// Getters and Setters
	public String getName() {
		return this.strName;
	}
	public void setName(String s) {
		this.strName = s;
	}

	public ArrayList<Arc> getAlArcs() {
		return this.alArcs;
	}

	// Display
	public String toString() {
		String sRet = this.strName + " : ";
		for (Arc a : alArcs) {
			sRet += a.displayVertex(this) +  " ";
		}
		return sRet;
	}
}
