package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import model.Arc;
import model.Graph;
import model.IParcourable;
import model.Vertex;

import view.FormNewGraph;
import view.HCI;

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

		/*-----Choix de l'utilisateur----*/
		//Frame pour le logo
		JFrame logoFrame = new JFrame();
		logoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel pLogo = new JPanel(){
			public void paintComponent(Graphics g){
				Image logo = null;
				try {
					logo = ImageIO.read(new File("images/Logo_LGP.png"));
				} catch (IOException e) {}
				g.drawImage(logo, 0, 0, null);
			}
		};
		pLogo.revalidate();
		pLogo.repaint();
		pLogo.setBackground(new Color(0,0,0,0));
		pLogo.setOpaque(false);
		logoFrame.add(pLogo);
		logoFrame.setUndecorated(true);
		logoFrame.setSize(530, 530);
		logoFrame.setLocationRelativeTo(null);
		logoFrame.setBackground(new Color(0,0,0,0));
				
		logoFrame.setVisible(true);
		FormNewGraph nouveauGraph = new FormNewGraph(hci, "Création d'un nouveau graphe", true, this);
		//Attend la fin de la saisie des parametres du graphe
		while(!nouveauGraph.getBEnd()){}
		logoFrame.dispose();
		hci.setVisible(true);
		/*-------------------------------*/
		
		// Initialize the arrrayList which permit to implement the undo and redo
		saveVertexList = new ArrayList<ArrayList<String>>();
		saveCoordList = new ArrayList<Point[]>();
		provSave();
		cptModif--;
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
			fw.write("IsMatrix=true\n");
			fw.write("Directed=" + graph.isDirected() + "\n");
			fw.write("Valued=" + graph.isValued() + "\n\n");
			fw.write(graph.displayMatrix());

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
		provSave();
		cptModif = 0;
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

//			System.out.println(saveVertexList);
//			System.out.println(cptModif);
		}
		
	}

	public void redo() {
		if (cptModif >= 0 && cptModif+1 < saveVertexList.size()) {
			System.out.println(cptModif);
			graph = ReaderAdjacencyList.ReadAdjacencyList(new ArrayList<String>(saveVertexList.get(cptModif+1)));
			hci.initHmVertexByTab(saveCoordList.get(cptModif+1));
			cptModif++;
//			System.out.println(saveVertexList);
//			System.out.println(cptModif);
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
		System.out.println(cptModif);
		if (cptModif < saveCoordList.size()) {
			for (int i = cptModif ; i < saveVertexList.size(); i++) {
				saveVertexList.remove(i);
			}
			for (int i = cptModif; i < saveCoordList.size(); i++) {
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
		System.out.println(saveCoordList);
//		System.out.println(saveVertexList);
//		System.out.println(cptModif);
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