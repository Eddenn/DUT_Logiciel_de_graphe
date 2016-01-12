package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

/**
 * Classe principale de l'IHM
 * @author Groupe 3
 * @version 2016-01-12
 */
public class HCI extends JFrame implements ActionListener, ListSelectionListener {
	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = -1456746983405013221L;
	private Controller ctrl;
	private Graph graph;
	private JPopupMenu popMenu;

	// Données du sommet
	private HashMap<String, Point> hmVertex;
	int xInitialize = 0, yInitialize = 0; // Used for the preferedsize of pGraph

	// Menu bar of this frame
	private JMenuBar menuBarMain;
	private JMenu menuFichier, menuEdition, menuExport, menuGraph, menuAide;
	private JMenuItem[] tabMenuItemFile = new JMenuItem[6];
	private JMenuItem[] tabMenuItemEdition = new JMenuItem[6];
	private JMenuItem[] tabMenuItemExport = new JMenuItem[2];
	private JMenuItem[] tabMenuItemGraph = new JMenuItem[7];
	private JMenuItem[] tabMenuItemAide = new JMenuItem[2];

	// List of "Object"
	private SwitchList slObject;

	// Main graph (draw)
	private GraphPanel pGraph;
	private JScrollPane jscrPanel;

	private JLabel lCoord;

	// Panel of JButton
	private JPanel pButton;
	private JButton buttonNew, buttonOpen, buttonSave, buttonZoomIn, buttonZoomOut, buttonUndo, buttonRedo, buttonSetting;

	// Items du menu contextuel
	private JMenuItem[] popUpItem = new JMenuItem[7];


	public HCI(Controller controller) {
		this.ctrl = controller;
		this.graph = controller.getGraph();
		
		// basic parameters of this frame
		this.setTitle("Logiciel pédagogique d'édition de graphe");

		this.setSize(900, 700);
		this.setPreferredSize(new Dimension(900, 700));
		ImageIcon img = new ImageIcon("images/Logo_32x32_LGP.png");
		this.setIconImage(img.getImage());
		this.setLocationRelativeTo(null);
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

		// MenuItem - Répéter
		tabMenuItemEdition[1] = new JMenuItem("<html>Répéter&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;&nbsp;<i>Ctrl+Y</i></html>");
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
		tabMenuItemEdition[5] = new JMenuItem("<html>Sélectionner tout&emsp;&nbsp;&nbsp;<i>Ctrl+A</i></html>");
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

		menuGraph = new JMenu("Graphe");

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
		
		// MenuItem - Coloriser un sommet
		tabMenuItemGraph[3] = new JMenuItem("<html>Coloriser un sommet</html>");
		tabMenuItemGraph[3].addActionListener(this);
		menuGraph.add(tabMenuItemGraph[3]);

		// Separator
		menuGraph.addSeparator();

		// MenuItem - Ajouter un arc
		tabMenuItemGraph[4] = new JMenuItem("<html>Ajouter un arc</html>");
		tabMenuItemGraph[4].addActionListener(this);
		menuGraph.add(tabMenuItemGraph[4]);

		// MenuItem - Modifier un arc
		tabMenuItemGraph[5] = new JMenuItem("<html>Modifier un arc</html>");
		tabMenuItemGraph[5].addActionListener(this);
		menuGraph.add(tabMenuItemGraph[5]);
		
		// MenuItem - Supprimer un arc
		tabMenuItemGraph[6] = new JMenuItem("<html>Supprimer un arc</html>");
		tabMenuItemGraph[6].addActionListener(this);
		menuGraph.add(tabMenuItemGraph[6]);

		// Add menuGraph to this frame
		menuBarMain.add(menuGraph);

		menuAide = new JMenu("Aide");

		// MenuItem - A Propos
		tabMenuItemAide[0] = new JMenuItem("<html>&Agrave; propos</html>");
		tabMenuItemAide[0].addActionListener(this);
		menuAide.add(tabMenuItemAide[0]);
		
		//Menu Item - Manuel Utilisateur
		tabMenuItemAide[1]=new JMenuItem("Manuel Utilisateur");
		tabMenuItemAide[1].addActionListener(this);
		menuAide.add(tabMenuItemAide[1]);

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
		pGraph = new GraphPanel(this, ctrl);
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
		buttonNew = new JButton(new ImageIcon("images/nouveau.png"));
		buttonNew.setContentAreaFilled(false);
		buttonNew.setBorderPainted(false);
		buttonNew.setRolloverIcon(new ImageIcon("images/nouveau_rollover.png"));
		buttonNew.setMargin(new Insets(0, 0, 0, 0));
		buttonNew.setBackground(new Color(255,255,255));
		buttonNew.setForeground(new Color(255,255,255));
		//buttonNew.setSize(1, height);
		buttonNew.setToolTipText("Nouveau");
		buttonNew.addActionListener(this);
		pButton.add(buttonNew);
		// Open file
		buttonOpen = new JButton(new ImageIcon("images/ouvrir.png"));
		buttonOpen.setContentAreaFilled(false);
		buttonOpen.setBorderPainted(false);
		buttonOpen.setRolloverIcon(new ImageIcon("images/ouvrir_rollover.png"));
		buttonOpen.setMargin(new Insets(0, 0, 0, 0));
		buttonOpen.setBackground(new Color(255,255,255));
		buttonOpen.setForeground(new Color(255,255,255));
		buttonOpen.setToolTipText("Ouvrir");
		buttonOpen.addActionListener(this);
		pButton.add(buttonOpen);
		// Save file
		buttonSave = new JButton(new ImageIcon("images/enregistrer.png"));
		buttonSave.setContentAreaFilled(false);
		buttonSave.setBorderPainted(false);
		buttonSave.setRolloverIcon(new ImageIcon("images/enregistrer_rollover.png"));
		buttonSave.setMargin(new Insets(0, 0, 0, 0));
		buttonSave.setBackground(new Color(255,255,255));
		buttonSave.setForeground(new Color(255,255,255));
		buttonSave.setToolTipText("Enregistrer");
		buttonSave.addActionListener(this);
		pButton.add(buttonSave);
		// Zoom in
		buttonZoomIn = new JButton(new ImageIcon("images/zoom_In.png"));
		buttonZoomIn.setContentAreaFilled(false);
		buttonZoomIn.setBorderPainted(false);
		buttonZoomIn.setRolloverIcon(new ImageIcon("images/zoom_In_rollover.png"));
		buttonZoomIn.setMargin(new Insets(0, 0, 0, 0));
		buttonZoomIn.setBackground(new Color(255,255,255));
		buttonZoomIn.setForeground(new Color(255,255,255));
		buttonZoomIn.setToolTipText("Agrandir");
		buttonZoomIn.addActionListener(this);
		pButton.add(buttonZoomIn);
		// Zoom out
		buttonZoomOut = new JButton(new ImageIcon("images/zoom_Out.png"));
		buttonZoomOut.setContentAreaFilled(false);
		buttonZoomOut.setBorderPainted(false);
		buttonZoomOut.setRolloverIcon(new ImageIcon("images/zoom_Out_rollover.png"));
		buttonZoomOut.setMargin(new Insets(0, 0, 0, 0));
		buttonZoomOut.setBackground(new Color(255,255,255));
		buttonZoomOut.setForeground(new Color(255,255,255));
		buttonZoomOut.setToolTipText("Réduire");
		buttonZoomOut.addActionListener(this);
		pButton.add(buttonZoomOut);
		
		// Undo
		buttonUndo = new JButton(new ImageIcon("images/undo.png"));
		buttonUndo.setContentAreaFilled(false);
		buttonUndo.setBorderPainted(false);
		buttonUndo.setRolloverIcon(new ImageIcon("images/undo_rollover.png"));
		buttonUndo.setMargin(new Insets(0, 0, 0, 0));
		buttonUndo.setBackground(new Color(255,255,255));
		buttonUndo.setForeground(new Color(255,255,255));
		buttonUndo.setToolTipText("Undo");
		buttonUndo.addActionListener(this);
		pButton.add(buttonUndo);
		
		// Redo
		buttonRedo = new JButton(new ImageIcon("images/redo.png"));
		buttonRedo.setContentAreaFilled(false);
		buttonRedo.setBorderPainted(false);
		buttonRedo.setRolloverIcon(new ImageIcon("images/redo_rollover.png"));
		buttonRedo.setMargin(new Insets(0, 0, 0, 0));
		buttonRedo.setBackground(new Color(255,255,255));
		buttonRedo.setForeground(new Color(255,255,255));
		buttonRedo.setToolTipText("Redo");
		buttonRedo.addActionListener(this);
		pButton.add(buttonRedo);
		
		// Parametre
		buttonSetting = new JButton(new ImageIcon("images/parametre.png"));
		buttonSetting.setContentAreaFilled(false);
		buttonSetting.setBorderPainted(false);
		buttonSetting.setRolloverIcon(new ImageIcon("images/parametre_rollover.png"));
		buttonSetting.setMargin(new Insets(0, 0, 0, 0));
		buttonSetting.setBackground(new Color(255,255,255));
		buttonSetting.setForeground(new Color(255,255,255));
		buttonSetting.setToolTipText("Paramètres");
		buttonSetting.addActionListener(this);
		pButton.add(buttonSetting);

		add(pButton, BorderLayout.NORTH);
		
		// Instancitation du menu contextuel et de ses Ã©lÃ©ments
		popMenu = new JPopupMenu();
		// MenuItem - Ajouter un sommet
		popUpItem[0] = new JMenuItem("<html>Ajouter un sommet</html>");
		popUpItem[0].addActionListener(this);
		popMenu.add(popUpItem[0]);

		// MenuItem - Modifier un sommet
		popUpItem[1] = new JMenuItem("<html>Modifier un sommet</html>");
		popUpItem[1].addActionListener(this);
		popMenu.add(popUpItem[1]);

		// MenuItem - Supprimer un sommet
		popUpItem[2] = new JMenuItem("<html>Supprimer un sommet</html>");
		popUpItem[2].addActionListener(this);
		popMenu.add(popUpItem[2]);

		// Separator
		popMenu.addSeparator();
		
		// MenuItem - Coloriser un sommet
		popUpItem[3] = new JMenuItem("<html>Coloriser un sommet</html>");
		popUpItem[3].addActionListener(this);
		popMenu.add(popUpItem[3]);
		
		// Separator
		popMenu.addSeparator();

		// MenuItem - Ajouter un arc
		popUpItem[4] = new JMenuItem("<html>Ajouter un arc</html>");
		popUpItem[4].addActionListener(this);
		popMenu.add(popUpItem[4]);

		// MenuItem - Modifier un arc
		popUpItem[5] = new JMenuItem("<html>Modifier un arc</html>");
		popUpItem[5].addActionListener(this);
		popMenu.add(popUpItem[5]);
		
		// MenuItem - Supprimer un arc
		popUpItem[6] = new JMenuItem("<html>Supprimer un arc</html>");
		popUpItem[6].addActionListener(this);
		popMenu.add(popUpItem[6]);

		// ----------------------//

		// **---------------------------**//

		initHmVertex();

		pGraph.setPreferredSize(
				new Dimension((int) (600 + pGraph.getiWidthEdge()), (int) (yInitialize + pGraph.getiHeightEdge())));
		pack();
		
		this.addKeyListener(pGraph);
	}

	/**
	 * Méthode qui initialise l'HashMap de sommet
	 */
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
	
	public void initHmVertexByTab(Point[] tab) {
		// Initialization of hmVertex
		hmVertex = new HashMap<String, Point>();
		
		ArrayList<Vertex> alVertex = ctrl.getGraph().getAlVertex();
		for (int i = 0; i < ctrl.getGraph().getAlVertex().size(); i++) {
			hmVertex.put(alVertex.get(i).getName(), tab[i]);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Switch the state of the SwitchList
		if (e.getSource() == slObject.getJBSwitch()) {
			slObject.switchState();

		/*-- FICHIER --*/
			// Nouveau
		} else if (e.getSource() == tabMenuItemFile[0] || e.getSource() == buttonNew) {
			new PopupNewGraph("Création d'un nouveau graphe", true, ctrl, this);
			// Ouvrir
		} else if (e.getSource() == tabMenuItemFile[1] || e.getSource() == buttonOpen) {
			JFileChooser dial = new JFileChooser(new File("."));
			if (dial.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
				ctrl.loadFile(dial.getSelectedFile().getAbsolutePath());
			// Enregistrer
		} else if (e.getSource()==tabMenuItemFile[2] || e.getSource()==buttonSave) {
			if(ctrl.getFile().equals("")){
				JFileChooser dial = new JFileChooser(new File("."));
				if (dial.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
					String path = dial.getSelectedFile().getAbsolutePath();
					if(path.substring(path.lastIndexOf("\\")).lastIndexOf(".")!=-1) {
						ctrl.saveFile(path.substring(0,path.lastIndexOf(".")) + ".txt");
					} else {
						ctrl.saveFile(path + ".txt");
					}
				}
			}else{
				ctrl.saveFile("");
			}
			// Enregistrer sous
		} else if (e.getSource()==tabMenuItemFile[3]) {
			JFileChooser dial = new JFileChooser(new File("."));
			if (dial.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				String path = dial.getSelectedFile().getAbsolutePath();
				if(path.substring(path.lastIndexOf("\\")).lastIndexOf(".")!=-1) {
					ctrl.saveFile(path.substring(0,path.lastIndexOf(".")) + ".txt");
				} else {
					ctrl.saveFile(path + ".txt");
				}
			}
			// Quitter
		} else if (e.getSource() == tabMenuItemFile[5]) {
			this.dispose();

		/*-- EDITION --*/
		} else if (e.getSource() == tabMenuItemEdition[0] || e.getSource() == buttonUndo) {
				ctrl.undo();
				this.refresh();
		} else if (e.getSource() == tabMenuItemEdition[1] || e.getSource() == buttonRedo) {
				ctrl.redo();
				this.refresh();
		
		/*-- EXPORTER --*/
			// Image
		} else if (e.getSource() == tabMenuItemExport[0]) {
			expImage();
			// PDF
		} else if (e.getSource() == tabMenuItemExport[1]) {
			JFileChooser dial = new JFileChooser(new File("."));
			if (dial.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
				PdfGenerator.generer(graph, dial.getName(), dial.getSelectedFile().getAbsolutePath() + ".pdf", this);
			
		/*-- GRAPH --*/
			// Ajouter un sommet
		} else if (e.getSource() == tabMenuItemGraph[0] || e.getSource() == popUpItem[0]) {
			new PopupAddVertex("Ajouter un sommet", true, ctrl, this);
			
			// Modifier un sommet
		} else if (e.getSource() == tabMenuItemGraph[1] || e.getSource() == popUpItem[1]) {
			if(pGraph.getAlSelected().size() > 1 ) {
				showError("Veuillez sélectionner un seul sommet.");
			} else if (pGraph.getAlSelected().size() == 0) {
				showError("Veuillez sélectionner un sommet.");
			} else {
				new PopupUpdateVertex("Modifier un sommet", true, ctrl, this);
			}
			
			// Supprimer un sommet
		} else if (e.getSource() == tabMenuItemGraph[2] || e.getSource() == popUpItem[2]) {
			for(String s : pGraph.getAlSelected()) {
				Vertex tmpVertex = null;
				for (Vertex v : ctrl.getGraph().getAlVertex()) {
					if (v.getName().equals(s)) {
						tmpVertex = v;
					}
				}
				ctrl.getGraph().deleteVertex(tmpVertex);
				this.hmVertex.remove(s);
			}
			setAlSelected(new ArrayList<String>());
			refresh();
			// Coloriser un sommet
		} else if (e.getSource() == tabMenuItemGraph[3] || e.getSource() == popUpItem[3]) {
			new PopupColorizeVertex("Coloriser un sommet", true, ctrl, this);

			// Ajouter un arc
		} else if (e.getSource() == tabMenuItemGraph[4] || e.getSource() == popUpItem[4]) {
			new PopupAddArc("Ajout d'un arc", true, ctrl, this);
			
			// Mofidier un arc
		} else if (e.getSource() == tabMenuItemGraph[5] || e.getSource() == popUpItem[5]) {
			new PopupUpdateArc("Modifier un arc", true, ctrl, this);

			// Supprimer un arc
		} else if (e.getSource() == tabMenuItemGraph[6] || e.getSource() == popUpItem[6]) {
			new PopupDeleteArc("Supprimer un arc", true, ctrl, this);
		}

		/*-- AIDE --*/
		else if (e.getSource() == tabMenuItemAide[0]) { // A propos
			JOptionPane.showMessageDialog(this,
					"<html>Projet tuteuré de deuxiÃ¨me année de DUT Informatique.<br/><center><h3>Groupe 3</h3>Alouache Mehdi<br/>Cavelier Guillaume<br/>Douchinï¿½Nicolas<br/>Dumont Mï¿½lanie<br/>Hazard Alexandre</center></html>",
					"A propos", 1);
		}else if(e.getSource()==tabMenuItemAide[1]){
			ctrl.openHelp();

		/*-- BUTTON --*/
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
			// Paramètres
		} else if (e.getSource() == buttonSetting) {
			new PopupSetting("Paramètres",true,ctrl,this);
		} 
		refresh();
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
	
	/**
	 * Méthode permettant d'exporter le grpahe en image.
	 */
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

	/**
	 * Méthode utiliser pour centrer une chaine de caractère
	 * @param str
	 * @param size
	 * @return
	 */
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

	/**
	 * Méthode permettant d'ajouter un sommet
	 * @param strName le nom du sommet
	 */
	public void addVertex(String strName) {
		hmVertex.put(strName, new Point(1, 1));
		slObject.refresh();
	}

	/**
	 * Méthode qui met à jour le panneau principal et la liste des composants
	 */
	public void refresh() {
		graph = ctrl.getGraph();
		slObject.refresh();
		repaint();
	}

	/**
	 * Méthode utiliser pour afficher un message d'erreur
	 * @param strError le chaine de caractère à afficher
	 */
	public void showError(String strError) {
		JOptionPane.showMessageDialog(null, strError, "Erreur", JOptionPane.ERROR_MESSAGE);
	}
	
	/*--------------------
	 * Getters et Setters 
	 *-------------------*/
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

	public SwitchList getSlObject() {
		return this.slObject;
	}
	public GraphPanel getGraphPanel() {
		return pGraph;
	}

	public JLabel getLabelCoord() {
		return this.lCoord;
	}

	public JPopupMenu getPopMenu() { return this.popMenu;}
	
	public void setGraph(Graph g) {
		this.graph = g;
	}
	
	public void permitModifArc (boolean b) {
		tabMenuItemGraph[5].setEnabled(b);
		popUpItem[5].setEnabled(b);
	}
}
