package model;

import java.util.ArrayList;

import controller.IControlable;

/**
 * Classe permettant de lancer l'algorithme de Dijkstra sur la matrice
 * 
 * @author Groupe 3
 * @version 2016-01-13
 *
 */
public class ParcoursDijkstra implements IParcourable {
	private IControlable ctrl;

	private int sommetActif;
	private int sommetDepart;

	private int ligArcActif;
	private int colArcActif;

	private int[][] matrice;
	private char[] lstSommet;

	private int[][] tChemins;
	private boolean[] sommetsTraites;
	private int[] filsPere;

	private String message;

	/**
	 * Contructeur de la classe ParcoursDijkstra
	 * 
	 * @param ctrl
	 *            instance du controleur
	 * @param sommetDepart
	 *            le sommet de d�part de l'algorithme
	 */
	public ParcoursDijkstra(IControlable ctrl, int sommetDepart) {
		// On initialise nos variables
		this.ctrl = ctrl;

		this.sommetActif = -1;
		this.ligArcActif = -1;
		this.colArcActif = -1;

		this.sommetDepart = sommetDepart;

		// On r�cup�re la matrice et la liste des sommets
		matrice = ctrl.getMatrice();
		lstSommet = ctrl.listeSommet();

		// On d�finit un tableau tChemins qui est un tableau d'entiers � deux
		// dimenssions ayant la taille de la matrice
		tChemins = new int[matrice.length][matrice.length];

		// Pour chaque �l�ment, on place -1
		for (int i = 0; i < tChemins.length; i++) {
			for (int j = 0; j < tChemins.length; j++) {
				tChemins[i][j] = -1;
			}
		}

		// On cr�e un tableau de bool�en qui permettra, la valeur false est
		// plac�e par d�faut
		sommetsTraites = new boolean[lstSommet.length];

		// Tableau � deux dimenssions permettant de lier un sommet � un autre
		// suivant une relation fils/pere, le fils est r�f�renc� par l'indice du
		// tableau, et le p�re par la valeur du tableau � cet indice
		filsPere = new int[matrice.length];
	}

	/**
	 * V�rifie si la premi�re lettre du nom du sommet pass�e en param�tre
	 * correspond � celle du sommet actif
	 * 
	 * @param sommet
	 *            premi�re lettre du nom du sommet
	 * @return vrai si le sommet actif correspond � la premi�re lettre indiqu�e
	 */
	public boolean sommetActif(char sommet) {
		return sommetActif != -1 && lstSommet[sommetActif] == sommet;
	}

	/**
	 * V�rifie si l'arc pass� en param�tre gr�ce au sommet d'origine et celui de
	 * destination correspond � l'arc courant
	 * 
	 * @param sommetOri
	 *            sommet d'origine de l'arc
	 * @param sommetDest
	 *            sommet de destination de l'arc
	 * 
	 * @return vrai si l'arc correspond aux param�tres et faux si ce n'est pas
	 *         le cas
	 */
	public boolean arcActif(char sommetOri, char sommetDest) {
		return this.colArcActif != -1 && this.ligArcActif != -1 && sommetOri == lstSommet[this.colArcActif]
				&& sommetDest == lstSommet[this.ligArcActif];
	}

	/**
	 * Retourne le message
	 * 
	 * @return chaine de caract�res contenant le message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Permet de lancer l'algorithme
	 */
	public void lancer() {
		// On d�finit le sommetDepart comme le sommetActif
		sommetActif = sommetDepart;

		// On ajoute comme p�re � sommetActif lui-m�me
		filsPere[sommetActif] = sommetActif;

		// On indique que le sommetActif est trait�
		sommetsTraites[sommetActif] = true;

		// On place la valeur 0 sur toute la colonne du sommet de d�part
		for (int i = 0; i < tChemins.length; i++) {
			tChemins[i][sommetActif] = 0;
		}

		// On parcourt le tableau ligne par ligne
		for (int i = 1; i < tChemins.length; i++) {
			// On r�cup�re les voisins du sommet actif
			ArrayList<Integer> alVoisins = getVoisins(sommetActif);

			// On parcourt chaque sommet voisin
			for (int iSommet = 0; iSommet < tChemins.length; iSommet++) {
				// On d�finit les variables ligArcActif et colArcActif en
				// fonction de la ligne et du sommetActif
				ligArcActif = i;
				colArcActif = sommetActif;

				// On v�rifie si la liste des voisins du sommet actif contient
				// le sommet parcouru
				if (alVoisins.contains(iSommet)) {
					// Si le sommet n'a pas �t� trait�
					if (!sommetsTraites[iSommet]) {
						// On v�rifie qu'il n'y a aucun chemin existant
						if (tChemins[i - 1][iSommet] == -1) {
							// S'il n'y en a pas, on lui ajoute une valeur
							tChemins[i][iSommet] = getValeurArc(sommetActif, iSommet) + tChemins[i - 1][sommetActif];

							// On ajoute le sommet actif comme p�re du sommet parcouru
							filsPere[iSommet] = sommetActif;
							
							// On met � jour l'IHM et on fait une pause
							ctrl.majIHM();
							this.pauseSynchro();
						} else {
							// S'il y a un chemin existant, on v�rifie que la nouvelle valeur est inf�rieure � l'ancienne
							if (tChemins[i - 1][sommetActif]
									+ getValeurArc(sommetActif, iSommet) < tChemins[i - 1][iSommet]) {
								// Si oui, on remplace par la nouvelle valeur
								tChemins[i][iSommet] = tChemins[i - 1][sommetActif]
										+ getValeurArc(sommetActif, iSommet);

								// On ajoute le sommet actif comme p�re du sommet parcouru
								filsPere[iSommet] = sommetActif;
								
								// On met � jour l'IHM et on fait une pause
								ctrl.majIHM();
								this.pauseSynchro();

							} else {
								// Si c'est plus grand, alors on r�cup�re l'ancienne valeur
								tChemins[i][iSommet] = tChemins[i - 1][iSommet];
								
								// On met � jour l'IHM
								ctrl.majIHM();
							}
						}
					}
				} else {
					// 
					
					if (tChemins[i - 1][iSommet] != -1) {
						tChemins[i][iSommet] = tChemins[i - 1][iSommet];
					}
				}
			}

			// On d�termine le nouveau sommet actif
			int min = -1;

			for (int j = 0; j < tChemins.length; j++) {
				if ((min == -1 && !sommetsTraites[j] && tChemins[i][j] != -1)
						|| (tChemins[i][j] < min && tChemins[i][j] != -1 && !sommetsTraites[j])) {
					sommetActif = j;
					min = tChemins[i][j];
				}
			}

			sommetsTraites[sommetActif] = true;

			for (int iRow = i; iRow < tChemins.length; iRow++) {
				tChemins[iRow][sommetActif] = tChemins[i][sommetActif];
			}
		}

		sommetActif = -1;
		ligArcActif = -1;
		colArcActif = -1;
	}

	public int[][] getTChemins() {
		return tChemins;
	}

	private void pauseSynchro() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
	}

	private ArrayList<Integer> getVoisins(int iSommet) {
		ArrayList<Integer> alVoisins = new ArrayList<Integer>();

		for (int i = 0; i < matrice.length; i++) {
			Integer integer = new Integer(i);

			if (matrice[i][iSommet] != -1) {
				alVoisins.add(integer);
			}
		}

		return alVoisins;
	}

	private int getValeurArc(int sommetA, int sommetB) {
		return matrice[sommetB][sommetA];
	}

}
