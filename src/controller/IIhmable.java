package controller;

public interface IIhmable {
	public int getNbSommet();

	public int getNbArc(int indSommet);

	public char getNomSommet(int indSommet);

	public int getValArc(int indSommetOri, int indArc);

	public char getNomSommetArc(int indSommetOri, int indArc);

	public boolean sommetActif(char sommet);

	public boolean arcActif(char sommetOri, char sommetDest);

	public String getMessage();

}