package controller;

import java.awt.Desktop;
import java.awt.Graphics2D;
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
 * Classe qui fait le lien entre l'IHM et le m�tier et qui v�rifie les actions
 * effectu�es
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
		// Cr�ation du graphe
		graph = new Graph(true, true);

		// Initialisation de la frame
		hci = new HCI(this);

		new StartFrame(this, hci);

		// Initialisation de l'arrayList qui permet d'impl�menter les fonctions
		// annuler et r�tablir
		initProvSave();
	}

	/*-------------------
	 * Gestion du graphe 
	 * -----------------*/
	/**
	 * M�thode qui cr�e un nouveau graphe vide avec les param�tres choisis par
	 * l'utilisateur.
	 * 
	 * @param bOriented
	 *            true s'il est orient�, false sinon.
	 * @param bValued
	 *            true s'il est valu�, false sinon.
	 */
	public void newGraph(boolean bOriented, boolean bValued) {
		graph = new Graph(bOriented, bValued);
		initProvSave();
		hci.setGraph(graph);
		hci.permitModifArc(graph.isValued());
		hci.initHmVertex();
		hci.refresh();
	}

	/**
	 * M�thode permettant de sauvegarder le graphe dans un fichier texte
	 * 
	 * @param strFileName
	 *            le chemin o� le fichier doit �tre enregistrer, si null, on
	 *            reprend le chemin sauvegard� pour �craser l'ancienne
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
			// ouverture du fichier en mode �criture
			fw = new FileWriter(file, false);

			// �criture des lignes de texte

			if (format.equals("matrice")) {
				fw.write("IsMatrix=true\n");
				
				fw.write("Directed=" + graph.isDirected() + "\n");
				fw.write("Valued=" + graph.isValued() + "\n\n");
				fw.write("-- Matrice :\n");
				fw.write(graph.displayMatrix() + "\n");
			}
			else {
				fw.write("IsMatrix=false\n");
	
				fw.write("Directed=" + graph.isDirected() + "\n");
				fw.write("Valued=" + graph.isValued() + "\n\n");
				fw.write("-- Liste d'adjacence :\n");
				fw.write(graph.getFormattedList() + "\n");
			}
			fw.write("-- Coordonn�es des points :\n");
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

			// Remise � zero des sauvegardes provisoires
			initProvSave();

		} catch (IOException e) {
			hci.showError("Probl�me d'enregistrement du fichier " + file + ".");
		}
	}

	/**
	 * M�thode permettant de charger un fichier texte
	 * 
	 * @param strFileName
	 *            le chemin du fichier � charger
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

		if (!bGraphWasNull && rf.haveCoord()) {
			Point[] tPoints = rf.getPoints();
			hci.initHmVertexByTab(tPoints);
		} else {
			hci.initHmVertex();
		}
		
		initProvSave();
		hci.refresh();
	}

	/**
	 * M�thode permettant d'ouvrir le manuel utilisateur sur un navigateur web
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
	 * M�thode permettant d'ajouter un sommet au graphe
	 * 
	 * @param strVertexName
	 *            le nom du sommet
	 * @return true si le nom est d�ja utilis�, false sinon
	 */
	public boolean addVertex(String strVertexName) {
		boolean bExist = false;
		if (graph.getVertex(strVertexName) != null) {
			hci.showError("Un sommet avec le nom " + strVertexName + " existe d�j�.");
			bExist = true;
		} else if (strVertexName.replaceAll(" ", "").equals("")) {
			hci.showError("Le nom de votre sommet ne peut pas �tre vide");
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
	 * M�thode permettant d'ajouter un arc ou une ar�te non valu�
	 * 
	 * @param v
	 *            le nom du premier sommet
	 * @param vBis
	 *            le nom du deuxi�me sommet
	 */
	public void addArc(Vertex v, Vertex vBis) {
		if (checkArcAlreadyExist(v, vBis)) {
			graph.addArc(v, vBis);
			provSave();
		} else {
			hci.showError("L'arc existe d�j�.");
		}
	}

	/**
	 * M�thode permettant d'ajouter un arc ou une ar�te valu�
	 * 
	 * @param v
	 *            le nom du premier sommet
	 * @param vBis
	 *            le nom du deuxi�me sommet
	 * @param iValue
	 *            la valeur de l'arc
	 */
	public void addArc(Vertex v, Vertex vBis, int iValue) {
		if (checkArcAlreadyExist(v, vBis)) {
			graph.addArc(v, vBis, iValue);
			provSave();
		} else
			hci.showError("L'arc existe d�j�.");
	}

	/**
	 * M�thode permettant de supprimer un arc ou une ar�te
	 * 
	 * @param v
	 *            le nom du premier sommet
	 * @param vBis
	 *            le nom du deuxi�me sommet
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
	 * M�thode qui v�rifie si un arc ou une ar�te existe d�j�
	 * 
	 * @param v
	 *            nom du premier sommet
	 * @param vBis
	 *            nom du deuxi�me sommet
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
	 * Annuler / R�tablir
	 *--------------------*/
	/**
	 * M�thode permettant d'annuler la derni�re action
	 */
	public void undo() {
		 if (cptModif > 1) { 
			ReaderFile rF = new ReaderFile(new ArrayList<String>(saveGraphList.get(cptModif-2)), graph.isDirected(), graph.isValued());
		 	graph = rF.getGraph();
		 	hci.initHmVertexByTab(rF.getPoints()); 
		 	cptModif--; 
		 }
	}

	/**
	 * M�thode permettant de r�tablir la derni�re action effectu�e
	 */
	public void redo() {
		if (cptModif >= 0 && cptModif < saveGraphList.size()) { 
			ReaderFile rF = new ReaderFile(new ArrayList<String>(saveGraphList.get(cptModif)), graph.isDirected(), graph.isValued());
			graph = rF.getGraph();
		  	hci.initHmVertexByTab(rF.getPoints()); 
		  	cptModif++; 
		  }
	}

	/**
	 * M�thode qui met � z�ro les arrayList contenant les actions effectu�es
	 * depuis la derni�re sauvegarde.
	 */
	public void initProvSave() {
		saveGraphList = new ArrayList<ArrayList<String>>();
		cptModif = 0;
		provSave();
	}

	/**
	 * M�thode permettant de sauvegarder l'�tat du graphe � un instant t.
	 * Utilis�e pour sauvegarder les actions effectu�es.
	 */
	public void provSave() {
		
		// Initialisation de la ArrayList contenant la liste d'adjacence du
		// graphe + les coordonn�es des points au moment o� l'utilisateur effectue une action
		ArrayList<String> alProv = graph.getFormattedListAlString();
		
		// Sauvegarde des coordonn�es
		alProv.add("-- Coordonn�es des points :\n");
		String sCoord = "[";
		int cpt = 0;
		int nbSommet = getNbSommet();
		for (Point c : hci.getHmVertex().values()) {
			sCoord+= (c.getX() + "," + c.getY());
			cpt++;
			if (cpt != nbSommet)
				sCoord+=";";
		}
		sCoord+="]";

		// Ajout des coordonn�es
		alProv.add(sCoord);
		
		// Ajout de la liste d'adjacence et des coordonn�es dans la ArrayList de sauvegarde
		saveGraphList.add(cptModif, alProv);
		
		// Suppression des derni�res actions effectu�es dans le cas o� l'utilisateur est revenu plusieurs fois et a effectu� une nouvelle action.
		int i = cptModif + 1;
		while (i < saveGraphList.size()) {
			saveGraphList.remove(i);
		}
		
		// Incr�mentation du compteur indiquant le nombre de modification
		// (Rep�re utilis� pour savoir notre position dans la ArrayList
		// permettant le retour en arri�re
		cptModif++;
	}

	/*--------------------------------------
	 * M�thodes de l'interface IControlable
	 *-------------------------------------*/

	public char[] listeSommet() {
		return graph.getListVertex();
	}

	public int[][] getMatrice() {
		graph.generateMatrix();
		return graph.getTMatrix();
	}

	public void majIHM() {
		if (parcours != null) {
			hci.showHiLightAlgorithm();
		}

	}
	
	public void repaintVertex() {
		hci.repaint();
	}

	public void startParcours() {
		this.parcours = new Parcours(this);
		this.parcours.lancer();
		hci.showInfo(parcours.getMessage());
	}
	
	public void startParcoursDijkstra(int iVertex) {
		this.parcours = new ParcoursDijkstra(this,iVertex);
		this.parcours.lancer();
	}
	
	public void startParcoursLienExiste(int iStartVertex, int iEndVertex) {
		this.parcours = new ParcoursLienExiste(this,iStartVertex,iEndVertex);
		this.parcours.lancer();
		hci.showInfo(parcours.getMessage());
	}
	
	/*
	 * -------------------------------- 
	 * M�thodes de l'interface IIhmable
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
			hci.showError("Un sommet avec le nom " + newName + " existe d�j�.");
			bUpdate = false;
		} else if (newName.replaceAll(" ", "").equals("")) {
			hci.showError("Le nom de votre sommet ne peut pas �tre vide");
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