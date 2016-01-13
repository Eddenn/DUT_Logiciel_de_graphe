package model;

import java.util.ArrayList;

/**
 * Classe permet de lier un sommet � son p�re ainsi qu'� ses fils
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
	 * Cr�e un sommet auquel on ajoute son p�re
	 * 
	 * @param iSommet
	 *            Le sommet courant
	 * @param listePere
	 *            instance du p�re
	 */
	public ListeFilsDuPere(int iSommet, ListeFilsDuPere listePere) {
		// On d�finit la valeur du sommet
		this.iSommet = iSommet;

		// Le p�re est initialis�
		this.listePere = listePere;

		// On initialise la liste des fils
		alFils = new ArrayList<ListeFilsDuPere>();

		// Compteur permettant de savoir combien de fils ont �t� trait�s
		iCpt = 0;
	}

	/**
	 * Permet d'obtenir le num�ro du sommet
	 * 
	 * @return num�ro du sommet
	 */
	public int getSommet() {
		return iSommet;
	}

	/**
	 * Permet d'obtenir l'instance du p�re
	 * 
	 * @return instance ListeFilsDuPere du p�re du sommet
	 */
	public ListeFilsDuPere getPere() {
		return listePere;
	}

	/**
	 * Permet d'ajouter un fils � la liste
	 * 
	 * @param i
	 *            num�ro du sommet du fils
	 */
	public void ajouterFils(int i) {
		alFils.add(new ListeFilsDuPere(i, this));
	}

	/**
	 * Permet d'obtenir le fils courant ou le p�re si on les a tous trait�s. Si
	 * on retourne un fils, on incr�mente le compteur de fils trait�s.
	 * 
	 * @return instance du fils courant ou le p�re s'ils ont tous �t� trait�s
	 */
	public ListeFilsDuPere getFilsCourantIncremente() {
		// On v�rifie s'il n'a pas de fils ou s'ils ont tous �t� trait�s
		if (alFils.size() == 0 || filsTousTraites()) {
			// Si oui, on retourne le p�re
			return listePere;
		}

		// sinon, on retourne le fils courant et on inr�mente le compteur de
		// fils trait�s
		return alFils.get(iCpt++);
	}

	/**
	 * Permet d'obtenir le fils courant ou le p�re s'ils ont tous �t� trait�s
	 * 
	 * @return instance du fils courant ou du p�re
	 */
	public ListeFilsDuPere getFilsCourant() {
		// On v�rifie s'il n'a pas de fils ou s'ils ont tous �t� trait�s
		if (alFils.size() == 0 || filsTousTraites()) {
			// Si oui, on retourne le p�re
			return listePere;
		}

		// sinon, on retourne le fils courant
		return alFils.get(iCpt);
	}

	/**
	 * V�rifie si tous les fils ot �t� trait�s
	 * 
	 * @return vrai si tous les fils sont trait�s, faux s'il en reste encore �
	 *         traiter
	 */
	public boolean filsTousTraites() {
		return iCpt == alFils.size();
	}
}