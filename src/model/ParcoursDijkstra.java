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
	 *            le sommet de départ de l'algorithme
	 */
	public ParcoursDijkstra(IControlable ctrl, int sommetDepart) {
		// On initialise nos variables
		this.ctrl = ctrl;

		this.sommetActif = -1;
		this.ligArcActif = -1;
		this.colArcActif = -1;

		this.sommetDepart = sommetDepart;

		// On récupére la matrice et la liste des sommets
		matrice = ctrl.getMatrice();
		lstSommet = ctrl.listeSommet();

		// On définit un tableau tChemins qui est un tableau d'entiers à deux
		// dimenssions ayant la taille de la matrice
		tChemins = new int[matrice.length][matrice.length];

		// Pour chaque élément, on place -1
		for (int i = 0; i < tChemins.length; i++) {
			for (int j = 0; j < tChemins.length; j++) {
				tChemins[i][j] = -1;
			}
		}

		// On crée un tableau de booléen qui permettra, la valeur false est
		// placée par défaut
		sommetsTraites = new boolean[lstSommet.length];

		// Tableau à deux dimenssions permettant de lier un sommet à un autre
		// suivant une relation fils/pere, le fils est référencé par l'indice du
		// tableau, et le père par la valeur du tableau à cet indice
		filsPere = new int[matrice.length];
	}

	/**
	 * Vérifie si la première lettre du nom du sommet passée en paramètre
	 * correspond à celle du sommet actif
	 * 
	 * @param sommet
	 *            première lettre du nom du sommet
	 * @return vrai si le sommet actif correspond à la première lettre indiquée
	 */
	public boolean sommetActif(char sommet) {
		return sommetActif != -1 && lstSommet[sommetActif] == sommet;
	}

	/**
	 * Vérifie si l'arc passé en paramètre grâce au sommet d'origine et celui de
	 * destination correspond à l'arc courant
	 * 
	 * @param sommetOri
	 *            sommet d'origine de l'arc
	 * @param sommetDest
	 *            sommet de destination de l'arc
	 * 
	 * @return vrai si l'arc correspond aux paramètres et faux si ce n'est pas
	 *         le cas
	 */
	public boolean arcActif(char sommetOri, char sommetDest) {
		return this.colArcActif != -1 && this.ligArcActif != -1 && sommetOri == lstSommet[this.colArcActif]
				&& sommetDest == lstSommet[this.ligArcActif];
	}

	/**
	 * Retourne le message
	 * 
	 * @return chaine de caractères contenant le message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Permet de lancer l'algorithme
	 */
	public void lancer() {
		// On définit le sommetDepart comme le sommetActif
		sommetActif = sommetDepart;

		// On ajoute comme père à sommetActif lui-même
		filsPere[sommetActif] = sommetActif;

		// On indique que le sommetActif est traité
		sommetsTraites[sommetActif] = true;

		// On place la valeur 0 sur toute la colonne du sommet de départ
		for (int i = 0; i < tChemins.length; i++) {
			tChemins[i][sommetActif] = 0;
		}

		// On parcourt le tableau ligne par ligne
		for (int i = 1; i < tChemins.length; i++) {
			// On récupére les voisins du sommet actif
			ArrayList<Integer> alVoisins = getVoisins(sommetActif);

			// On parcourt chaque sommet voisin
			for (int iSommet = 0; iSommet < tChemins.length; iSommet++) {
				// On définit les variables ligArcActif et colArcActif en
				// fonction de la ligne et du sommetActif
				ligArcActif = i;
				colArcActif = sommetActif;

				// On vérifie si la liste des voisins du sommet actif contient
				// le sommet parcouru
				if (alVoisins.contains(iSommet)) {
					// Si le sommet n'a pas été traité
					if (!sommetsTraites[iSommet]) {
						// On vérifie qu'il n'y a aucun chemin existant
						if (tChemins[i - 1][iSommet] == -1) {
							// S'il n'y en a pas, on lui ajoute une valeur
							tChemins[i][iSommet] = getValeurArc(sommetActif, iSommet) + tChemins[i - 1][sommetActif];

							// On ajoute le sommet actif comme père du sommet
							// parcouru
							filsPere[iSommet] = sommetActif;

							// On met à jour l'IHM et on fait une pause
							ctrl.majIHM();
							this.pauseSynchro();
						} else {
							// S'il y a un chemin existant, on vérifie que la
							// nouvelle valeur est inférieure à l'ancienne
							if (tChemins[i - 1][sommetActif]
									+ getValeurArc(sommetActif, iSommet) < tChemins[i - 1][iSommet]) {
								// Si oui, on remplace par la nouvelle valeur
								tChemins[i][iSommet] = tChemins[i - 1][sommetActif]
										+ getValeurArc(sommetActif, iSommet);

								// On ajoute le sommet actif comme père du
								// sommet parcouru
								filsPere[iSommet] = sommetActif;

								// On met à jour l'IHM et on fait une pause
								ctrl.majIHM();
								this.pauseSynchro();

							} else {
								// Si c'est plus grand, alors on récupère
								// l'ancienne valeur
								tChemins[i][iSommet] = tChemins[i - 1][iSommet];

								// On met à jour l'IHM
								ctrl.majIHM();
							}
						}
					}
				} else {
					// S'il n'est pas dans la liste des voisins et qu'il a déjà
					// eu une valeur
					if (tChemins[i - 1][iSommet] != -1) {
						// dans ce cas, on réapplique l'ancienne valeur à la
						// ligne courante
						tChemins[i][iSommet] = tChemins[i - 1][iSommet];
					}
				}
			}

			// On va à présent déterminer le sommet actif, ça doit être celui
			// avec la plus petite valeur
			int min = -1;

			// On parcourt le tableau des chemins
			for (int j = 0; j < tChemins.length; j++) {
				// Si le sommet n'est pas traité et que le chemin est inférieur
				// au minimum
				if ((min == -1 && !sommetsTraites[j] && tChemins[i][j] != -1)
						|| (tChemins[i][j] < min && tChemins[i][j] != -1 && !sommetsTraites[j])) {

					// Le sommet parcouru devient le sommet actif et on remplace
					// le minimum par la valeur du sommet
					sommetActif = j;
					min = tChemins[i][j];
				}
			}

			// On indique que le sommet actif est traité
			sommetsTraites[sommetActif] = true;

			// On place la valeur du sommet actif sur les lignes restantes
			for (int iRow = i; iRow < tChemins.length; iRow++) {
				tChemins[iRow][sommetActif] = tChemins[i][sommetActif];
			}
		}

		// Le programme est fini, on remet à -1 les valeurs pour le sommet actif
		// et pour l'arc actif (affichage)
		sommetActif = -1;
		ligArcActif = -1;
		colArcActif = -1;
	}

	/**
	 * Retourne le tableau des chemins
	 * 
	 * @return tableau d'entiers à deux dimenssions contenant les chemins vers
	 *         les sommets en partant du sommet de départ
	 */
	public int[][] getTChemins() {
		return tChemins;
	}

	/**
	 * Permet d'effectuer une pause dans l'algorithme
	 */
	private void pauseSynchro() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
	}

	/**
	 * Permet d'obtenir les voisins d'un sommet passé en paramètre
	 * 
	 * @param iSommet
	 *            sommet actif dont on cherche à avoir les sommets
	 * @return ArrayList<Integer> de sommets qui sont les voisins du sommet
	 *         passé en paramètre
	 */
	private ArrayList<Integer> getVoisins(int iSommet) {
		// On instancie l'ArrayList
		ArrayList<Integer> alVoisins = new ArrayList<Integer>();

		// On parcourt la matrice
		for (int i = 0; i < matrice.length; i++) {
			// On instancie un objet Integer
			Integer integer = new Integer(i);

			// S'il le sommet actif à un arc vers le somemt parcouru alors on
			// l'ajoute à la liste des voisins
			if (matrice[i][iSommet] != -1) {
				alVoisins.add(integer);
			}
		}

		// On retourne la liste des voisins
		return alVoisins;
	}

	/**
	 * Permet d'obtenir la valeur d'un arc allant du sommetA vers le sommetB
	 * 
	 * @param sommetA
	 *            sommet de départ de l'arc
	 * @param sommetB
	 *            sommet d'arrivé de l'arc
	 * @return valeur de l'arc
	 */
	private int getValeurArc(int sommetA, int sommetB) {
		return matrice[sommetB][sommetA];
	}

}
