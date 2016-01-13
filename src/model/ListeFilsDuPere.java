package model;

import java.util.ArrayList;

/**
 * Classe permet de lier un sommet à son père ainsi qu'à ses fils
 * 
 * @author Groupe 3
 * @version 2016-01-13
 */
public class ListeFilsDuPere {
	private int iSommet;
	private ListeFilsDuPere listePere;
	private ArrayList<ListeFilsDuPere> alFils;
	private int iCpt;

	/**
	 * Crée un sommet auquel on ajoute son père
	 * 
	 * @param iSommet
	 *            Le sommet courant
	 * @param listePere
	 *            instance du père
	 */
	public ListeFilsDuPere(int iSommet, ListeFilsDuPere listePere) {
		// On définit la valeur du sommet
		this.iSommet = iSommet;

		// Le père est initialisé
		this.listePere = listePere;

		// On initialise la liste des fils
		alFils = new ArrayList<ListeFilsDuPere>();

		// Compteur permettant de savoir combien de fils ont été traités
		iCpt = 0;
	}

	/**
	 * Permet d'obtenir le numéro du sommet
	 * 
	 * @return numéro du sommet
	 */
	public int getSommet() {
		return iSommet;
	}

	/**
	 * Permet d'obtenir l'instance du père
	 * 
	 * @return instance ListeFilsDuPere du père du sommet
	 */
	public ListeFilsDuPere getPere() {
		return listePere;
	}

	/**
	 * Permet d'ajouter un fils à la liste
	 * 
	 * @param i
	 *            numéro du sommet du fils
	 */
	public void ajouterFils(int i) {
		alFils.add(new ListeFilsDuPere(i, this));
	}

	/**
	 * Permet d'obtenir le fils courant ou le père si on les a tous traités. Si
	 * on retourne un fils, on incrémente le compteur de fils traités.
	 * 
	 * @return instance du fils courant ou le père s'ils ont tous été traités
	 */
	public ListeFilsDuPere getFilsCourantIncremente() {
		// On vérifie s'il n'a pas de fils ou s'ils ont tous été traités
		if (alFils.size() == 0 || filsTousTraites()) {
			// Si oui, on retourne le père
			return listePere;
		}

		// sinon, on retourne le fils courant et on inrémente le compteur de
		// fils traités
		return alFils.get(iCpt++);
	}

	/**
	 * Permet d'obtenir le fils courant ou le père s'ils ont tous été traités
	 * 
	 * @return instance du fils courant ou du père
	 */
	public ListeFilsDuPere getFilsCourant() {
		// On vérifie s'il n'a pas de fils ou s'ils ont tous été traités
		if (alFils.size() == 0 || filsTousTraites()) {
			// Si oui, on retourne le père
			return listePere;
		}

		// sinon, on retourne le fils courant
		return alFils.get(iCpt);
	}

	/**
	 * Vérifie si tous les fils ot été traités
	 * 
	 * @return vrai si tous les fils sont traités, faux s'il en reste encore à
	 *         traiter
	 */
	public boolean filsTousTraites() {
		return iCpt == alFils.size();
	}
}