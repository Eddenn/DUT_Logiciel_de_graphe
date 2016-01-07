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
		cptModif = 0;
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
<<<<<<< HEAD
		boolean bExist = false;

=======
		provSave();
		saveVertexList.add(graph.getFormattedListAlString());
		cptModif++;
		
		boolean bExist=false;
>>>>>>> 33d176e7099356ac59cb41e01c1e4fe983cb46cb
		if (graph.getVertex(strVertexName) != null) {
			hci.showError("Un sommet avec le nom " + strVertexName + " existe déjà.");
			bExist = true;
		} else if (strVertexName.replaceAll(" ", "").equals("")) {
			hci.showError("Le nom de votre sommet ne peut pas être vide");
			bExist = true;
		} else {
			graph.addVertex(strVertexName);
			hci.addVertex(strVertexName);
		}
		provSave();
		return bExist;
	}

	public void addArc(Vertex v, Vertex vBis) {
<<<<<<< HEAD
		if (checkArcAlreadyExist(v, vBis)) {
=======
		provSave();
		if (checkArcAlreadyExist(v,vBis)) {
>>>>>>> 33d176e7099356ac59cb41e01c1e4fe983cb46cb
			graph.addArc(v, vBis);
		} else {
			hci.showError("L'arc existe déjà.");
		}
		provSave();
	}

	public void addArc(Vertex v, Vertex vBis, int iValue) {
<<<<<<< HEAD
		if (checkArcAlreadyExist(v, vBis)) {
=======
		provSave();
		if (checkArcAlreadyExist(v,vBis)) {
>>>>>>> 33d176e7099356ac59cb41e01c1e4fe983cb46cb
			graph.addArc(v, vBis, iValue);
		} else {
			hci.showError("L'arc existe déjà.");
		}
		provSave();
	}

	public void delArc(Vertex v, Vertex vBis) {
		for (int i = 0; i < v.getAlArcs().size(); i++) {
			if (v.getAlArcs().get(i).getVertex() == vBis)
				graph.deleteArc(v.getAlArcs().get(i));
		}
		provSave();
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
<<<<<<< HEAD
		if (cptModif > 0) {
			System.out.println(saveVertexList.get(cptModif-1));
			graph = ReaderAdjacencyList.ReadAdjacencyList(saveVertexList.get(cptModif-1));
			hci.initHmVertex();
			cptModif--;
		}
=======
		HashMap<String, ArrayList<String>> hmAdjacency = graph.generateAdjacencyList();
		System.out.println(hmAdjacency);
		// for (String key : hmAdjacency.)
>>>>>>> 33d176e7099356ac59cb41e01c1e4fe983cb46cb
	}

	public void redo() {

	}

<<<<<<< HEAD

=======
>>>>>>> 33d176e7099356ac59cb41e01c1e4fe983cb46cb
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
		ArrayList<String> alProv = graph.getFormattedListAlString();
		alProv.add(0,"Valued="+graph.isValued());
		alProv.add(0,"Directed="+graph.isDirected());
		
		if (cptModif < saveVertexList.size()) {
			for (int i = cptModif ; i < saveVertexList.size(); i++) {
				saveVertexList.remove(i);
				saveCoordList.remove(i);
			}
		}
		
		System.out.println(alProv);
		saveVertexList.add(alProv);
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