package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.Controller;
import model.Graph;
import model.PdfGenerator;
import model.Vertex;


public class HCI extends JFrame implements ActionListener, ListSelectionListener {
	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = -1456746983405013221L;
	private Controller ctrl;
	private Graph graph;
	private JPopupMenu popMenu;

	// Data of Vertex
	protected static HashMap<String, Point> hmVertex;
	int xInitialize = 0, yInitialize = 0; // Used for the preferedsize of pGraph

	// Menu bar of this frame
	private JMenuBar menuBarMain;
	private JMenu menuFichier, menuEdition, menuExport, menuGraph, menuAide;
	private JMenuItem[] tabMenuItemFile = new JMenuItem[6];
	private JMenuItem[] tabMenuItemEdition = new JMenuItem[6];
	private JMenuItem[] tabMenuItemExport = new JMenuItem[2];
	private JMenuItem[] tabMenuItemGraph = new JMenuItem[5];
	private JMenuItem[] tabMenuItemAide = new JMenuItem[1];

	// List of "Object"
	private SwitchList slObject;

	// Main graph (draw)
	private GraphPanel pGraph;
	private JScrollPane jscrPanel;

	private JLabel lCoord;

	// Panel of JButton
	private JPanel pButton;
	private JButton buttonNew, buttonOpen, buttonSave, buttonZoomIn, buttonZoomOut;

	// Items du menu contextuel
	private JMenuItem popUpAddVertex;
	private JMenuItem popUpAddArc;
	private JMenuItem popUpEditVertex;
	private JMenuItem popUpDeleteVertex;
	private JMenuItem popUpDeleteArc;

	public HCI(Controller controller) {
		this.ctrl = controller;
		this.graph = controller.getGraph();

		// basic parameters of this frame
		this.setTitle("Logiciel pedagogique de graph");

		this.setSize(900, 700);
		setPreferredSize(new Dimension(900, 700));
		this.setLocation(250, 0);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);


		// **---Contents of this frame---**//
		// ---Menu bar---//
		menuBarMain = new JMenuBar();
		menuFichier = new JMenu("Fichier");

		/*
		 * Tips : &nbsp; = normal space &thinsp; = thin space &emsp; = 4 normal
		 * spaces
		 */
		// MenuItem - Nouveau
		tabMenuItemFile[0] = new JMenuItem(
				"<html>Nouveau...&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;<i>Ctrl+N</i></html>");
		tabMenuItemFile[0].addActionListener(this);
		menuFichier.add(tabMenuItemFile[0]);

		// MenuItem - Ouvrir
		tabMenuItemFile[1] = new JMenuItem(
				"<html>Ouvrir...&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;<i>Ctrl+O</i></html>");
		tabMenuItemFile[1].addActionListener(this);
		menuFichier.add(tabMenuItemFile[1]);

		// Separator
		menuFichier.addSeparator();

		// MenuItem - Enregistrer
		tabMenuItemFile[2] = new JMenuItem(
				"<html>Enregister&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;<i>Ctrl+S</i></html>");
		tabMenuItemFile[2].addActionListener(this);
		menuFichier.add(tabMenuItemFile[2]);

		// MenuItem - Enregistrer sous
		tabMenuItemFile[3] = new JMenuItem("<html>Enregister sous...&emsp;<i>Ctrl+Maj+S</i></html>");
		tabMenuItemFile[3].addActionListener(this);
		menuFichier.add(tabMenuItemFile[3]);

		// Separator
		menuFichier.addSeparator();

		// MenuItem - Imprimer
		tabMenuItemFile[4] = new JMenuItem(
				"<html>Imprimer&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;&nbsp;<i>Ctrl+P</i></html>");
		tabMenuItemFile[4].addActionListener(this);
		menuFichier.add(tabMenuItemFile[4]);

		// Separator
		menuFichier.addSeparator();

		// MenuItem - Quitter
		tabMenuItemFile[5] = new JMenuItem("<html>Quitter</html>");
		tabMenuItemFile[5].addActionListener(this);
		menuFichier.add(tabMenuItemFile[5]);

		// Add menuFichier to this frame
		menuBarMain.add(menuFichier);

		menuEdition = new JMenu("Edition");

		// MenuItem - Annuler
		tabMenuItemEdition[0] = new JMenuItem(
				"<html>Annuler&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;&nbsp;&thinsp;<i>Ctrl+Z</i></html>");
		tabMenuItemEdition[0].addActionListener(this);
		menuEdition.add(tabMenuItemEdition[0]);

		// MenuItem - Rï¿½pï¿½ter
		tabMenuItemEdition[1] = new JMenuItem(
				"<html>Rï¿½pï¿½ter&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;&nbsp;<i>Ctrl+Y</i></html>");
		tabMenuItemEdition[1].addActionListener(this);
		menuEdition.add(tabMenuItemEdition[1]);

		// Separator
		menuEdition.addSeparator();

		// MenuItem - Couper
		tabMenuItemEdition[2] = new JMenuItem("<html>Couper&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<i>Ctrl+X</i></html>");
		tabMenuItemEdition[2].addActionListener(this);
		menuEdition.add(tabMenuItemEdition[2]);

		// MenuItem - Copier
		tabMenuItemEdition[3] = new JMenuItem(
				"<html>Copier&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;<i>Ctrl+C</i></html>");
		tabMenuItemEdition[3].addActionListener(this);
		menuEdition.add(tabMenuItemEdition[3]);

		// MenuItem - Coller
		tabMenuItemEdition[4] = new JMenuItem(
				"<html>Coller&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;<i>Ctrl+V</i></html>");
		tabMenuItemEdition[4].addActionListener(this);
		menuEdition.add(tabMenuItemEdition[4]);

		// Separator
		menuEdition.addSeparator();

		// MenuItem - Selectionner tout
		tabMenuItemEdition[5] = new JMenuItem("<html>Sï¿½lectionner tout&emsp;&nbsp;&nbsp;<i>Ctrl+A</i></html>");
		tabMenuItemEdition[5].addActionListener(this);
		menuEdition.add(tabMenuItemEdition[5]);

		// Add menuEdition to this frame
		menuBarMain.add(menuEdition);

		// Export
		menuExport = new JMenu("Exporter");
		// Menu Item - Image
		tabMenuItemExport[0] = new JMenuItem("Image   ");
		tabMenuItemExport[0].addActionListener(this);
		menuExport.add(tabMenuItemExport[0]);
		// MenuItem - PDF
		tabMenuItemExport[1] = new JMenuItem("Fichier PDF  ");
		tabMenuItemExport[1].addActionListener(this);
		menuExport.add(tabMenuItemExport[1]);

		menuBarMain.add(menuExport);

		menuGraph = new JMenu("Graph");

		// MenuItem - Ajouter un sommet
		tabMenuItemGraph[0] = new JMenuItem("<html>Ajouter un sommet</html>");
		tabMenuItemGraph[0].addActionListener(this);
		menuGraph.add(tabMenuItemGraph[0]);

		// MenuItem - Modifier un sommet
		tabMenuItemGraph[1] = new JMenuItem("<html>Modifier un sommet</html>");
		tabMenuItemGraph[1].addActionListener(this);
		menuGraph.add(tabMenuItemGraph[1]);

		// MenuItem - Supprimer un sommet
		tabMenuItemGraph[2] = new JMenuItem("<html>Supprimer un sommet</html>");
		tabMenuItemGraph[2].addActionListener(this);
		menuGraph.add(tabMenuItemGraph[2]);

		// Separator
		menuGraph.addSeparator();

		// MenuItem - Ajouter un arc
		tabMenuItemGraph[3] = new JMenuItem("<html>Ajouter un arc</html>");
		tabMenuItemGraph[3].addActionListener(this);
		menuGraph.add(tabMenuItemGraph[3]);

		// MenuItem - Ajouter un arc
		tabMenuItemGraph[4] = new JMenuItem("<html>Supprimer un arc</html>");
		tabMenuItemGraph[4].addActionListener(this);
		menuGraph.add(tabMenuItemGraph[4]);

		// Add menuGraph to this frame
		menuBarMain.add(menuGraph);

		menuAide = new JMenu("Aide");

		// MenuItem - A Propos
		tabMenuItemAide[0] = new JMenuItem("<html>&Agrave; propos</html>");
		tabMenuItemAide[0].addActionListener(this);
		menuAide.add(tabMenuItemAide[0]);

		// Add menuAide to this frame
		menuBarMain.add(menuAide);

		menuBarMain.setPreferredSize(new Dimension(250, 20));
		setJMenuBar(menuBarMain);
		// -----------//

		// ---List of "Object" --//
		slObject = new SwitchList(this);
		slObject.setBorder(BorderFactory.createLineBorder(Color.black));
		add(slObject, BorderLayout.WEST);
		slObject.switchState();
		// ----------------------//

		// ---------Graph--------//
		pGraph = new GraphPanel(this);
		jscrPanel = new JScrollPane(pGraph);

		JPanel panelCenter = new JPanel(new BorderLayout());
		lCoord = new JLabel("");
		panelCenter.add(jscrPanel);
		panelCenter.add(lCoord, "South");
		add(panelCenter, BorderLayout.CENTER);
		// ----------------------//

		// -------ButtonBar------//
		pButton = new JPanel();
		pButton.setBackground(Color.WHITE);
		pButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		pButton.setBorder(BorderFactory.createLineBorder(Color.black));

		// New file
		buttonNew = new JButton("Nouveau");
		buttonNew.addActionListener(this);
		pButton.add(buttonNew);
		// Open file
		buttonOpen = new JButton("Ouvrir");
		buttonOpen.addActionListener(this);
		pButton.add(buttonOpen);
		// Save file
		buttonSave = new JButton("Enregistrer");
		buttonSave.addActionListener(this);
		pButton.add(buttonSave);
		// Zoom in
		buttonZoomIn = new JButton("+");
		buttonZoomIn.addActionListener(this);
		pButton.add(buttonZoomIn);
		// Zoom out
		buttonZoomOut = new JButton("-");
		buttonZoomOut.addActionListener(this);
		pButton.add(buttonZoomOut);

		add(pButton, BorderLayout.NORTH);
<<<<<<< HEAD
=======
		
		// Instancitation du menu contextuel et de ses Ã©lÃ©ments
		popMenu = new JPopupMenu();

		popUpAddVertex = new JMenuItem("Ajouter un sommet");
		popUpAddVertex.addActionListener(this);
		popMenu.add(popUpAddVertex);

		popUpEditVertex = new JMenuItem("Modifier un sommet");
		popUpEditVertex.addActionListener(this);
		popMenu.add(popUpEditVertex);

		popUpDeleteVertex = new JMenuItem("Supprimer un sommet");
		popUpDeleteVertex.addActionListener(this);
		popMenu.add(popUpDeleteVertex);

		popUpAddArc = new JMenuItem("Ajouter un arc");
		popUpAddArc.addActionListener(this);
		popMenu.add(popUpAddArc);

		popUpDeleteArc = new JMenuItem("Supprimer un arc");
		popUpDeleteArc.addActionListener(this);
		popMenu.add(popUpDeleteArc);
>>>>>>> 33d176e7099356ac59cb41e01c1e4fe983cb46cb
		// ----------------------//

		// **---------------------------**//

		initHmVertex();

		pGraph.setPreferredSize(
				new Dimension((int) (600 + pGraph.getiWidthEdge()), (int) (yInitialize + pGraph.getiHeightEdge())));
		pack();
<<<<<<< HEAD
=======
		this.addKeyListener(pGraph);
>>>>>>> 33d176e7099356ac59cb41e01c1e4fe983cb46cb
		setVisible(true);
	}

	public void initHmVertex() {
		xInitialize = 0;
		yInitialize = 0;
		// Initialization of hmVertex
		hmVertex = new HashMap<String, Point>();
		for (Vertex v : ctrl.getGraph().getAlVertex()) {
			hmVertex.put(v.getName(), new Point(xInitialize, yInitialize));

			if (xInitialize < 600 - pGraph.getiWidthEdge()) {
				xInitialize += pGraph.getiWidthEdge() * 2;
			} else {
				yInitialize += pGraph.getiHeightEdge() * 2;
				xInitialize = 0;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Switch the state of the SwitchList
		if (e.getSource() == slObject.getJBSwitch()) {
			slObject.switchState();

			// FICHIER
			// Nouveau
		} else if (e.getSource() == tabMenuItemFile[0] || e.getSource() == buttonNew) {
			new FormNewGraph(this, "Création d'un nouveau graphe", true, ctrl);
			// Ouvrir
		} else if (e.getSource() == tabMenuItemFile[1] || e.getSource() == buttonOpen) {
			JFileChooser dial = new JFileChooser(new File("."));
			if (dial.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
				ctrl.loadFile(dial.getSelectedFile().getAbsolutePath());
			// Enregistrer sous
		} else if (e.getSource() == tabMenuItemFile[3]) {
			JFileChooser dial = new JFileChooser(new File("."));
			if (dial.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
				ctrl.saveFile(dial.getSelectedFile().getAbsolutePath() + ".txt");
			// Quitter
		} else if (e.getSource() == tabMenuItemFile[5]) {
			this.dispose();

			// EDITION
		} else if (e.getSource() == tabMenuItemEdition[0]) {
				ctrl.undo();
				this.refresh();
		
			// EXPORTER
			// Image
		} else if (e.getSource() == tabMenuItemExport[0]) {
			expImage();
			// PDF
		} else if (e.getSource() == tabMenuItemExport[1]) {
			JFileChooser dial = new JFileChooser(new File("."));
			if (dial.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
				PdfGenerator.generer(graph, dial.getName(), dial.getSelectedFile().getAbsolutePath() + ".pdf", this);

			// GRAPH
			// Ajouter un sommet
		} else if (e.getSource() == tabMenuItemGraph[0]) {
			new Form(this, "Ajouter un sommet", true, ctrl);
			// Modifier un sommet
		} else if (e.getSource() == tabMenuItemGraph[1]) {
			if(pGraph.getAlSelected().size() > 1 ) {
				showError("Veuilliez sélectionné un seul sommet.");
			} else if (pGraph.getAlSelected().size() == 0) {
				showError("Veuilliez sélectionné un sommet.");
			} else {
				new Form(this, "Modifier un sommet", true, ctrl);
			}
			// Supprimer un sommet
		} else if (e.getSource() == tabMenuItemGraph[2]) {
			for(String s : pGraph.getAlSelected()) {
				Vertex tmpVertex = null;
				for (Vertex v : ctrl.getGraph().getAlVertex()) {
					if (v.getName().equals(s)) {
						tmpVertex = v;
					}
				}
				ctrl.getGraph().deleteVertex(tmpVertex);
				HCI.hmVertex.remove(s);
			}
			setAlSelected(new ArrayList<String>());
			refresh();
			// Ajouter un arc
		} else if (e.getSource() == tabMenuItemGraph[3]) {
			new FormAddArc(this, "Ajout d'un arc", true, ctrl);

			// Supprimer un arc
		} else if (e.getSource() == tabMenuItemGraph[4]) {
			for(String s : pGraph.getAlSelected()) {
				if (s.matches("[?{5}-{6}?{6}])")) {
					String vName = s.substring(0, 5);
					vName = vName.replaceAll(" ", "");
					
					String vBisName = s.substring(12);
					vBisName = vBisName.replaceAll(" ", "");
					
					System.out.println(vName + vBisName);
					
				    ctrl.delArc(graph.getVertex(vName), graph.getVertex(vBisName));		
				}
			}
			setAlSelected(new ArrayList<String>());
			refresh();
		}

		// AIDE
		else if (e.getSource() == tabMenuItemAide[0]) { // A propos
			JOptionPane.showMessageDialog(this,
					"<html>Projet tuteuré de deuxième année de DUT Informatique.<br/><center><h3>Groupe 3</h3>Alouache Mehdi<br/>Cavelier Guillaume<br/>Douchinï¿½Nicolas<br/>Dumont Mï¿½lanie<br/>Hazard Alexandre</center></html>",
					"A propos", 1);

			// BUTTON
			// Zoom in
		} else if (e.getSource() == buttonZoomIn) {
			pGraph.zoomIn();
			pGraph.repaint();
			pGraph.revalidate();
			// Zoom out
		} else if (e.getSource() == buttonZoomOut) {
			pGraph.zoomOut();
			pGraph.repaint();
			pGraph.revalidate();

			// Items du menu contextuel
		} else if (e.getSource() == popUpAddArc) {
			new FormAddArc(this, "Ajouter un arc", true, ctrl);

		} else if (e.getSource() == popUpAddVertex) {
			new Form(this, "Ajouter un sommet", true, ctrl);

		} else if (e.getSource() == popUpEditVertex) {
			new FormAddArc(this, "Modifier un sommet", true, ctrl);

			/* Non implÃ©mentÃ©e pour le moment */
		} // else if (e.getSource() == popUpDeleteArc) {
			// new FormAddArc(this, "Ajouter un arc", true, ctrl);
			//
			// }else if (e.getSource() == popUpEditArc) {
			// new FormAddArc(this, "Mofifier un arc", true, ctrl);
			// }

		refresh();
	}

	private void expImage() {
		FileNameExtensionFilter filterPNG = new FileNameExtensionFilter("PNG (*.png)", ".png");
		FileNameExtensionFilter filterJPG = new FileNameExtensionFilter("JPEG (*.jpg;*.jpeg;*.jpe;*.jfif)", ".jpg");
		FileNameExtensionFilter filterGIF = new FileNameExtensionFilter("GIF (*.gif)", ".gif");

		JFileChooser dial = new JFileChooser(new File("."));
		dial.setAcceptAllFileFilterUsed(false);
		dial.addChoosableFileFilter(filterPNG);
		dial.addChoosableFileFilter(filterJPG);
		dial.addChoosableFileFilter(filterGIF);

		if (dial.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			String ext = "";
			String extension = dial.getFileFilter().getDescription();

			if (extension.equals("PNG (*.png)")) {
				ext = "png";
			}

			if (extension.equals("JPEG (*.jpg;*.jpeg;*.jpe;*.jfif)")) {
				ext = "jpg";
			}

			if (extension.equals("GIF (*.gif)")) {
				ext = "gif";
			}

			Dimension size = pGraph.getSize();
			BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = image.createGraphics();
			pGraph.paint(g2);

			try {
				ImageIO.write(image, ext, new File(dial.getSelectedFile().getAbsolutePath() + "." + ext));
			} catch (Exception ex) {
			}
		}
	}

	// Utiliser pour centrer une chaine de caractere
	public static String centerStr(String str, int size) {
		if (str == null || size <= str.length())
			return str;

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < (size - str.length()) / 2; i++) {
			sb.append(" ");
		}

		sb.append(str);

		while (sb.length() < size) {
			sb.append(" ");
		}

		return sb.toString();
	}

	public void addVertex(String strName) {
		hmVertex.put(strName, new Point(0, 0));
		slObject.refresh();
	}

	public void refresh() {
		graph = ctrl.getGraph();
		slObject.refresh();
		repaint();
	}

	public ArrayList<String> getAlSelected() {
		return pGraph.getAlSelected();
	}

	public HashMap<String, Point> getHmVertex() {
		return hmVertex;
	}

	public void setAlSelected(ArrayList<String> s) {
		pGraph.setAlSelected(s);
	}

	public Graph getGraph() {
		return graph;
	}

	public GraphPanel getGraphPanel() {
		return pGraph;
	}

	public JLabel getLabelCoord() {
		return this.lCoord;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == slObject.getListOfObject()) {
			pGraph.getAlSelected().clear();
			for( String s : slObject.getListOfObject().getSelectedValuesList() ) {
				pGraph.getAlSelected().add(s);
			}
			repaint();
		}
	}

	public void showError(String strError) {
		JOptionPane.showMessageDialog(null, strError, "Erreur", JOptionPane.ERROR_MESSAGE);
	}

	public JPopupMenu getPopMenu() { return this.popMenu;}
}
