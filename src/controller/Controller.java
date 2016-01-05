package controller;
import java.io.FileWriter;
import java.io.IOException;

import model.Graph;
import model.Vertex;
import view.HCI;

public class Controller {
	@SuppressWarnings("unused")
	private HCI hci;
	private Graph g;
	
	public Controller() {
		//Create graph
		g = new Graph(true, true);
		
		//Initialize the frame
		hci = new HCI(this);
	}
	
	
	public void saveFile(String strFileName) {
		FileWriter fw = null;
		
		try {
			// ouverture du fichier en mode écriture
			fw = new FileWriter(strFileName,false);
			
			// écriture des lignes de texte
			fw.write("Directed="+ g.isbDirected() + "\n");
			fw.write("Valued=" + g.isbValued() + "\n\n");
			fw.write(g.displayMatrix());
			
			// fermeture du fichier
			fw.close();
		} 
		catch (IOException e) {
			System.out.println("Problème d'écriture dans le fichier "+ strFileName + ".");
		}
	}
	
	public void loadFile(String strFileName) {
		g = ReaderMatrix.readMatrix(strFileName);
		hci.initHmVertex();
		hci.refresh();
	}
	
	public void newGraph(boolean bOriented, boolean bValued) {
		g = new Graph(bOriented,bValued);
		hci.initHmVertex();
		hci.refresh();
	}
	
	public void addVertex(String nom) {
		g.addVertex(nom);
		hci.addVertex(nom);
	}
	
	public void addArc(Vertex v, Vertex vBis) {
		g.addArc(v, vBis);
	}
	
	public void addArc(Vertex v, Vertex vBis, int iValue) {
		g.addArc(v, vBis, iValue);
	}
	
	public static void main(String[] args) {
		new Controller();
	}
	
	public Graph getGraph() {
		return g;
	}
}