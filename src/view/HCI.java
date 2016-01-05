package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.Controller;
import model.Vertex;
/*testMel*/
public class HCI extends JFrame implements ActionListener,ListSelectionListener{
	/** Serial Version
	  */
	private static final long serialVersionUID = -1456746983405013221L;
	private Controller ctrl;
	
	//Data of Vertex
	protected static HashMap<String, Point> hmVertex;
    int xInitialize = 0,yInitialize = 0;	//Used for the preferedsize of pGraph
	
	//Menu bar of this frame
	private JMenuBar menuBarMain;
	private JMenu menuFichier,menuEdition,menuExport,menuGraph,menuAide;
	private JMenuItem[] tabMenuItemFichier = new JMenuItem[6];
	private JMenuItem[] tabMenuItemEdition = new JMenuItem[6];
	private JMenuItem[] tabMenuItemExport = new JMenuItem[1];
	private JMenuItem[] tabMenuItemGraph = new JMenuItem[3];
	private JMenuItem[] tabMenuItemAide = new JMenuItem[1];
	//List of "Object"
	private SwitchList slObject;
	//Main graph (draw)
	private GraphPanel pGraph;
	private JScrollPane jscrPanel;
	//Panel of JButton
	private JPanel pButton;
	private JButton buttonNew,buttonOpen,buttonSave,buttonZoomIn,buttonZoomOut;
	public HCI(Controller ctrl) {
		this.ctrl=ctrl;

		//basic parameters of this frame
		this.setTitle("Logiciel pedagogique de graph");
	    this.setSize(900, 700);
	    setPreferredSize(new Dimension(900, 700));
	    this.setLocation(250,0);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setResizable(true);
	    this.setLayout(new BorderLayout());
	    
	    //**---Contents of this frame---**//
	    //---Menu bar---//
	    menuBarMain = new JMenuBar();
	    menuFichier = new JMenu("Fichier");
	    
	    /*Tips : &nbsp;   = normal space
	     *		 &thinsp; = thin space
	     *		 &emsp;   = 4 normal spaces
	     */
		    //MenuItem - Nouveau
		    tabMenuItemFichier[0] = new JMenuItem("<html>Nouveau...&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;<i>Ctrl+N</i></html>");
		    tabMenuItemFichier[0].addActionListener(this);
		    menuFichier.add(tabMenuItemFichier[0]);
		    
		    //MenuItem - Ouvrir
		    tabMenuItemFichier[1] = new JMenuItem("<html>Ouvrir...&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;<i>Ctrl+O</i></html>");
		    tabMenuItemFichier[1].addActionListener(this);
		    menuFichier.add(tabMenuItemFichier[1]);
		    
		    //Separator
		    menuFichier.addSeparator();
		    
		    //MenuItem - Enregistrer 
		    tabMenuItemFichier[2] = new JMenuItem("<html>Enregister&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;<i>Ctrl+S</i></html>");
		    tabMenuItemFichier[2].addActionListener(this);
		    menuFichier.add(tabMenuItemFichier[2]);
		    
		    //MenuItem - Enregistrer sous
		    tabMenuItemFichier[3] = new JMenuItem("<html>Enregister sous...&emsp;<i>Ctrl+Maj+S</i></html>");
		    tabMenuItemFichier[3].addActionListener(this);
		    menuFichier.add(tabMenuItemFichier[3]);
		    
		    //Separator
		    menuFichier.addSeparator();
		    
		    //MenuItem - Imprimer 
		    tabMenuItemFichier[4] = new JMenuItem("<html>Imprimer&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;&nbsp;<i>Ctrl+P</i></html>");
		    tabMenuItemFichier[4].addActionListener(this);
		    menuFichier.add(tabMenuItemFichier[4]);
		    
		    //Separator
		    menuFichier.addSeparator();
		    
		    //MenuItem - Quitter
		    tabMenuItemFichier[5] = new JMenuItem("<html>Quitter</html>");
		    tabMenuItemFichier[5].addActionListener(this);
		    menuFichier.add(tabMenuItemFichier[5]);
	    
	    //Add menuFichier to this frame
	    menuBarMain.add(menuFichier);
	    
	    menuEdition = new JMenu("Edition");
	    
		    //MenuItem - Annuler
	    	tabMenuItemEdition[0] = new JMenuItem("<html>Annuler&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;&nbsp;&thinsp;<i>Ctrl+Z</i></html>");
	    	tabMenuItemEdition[0].addActionListener(this);
		    menuEdition.add(tabMenuItemEdition[0]);
		    
		    //MenuItem - Répéter
		    tabMenuItemEdition[1] = new JMenuItem("<html>Répéter&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;&nbsp;<i>Ctrl+Y</i></html>");
		    tabMenuItemEdition[1].addActionListener(this);
		    menuEdition.add(tabMenuItemEdition[1]);
		    
		    //Separator
		    menuEdition.addSeparator();
		    
		    //MenuItem - Couper 
		    tabMenuItemEdition[2] = new JMenuItem("<html>Couper&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<i>Ctrl+X</i></html>");
		    tabMenuItemEdition[2].addActionListener(this);
		    menuEdition.add(tabMenuItemEdition[2]);
		    
		    //MenuItem - Copier
		    tabMenuItemEdition[3] = new JMenuItem("<html>Copier&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;<i>Ctrl+C</i></html>");
		    tabMenuItemEdition[3].addActionListener(this);
		    menuEdition.add(tabMenuItemEdition[3]);
		    
		    //MenuItem - Coller 
		    tabMenuItemEdition[4] = new JMenuItem("<html>Coller&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;<i>Ctrl+V</i></html>");
		    tabMenuItemEdition[4].addActionListener(this);
		    menuEdition.add(tabMenuItemEdition[4]);
		    
		    //Separator
		    menuEdition.addSeparator();
		    
		    //MenuItem - Selectionner tout
		    tabMenuItemEdition[5] = new JMenuItem("<html>Sélectionner tout&emsp;&nbsp;&nbsp;<i>Ctrl+A</i></html>");
		    tabMenuItemEdition[5].addActionListener(this);
		    menuEdition.add(tabMenuItemEdition[5]);
	    
	    //Add menuEdition to this frame
	    menuBarMain.add(menuEdition);
	    
	  //Export
	    menuExport= new JMenu("Exporter");
		    //Menu Item - Image
		    tabMenuItemExport[0] = new JMenuItem("Image   ");
		    tabMenuItemExport[0].addActionListener(this);
		    menuExport.add(tabMenuItemExport[0]);
	    
	    menuBarMain.add(menuExport);	
	    
	    menuGraph = new JMenu("Graph");
	    
		    //MenuItem - Ajouter un sommet
	    	tabMenuItemGraph[0] = new JMenuItem("<html>Ajouter un sommet</html>");
	    	tabMenuItemGraph[0].addActionListener(this);
	    	menuGraph.add(tabMenuItemGraph[0]);
	    	
		    //MenuItem - Modifier un sommet
	    	tabMenuItemGraph[1] = new JMenuItem("<html>Modifier un sommet</html>");
	    	tabMenuItemGraph[1].addActionListener(this);
	    	menuGraph.add(tabMenuItemGraph[1]);
	    	
		    //MenuItem - Supprimer un sommet
	    	tabMenuItemGraph[2] = new JMenuItem("<html>Supprimer un sommet</html>");
	    	tabMenuItemGraph[2].addActionListener(this);
	    	menuGraph.add(tabMenuItemGraph[2]);

    	//Add menuGraph to this frame
    	menuBarMain.add(menuGraph);
	    
	    menuAide = new JMenu("Aide");
	    
		    //MenuItem - A Propos
	    	tabMenuItemAide[0] = new JMenuItem("<html>&Agrave; propos</html>");
	    	tabMenuItemAide[0].addActionListener(this);
	    	menuAide.add(tabMenuItemAide[0]);
    
	    //Add menuAide to this frame
	    menuBarMain.add(menuAide);
	    
	    menuBarMain.setPreferredSize(new Dimension(250, 20));
	    setJMenuBar(menuBarMain);
	    //-----------//
	    
	    //---List of "Object" --//
	    slObject = new SwitchList(this);
	    slObject.setBorder(BorderFactory.createLineBorder(Color.black));
	    add(slObject,BorderLayout.WEST);
	    slObject.switchState();
	    //----------------------//
	    
	    //---------Graph--------//
	    pGraph = new GraphPanel();
	    jscrPanel = new JScrollPane(pGraph);
	    add(jscrPanel,BorderLayout.CENTER);
	    //----------------------//
	    
	    //-------ButtonBar------//
	    pButton = new JPanel();
	    pButton.setBackground(Color.WHITE);
	    pButton.setLayout(new FlowLayout(FlowLayout.LEFT));
	    pButton.setBorder(BorderFactory.createLineBorder(Color.black));
	    
	    //New file
	    buttonNew = new JButton("Fichier");
	    buttonNew.addActionListener(this);
	    pButton.add(buttonNew);
	    //Open file
	    buttonOpen = new JButton("Ouvrir");
	    buttonOpen.addActionListener(this);
	    pButton.add(buttonOpen);
	    //Save file
	    buttonSave = new JButton("Enregistrer");
	    buttonSave.addActionListener(this);
	    pButton.add(buttonSave);
	    //Zoom in
	    buttonZoomIn = new JButton("+");
	    buttonZoomIn.addActionListener(this);
	    pButton.add(buttonZoomIn);
	    //Zoom out
	    buttonZoomOut = new JButton("-");
	    buttonZoomOut.addActionListener(this);
	    pButton.add(buttonZoomOut);
	    
	    add(pButton,BorderLayout.NORTH);
	    //----------------------//
	    
	    //**---------------------------**//
	    
	    
	    initHmVertex();
	    
	    pGraph.setPreferredSize(new Dimension((int)(600+pGraph.getiWidthEdge()),(int)(yInitialize+pGraph.getiHeightEdge())));
	    pack();
		setVisible(true);
	}
	
	public void initHmVertex() {
		//Initialization of hmVertex
	    hmVertex = new HashMap<String, Point>();
	    for( Vertex v : ctrl.getGraph().getAlVertex() ) {
			hmVertex.put(v.getName(), new Point(xInitialize, yInitialize));	
			
	    	if(xInitialize < 600 - pGraph.getiWidthEdge()) {
		    	xInitialize += pGraph.getiWidthEdge()*2;
	    	} else {
	    		yInitialize += pGraph.getiHeightEdge()*2;
	    		xInitialize = 0;
	    	}
	    }
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == slObject.getJBSwitch() ) {		//Switch the state of the SwitchList
			slObject.switchState();			
		} else if (e.getSource() == tabMenuItemFichier[5]) {//Quit
			this.dispose();
		} else if (e.getSource() == tabMenuItemAide[0]) {	//A propos
			JOptionPane.showMessageDialog(this, "<html>Projet tuteuré de deuxième année de DUT Informatique.<br/><center><h3>Groupe 3</h3>Alouache Mehdi<br/>Cavelier Guillaume<br/>Douchin Nicolas<br/>Dumont Mélanie<br/>Hazard Alexandre</center></html>","A propos",1);
		} else if (e.getSource() == buttonZoomIn) {			//Zoom in
			pGraph.zoomIn();
			pGraph.repaint();
		    pGraph.revalidate();
			
		} else if (e.getSource() == buttonZoomOut) {		//Zoom out
			pGraph.zoomOut();
		    pGraph.repaint();
		    pGraph.revalidate();
		 
		} else if (e.getSource() == tabMenuItemGraph[0]) {	//Ajouter sommet
			new Form(this, "Ajouter un sommet", true, ctrl);
		} else if (e.getSource() == tabMenuItemGraph[1]) {	//Modifier sommet
			new Form(this, "Modifier un sommet",true, ctrl);
			System.out.println(hmVertex.toString());
		} else if (e.getSource() == tabMenuItemGraph[2]) {	//Supprimer sommet
			Vertex tmpVertex = null;
			for(Vertex v : ctrl.getGraph().getAlVertex()) {
				if( v.getName().equals(getStrSelected()) ) {
					tmpVertex = v;
				}
			}
			ctrl.getGraph().deleteVertex(tmpVertex);
			HCI.hmVertex.remove(getStrSelected());
			setStrSelected(null);
			refresh();
		}
		
		//Export version
		if(e.getSource()==tabMenuItemExport[0]){
			expImage();
		}
	}
	
	private void expImage(){
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
			BufferedImage image = new BufferedImage(size.width,size.height,BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = image.createGraphics();
			pGraph.paint(g2);
		
			try {
				ImageIO.write(image, ext, new File(dial.getSelectedFile().getAbsolutePath() + "." + ext));
			} catch (Exception ex) {}
		}
	}
	
	//Utiliser pour centrer une chaine de caractere
	public static String centerStr(String s, int size) {
	    if (s == null || size <= s.length())
	        return s;
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < (size - s.length()) / 2; i++) {
	        sb.append(" ");
	    }
	    sb.append(s);
	    while (sb.length() < size) {
	        sb.append(" ");
	    }
	    return sb.toString();
	}

	public void addVertex(String nom) {
		hmVertex.put(nom, new Point(0,0));
		slObject.refresh();
	}
	
	public void refresh() {
		slObject.refresh();
		repaint();
	}
	
	public String getStrSelected() 		 {return pGraph.getStrSelected();}
	public void setStrSelected(String s) {pGraph.setStrSelected(s);}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == slObject.getListOfObject()) {
			pGraph.setStrSelected((String)slObject.getListOfObject().getSelectedValue());
			repaint();
		}
	}
}
