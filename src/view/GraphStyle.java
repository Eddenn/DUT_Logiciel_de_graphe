package view;

import java.awt.Color;

public enum GraphStyle {
	//Objets directement construits
	BASIC (new Color(0,0,0)      ,new Color(255,255,255),new Color(0,0,0)      ),
	DARK  (new Color(255,255,255),new Color(0,0,0)      ,new Color(255,255,255));
	   
	//Getters
	public Color getEdgeBackground() {return edgeBackground;}
	public Color getEdgeBorder() {return edgeBorder;}
	public Color getEdgeText() {return edgeText;}
	public Color getArcLine() {return arcLine;}
	public Color getArcText() {return arcText;}

	//Arguments
	private Color edgeBackground;
	private Color edgeBorder;
	private Color edgeText;
	private Color arcLine;
	private Color arcText;
	   
	private GraphStyle(Color edgeBorder,Color edgeBackground,Color edgeText) {
		this.edgeBorder = edgeBorder;
		this.edgeBackground = edgeBackground;
		this.edgeText = edgeText;
	}
}