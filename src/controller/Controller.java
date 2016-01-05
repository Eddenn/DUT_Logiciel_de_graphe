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
		hci.initHmVertex();
		hci.refresh();
	}

	public void newGraph(boolean bOriented, boolean bValued) {
		graph = new Graph(bOriented, bValued);
		hci.initHmVertex();
		hci.refresh();
	}

	public void addVertex(String strVertexName) {
		// On vérifie qu'aucun sommet ne porte ce nom
		if (graph.getVertex(strVertexName) != null) {
			// Méthode à ajouter pour afficher une erreur.
		} else {
			graph.addVertex(strVertexName);
			hci.addVertex(strVertexName);
		}
	}

	public void addArc(Vertex v, Vertex vBis) {
		if (checkArcAlreadyExist(v,vBis)) {
			graph.addArc(v, vBis);
		} else {
			// Afficher message d'erreur
		}
	}

	public void addArc(Vertex v, Vertex vBis, int iValue) {
		if (checkArcAlreadyExist(v,vBis)) {
			graph.addArc(v, vBis, iValue);
		} else {
			// Afficher message d'erreur
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