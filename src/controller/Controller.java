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

public class Controller {
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
		cptModif = 0;
	}

	public void saveFile(String strFileName) {
		FileWriter fw = null;

		try {
			// ouverture du fichier en mode écriture
			fw = new FileWriter(strFileName, false);

			// écriture des lignes de texte
			fw.write("Directed=" + graph.isDirected() + "\n");
			fw.write("Valued=" + graph.isValued() + "\n\n");
			fw.write(graph.displayMatrix());

			// fermeture du fichier
			fw.close();
		} catch (IOException e) {
			System.out.println("Problème d'écriture dans le fichier " + strFileName + ".");
		}
	}

	public void loadFile(String strFileName) {
		graph = Reader.read(strFileName);

		if (graph == null) {
<<<<<<< HEAD
			graph = new Graph(true,true);
			hci.showError("Format du fichier invalide.");
=======
			graph = new Graph(true, true);
			hci.setError("Format du fichier invalide.");
>>>>>>> 90e92e350859b560c292cf78e011166293673439
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
<<<<<<< HEAD
		provSave();
		saveVertexList.add(graph.getFormattedListAlString());
		cptModif++;
		
		boolean bExist=false;
=======
		boolean bExist = false;

>>>>>>> c27c84337f1f962b75f00c9ee285ad35d8f8dfe2
		if (graph.getVertex(strVertexName) != null) {
<<<<<<< HEAD
			hci.showError("Un sommet avec le nom " + strVertexName + " existe déjà.");
			bExist=true;
=======
			hci.setError("Un sommet avec le nom " + strVertexName + " existe déjà.");
			bExist = true;
		} else if (strVertexName.replaceAll(" ", "").equals("")) {
			hci.setError("Le nom de votre sommet ne peut pas être vide");
			bExist = true;
>>>>>>> 90e92e350859b560c292cf78e011166293673439
		} else {
			graph.addVertex(strVertexName);
			hci.addVertex(strVertexName);
		}

		return bExist;
	}

	public void addArc(Vertex v, Vertex vBis) {
<<<<<<< HEAD
		provSave();
		if (checkArcAlreadyExist(v,vBis)) {
=======
		if (checkArcAlreadyExist(v, vBis)) {
>>>>>>> c27c84337f1f962b75f00c9ee285ad35d8f8dfe2
			graph.addArc(v, vBis);
		} else {
			hci.showError("L'arc existe déjà.");
		}
	}

	public void addArc(Vertex v, Vertex vBis, int iValue) {
<<<<<<< HEAD
		provSave();
		if (checkArcAlreadyExist(v,vBis)) {
=======
		if (checkArcAlreadyExist(v, vBis)) {
>>>>>>> c27c84337f1f962b75f00c9ee285ad35d8f8dfe2
			graph.addArc(v, vBis, iValue);
		} else {
			hci.showError("L'arc existe déjà.");
		}
	}

	public void delArc(Vertex v, Vertex vBis) {
		provSave();
		for (int i = 0; i < v.getAlArcs().size(); i++) {
			if (v.getAlArcs().get(i).getVertex() == vBis)
				graph.deleteArc(v.getAlArcs().get(i));
		}
	}

	private boolean checkArcAlreadyExist(Vertex v, Vertex vBis) {
		for (Arc arc : v.getAlArcs()) {
			if (arc.getVertex() == vBis) {
				return false;
			}
		}

		return true;
	}<<<<<<<HEAD

	=======

	public void undo() {
		if (cptModif > 0) {
			
		}
	}

	public void redo() {

	}

	>>>>>>>dbe59d79167dcc64767596d36ac62e02618989aa

	public static void main(String[] args) {
		new Controller();
	}

	public Graph getGraph() {
		return graph;
	}
	
	public void provSave() {
		saveVertexList.add(graph.getFormattedListAlString());
		Point[] tabPoint = new Point[graph.getAlVertex().size()];
		
		int cpt = 0;
		for (Point c : hci.getHmVertex().values()) {
			tabPoint[cpt] = c;
			cpt++;
		}
		
		saveCoordList.add(tabPoint);
		
		cptModif++;
	}
}