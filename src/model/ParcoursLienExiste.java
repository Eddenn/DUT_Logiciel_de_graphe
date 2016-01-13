package model;

import java.util.ArrayList;

import controller.IControlable;

/**
 * Classe permettant de lancer l'algorithme "Lien existe", il permet de vérifier
 * si un chemin existe entre deux sommets
 * 
 * @author Groupe 3
 * @version 2016-01-13
 *
 */
public class ParcoursLienExiste implements IParcourable {
	private IControlable ctrl;

	private int sommetActif;

	private int sommetDepart;
	private int sommetArrive;

	private int ligArcActif;
	private int colArcActif;

	private String message;

	private int[][] matrice;
	private char[] lstSommet;

	private ArrayList<Integer> alSommetsTraites;

	private ListeFilsDuPere lfdpArrive;

	private int iValeurChemin;

	/**
	 * Constructeur de la classe ParcoursLienExiste
	 * 
	 * @param ctrl
	 *            instance du controleur
	 * @param sommetDepart
	 *            le sommet de départ
	 * @param sommetArrive
	 *            le sommet d'arrivé
	 */
	public ParcoursLienExiste(IControlable ctrl, int sommetDepart, int sommetArrive) {
		// On initialise nos variables
		this.ctrl = ctrl;

		this.sommetDepart = sommetDepart;
		this.sommetArrive = sommetArrive;

		this.sommetActif = -1;
		this.ligArcActif = -1;
		this.colArcActif = -1;

		this.message = "";

		// On récupére la matrice et la liste des sommets
		matrice = ctrl.getMatrice();
		lstSommet = ctrl.listeSommet();

		// On définit une arraylist de sommets traités
		alSommetsTraites = new ArrayList<Integer>();

		// On définit un objet ListeFilsDuPere d'arrivé à null et une valeur de
		// chemin à -1
		lfdpArrive = null;
		iValeurChemin = -1;
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

		// On définit l'arc actif
		ligArcActif = sommetActif;
		colArcActif = sommetActif;

		// On instancie un nouvel objet ListeFilsDuPere qui contient le
		// sommetActif mais n'a pas de père
		ListeFilsDuPere lfdp = new ListeFilsDuPere(sommetActif, null);

		// On lance la méthode récurrsive
		recur(lfdp);

		// Si on a trouvé un chemin, alors lfdpArrive n'est pas null
		if (lfdpArrive != null) {
			// On définit les valeurs du sommet de l'arc actif
			sommetActif = lfdpArrive.getSommet();
			ligArcActif = lfdpArrive.getSommet();

			if (lfdpArrive.getPere() != null) {
				colArcActif = lfdpArrive.getPere().getSommet();
			} else {
				colArcActif = lfdpArrive.getSommet();
			}

			// lfdp prend la valeur de lfdpArrive
			lfdp = lfdpArrive;

			// On parcourt dans le sens inverse lfdp pour aller jusqu'au sommet
			// de départ
			while (lfdp != null) {
				if (lfdp.getPere() != null) {
					// On incrémente la valeur du chemin
					if (iValeurChemin == -1) {
						iValeurChemin = 0;
					}

					iValeurChemin += matrice[lfdp.getSommet()][lfdp.getPere().getSommet()];
				}

				// On passe au père
				lfdp = lfdp.getPere();
			}
		}

		if (iValeurChemin != -1) {
			// On définit le message disant qu'on a trouvé un chemin et on
			// indique la valeur du chemin
			message = "Il existe un chemin partant de " + lstSommet[sommetDepart] + " vers " + lstSommet[sommetArrive]
					+ " vallant " + iValeurChemin;
		} else {
			// Nous n'avons pas trouvé de chemin, nous l'indiquons à
			// l'utilisateur
			message = "Il n'y a pas de chemin entre " + lstSommet[sommetDepart] + " et " + lstSommet[sommetArrive]
					+ ".";
		}
		// On met à jour l'IHM
		ctrl.majIHM();
	}

	/**
	 * Méthode recursive permettant de parcourir les fils du sommet courant
	 * 
	 * @param lfdp
	 *            sommet courant
	 * @return vrai si on peut continuer et faux si on a trouvé le chemin ou si
	 *         nous n'avons rien trouvé
	 */
	private void recur(ListeFilsDuPere lfdp) {
		// On met à jour l'IHM
		ctrl.majIHM();
		this.pauseSynchro();

		// On récupère la liste des fils et on les ajoute au sommet actif
		for (Integer integer : getFils(sommetActif)) {
			lfdp.ajouterFils(integer.intValue());
		}

		// Si le sommet actif est égal au sommet de départ (on est revenu au
		// début) et que tous e
		if (lfdp.getSommet() == sommetDepart && lfdp.filsTousTraites()) {
			// On arrête l'algorithme
			return;
		} else if (sommetArrive == lfdp.getFilsCourant().getSommet()) {
			// On définit lfdpArrive car nous avons trouvé le sommet d'arrivé
			lfdpArrive = lfdp.getFilsCourant();

			return;
		} else {
			// On définit le sommet actif ainsi que l'arc actif
			sommetActif = lfdp.getFilsCourant().getSommet();
			ligArcActif = lfdp.getFilsCourant().getSommet();
			colArcActif = lfdp.getSommet();

			// On met à jour l'IHM
			ctrl.majIHM();

			// On récupère le prochain fils du sommet courant
			lfdp = lfdp.getFilsCourantIncremente();
			recur(lfdp);
		}
	}

	/**
	 * Permet d'obtenir les fils d'un sommet mis en paramètre
	 * 
	 * @param iSommet
	 *            sommet courant dont on cherche à avoir le sommet
	 * @return ArrayList<Integer> faisant référence aux sommets fils du sommet
	 *         passé en paramètre
	 */
	private ArrayList<Integer> getFils(int iSommet) {
		// Si le sommet est différent du sommet d'arrivé
		if (iSommet != sommetArrive) {
			// alors on l'ajoute à la liste des sommets traités
			alSommetsTraites.add(new Integer(iSommet));
		}

		// Instanciation de l'arraylist
		ArrayList<Integer> alInteger = new ArrayList<Integer>();

		// On parcourt la matrice
		for (int i = 0; i < matrice.length; i++) {
			// On instancie un objet Integer
			Integer integer = new Integer(i);

			// S'il le sommet actif à un arc vers le somemt parcouru alors on
			// l'ajoute à la liste des voisins et qu'il n'a pas été traité
			if (matrice[i][iSommet] != -1 && !alSommetsTraites.contains(integer)) {
				alInteger.add(integer);
			}
		}

		// On retourne la liste des fils
		return alInteger;
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
}