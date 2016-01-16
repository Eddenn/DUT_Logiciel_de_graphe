package model;

public interface IParcourable {
	public boolean sommetActif(char sommet);

	public boolean arcActif(char sommetOri, char sommetDest);

	public String getMessage();

	public void lancer();

}