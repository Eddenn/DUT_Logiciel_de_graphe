package model;

import java.util.ArrayList;

import controller.IControlable;

/**
 * Classe permettant de lancer l'algorithme "Lien existe", il permet de v�rifier
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
	 *            le sommet de d�part
	 * @param sommetArrive
	 *            le sommet d'arriv�
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

		// On r�cup�re la matrice et la liste des sommets
		matrice = ctrl.getMatrice();
		lstSommet = ctrl.listeSommet();

		// On d�finit une arraylist de sommets trait�s
		alSommetsTraites = new ArrayList<Integer>();

		// On d�finit un objet ListeFilsDuPere d'arriv� � null et une valeur de
		// chemin � -1
		lfdpArrive = null;
		iValeurChemin = -1;
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

		// On d�finit l'arc actif
		ligArcActif = sommetActif;
		colArcActif = sommetActif;

		// On instancie un nouvel objet ListeFilsDuPere qui contient le
		// sommetActif mais n'a pas de p�re
		ListeFilsDuPere lfdp = new ListeFilsDuPere(sommetActif, null);

		// On lance la m�thode r�currsive
		recur(lfdp);

		// Si on a trouv� un chemin, alors lfdpArrive n'est pas null
		if (lfdpArrive != null) {
			// On d�finit les valeurs du sommet de l'arc actif
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
			// de d�part
			while (lfdp != null) {
				if (lfdp.getPere() != null) {
					// On incr�mente la valeur du chemin
					if (iValeurChemin == -1) {
						iValeurChemin = 0;
					}

					iValeurChemin += matrice[lfdp.getSommet()][lfdp.getPere().getSommet()];
				}

				// On passe au p�re
				lfdp = lfdp.getPere();
			}
		}

		if (iValeurChemin != -1) {
			// On d�finit le message disant qu'on a trouv� un chemin et on
			// indique la valeur du chemin
			message = "Il existe un chemin partant de " + lstSommet[sommetDepart] + " vers " + lstSommet[sommetArrive]
					+ " vallant " + iValeurChemin;
		} else {
			// Nous n'avons pas trouv� de chemin, nous l'indiquons �
			// l'utilisateur
			message = "Il n'y a pas de chemin entre " + lstSommet[sommetDepart] + " et " + lstSommet[sommetArrive]
					+ ".";
		}
		// On met � jour l'IHM
		ctrl.majIHM();
	}

	/**
	 * M�thode recursive permettant de parcourir les fils du sommet courant
	 * 
	 * @param lfdp
	 *            sommet courant
	 * @return vrai si on peut continuer et faux si on a trouv� le chemin ou si
	 *         nous n'avons rien trouv�
	 */
	private void recur(ListeFilsDuPere lfdp) {
		// On met � jour l'IHM
		ctrl.majIHM();
		this.pauseSynchro();

		// On r�cup�re la liste des fils et on les ajoute au sommet actif
		for (Integer integer : getFils(sommetActif)) {
			lfdp.ajouterFils(integer.intValue());
		}

		// Si le sommet actif est �gal au sommet de d�part (on est revenu au
		// d�but) et que tous e
		if (lfdp.getSommet() == sommetDepart && lfdp.filsTousTraites()) {
			// On arr�te l'algorithme
			return;
		} else if (sommetArrive == lfdp.getFilsCourant().getSommet()) {
			// On d�finit lfdpArrive car nous avons trouv� le sommet d'arriv�
			lfdpArrive = lfdp.getFilsCourant();

			return;
		} else {
			// On d�finit le sommet actif ainsi que l'arc actif
			sommetActif = lfdp.getFilsCourant().getSommet();
			ligArcActif = lfdp.getFilsCourant().getSommet();
			colArcActif = lfdp.getSommet();

			// On met � jour l'IHM
			ctrl.majIHM();

			// On r�cup�re le prochain fils du sommet courant
			lfdp = lfdp.getFilsCourantIncremente();
			recur(lfdp);
		}
	}

	/**
	 * Permet d'obtenir les fils d'un sommet mis en param�tre
	 * 
	 * @param iSommet
	 *            sommet courant dont on cherche � avoir le sommet
	 * @return ArrayList<Integer> faisant r�f�rence aux sommets fils du sommet
	 *         pass� en param�tre
	 */
	private ArrayList<Integer> getFils(int iSommet) {
		// Si le sommet est diff�rent du sommet d'arriv�
		if (iSommet != sommetArrive) {
			// alors on l'ajoute � la liste des sommets trait�s
			alSommetsTraites.add(new Integer(iSommet));
		}

		// Instanciation de l'arraylist
		ArrayList<Integer> alInteger = new ArrayList<Integer>();

		// On parcourt la matrice
		for (int i = 0; i < matrice.length; i++) {
			// On instancie un objet Integer
			Integer integer = new Integer(i);

			// S'il le sommet actif � un arc vers le somemt parcouru alors on
			// l'ajoute � la liste des voisins et qu'il n'a pas �t� trait�
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