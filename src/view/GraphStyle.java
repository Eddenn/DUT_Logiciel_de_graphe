package view;

import java.awt.Color;

public enum GraphStyle {
	//Objets directement construits
	Basique (Color.BLACK,Color.WHITE,Color.BLACK,Color.GRAY,Color.BLACK,new Color(238,238,238)),
	Sombre  (Color.WHITE,Color.BLACK,Color.WHITE,Color.GRAY,Color.WHITE,new Color(0,0,60)),
	Personnalise (Color.BLACK,Color.WHITE,Color.BLACK,Color.GRAY,Color.BLACK,new Color(238,238,238));
	
	//Getters
	public Color getEdgeBackground() {return edgeBackground;}
	public Color getEdgeBorder() {return edgeBorder;}
	public Color getEdgeText() {return edgeText;}
	public Color getArcLine() {return arcLine;}
	public Color getArcText() {return arcText;}
	public Color getBackground() {return background;}

	//Setters
	public void setEdgeBackground(Color edgeBackground) {this.edgeBackground = edgeBackground;}
	public void setEdgeBorder(Color edgeBorder) {this.edgeBorder = edgeBorder;}
	public void setEdgeText(Color edgeText) {this.edgeText = edgeText;}
	public void setArcLine(Color arcLine) {this.arcLine = arcLine;}
	public void setArcText(Color arcText) {this.arcText = arcText;}
	public void setBackground(Color background) {this.background = background;}
	
	//Arguments
	private Color edgeBackground;
	private Color edgeBorder;
	private Color edgeText;
	private Color arcLine;
	private Color arcText;
	private Color background;
	   
	//Constructeur
	private GraphStyle(Color edgeBorder, Color edgeBackground, Color edgeText, Color arcLine, Color arcText, Color background) {
		this.edgeBorder = edgeBorder;
		this.edgeBackground = edgeBackground;
		this.edgeText = edgeText;
		this.arcLine = arcLine;
		this.arcText = arcText;
		this.background = background;
	}
	
	public String toString() {
		return "edgeBackground="+edgeBackground.getRed()+","+edgeBackground.getGreen()+","+edgeBackground.getBlue()+
			   "edgeBorder="+edgeBorder.getRed()+","+edgeBorder.getGreen()+","+edgeBorder.getBlue()+
			   "edgeText="+edgeText.getRed()+","+edgeText.getGreen()+","+edgeText.getBlue()+
			   "arcLine="+arcLine.getRed()+","+arcLine.getGreen()+","+arcLine.getBlue()+
			   "arcText="+arcText.getRed()+","+arcText.getGreen()+","+arcText.getBlue()+
			   "background="+background.getRed()+","+background.getGreen()+","+background.getBlue();
	}
}