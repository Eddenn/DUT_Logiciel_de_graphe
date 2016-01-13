package controller;

import java.awt.Desktop;
import java.awt.Menu;
import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.IControlable;
import controller.IIhmable;
import model.Arc;
import model.Graph;
import model.IParcourable;
import model.Parcours;
import model.ParcoursDijkstra;
import model.ParcoursLienExiste;
import model.Vertex;
import view.GraphStyle;
import view.HCI;
import view.StartFrame;

/**
 * Classe qui fait le lien entre l'IHM et le métier et qui vérifie les actions
 * effectuées
 * 
 * @author Groupe 3
 * @version 2016-01-08
 */
public class Controller implements IControlable, IIhmable {
	private HCI hci;
	private Graph graph;
	private ArrayList<ArrayList<String>> saveGraphList;
	private int cptModif;
	private static String file = "";

	private IParcourable parcours;

	public static void main(String[] args) {
		new Controller();
	}

	/**
	 * Constructeur qui instancie un graphe et l'ihm
	 */
	public Controller() {
		// Création du graphe
		graph = new Graph(true, true);

		// Initialisation de la frame
		hci = new HCI(this);

		new StartFrame(this, hci);

		// Initialisation de l'arrayList qui permet d'implémenter les fonctions
		// annuler et rétablir
		initProvSave();
	}

	/*-------------------
	 * Gestion du graphe 
	 * -----------------*/
	/**
	 * Méthode qui crée un nouveau graphe vide avec les paramêtres choisis par
	 * l'utilisateur.
	 * 
	 * @param bOriented
	 *            true s'il est orienté, false sinon.
	 * @param bValued
	 *            true s'il est valué, false sinon.
	 */
	public void newGraph(boolean bOriented, boolean bValued) {
		graph = new Graph(bOriented, bValued);
		initProvSave();
		hci.setGraph(graph);
		hci.permitModifArc(graph.isValued());
		hci.initHmVertex();
		hci.refresh();
		hci.setInfo();
	}

	/**
	 * Méthode permettant de sauvegarder le graphe dans un fichier texte
	 * 
	 * @param strFileName
	 *            le chemin où le fichier doit être enregistré, si null, on
	 *            reprend le chemin sauvegardé pour écraser l'ancienne
	 *            sauvegarde
	 */
	public void saveFile(String strFileName, String format) {
		// Si strFileName contient quelque chose, il s'agit d'enregistrer sous
		// sinon il s'agit d'enregistrer
		if (!strFileName.equals("")) {
			file = strFileName;
		}
		FileWriter fw = null;

		try {
			// ouverture du fichier en mode écriture
			fw = new FileWriter(file, false);

			// écriture des lignes de texte

			if (format.equals("matrice")) {
				fw.write("IsMatrix=true\n");

				fw.write("Directed=" + graph.isDirected() + "\n");
				fw.write("Valued=" + graph.isValued() + "\n\n");
				fw.write("-- Matrice :\n");
				fw.write(graph.displayMatrix() + "\n");
			} else {
				fw.write("IsMatrix=false\n");

				fw.write("Directed=" + graph.isDirected() + "\n");
				fw.write("Valued=" + graph.isValued() + "\n\n");
				fw.write("-- Liste d'adjacence :\n");
				fw.write(graph.getFormattedList() + "\n");
			}
			fw.write("-- Coordonnées des points :\n");
			fw.write("[");

			Point[] tabPoint = new Point[graph.getAlVertex().size()];
			int cpt = 0;
			for (Point c : hci.getHmVertex().values()) {
				tabPoint[cpt] = c;
				cpt++;
			}

			int size = tabPoint.length;
			for (int i = 0; i < size; i++) {
				fw.write(tabPoint[i].getX() + "," + tabPoint[i].getY());
				if (i != size - 1)
					fw.write(";");
			}
			fw.write("]\n");
			fw.write("-- Style :\n");
			fw.write(hci.getGraphPanel().getStyle().toStringSave());

			// fermeture du fichier
			fw.close();

			// Remise à zero des sauvegardes provisoires
			initProvSave();

		} catch (IOException e) {
			hci.showError("Problème d'enregistrement du fichier " + file + ".");
		}
	}

	/**
	 * Méthode permettant de charger un fichier texte
	 * 
	 * @param strFileName
	 *            le chemin du fichier à charger
	 * @throws Exception
	 */
	public void loadFile(String strFileName) throws Exception {

		if (strFileName.indexOf(".txt") < 0) {
			hci.afficherMessage("Format de fichier invalide : .txt uniquement");
			return;
		}

		ReaderFile rf = new ReaderFile(strFileName);

		graph = rf.getGraph();
		GraphStyle style = rf.getStyle();
		hci.getGraphPanel().setStyle(style);
		hci.getGraphPanel().setBackground(style.getBackground());

		boolean bGraphWasNull = false;

		if (graph == null) {
			bGraphWasNull = true;
			graph = new Graph(true, true);
			hci.showError("Format du fichier invalide.");
		}

		hci.setGraph(graph);
		hci.permitModifArc(graph.isValued());

		if (!bGraphWasNull && rf.hasCoord()) {
			Point[] tPoints = rf.getPoints();
			hci.initHmVertexByTab(tPoints);
		} else {
			hci.initHmVertex();
		}

		initProvSave();
		hci.refresh();
		hci.setInfo();
	}

	/**
	 * Méthode permettant d'ouvrir le manuel utilisateur sur un navigateur web
	 */
	public void openHelp() {
		Desktop desktop = null;
		try {
			if (Desktop.isDesktopSupported()) {
				desktop = Desktop.getDesktop();
				desktop.open(new File("manuel/index.html"));
			}
		} catch (Exception ex) {
			Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Méthode permettant d'ouvrir la documentation javadoc sur un navigateur
	 * web
	 */
	public void openJavadoc() {
		Desktop desktop = null;
		try {
			if (Desktop.isDesktopSupported()) {
				desktop = Desktop.getDesktop();
				desktop.open(new File("javadoc/index.html"));
			}
		} catch (Exception ex) {
			Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Méthode qui exporte le graphe en matrice ou liste d'adjacence dans un
	 * fichier texte
	 * 
	 * @param strFileName
	 *            le chemin du fichier
	 * @param format
	 *            le format soit matrice soit liste
	 */
	public void export(String strFileName, String format) {
		FileWriter fw = null;

		try {
			// ouverture du fichier en mode écriture
			fw = new FileWriter(strFileName, false);

			// écriture des lignes de texte
			fw.write("Graphe ");
			// Orienté ou non
			if (graph.isDirected()) {
				fw.write("orienté ");
			} else {
				fw.write("non orienté ");
			}
			// Valué ou non
			if (graph.isValued()) {
				fw.write("et valué. \n\n");
			} else {
				fw.write("et non valué.. \n\n");
			}
			// matrice ou liste d'adjacence
			if (format.equals("matrice")) {
				fw.write("-- Matrice :\n");
				fw.write(graph.displayMatrix() + "\n");
			} else {
				fw.write("-- Liste d'adjacence :\n");
				fw.write(graph.getFormattedList() + "\n");
			}

			// fermeture du fichier
			fw.close();

		} catch (IOException e) {
			hci.showError("Problème d'enregistrement du fichier " + strFileName + ".");
		}
	}

	/*-----------------------
	 * Gestion des composants
	 *------------------------*/
	/**
	 * Méthode permettant d'ajouter un sommet au graphe
	 * 
	 * @param strVertexName
	 *            le nom du sommet
	 * @return true si le nom est déjà utilisé, false sinon
	 */
	public boolean addVertex(String strVertexName) {
		boolean bExist = false;
		if (graph.getVertex(strVertexName) != null) {
			hci.showError("Un sommet avec le nom " + strVertexName + " existe déjà.");
			bExist = true;
		} else if (strVertexName.replaceAll(" ", "").equals("")) {
			hci.showError("Le nom de votre sommet ne peut pas être vide");
			bExist = true;
		} else {
			graph.addVertex(strVertexName);
			hci.addVertex(strVertexName);
			provSave();
		}
		return bExist;
	}

	public boolean deleteVertex(String s) {
		Vertex v = graph.getVertex(s);
		if (v != null) {
			graph.deleteVertex(v);
			provSave();
			return true;
		}
		return false;
	}

	public void deleteMultipleVertex(ArrayList<String> s) {
		for (int i = 0; i < s.size(); i++) {
			Vertex v = graph.getVertex(s.get(i));
			if (v != null) {
				graph.deleteVertex(v);
				hci.getHmVertex().remove(s.get(i));
			}
		}
		provSave();
	}

	/**
	 * Méthode permettant d'ajouter un arc ou une arête non valué.
	 * 
	 * @param v
	 *            le nom du premier sommet
	 * @param vBis
	 *            le nom du deuxième sommet
	 */
	public void addArc(Vertex v, Vertex vBis) {
		if (checkArcAlreadyExist(v, vBis)) {
			graph.addArc(v, vBis);
			provSave();
		} else {
			hci.showError("L'arc existe déjà.");
		}
	}

	/**
	 * Méthode permettant d'ajouter un arc ou une arête valué
	 * 
	 * @param v
	 *            le nom du premier sommet
	 * @param vBis
	 *            le nom du deuxième sommet
	 * @param iValue
	 *            la valeur de l'arc
	 */
	public void addArc(Vertex v, Vertex vBis, int iValue) {
		if (checkArcAlreadyExist(v, vBis)) {
			graph.addArc(v, vBis, iValue);
			provSave();
		} else
			hci.showError("L'arc existe déjà.");
	}

	/**
	 * Méthode permettant de supprimer un arc ou une arête
	 * 
	 * @param v
	 *            le nom du premier sommet
	 * @param vBis
	 *            le nom du deuxième sommet
	 */
	public boolean delArc(Vertex v, Vertex vBis) {
		if (graph.getAlVertex().contains(v) && graph.getAlVertex().contains(vBis)) {
			if (graph.deleteArc(v, vBis)) {
				provSave();
				return true;
			}
		}
		return false;
	}

	/**
	 * Méthode qui vérifie si un arc ou une arête existe déjà.
	 * 
	 * @param v
	 *            nom du premier sommet
	 * @param vBis
	 *            nom du deuxième sommet
	 * @return true s'il existe, false sinon
	 */
	private boolean checkArcAlreadyExist(Vertex v, Vertex vBis) {
		for (Arc arc : v.getAlArcs()) {
			if (arc.getVertex() == vBis) {
				return false;
			}
		}

		return true;
	}

	/*---------------------
	 * Annuler / Rétablir
	 *--------------------*/
	/**
	 * Méthode permettant d'annuler la dernière action
	 */
	public void undo() {
		if (cptModif > 1) {
			ReaderFile rF = new ReaderFile(new ArrayList<String>(saveGraphList.get(cptModif - 2)), graph.isDirected(),
					graph.isValued());
			graph = rF.getGraph();
			hci.initHmVertexByTab(rF.getPoints());
			cptModif--;
		}
	}

	/**
	 * Méthode permettant de rétablir la derniére action effectuée
	 */
	public void redo() {
		if (cptModif >= 0 && cptModif < saveGraphList.size()) {
			ReaderFile rF = new ReaderFile(new ArrayList<String>(saveGraphList.get(cptModif)), graph.isDirected(),
					graph.isValued());
			graph = rF.getGraph();
			hci.initHmVertexByTab(rF.getPoints());
			cptModif++;
		}
	}

	/**
	 * Méthode qui met à zéro les arrayList contenant les actions effectuées
	 * depuis la dernière sauvegarde.
	 */
	public void initProvSave() {
		saveGraphList = new ArrayList<ArrayList<String>>();
		cptModif = 0;
		provSave();
	}

	/**
	 * Méthode permettant de sauvegarder l'état du graphe à un instant t.
	 * Utilisée pour sauvegarder les actions effectuées.
	 */
	public void provSave() {

		// Initialisation de la ArrayList contenant la liste d'adjacence du
		// graphe + les coordonnées des points au moment où l'utilisateur
		// effectue une action
		ArrayList<String> alProv = graph.getFormattedListAlString();

		// Sauvegarde des coordonnées
		alProv.add("-- Coordonnées des points :\n");
		String sCoord = "[";
		int cpt = 0;
		int nbSommet = getNbSommet();
		for (Point c : hci.getHmVertex().values()) {
			sCoord += (c.getX() + "," + c.getY());
			cpt++;
			if (cpt != nbSommet)
				sCoord += ";";
		}
		sCoord += "]";

		// Ajout des coordonnées
		alProv.add(sCoord);

		// Ajout de la liste d'adjacence et des coordonnées dans la ArrayList de
		// sauvegarde
		saveGraphList.add(cptModif, alProv);

		// Suppression des dernières actions effectuées dans le cas ou
		// l'utilisateur est revenu plusieurs fois et a effectué une nouvelle
		// action.
		int i = cptModif + 1;
		while (i < saveGraphList.size()) {
			saveGraphList.remove(i);
		}

		// Incrémentation du compteur indiquant le nombre de modification
		// (Repère utilisé pour savoir notre position dans la ArrayList
		// permettant le retour en arrière
		cptModif++;
	}

	/*--------------------------------------
	 * Méthodes de l'interface IControlable
	 *-------------------------------------*/

	/**
	 * Renvoie un tableau de caractère contenant la première lettre de chaque
	 * sommet
	 * 
	 * @param tableau
	 *            de caractère contenant la première lettre de chaque sommet
	 */
	public char[] listeSommet() {
		return graph.getListVertex();
	}

	/**
	 * Renvoie la matrice
	 * 
	 * @param matrice
	 *            du graphe
	 */
	public int[][] getMatrice() {
		// On génère la matrice
		graph.generateMatrix();

		// On retourne la matrice générée
		return graph.getTMatrix();
	}

	/**
	 * Permet de mettre à jour l'IHM pour les classes Parcours
	 */
	public void majIHM() {
		// on vérifie qu'on a bien un parcours de lancé
		if (parcours != null) {
			// On affiche une surbrillance sur le sommet et arc actifs
			hci.showHiLightAlgorithm();

			// Si nous sommes dans l'algorithme ParcoursDijkstra
			if (parcours instanceof ParcoursDijkstra) {
				// On vérifie si une fenêtre du tableau de Dijkstra est ouvert
				if (hci.getMatrixDialog() != null) {
					// Si oui, on la ferme
					hci.getMatrixDialog().dispose();
				}
				
				// On ouvre une nouvelle fenêtre pour afficher le tableau de Dijkstra
				hci.openMatrix(((ParcoursDijkstra) (parcours)).getTChemins(), "Tableau de Dijkstra");
				hci.getMatrixDialog().paint(hci.getMatrixDialog().getGraphics());
			}
		}
	}

	/**
	 * Permet de démarrer le parcours "PlusGrandeValeur"
	 */
	public void startParcours() {
		// On nettoie la liste des sommets et arcs sélectionnés
		hci.getGraphPanel().clearAlSelected();
		hci.getGraphPanel().paintAll(hci.getGraphPanel().getGraphics());
		
		// On instancie un parcours et on le lance
		this.parcours = new Parcours(this);
		this.parcours.lancer();
		
		// On met à jour l'IHM
		majIHM();
		
		// On affiche le message de retour
		hci.showInfo(parcours.getMessage());
	}

	public void startParcoursDijkstra(int iVertex) {
		// On nettoie la liste des sommets et arcs sélectionnés
		hci.getGraphPanel().clearAlSelected();
		hci.getGraphPanel().paintAll(hci.getGraphPanel().getGraphics());
		
		this.parcours = new ParcoursDijkstra(this, iVertex);
		this.parcours.lancer();
		
		// On met à jour l'IHM
		majIHM();
		
		// On affiche le message de retour
		hci.showInfo("L'algorithme est terminé.");
	}

	public void startParcoursLienExiste(int iStartVertex, int iEndVertex) {
		// On nettoie la liste des sommets et arcs sélectionnés
		hci.getGraphPanel().clearAlSelected();
		hci.getGraphPanel().paintAll(hci.getGraphPanel().getGraphics());
		
		this.parcours = new ParcoursLienExiste(this, iStartVertex, iEndVertex);
		this.parcours.lancer();
		
		// On met à jour l'IHM
		majIHM();
		
		// On affiche le message de retour
		hci.showInfo(parcours.getMessage());
	}

	/*
	 * -------------------------------- Méthodes de l'interface IIhmable
	 * ---------------------------------
	 */
	@Override
	public int getNbSommet() {
		return graph.getAlVertex().size();
	}

	@Override
	public int getNbArc(int indSommet) {
		return graph.getAlVertex().get(indSommet).getAlArcs().size();
	}

	@Override
	public char getNomSommet(int indSommet) {
		return graph.getAlVertex().get(indSommet).getName().charAt(0);
	}

	@Override
	public int getValArc(int indSommetOri, int indArc) {
		return graph.getAlVertex().get(indSommetOri).getAlArcs().get(indArc).getIValue();
	}

	@Override
	public char getNomSommetArc(int indSommetOri, int indArc) {
		return graph.getAlVertex().get(indSommetOri).getAlArcs().get(indArc).getVertex().getName().charAt(0);
	}

	@Override
	public boolean sommetActif(char sommet) {
		if (parcours.sommetActif(sommet))
			return true;
		return false;
	}

	@Override
	public boolean arcActif(char sommetOri, char sommetDest) {
		if (parcours.arcActif(sommetOri, sommetDest))
			return true;
		return false;
	}

	@Override
	public String getMessage() {
		return parcours.getMessage();
	}

	/**
	 * Permet de mettre à jour les sommets
	 * @param oldName ancien nom du sommet
	 * @param newName nouveau nom du sommet
	 * @return faux si la mise à jour a échoué et vrai si elle a réussi
	 */
	public boolean updateVertex(String oldName, String newName) {
		boolean bUpdate = true;
		if (graph.getVertex(newName) != null) {
			hci.showError("Un sommet avec le nom " + newName + " existe déjà.");
			bUpdate = false;
		} else if (newName.replaceAll(" ", "").equals("")) {
			hci.showError("Le nom de votre sommet ne peut pas être vide");
			bUpdate = false;
		} else {
			graph.updateVertex(oldName, newName);
			provSave();
		}
		return bUpdate;
	}

	/**
	 * Permet de mettre à jour un arc entre deux sommets
	 * @param v sommet de départ de l'arc
	 * @param vBis sommet d'arrivé de l'arc
	 * @param value valeur de l'arc
	 * @return faux si la mise à jour a échoué et vrai si elle a réussi
	 */
	public boolean updateArc(Vertex v, Vertex vBis, int value) {
		for (Arc a : v.getAlArcs()) {
			if (a.getVertex() == vBis) {
				graph.updateArc(a, value);
				provSave();
				return true;
			}

		}
		return false;
	}

	/*------------------
	 * Getter / Setter
	 * ----------------*/

	public Graph getGraph() {
		return graph;
	}

	public String getFile() {
		return file;
	}
}