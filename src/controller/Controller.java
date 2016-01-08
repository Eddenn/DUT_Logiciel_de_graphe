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

public class Controller implements IControlable, IIhmable {
	private HCI hci;
	private Graph graph;
	private ArrayList<ArrayList<String>> saveVertexList;
	private ArrayList<Point[]> saveCoordList;
	private int cptModif;
	private static String file="";
	
	private IParcourable parcours; 

	public Controller() {
		// Create graph
		graph = new Graph(true, true);

		// Initialize the frame
		hci = new HCI(this);
		
		new StartFrame(this,hci);
		
		// Initialize the arrrayList which permit to implement the undo and redo
		initProvSave();
	}

	public void saveFile(String strFileName) {
		if(!strFileName.equals("")){
			file=strFileName;
		}
		FileWriter fw = null;

		try {
			// ouverture du fichier en mode ï¿½criture
			fw = new FileWriter(file, false);

			// ï¿½criture des lignes de texte
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
			
			// Remise a zero des sauvegarde provisoire
			initProvSave();
			
		} catch (IOException e) {
			hci.showError("Problï¿½me d'enregistrement du fichier " + file+ ".");
		}
	}

	public void initProvSave() {
		saveVertexList = new ArrayList<ArrayList<String>>();
		saveCoordList = new ArrayList<Point[]>();
		cptModif = 0;
		provSave();
	}
	
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

	public void newGraph(boolean bOriented, boolean bValued) {
		graph = new Graph(bOriented, bValued);
		hci.initHmVertex();
		hci.refresh();
	}

	public boolean addVertex(String strVertexName) {
		boolean bExist=false;
		if (graph.getVertex(strVertexName) != null) {
			hci.showError("Un sommet avec le nom " + strVertexName + " existe dï¿½jï¿½.");
			bExist = true;
		} else if (strVertexName.replaceAll(" ", "").equals("")) {
			hci.showError("Le nom de votre sommet ne peut pas ï¿½tre vide");
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
			hci.showError("L'arc existe dï¿½jï¿½.");
		}
	}

	public void addArc(Vertex v, Vertex vBis, int iValue) {
		if (checkArcAlreadyExist(v,vBis)) {
			graph.addArc(v, vBis, iValue);
			provSave();
		} else
			hci.showError("L'arc existe dï¿½jï¿½.");
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
		if (cptModif > 1) {
			graph = ReaderAdjacencyList.ReadAdjacencyList(new ArrayList<String>(saveVertexList.get(cptModif-2)));
			hci.initHmVertexByTab(saveCoordList.get(cptModif-2));
			cptModif--;
		}
		
	}

	public void redo() {
		if (cptModif >= 0 && cptModif < saveVertexList.size()) {
			graph = ReaderAdjacencyList.ReadAdjacencyList(new ArrayList<String>(saveVertexList.get(cptModif)));
			hci.initHmVertexByTab(saveCoordList.get(cptModif));
			cptModif++;
		}
	}

	public static void main(String[] args) {
		new Controller();
	}
	public Graph getGraph() {return graph;}
	@Override
	public char[] listeSommet() {return graph.getListVertex();}
	@Override
	public int[][] getMatrice() {return graph.getTMatrix();}
	@Override
	public void majIHM() {hci.refresh();}
	
	public void provSave() {
		
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
	
	/*MÃ©thodes de l'interface IIhmable */
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
	public String getFile(){
		return file;
	}
}