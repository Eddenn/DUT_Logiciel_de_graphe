package model;

import java.util.ArrayList;

public class ListeFilsDuPere {
	private int iSommet;
	private ListeFilsDuPere listePere;
	private ArrayList<ListeFilsDuPere> alFils;
	private int iCpt;
	
	public ListeFilsDuPere(int iSommet, ListeFilsDuPere listePere) {
		this.iSommet = iSommet;

		this.listePere = listePere;
		
		alFils = new ArrayList<ListeFilsDuPere>();
		
		iCpt = 0;
	}

	public int getSommet() {
		return iSommet;
	}
	public ListeFilsDuPere getPere() {
		return listePere;
	}
	
	public void ajouterFils(int i) {
		alFils.add(new ListeFilsDuPere(i, this));
	}
	
	public void incrementeFilsTraite() {
		iCpt++;
	}
	
	public int getNbFils() {
		return alFils.size();
	}

	public ListeFilsDuPere getFilsCourantIncremente() {
		if (alFils.size() == 0 || filsTousTraites()) {
			return listePere;
		}

		return alFils.get(iCpt++);
	}
	public ListeFilsDuPere getFilsCourant() {
		if (alFils.size() == 0 || filsTousTraites()) {
			return listePere;
		}
		
		return alFils.get(iCpt);
	}
	
	public boolean filsTousTraites() {
		return iCpt == alFils.size();
	}
	
	public ArrayList<ListeFilsDuPere> getAlFils() {
		return alFils;
	}
	
	public int getCpt() {
		return iCpt;
	}
	
	public String toString() {
		return iSommet + "\n";
	}
}