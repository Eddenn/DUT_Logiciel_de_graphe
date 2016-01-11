package controller;

import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import model.Arc;
import model.Graph;
import model.IParcourable;
import model.Vertex;

import view.HCI;
import view.StartFrame;

/**
 * Classe qui fait le lien entre l'IHM et le métier et qui vérifie les actions effectuées
 * @author Groupe 3
 * @version 2016-01-08
 */
public class Controller implements IControlable, IIhmable {
	private HCI hci;
	private Graph graph;
	private ArrayList<ArrayList<String>> saveVertexList;
	private ArrayList<Point[]> saveCoordList;
	private int cptModif;
	private static String file="";
	
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
		
		new StartFrame(this,hci);
		
		// Initialisation de l'arrayList qui permet d'implémenter les fonctions annuler et rétablir
		initProvSave();
	}

	/*-------------------
	 * Gestion du graphe 
	 * -----------------*/
	/**
	 * Méthode qui crée un nouveau graphe vide avec les paramètres choisis par l'utilisateur.
	 * @param bOriented true s'il est orienté, false sinon.
	 * @param bValued true s'il est valué, false sinon.
	 */
	public void newGraph(boolean bOriented, boolean bValued) {
		graph = new Graph(bOriented, bValued);
		hci.initHmVertex();
		hci.refresh();
	}
	
	/**
	 * Méthode permettant de sauvegarder le graphe dans un fichier texte
	 * @param strFileName le chemin où le fichier doit être enregistrer, 
	 * 		  si null, on reprend le chemin sauvegardé pour écraser l'ancienne sauvegarde
	 */
	public void saveFile(String strFileName) {
		//Si strFileName contient quelque chose, il s'agit d'enregistrer sous sinon il s'agit d'enregistrer
		if(!strFileName.equals("")){
			file=strFileName;
		}
		FileWriter fw = null;

		try {
			// ouverture du fichier en mode écriture
			fw = new FileWriter(file, false);

			// écriture des lignes de texte

			fw.write("IsMatrix=false\n");

			fw.write("Directed=" + graph.isDirected() + "\n");
			fw.write("Valued=" + graph.isValued() + "\n\n");
			fw.write(graph.getFormattedList()+"\n");
			fw.write("[");
			
			Point[] tabPoint = new Point[graph.getAlVertex().size()];
			int cpt = 0;
			for (Point c : hci.getHmVertex().values()) {
				tabPoint[cpt] = c;
				cpt++;
			}
			
			int size = tabPoint.length;
			for (int i = 0; i < size; i++) {
				fw.write(tabPoint[i].getX()+","+ tabPoint[i].getY());
				if (i != size-1) 
					fw.write(";");
			}
			fw.write("]");

			// fermeture du fichier
			fw.close();
			
			// Remise à zero des sauvegardes provisoires
			initProvSave();
			
		} catch (IOException e) {
			hci.showError("Problème d'enregistrement du fichier " + file+ ".");
		}
	}

	/**
	 * Méthode permettant de charger un fichier texte
	 * @param strFileName le chemin du fichier à charger
	 */
	public void loadFile(String strFileName) {
		graph = Reader.read(strFileName);

		if (graph == null) {
			graph = new Graph(true,true);
			hci.showError("Format du fichier invalide.");
		}

		hci.initHmVertex();
		initProvSave();
		provSave();
		hci.refresh();
	}
	
	/*-----------------------
	 * Gestion des composants
	 *------------------------*/
	/**
	 * Méthode permettant d'ajouter un sommet au graphe
	 * @param strVertexName le nom du sommet
	 * @return true si le nom est déja utilisé, false sinon
	 */
	public boolean addVertex(String strVertexName) {
		boolean bExist=false;
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

	/**
	 * Méthode permettant d'ajouter un arc ou une arrête non valué
	 * @param v le nom du premier sommet
	 * @param vBis le nom du deuxième sommet
	 */
	public void addArc(Vertex v, Vertex vBis) {
		if (checkArcAlreadyExist(v,vBis)) {
			graph.addArc(v, vBis);
			provSave();
		} else {
			hci.showError("L'arc existe déjà.");
		}
	}

	/**
	 * Méthode permettant d'ajouter un arc ou une arrête valué
	 * @param v le nom du premier sommet
	 * @param vBis le nom du deuxième sommet
	 * @param iValue la valeur de l'arc
	 */
	public void addArc(Vertex v, Vertex vBis, int iValue) {
		if (checkArcAlreadyExist(v,vBis)) {
			graph.addArc(v, vBis, iValue);
			provSave();
		} else
			hci.showError("L'arc existe déjà.");
	}

	/**
	 * Méthode permettant de supprimer un arc ou une arrête
	 * @param v le nom du premier sommet
	 * @param vBis le nom du deuxième sommet
	 */
	public boolean delArc(Vertex v, Vertex vBis) {
		if (graph.getAlVertex().contains(v) && graph.getAlVertex().contains(vBis)) {
			return (graph.deleteArc(v, vBis));
		}
		return false;
	}

	/**
	 * Méthode qui vérifie si un arc ou une arrête existe déjà
	 * @param v nom du premier sommet
	 * @param vBis nom du deuxième sommet
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
		//Ne sait pas lequel gardé
		/*if (cptModif > 0) {
			graph = ReaderAdjacencyList.ReadAdjacencyList(new ArrayList<String>(saveVertexList.get(cptModif--)));
			hci.initHmVertexByTab(saveCoordList.get(cptModif));
		}*/	
		if (cptModif > 1) {
			graph = ReaderAdjacencyList.ReadAdjacencyList(new ArrayList<String>(saveVertexList.get(cptModif-2)));
			hci.initHmVertexByTab(saveCoordList.get(cptModif-2));
			cptModif--;
		}
		

	}

	/**
	 * Méthode permettant de rétablir la dernière action supprimer
	 */
	public void redo() {
		if (cptModif >= 0 && cptModif < saveVertexList.size()) {
			graph = ReaderAdjacencyList.ReadAdjacencyList(new ArrayList<String>(saveVertexList.get(cptModif)));
			hci.initHmVertexByTab(saveCoordList.get(cptModif));
			cptModif++;
		}
	}

	/**
	 * Méthode qui met à zéro les arrayList contenant les actions effectués depuis la dernière sauvegarde.
	 */
	public void initProvSave() {
		saveVertexList = new ArrayList<ArrayList<String>>();
		saveCoordList = new ArrayList<Point[]>();
		cptModif = 0;
		provSave();
	}
	
	/**
	 * Méthode permettant de sauvegarder l'état du graphe à une instant t. Utiliser pour sauvegarder les actions effectuées.
	 */
	public void provSave() {
<<<<<<< HEAD
//		// Incrémentation du compteur indiquant le nombre de modification (Repère utilisé pour savoir notre position dans la ArrayList permettant le retour en arrière
//		cptModif++;

=======
>>>>>>> 56a532d09dce8a6baa172292f9fea3a433fcbffb
		// Initialisation de la ArrayList contenant la liste d'adjacence du graphe au moment oï¿½ l'utilisateur effectue une action
		ArrayList<String> alProv = graph.getFormattedListAlString();
		alProv.add(0,"Valued="+graph.isValued());
		alProv.add(0,"Directed="+graph.isDirected());
		
		// Suppression des derniÃ¨res actions effectuÃ©es dans le cas oÃ¹ l'utilisateur effectue une nouvelle action sans redo
		if (cptModif < saveCoordList.size()) {
			for (int i = cptModif ; i < saveVertexList.size(); i++) {
				saveVertexList.remove(cptModif);
			}
			for (int i = cptModif; i < saveCoordList.size(); i++) {
				saveCoordList.remove(cptModif);
			}
		}
		
		// Ajout de la liste d'adjacence dans la ArrayList de sauvegarde
		saveVertexList.add(cptModif, alProv);
		
		// Sauvegarde des coordonnÃ©es
		Point[] tabPoint = new Point[graph.getAlVertex().size()];
		int cpt = 0;
		for (Point c : hci.getHmVertex().values()) {
			tabPoint[cpt] = new Point( (int)(c.x/hci.getGraphPanel().getZoom()) , (int)(c.y/hci.getGraphPanel().getZoom()) );
			cpt++;
		}
		
		saveCoordList.add(cptModif,tabPoint);
		
		
		int i = cptModif+1;
		while (i < saveVertexList.size()) {
			saveVertexList.remove(i);
			saveCoordList.remove(i);
		}
		
		// Incrémentation du compteur indiquant le nombre de modification (Repère utilisé pour savoir notre position dans la ArrayList permettant le retour en arrière
		cptModif++;
	}
	
	/*--------------------------------------
	 * Méthodes de l'interface IControlable
	 *-------------------------------------*/

	public char[] listeSommet() {return graph.getListVertex();}

	public int[][] getMatrice() {return graph.getTMatrix();}

	public void majIHM() {hci.refresh();}
	
	/* --------------------------------
	 * Méthodes de l'interface IIhmable 
	 * ---------------------------------*/
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
	
	/*------------------
	 * Getter / Setter
	 * ----------------*/
	 
	public Graph getGraph() {return graph;}
	public String getFile() {return file; }
	
}