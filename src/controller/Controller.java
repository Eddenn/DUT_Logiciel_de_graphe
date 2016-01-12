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

import model.Arc;
import model.Graph;
import model.IParcourable;
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
	private ArrayList<ArrayList<String>> saveVertexList;
	private ArrayList<Point[]> saveCoordList;
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
	 * Méthode qui crée un nouveau graphe vide avec les paramétres choisis par
	 * l'utilisateur.
	 * 
	 * @param bOriented
	 *            true s'il est orienté, false sinon.
	 * @param bValued
	 *            true s'il est valué, false sinon.
	 */
	public void newGraph(boolean bOriented, boolean bValued) {
		graph = new Graph(bOriented, bValued);
		hci.setGraph(graph);
		hci.permitModifArc(graph.isValued());
		hci.initHmVertex();
		hci.refresh();
	}

	/**
	 * Méthode permettant de sauvegarder le graphe dans un fichier texte
	 * 
	 * @param strFileName
	 *            le chemin oé le fichier doit étre enregistrer, si null, on
	 *            reprend le chemin sauvegardé pour écraser l'ancienne
	 *            sauvegarde
	 */
	public void saveFile(String strFileName) {
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

			fw.write("IsMatrix=false\n");

			fw.write("Directed=" + graph.isDirected() + "\n");
			fw.write("Valued=" + graph.isValued() + "\n\n");
			fw.write("-- Liste d'adjacence :\n");
			fw.write(graph.getFormattedList() + "\n");
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
			fw.write(hci.getGraphPanel().getStyle().toString());

			// fermeture du fichier
			fw.close();

			// Remise é zero des sauvegardes provisoires
			initProvSave();

		} catch (IOException e) {
			hci.showError("ProblÃ¨me d'enregistrement du fichier " + file + ".");
		}
	}

	/**
	 * Méthode permettant de charger un fichier texte
	 * 
	 * @param strFileName
	 *            le chemin du fichier é charger
	 */
	public void loadFile(String strFileName) {
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

		if (!bGraphWasNull) {
			Point[] tPoints = rf.getPoints();
			hci.initHmVertexByTab(tPoints);
		} else {
			hci.initHmVertex();
		}
		
		initProvSave();
		provSave();
		hci.refresh();
	}

	/**
	 * Méthode permettant d'ouvrir le manuel utilisateur sur un navigateur web
	 */
	public void openHelp(){
		Desktop desktop = null; 
		try { 
			if (Desktop.isDesktopSupported()) { 
				desktop = Desktop.getDesktop(); 
				desktop.open(new File("manuel/index.html"));
			} 
		} 
		catch (Exception ex) { Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex); }
	}
	
	/*-----------------------
	 * Gestion des composants
	 *------------------------*/
	/**
	 * Méthode permettant d'ajouter un sommet au graphe
	 * 
	 * @param strVertexName
	 *            le nom du sommet
	 * @return true si le nom est déja utilisé, false sinon
	 */
	public boolean addVertex(String strVertexName) {
		boolean bExist = false;
		if (graph.getVertex(strVertexName) != null) {
			hci.showError("Un sommet avec le nom " + strVertexName + " existe déjé.");
			bExist = true;
		} else if (strVertexName.replaceAll(" ", "").equals("")) {
			hci.showError("Le nom de votre sommet ne peut pas étre vide");
			bExist = true;
		} else {
			graph.addVertex(strVertexName);
			hci.addVertex(strVertexName);
			provSave();
		}
		return bExist;
	}

	/**
	 * Méthode permettant d'ajouter un arc ou une arréte non valué
	 * 
	 * @param v
	 *            le nom du premier sommet
	 * @param vBis
	 *            le nom du deuxiéme sommet
	 */
	public void addArc(Vertex v, Vertex vBis) {
		if (checkArcAlreadyExist(v, vBis)) {
			graph.addArc(v, vBis);
			provSave();
		} else {
			hci.showError("L'arc existe déjé.");
		}
	}

	/**
	 * Méthode permettant d'ajouter un arc ou une arréte valué
	 * 
	 * @param v
	 *            le nom du premier sommet
	 * @param vBis
	 *            le nom du deuxiéme sommet
	 * @param iValue
	 *            la valeur de l'arc
	 */
	public void addArc(Vertex v, Vertex vBis, int iValue) {
		if (checkArcAlreadyExist(v, vBis)) {
			graph.addArc(v, vBis, iValue);
			provSave();
		} else
			hci.showError("L'arc existe déjé.");
	}

	/**
	 * Méthode permettant de supprimer un arc ou une arréte
	 * 
	 * @param v
	 *            le nom du premier sommet
	 * @param vBis
	 *            le nom du deuxiéme sommet
	 */
	public boolean delArc(Vertex v, Vertex vBis) {
		if (graph.getAlVertex().contains(v) && graph.getAlVertex().contains(vBis)) {
			return (graph.deleteArc(v, vBis));
		}
		return false;
	}

	/**
	 * Méthode qui vérifie si un arc ou une arête existe déjà
	 * 
	 * @param v
	 *            nom du premier sommet
	 * @param vBis
	 *            nom du deuxiéme sommet
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
			ReaderFile rF = new ReaderFile(new ArrayList<String>(saveVertexList.get(cptModif-2)), graph.isDirected(), graph.isValued());
		 	graph = rF.getGraph();
		 	hci.initHmVertexByTab(rF.getPoints()); 
		 	cptModif--; 
		 }
		 

	}

	/**
	 * Méthode permettant de rétablir la derniére action supprimer
	 */
	public void redo() {
		
		
		if (cptModif >= 0 && cptModif < saveVertexList.size()) { 
			ReaderFile rF = new ReaderFile(new ArrayList<String>(saveVertexList.get(cptModif)), graph.isDirected(), graph.isValued());
			graph = rF.getGraph();
		  	hci.initHmVertexByTab(rF.getPoints()); 
		  	cptModif++; 
		  }
		 
	}

	/**
	 * Méthode qui met é zéro les arrayList contenant les actions effectués
	 * depuis la derniére sauvegarde.
	 */
	public void initProvSave() {
		saveVertexList = new ArrayList<ArrayList<String>>();
		saveCoordList = new ArrayList<Point[]>();
		cptModif = 0;
		provSave();
	}

	/**
	 * Méthode permettant de sauvegarder l'état du graphe é une instant t.
	 * Utiliser pour sauvegarder les actions effectuées.
	 */
	public void provSave() {
		
		// Initialisation de la ArrayList contenant la liste d'adjacence du
		// graphe au moment où l'utilisateur effectue une action
		ArrayList<String> alProv = graph.getFormattedListAlString();
//		alProv.add(0, "Valued=" + graph.isValued());
//		alProv.add(0, "Directed=" + graph.isDirected());
		
		// Sauvegarde des coordonnÃ©es
		alProv.add("-- Coordonnées des points :\n");
		String sCoord = "[";
		
		Point[] tabPoint = new Point[graph.getAlVertex().size()];
		int cpt = 0;
		for (Point c : hci.getHmVertex().values()) {
			tabPoint[cpt] = c;
			cpt++;
		}

		int size = tabPoint.length;
		for (int j = 0; j < size; j++) {
			sCoord+= (tabPoint[j].getX() + "," + tabPoint[j].getY());
			if (j != size - 1)
				sCoord+=";";
		}
		sCoord+="]";
		
		alProv.add(sCoord);
		
		
		// Ajout de la liste d'adjacence dans la ArrayList de sauvegarde
		saveVertexList.add(cptModif, alProv);
		
		int i = cptModif + 1;
		while (i < saveVertexList.size()) {
			saveVertexList.remove(i);
			saveCoordList.remove(i);
		}

		// Incrémentation du compteur indiquant le nombre de modification
		// (Repère utilisé pour savoir notre position dans la ArrayList
		// permettant le retour en arrière
		cptModif++;
	}

	/*--------------------------------------
	 * Méthodes de l'interface IControlable
	 *-------------------------------------*/

	public char[] listeSommet() {
		return graph.getListVertex();
	}

	public int[][] getMatrice() {
		return graph.getTMatrix();
	}

	public void majIHM() {
		hci.refresh();
	}

	/*
	 * -------------------------------- 
	 * Méthodes de l'interface IIhmable
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

	public boolean updateArc(Vertex v, Vertex vBis, int value) {
		for (Arc a : v.getAlArcs()) {
			if (a.getVertex() == vBis) {
				graph.updateArc(a, value);
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