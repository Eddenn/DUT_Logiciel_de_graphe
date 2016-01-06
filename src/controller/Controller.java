package controller;

import java.io.FileWriter;
import java.io.IOException;

import model.Arc;
import model.Graph;
import model.Vertex;
import view.HCI;

public class Controller {
	private HCI hci;
	private Graph graph;

	public Controller() {
		// Create graph
		graph = new Graph(true, true);

		// Initialize the frame
		hci = new HCI(this);
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
		graph = ReaderMatrix.readMatrix(strFileName);
		
		if (graph == null) {
			graph = new Graph(true,true);
			hci.setError("Format du fichier invalide.");
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
			hci.setError("Un sommet avec le nom " + strVertexName + " existe déjà.");
			bExist=true;
		} else {
			graph.addVertex(strVertexName);
			hci.addVertex(strVertexName);
		}
		return bExist;
	}

	public void addArc(Vertex v, Vertex vBis) {
		if (checkArcAlreadyExist(v,vBis)) {
			graph.addArc(v, vBis);
		} else {
			hci.setError("L'arc existe déjà.");
		}
	}

	public void addArc(Vertex v, Vertex vBis, int iValue) {
		if (checkArcAlreadyExist(v,vBis)) {
			graph.addArc(v, vBis, iValue);
		} else {
			hci.setError("L'arc existe déjà.");
		}
	}
	
	public void delArc(Vertex v, Vertex vBis) {
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
	}
	
	public static void main(String[] args) {
		new Controller();
	}

	public Graph getGraph() {
		return graph;
	}
}