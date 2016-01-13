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
import model.Arc;
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

	// Menu bar de la frame
	private JMenuBar menuBarMain;
	private JMenu menuFichier, menuEdition, menuExport, menuGraph, menuAlgo, menuAide;
	private JMenuItem[] tabMenuItemFile = new JMenuItem[5];
	private JMenuItem[] tabMenuItemEdition = new JMenuItem[6];
	private JMenuItem[] tabMenuItemExport = new JMenuItem[4];
	private JMenuItem[] tabMenuItemGraph = new JMenuItem[7];
	private JMenuItem[] tabMenuItemAlgo = new JMenuItem[3];
	private JMenuItem[] tabMenuItemAide = new JMenuItem[2];

	// Liste d'Objet
	private SwitchList slObject;

	// Main graph (draw)
	private GraphPanel pGraph;
	private JScrollPane jscrPanel;

	private JPanel pBottomGraph;
	private JLabel lCoord,lZoom;
	private JButton buttonMatrix;

	// Panel de JButton
	private JPanel pButton, pTopButton, pBottomButton;
	private JButton buttonNew, buttonOpen, buttonSave, buttonZoomIn, buttonZoomOut, buttonUndo, buttonRedo,
			buttonSetting;
	private JButton buttonAddVertex, buttonUpdateVertex, buttonDeleteVertex, buttonAddArc, buttonUpdateArc,
			buttonDeleteArc;

	// Items du menu contextuel
	private JMenuItem[] popUpItem = new JMenuItem[7];

	// JDialog de matrice
	private JDialog matrixDialog;

	// Booleen permettant de savoir si l'utilisateur vient de sauvegarder
	boolean bSaved;

	public HCI(Controller controller) {
		this.ctrl = controller;
		this.graph = controller.getGraph();

		//Paramètre basique de la frame
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

		// **---Contenu de la frame---**//
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

		// Separateur
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

		// Separateur
		menuFichier.addSeparator();

		// MenuItem - Quitter
		tabMenuItemFile[4] = new JMenuItem("<html>Quitter</html>");
		tabMenuItemFile[4].addActionListener(this);
		menuFichier.add(tabMenuItemFile[4]);

		menuBarMain.add(menuFichier);

		menuEdition = new JMenu("Edition");

		// MenuItem - Annuler
		tabMenuItemEdition[0] = new JMenuItem(
				"<html>Annuler&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;&nbsp;&thinsp;<i>Ctrl+Z</i></html>");
		tabMenuItemEdition[0].addActionListener(this);
		menuEdition.add(tabMenuItemEdition[0]);

		// MenuItem - Répéter
		tabMenuItemEdition[1] = new JMenuItem(
				"<html>Répéter&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;&nbsp;<i>Ctrl+Y</i></html>");
		tabMenuItemEdition[1].addActionListener(this);
		menuEdition.add(tabMenuItemEdition[1]);

		// Separateur
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

		// Separateur
		menuEdition.addSeparator();

		// MenuItem - Selectionner tout
		tabMenuItemEdition[5] = new JMenuItem("<html>Sélectionner tout&emsp;&nbsp;&nbsp;<i>Ctrl+A</i></html>");
		tabMenuItemEdition[5].addActionListener(this);
		menuEdition.add(tabMenuItemEdition[5]);

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
		// MenuItem - Matrice
		tabMenuItemExport[2] = new JMenuItem("Matrice  ");
		tabMenuItemExport[2].addActionListener(this);
		menuExport.add(tabMenuItemExport[2]);
		// MenuItem - Liste d'adjacence
		tabMenuItemExport[3] = new JMenuItem("Liste d'adjacence  ");
		tabMenuItemExport[3].addActionListener(this);
		menuExport.add(tabMenuItemExport[3]);

		menuBarMain.add(menuExport);

		//GRAPHE
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

		// Separateur
		menuGraph.addSeparator();

		// MenuItem - Coloriser un sommet
		tabMenuItemGraph[3] = new JMenuItem("<html>Coloriser un sommet</html>");
		tabMenuItemGraph[3].addActionListener(this);
		menuGraph.add(tabMenuItemGraph[3]);

		// Separateur
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

		menuBarMain.add(menuGraph);

		//ALGORITHMES
		menuAlgo = new JMenu("Algorithme");
		// MenuItem - PlusGrandeValeur
		tabMenuItemAlgo[0] = new JMenuItem("PlusGrandeValeur");
		tabMenuItemAlgo[0].addActionListener(this);
		menuAlgo.add(tabMenuItemAlgo[0]);

		// MenuItem - RechercheChemin
		tabMenuItemAlgo[1] = new JMenuItem("Recherche chemin");
		tabMenuItemAlgo[1].addActionListener(this);
		menuAlgo.add(tabMenuItemAlgo[1]);

		// Menu Item - Dijkstra-Moore
		tabMenuItemAlgo[2] = new JMenuItem("Dijkstra-Moore");
		tabMenuItemAlgo[2].addActionListener(this);
		menuAlgo.add(tabMenuItemAlgo[2]);

		menuBarMain.add(menuAlgo);

		//AIDE
		menuAide = new JMenu("Aide");

		// MenuItem - A Propos
		tabMenuItemAide[0] = new JMenuItem("<html>&Agrave; propos</html>");
		tabMenuItemAide[0].addActionListener(this);
		menuAide.add(tabMenuItemAide[0]);

		// Menu Item - Manuel Utilisateur
		tabMenuItemAide[1] = new JMenuItem("Manuel Utilisateur");
		tabMenuItemAide[1].addActionListener(this);
		menuAide.add(tabMenuItemAide[1]);

		menuBarMain.add(menuAide);

		menuBarMain.setPreferredSize(new Dimension(250, 20));
		setJMenuBar(menuBarMain);
		// -----------//

		// ---Liste d'objet --//
		slObject = new SwitchList(this);
		slObject.setBorder(BorderFactory.createLineBorder(Color.black));
		add(slObject, BorderLayout.WEST);
		slObject.switchState();
		// ----------------------//

		// ---------Graphe--------//
		pGraph = new GraphPanel(this, ctrl);
		
		pBottomGraph = new JPanel(new GridLayout(1,2));
		lCoord = new JLabel("");
		pBottomGraph.add(lCoord);
		JPanel pZoom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		lZoom = new JLabel("100%");
		pZoom.add(lZoom);
		pBottomGraph.add(pZoom);
		
		jscrPanel = new JScrollPane(pGraph);

		JPanel panelCenter = new JPanel(new BorderLayout());
		panelCenter.add(jscrPanel);
		panelCenter.add(pBottomGraph, "South");
		add(panelCenter, BorderLayout.CENTER);
		// ----------------------//

		// -------ButtonBar------//
		pButton = new JPanel(new GridLayout(1, 2));
		pButton.setBackground(Color.WHITE);
		pButton.setBorder(BorderFactory.createLineBorder(Color.black));

		pTopButton = new JPanel(new FlowLayout(FlowLayout.LEFT));

		// Nouveau fichier
		buttonNew = initSmoothButton("Nouveau", "/nouveau.png", "/nouveau_rollover.png");
		pTopButton.add(buttonNew);
		// Ouvrir fichier
		buttonOpen = initSmoothButton("Ouvrir", "/ouvrir.png", "/ouvrir_rollover.png");
		pTopButton.add(buttonOpen);
		// Enregistrer fichier
		buttonSave = initSmoothButton("Enregistrer", "/enregistrer.png", "/enregistrer_rollover.png");
		pTopButton.add(buttonSave);
		// Zoom in
		buttonZoomIn = initSmoothButton("Agrandir", "/zoom_In.png", "/zoom_In_rollover.png");
		pTopButton.add(buttonZoomIn);
		// Zoom out
		buttonZoomOut = initSmoothButton("Réduire", "/zoom_Out.png", "/zoom_Out_rollover.png");
		pTopButton.add(buttonZoomOut);
		// Annuler
		buttonUndo = initSmoothButton("Défaire", "/undo.png", "/undo_rollover.png");
		pTopButton.add(buttonUndo);
		// Réduire
		buttonRedo = initSmoothButton("Refaire", "/redo.png", "/redo_rollover.png");
		pTopButton.add(buttonRedo);
		// Matrice
		buttonMatrix = initSmoothButton("Afficher la matrice", "/matrice.png", "/matrice_rollover.png");
		pTopButton.add(buttonMatrix);
		// Parametre
		buttonSetting = initSmoothButton("Paramètres", "/parametre.png", "/parametre_rollover.png");
		pTopButton.add(buttonSetting);

		pButton.add(pTopButton);

		pBottomButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		// Ajouter un sommet
		buttonAddVertex = initSmoothButton("Ajouter un sommet", "/ajouterSommet.png", "/ajouterSommet_rollover.png");
		pBottomButton.add(buttonAddVertex);
		// Modifier un sommet
		buttonUpdateVertex = initSmoothButton("Modifier un sommet", "/modifierSommet.png",
				"/modifierSommet_rollover.png");
		pBottomButton.add(buttonUpdateVertex);
		// Supprimer un sommet
		buttonDeleteVertex = initSmoothButton("Supprimer un sommet", "/supprimerSommet.png",
				"/supprimerSommet_rollover.png");
		pBottomButton.add(buttonDeleteVertex);

		// Separateur
		JButton buttonSeparator = initSmoothButton("", "/separator.png", "/separator.png");
		buttonSeparator.setEnabled(false);
		;
		pBottomButton.add(buttonSeparator);

		// Ajouter un sommet
		buttonAddArc = initSmoothButton("Ajouter un arc", "/ajouterArc.png", "/ajouterArc_rollover.png");
		pBottomButton.add(buttonAddArc);
		// Modifier un sommet
		buttonUpdateArc = initSmoothButton("Modifier un arc", "/modifierArc.png", "/modifierArc_rollover.png");
		pBottomButton.add(buttonUpdateArc);
		// Supprimer un sommet
		buttonDeleteArc = initSmoothButton("Supprimer un arc", "/supprimerArc.png", "/supprimerArc_rollover.png");
		pBottomButton.add(buttonDeleteArc);

		pButton.add(pBottomButton);

		add(pButton, BorderLayout.NORTH);

		// Instancitation du menu contextuel et de ses éléments
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

		// Separateur
		popMenu.addSeparator();

		// MenuItem - Coloriser un sommet
		popUpItem[3] = new JMenuItem("<html>Coloriser un sommet</html>");
		popUpItem[3].addActionListener(this);
		popMenu.add(popUpItem[3]);

		// Separateur
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
	 * Méthode utiliser pour ouvrir une fenêtre contenant la matrice
	 * @param tMatrix un double tableau contenant les informations de la matrice
	 */
	public void openMatrix(int[][] tMatrix, String strTitle) {

		String str = "<html><body><table><tbody><tr>";
		for (Vertex v : this.getGraph().getAlVertex()) {
			str += "<td style=\"border: 1px solid black;\">" + v.getName().charAt(0) + "</td>";
		}
		str += "</tr>";
		for (int i = 0; i < tMatrix.length; i++) {
			str += "<tr>";
			for (int j = 0; j < tMatrix[0].length; j++) {
				str += "<td style=\"border: 1px solid black;\">" + String.valueOf(tMatrix[i][j]) + "</td>";
			}
			str += "</tr>";
		}
		str += "</tbody></table></body></html>";

		matrixDialog = new JDialog(this,strTitle);
		JPanel pStr = new JPanel();
		pStr.add(new JLabel(str));
		matrixDialog.setContentPane(pStr);
		matrixDialog.pack();
		matrixDialog.setVisible(true);
	}

	public JDialog getMatrixDialog() {
		return matrixDialog;
	}

	/**
	 * Permet de créer des boutons 
	 * @param toolTipsText bulle info
	 * @param icon chemin de l'icone
	 * @param icon_rollover chemin de l'icone lorsque le curseur est dessus
	 * @return un bouton
	 */
	public JButton initSmoothButton(String toolTipsText, String icon, String icon_rollover) {
		// Parametre
		JButton button = new JButton(new ImageIcon(getClass().getResource(icon)));
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setRolloverIcon(new ImageIcon(getClass().getResource(icon_rollover)));
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBackground(new Color(255, 255, 255));
		button.setForeground(new Color(255, 255, 255));
		button.setToolTipText(toolTipsText);
		button.addActionListener(this);
		return button;
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

	/**
	 * Permet d'initialiser le tableau de coordonnée dans hci en lui passant un tableau de point
	 * @param tab tableau de point
	 */
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
			if (bSaved == false) {
				// On propose à l'utilisateur de sauvegarder son travail avant
				// de continuer
				String[] tabVal = { "Enregistrer et continuer", "Continuer sans enregistrer", "Annuler" };
				int val = JOptionPane.showOptionDialog(this,
						"La création d'un nouveau graphe entrainera la perte du graphe actuel s'il n'a pas été sauvegardé. \nVoulez vous continuer ?",
						"Création d'un nouveau graphe", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
						tabVal, tabVal[0]);

				if (val == 0) {
					if (ctrl.getFile().equals(""))
						saveDialog();
					else
						ctrl.saveFile("", "adjacence");
					new PopupNewGraph("Création d'un nouveau graphe", true, ctrl, this);
				} else if (val == 1)
					new PopupNewGraph("Création d'un nouveau graphe", true, ctrl, this);
			} else
				new PopupNewGraph("Création d'un nouveau graphe", true, ctrl, this);
			
			//Reset style personnalise
			GraphStyle.Personnalise.setEdgeBorder(Color.BLACK);
			GraphStyle.Personnalise.setEdgeBackground(Color.WHITE);
			GraphStyle.Personnalise.setEdgeText(Color.BLACK);
			GraphStyle.Personnalise.setArcLine(Color.GRAY);
			GraphStyle.Personnalise.setArcText(Color.BLACK);
			GraphStyle.Personnalise.setBackground(new Color(238,238,238));
			getGraphPanel().setBackground(new Color(238,238,238));

			// Ouvrir
		} else if (e.getSource() == tabMenuItemFile[1] || e.getSource() == buttonOpen) {
			JFileChooser dial = new JFileChooser(new File("."));
			if (dial.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
				try {
					ctrl.loadFile(dial.getSelectedFile().getAbsolutePath());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Les fchiers contiennent probablement des erreurs", "Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
			// Enregistrer
		} else if (e.getSource() == tabMenuItemFile[2] || e.getSource() == buttonSave) {
			if (ctrl.getFile().equals("")) {
				saveDialog();
			} else {
				ctrl.saveFile("", "adjacence");
			}
			// Enregistrer sous
		} else if (e.getSource() == tabMenuItemFile[3]) {
			saveDialog();
			// Quitter
		} else if (e.getSource() == tabMenuItemFile[4]) {
			System.exit(0);

			/*-- EDITION --*/
		} else if (e.getSource() == tabMenuItemEdition[0] || e.getSource() == buttonUndo) {
			ctrl.undo();
			bSaved = false;
			this.refresh();
		} else if (e.getSource() == tabMenuItemEdition[1] || e.getSource() == buttonRedo) {
			ctrl.redo();
			bSaved = false;
			this.refresh();

		} else if (e.getSource() == tabMenuItemEdition[2]) {
			pGraph.cutEdge();
		} else if (e.getSource() == tabMenuItemEdition[3]) {
			pGraph.copyEdge();
		} else if (e.getSource() == tabMenuItemEdition[4]) {
			pGraph.pasteEdge();
		} else if (e.getSource() == tabMenuItemEdition[5]) {
			pGraph.selectAll();

			/*-- EXPORTER --*/
			// Image
		} else if (e.getSource() == tabMenuItemExport[0]) {
			expImage();
			// PDF
		} else if (e.getSource() == tabMenuItemExport[1]) {
			JFileChooser dial = new JFileChooser(new File("."));
			if (dial.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
				PdfGenerator.generer(graph, dial.getSelectedFile().getAbsolutePath() + ".pdf", this);
			// Matrice
		} else if (e.getSource() == tabMenuItemExport[2]) {
			JFileChooser dial = new JFileChooser(new File("."));
			if (dial.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
				ctrl.export(dial.getSelectedFile().getAbsolutePath() + ".txt", "matrice");
			// Liste d'adjacence
		} else if (e.getSource() == tabMenuItemExport[3]) {
			JFileChooser dial = new JFileChooser(new File("."));
			if (dial.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
				ctrl.export(dial.getSelectedFile().getAbsolutePath() + ".txt", "liste");
			/*-- GRAPHE --*/
			// Ajouter un sommet
		} else if (e.getSource() == tabMenuItemGraph[0] || e.getSource() == popUpItem[0]
				|| e.getSource() == buttonAddVertex) {
			new PopupAddVertex("Ajouter un sommet", true, ctrl, this);
			bSaved = false;

			// Modifier un sommet
		} else if (e.getSource() == tabMenuItemGraph[1] || e.getSource() == popUpItem[1]
				|| e.getSource() == buttonUpdateVertex) {
			if (pGraph.getAlSelected().size() > 1) {
				showError("Veuillez sélectionner un seul sommet.");
			} else if (pGraph.getAlSelected().size() == 0 || pGraph.getAlSelected().get(0).charAt(0) == ' ') {
				showError("Veuillez sélectionner un sommet.");
			} else {
				new PopupUpdateVertex("Modifier un sommet", true, ctrl, this);
				bSaved = false;
			}

			// Supprimer un sommet
		} else if (e.getSource() == tabMenuItemGraph[2] || e.getSource() == popUpItem[2]
				|| e.getSource() == buttonDeleteVertex) {
			ctrl.deleteMultipleVertex(pGraph.getAlSelected());
			setAlSelected(new ArrayList<String>());
			bSaved = false;
			refresh();
			// Coloriser un sommet
		} else if (e.getSource() == tabMenuItemGraph[3] || e.getSource() == popUpItem[3]) {
			new PopupColorizeVertex("Coloriser un sommet", true, ctrl, this);
			bSaved = false;

			// Ajouter un arc
		} else if (e.getSource() == tabMenuItemGraph[4] || e.getSource() == popUpItem[4]
				|| e.getSource() == buttonAddArc) {
			new PopupAddArc("Ajout d'un arc", true, ctrl, this);
			bSaved = false;

			// Mofidier un arc
		} else if (e.getSource() == tabMenuItemGraph[5] || e.getSource() == popUpItem[5]
				|| e.getSource() == buttonUpdateArc) {
			new PopupUpdateArc("Modifier un arc", true, ctrl, this);
			bSaved = false;

			// Supprimer un arc
		} else if (e.getSource() == tabMenuItemGraph[6] || e.getSource() == popUpItem[6]
				|| e.getSource() == buttonDeleteArc) {
			new PopupDeleteArc("Supprimer un arc", true, ctrl, this);
			bSaved = false;

			/*--ALGORITHMES--*/
			// Plus grande valeur
		} else if (e.getSource() == tabMenuItemAlgo[0]) {
			showInfo("Lancement de l'algorithme");
			ctrl.startParcours();
			// Rechercher chemin
		} else if (e.getSource() == tabMenuItemAlgo[1]) {
			new PopupAlgoRC("Algorithme de recherche de chemin", true, ctrl, this);

			// Dijkstra-Moore
		} else if (e.getSource() == tabMenuItemAlgo[2]) {
			new PopupAlgoDM("Algorithme de Dijkstra-Moore", true, ctrl, this);

			/*-- AIDE --*/
		} else if (e.getSource() == tabMenuItemAide[0]) { // A propos
			JOptionPane.showMessageDialog(this,
					"<html>Projet tuteuré de deuxième année de DUT Informatique.<br/><center><h3>Groupe 3</h3>Alouache Mehdi<br/>Cavelier Guillaume<br/>Douchin Nicolas<br/>Dumont Mélanie<br/>Hazard Alexandre</center></html>",
					"A propos", 1);
		} else if (e.getSource() == tabMenuItemAide[1]) {
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
			
			// Affiche la martrice
		} else if (e.getSource() == buttonMatrix) {
			if(matrixDialog!=null)matrixDialog.dispose();
			openMatrix(graph.generateMatrix(),"Matrice");
			
			// Paramètres
		} else if (e.getSource() == buttonSetting) {
			new PopupSetting("Paramètres", true, ctrl, this);
		}
		refresh();
	}

	@SuppressWarnings("deprecation")
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == slObject.getListOfObject()) {
			pGraph.getAlSelected().clear();
			for( Object s : slObject.getListOfObject().getSelectedValues() ) {
				pGraph.getAlSelected().add((String)s);
			}
			repaint();
		}
	}

	/**
	 * Méthode permettant d'exporter le graphe en image.
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
		menuAlgo.setVisible(graph.isValued());
		slObject.refresh();
		repaint();
	}

	/**
	 * Méthode utiliser pour afficher un message d'erreur
	 * @param strError la chaine de caractère à afficher
	 */
	public void showError(String strError) {
		JOptionPane.showMessageDialog(null, strError, "Erreur", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Méthode utiliser pour afficher un message d'information
	 * @param strInfo chaine de caractère à afficher
	 */
	public void showInfo(String strInfo) {
		JOptionPane.showMessageDialog(null, strInfo, "Information", JOptionPane.INFORMATION_MESSAGE);
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

	/**
	 * Méthode permettant d'afficher les coordonnées du point selectionné
	 * @return un label
	 */
	public JLabel getLabelCoord() {
		return this.lCoord;
	}

	public JLabel getLabelZoom() {
		return this.lZoom;
	}
	
	/**
	 * Méthode permettant de modifier les informations du graphe
	 */
	public void setInfo() {
		String str;
		str=("Graphe ");
		if(graph.isDirected()){
			str+=("orienté");
		}else{
			str+=("non orienté");
		}
		if(graph.isValued()){
			str+=(" et valué");
		}else{
			str+=(" et non valué");
		}
		slObject.setInfo(str);
	}

	public Controller getController() {
		return this.ctrl;
	}

	public JPopupMenu getPopMenu() {
		return this.popMenu;
	}

	public void setGraph(Graph g) {
		this.graph = g;
	}

	/**
	 * Méthode permettant d'empêcher l'utilisateur de modifier un arc s'il n'y en a pas.
	 * Lorsqu'il y a un arc de créer, l'utilisateur peut de nouveau cliquer sur" modifier un arc".
	 * @param b vrai s'il peut modifier, false s'il ne peut pas
	 */
	public void permitModifArc(boolean b) {
		tabMenuItemGraph[5].setEnabled(b);
		popUpItem[5].setEnabled(b);
		buttonUpdateArc.setEnabled(b);
	}

	/**
	 * Méthode permettant à l'utilisateur de choisir d'enregistrer son graphe sous forme de matrice ou de liste d'adjacence.
	 */
	public void saveDialog() {

		FileNameExtensionFilter filterMatrix = new FileNameExtensionFilter("Enregistrer en matrice (*.txt)", ".txt");
		FileNameExtensionFilter filterAdjacencyList = new FileNameExtensionFilter(
				"Enregistrer en liste d'adjacence (*.txt)", ".txt");

		JFileChooser dial = new JFileChooser(new File("."));
		dial.setAcceptAllFileFilterUsed(false);
		dial.addChoosableFileFilter(filterAdjacencyList);
		dial.addChoosableFileFilter(filterMatrix);

		if (dial.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			String format = "";
			String extension = dial.getFileFilter().getDescription();

			if (extension.equals("Export en matrice (*.txt)")) {
				format = "matrice";
			}

			if (extension.equals("Export en liste d'adjacence (*.txt)")) {
				format = "adjacence";
			}

			String path = dial.getSelectedFile().getAbsolutePath();
			if (path.substring(path.lastIndexOf("\\")).lastIndexOf(".") != -1) {
				ctrl.saveFile(path.substring(0, path.lastIndexOf(".")) + ".txt", format);
			} else {
				ctrl.saveFile(path + ".txt", format);
			}
			bSaved = true;
		}
	}

	/**
	 * Méthode permettant de démarrer le parcours de de la plus grande valeur
	 */
	public void startParcours() {
		ctrl.startParcours();
	}

	/**
	 * Méthode permettant d'afficher un message.
	 * @param s la chaine à afficher
	 */
	public void afficherMessage(String s) {
		javax.swing.JOptionPane.showMessageDialog(null, s);
	}

	/**
	 * Méthode permettant de savoir si l'utilisateur vient d'enregistrer
	 * @param b true s'il vient d'enregistrer, false sinon
	 */
	public void setBSaved(boolean b) {
		bSaved = b;
	}

	/**
	 * Méthode permettant de mettre en évidence les sommets et arcs actifs lors d'application d'algorithme de parcours
	 */
	public void showHiLightAlgorithm() {
		ArrayList<String> alSelected = pGraph.getAlSelected();

		alSelected.clear();

		for (Vertex vertex : graph.getAlVertex()) {
			if (ctrl.sommetActif(vertex.getName().charAt(0))) {
				alSelected.add(vertex.getName());
			}

			for (Arc arc : vertex.getAlArcs()) {
				if (ctrl.arcActif(vertex.getName().charAt(0), arc.getVertex().getName().charAt(0))) {
					if (graph.isValued()) {
						if (graph.isDirected()) {
							// Valué et orienté
							alSelected.add(HCI.centerStr(vertex.getName(), 5) + "------"
									+ HCI.centerStr("" + arc.getIValue(), 7) + "----->"
									+ HCI.centerStr(arc.getVertex().getName(), 5));
						} else {
							// Valué et non orienté
							alSelected.add(HCI.centerStr(vertex.getName(), 5) + "------"
									+ HCI.centerStr("" + arc.getIValue(), 7) + "------"
									+ HCI.centerStr(arc.getVertex().getName(), 5));
						}
					} else {
						if (graph.isDirected()) {
							// Non valué et orienté
							alSelected.add(HCI.centerStr(vertex.getName(), 5) + "-------->"
									+ HCI.centerStr(arc.getVertex().getName(), 5));
						} else {
							// Non valué et non orienté
							alSelected.add(HCI.centerStr(vertex.getName(), 5) + "---------"
									+ HCI.centerStr(arc.getVertex().getName(), 5));
						}
					}
				}
			}
		}
	
		pGraph.paintVertexAndArc((Graphics2D)pGraph.getGraphics());

	}
}
