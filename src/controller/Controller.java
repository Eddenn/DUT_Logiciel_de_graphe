package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.awt.Point;

import model.Arc;
import model.Graph;
import model.Vertex;
import view.HCI;

public class Controller implements IControlable {
	private HCI hci;
	private Graph graph;
	private ArrayList<ArrayList<String>> saveVertexList;
	private ArrayList<Point[]> saveCoordList;
	private int cptModif;

	public Controller() {
		// Create graph
		graph = new Graph(true, true);

		// Initialize the frame
		hci = new HCI(this);

		// Initialize the arrrayList which permit to implement the undo and redo
		saveVertexList = new ArrayList<ArrayList<String>>();
		saveCoordList = new ArrayList<Point[]>();
		provSave();
		cptModif--;
	}

	public void saveFile(String strFileName) {
		FileWriter fw = null;

		try {
			// ouverture du fichier en mode écriture
			fw = new FileWriter(strFileName, false);

			// écriture des lignes de texte
			fw.write("IsMatrix=true\n");
			fw.write("Directed=" + graph.isDirected() + "\n");
			fw.write("Valued=" + graph.isValued() + "\n\n");
			fw.write(graph.displayMatrix());

			// fermeture du fichier
			fw.close();
		} catch (IOException e) {
			hci.showError("Problème d'écriture dans le fichier " + strFileName + ".");
		}
	}

	public void loadFile(String strFileName) {
		graph = Reader.read(strFileName);

		if (graph == null) {
			graph = new Graph(true,true);
			hci.showError("Format du fichier invalide.");
		}

		hci.initHmVertex();
		hci.refresh();
	}

	public void newGraph(boolean bOriented, boolean bValued) {
		graph = new Graph(bOriented, bValued);
		hci.initHmVertex();
		hci.refresh();
	}

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

	public void addArc(Vertex v, Vertex vBis) {
		if (checkArcAlreadyExist(v,vBis)) {
			graph.addArc(v, vBis);
			provSave();
		} else {
			hci.showError("L'arc existe déjà.");
		}
	}

	public void addArc(Vertex v, Vertex vBis, int iValue) {
		if (checkArcAlreadyExist(v,vBis)) {
			graph.addArc(v, vBis, iValue);
			provSave();
		} else
			hci.showError("L'arc existe déjà.");
	}

	public void delArc(Vertex v, Vertex vBis) {
		for (int i = 0; i < v.getAlArcs().size(); i++) {
			if (v.getAlArcs().get(i).getVertex() == vBis) {
				graph.deleteArc(v.getAlArcs().get(i));
				provSave();
			}
		}
	}

	private boolean checkArcAlreadyExist(Vertex v, Vertex vBis) {
		for (Arc arc : v.getAlArcs()) {
			if (arc.getVertex() == vBis) {
				return false;
			}
		}

		return true;
	}

	public void undo() {
		if (cptModif > 0) {
			graph = ReaderAdjacencyList.ReadAdjacencyList(new ArrayList<String>(saveVertexList.get(cptModif-1)));
			hci.initHmVertexByTab(saveCoordList.get(cptModif-1));
			cptModif--;
			System.out.println(saveVertexList);
			System.out.println(cptModif);
		}
		
	}

	public void redo() {
		if (cptModif >= 0 && cptModif < saveVertexList.size()) {
			graph = ReaderAdjacencyList.ReadAdjacencyList(new ArrayList<String>(saveVertexList.get(cptModif+1)));
			hci.initHmVertexByTab(saveCoordList.get(cptModif+1));
			cptModif++;
			System.out.println(saveVertexList);
			System.out.println(cptModif);
		}
	}

	public static void main(String[] args) {
		new Controller();
	}

	public Graph getGraph() {
		return graph;
	}
	
	@Override
	public char[] listeSommet() {
		return graph.getListVertex();
	}

	@Override
	public int[][] getMatrice() {
		return graph.getTMatrix();
	}

	@Override
	public void majIHM() {
		hci.refresh();
	}
	
	public void provSave() {
		// Incrémentation du compteur indiquant le nombre de modification (Repère utilisé pour savoir notre position dans la ArrayList permettant le retour en arrière
		cptModif++;
		
		// Initialisation de la ArrayList contenant la liste d'adjacence du graphe au moment où l'utilisateur effectue une action
		ArrayList<String> alProv = graph.getFormattedListAlString();
		alProv.add(0,"Valued="+graph.isValued());
		alProv.add(0,"Directed="+graph.isDirected());
		
		// Suppression des dernières actions effectuées dans le cas où l'utilisateur effectue une nouvelle action sans redo
		if (cptModif < saveVertexList.size()) {
			for (int i = cptModif ; i < saveVertexList.size(); i++) {
				saveVertexList.remove(i);
				saveCoordList.remove(i);
			}
		}
		
		// Ajout de la liste d'adjacence dans la ArrayList de sauvegarde
		saveVertexList.add(alProv);
		
		// Sauvegarde des coordonnées
		Point[] tabPoint = new Point[graph.getAlVertex().size()];
		int cpt = 0;
		for (Point c : hci.getHmVertex().values()) {
			tabPoint[cpt] = new Point(c);
			cpt++;
		}
		
		saveCoordList.add(tabPoint);
		System.out.println(saveVertexList);
		System.out.println(cptModif);
	}
}