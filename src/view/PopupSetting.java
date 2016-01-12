package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;

/**
 * Classe qui gère la fenêtre utilisateur pour choisir et modifier le style d'un graphe
 * @author Groupe 3
 * @version 2016-01-12
 */
public class PopupSetting extends Popup {

	private static final long serialVersionUID = 7294187771970061680L;
	private JComboBox<GraphStyle> boxStyle;
	private JButton buttonEdgeBackground;
	private JButton buttonEdgeBorder;
	private JButton buttonEdgeText;
	private JButton buttonArcLine;
	private JButton buttonArcText;
	private JButton buttonBackground;
	private GraphStyle style;
	   
	/**
	 * Méthode qui instancie la pop-up pour modifier le style du graphe.
	 * @param title le titre de la pop-up
	 * @param modal 
	 * @param ctrl le controleur utilisé
	 * @param hci le hci utilisé
	 */
	public PopupSetting(String title, boolean modal, Controller ctrl, HCI hci) {
		super(title, modal, ctrl, hci);
		JPanel pStyle = new JPanel(new BorderLayout());
		pStyle.setBorder(BorderFactory.createTitledBorder("Style"));
		
		style = hci.getGraphPanel().getStyle();
		
		//Styles prédéfinis
		boxStyle = new JComboBox(GraphStyle.values());
		boxStyle.setSelectedItem(style);
		boxStyle.addActionListener(this);
		pStyle.add(boxStyle,BorderLayout.NORTH);
		
		//Personnalisé
		JPanel pColor = new JPanel(new GridLayout(4,3));
		
		pColor.add(new JLabel("Fonds des sommets"));
		pColor.add(new JLabel("Bord des sommets"));
		pColor.add(new JLabel("Textes des sommets"));
		
		buttonEdgeBackground = new JButton();
		buttonEdgeBackground.setBackground(style.getEdgeBackground());
		buttonEdgeBackground.addActionListener(this);
		pColor.add(buttonEdgeBackground);
		buttonEdgeBorder = new JButton();
		buttonEdgeBorder.setBackground(style.getEdgeBorder());
		buttonEdgeBorder.addActionListener(this);
		pColor.add(buttonEdgeBorder);
		buttonEdgeText = new JButton();
		buttonEdgeText.setBackground(style.getEdgeText());
		buttonEdgeText.addActionListener(this);
		pColor.add(buttonEdgeText);
		
		pColor.add(new JLabel("Lignes des arcs"));
		pColor.add(new JLabel("Textes des arcs"));
		pColor.add(new JLabel("Fonds"));
		
		buttonArcLine = new JButton();
		buttonArcLine.setBackground(style.getArcLine());
		buttonArcLine.addActionListener(this);
		pColor.add(buttonArcLine);
		buttonArcText = new JButton();
		buttonArcText.setBackground(style.getArcText());
		buttonArcText.addActionListener(this);
		pColor.add(buttonArcText);
		buttonBackground = new JButton();
		buttonBackground.setBackground(style.getBackground());
		buttonBackground.addActionListener(this);
		pColor.add(buttonBackground);
		
		pStyle.add(pColor,BorderLayout.CENTER);
		
		add(pStyle);

		setSize(400,200);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Méthode qui modifie les couleurs sur le graphe selon les couleurs du style séléctionner.
	 */
	public void refreshColor() {
		hci.getGraphPanel().setStyle(style);
		hci.getGraphPanel().setBackground(style.getBackground());
		buttonEdgeBackground.setBackground(style.getEdgeBackground());
		buttonEdgeBorder.setBackground(style.getEdgeBorder());
		buttonEdgeText.setBackground(style.getEdgeText());
		buttonArcLine.setBackground(style.getArcLine());
		buttonArcText.setBackground(style.getArcText());
		buttonBackground.setBackground(style.getBackground());
		hci.getGraphPanel().repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == boxStyle) {
			style = (GraphStyle) boxStyle.getSelectedItem();
			refreshColor();
		}
		if(e.getSource() == buttonEdgeBackground) {
			Color c = JColorChooser.showDialog(null,"Sélection de couleur", null);
            if (c != null) {
            	GraphStyle oldStyle = style;
                style = GraphStyle.Personnalise;
                //Replace old style by "personnalise" style
                style.setEdgeBackground(oldStyle.getEdgeBackground());
                style.setEdgeBorder(oldStyle.getEdgeBorder());
                style.setEdgeText(oldStyle.getEdgeText());
                style.setArcLine(oldStyle.getArcLine());
                style.setArcText(oldStyle.getArcText());
                style.setBackground(oldStyle.getBackground());
                
                style.setEdgeBackground(c);
                refreshColor();
                boxStyle.setSelectedItem(GraphStyle.Personnalise);
            }
		}
		if(e.getSource() == buttonEdgeBorder) {
			Color c = JColorChooser.showDialog(null,"Sélection de couleur", null);
            if (c != null) {
            	GraphStyle oldStyle = style;
                style = GraphStyle.Personnalise;
                //Replace old style by "personnalise" style
                style.setEdgeBackground(oldStyle.getEdgeBackground());
                style.setEdgeBorder(oldStyle.getEdgeBorder());
                style.setEdgeText(oldStyle.getEdgeText());
                style.setArcLine(oldStyle.getArcLine());
                style.setArcText(oldStyle.getArcText());
                style.setBackground(oldStyle.getBackground());
                
                style.setEdgeBorder(c);
                refreshColor();
                boxStyle.setSelectedItem(GraphStyle.Personnalise);
            }
		}
		if(e.getSource() == buttonEdgeText) {
			Color c = JColorChooser.showDialog(null,"Sélection de couleur", null);
            if (c != null) {
            	GraphStyle oldStyle = style;
                style = GraphStyle.Personnalise;
                //Replace old style by "personnalise" style
                style.setEdgeBackground(oldStyle.getEdgeBackground());
                style.setEdgeBorder(oldStyle.getEdgeBorder());
                style.setEdgeText(oldStyle.getEdgeText());
                style.setArcLine(oldStyle.getArcLine());
                style.setArcText(oldStyle.getArcText());
                style.setBackground(oldStyle.getBackground());
                
                style.setEdgeText(c);
                refreshColor();
                boxStyle.setSelectedItem(GraphStyle.Personnalise);
            }
		}
		if(e.getSource() == buttonArcLine) {
			Color c = JColorChooser.showDialog(null,"Sélection de couleur", null);
            if (c != null) {
            	GraphStyle oldStyle = style;
                style = GraphStyle.Personnalise;
                //Replace old style by "personnalise" style
                style.setEdgeBackground(oldStyle.getEdgeBackground());
                style.setEdgeBorder(oldStyle.getEdgeBorder());
                style.setEdgeText(oldStyle.getEdgeText());
                style.setArcLine(oldStyle.getArcLine());
                style.setArcText(oldStyle.getArcText());
                style.setBackground(oldStyle.getBackground());
                
                style.setArcLine(c);
                refreshColor();
                boxStyle.setSelectedItem(GraphStyle.Personnalise);
            }
		}
		if(e.getSource() == buttonArcText) {
			Color c = JColorChooser.showDialog(null,"Sélection de couleur", null);
            if (c != null) {
            	GraphStyle oldStyle = style;
                style = GraphStyle.Personnalise;
                //Replace old style by "personnalise" style
                style.setEdgeBackground(oldStyle.getEdgeBackground());
                style.setEdgeBorder(oldStyle.getEdgeBorder());
                style.setEdgeText(oldStyle.getEdgeText());
                style.setArcLine(oldStyle.getArcLine());
                style.setArcText(oldStyle.getArcText());
                style.setBackground(oldStyle.getBackground());
                
                style.setArcText(c);
                refreshColor();
                boxStyle.setSelectedItem(GraphStyle.Personnalise);
            }
		}
		if(e.getSource() == buttonBackground) {
			Color c = JColorChooser.showDialog(null,"Sélection de couleur", null);
            if (c != null) {
            	GraphStyle oldStyle = style;
                style = GraphStyle.Personnalise;
                //Replace old style by "personnalise" style
                style.setEdgeBackground(oldStyle.getEdgeBackground());
                style.setEdgeBorder(oldStyle.getEdgeBorder());
                style.setEdgeText(oldStyle.getEdgeText());
                style.setArcLine(oldStyle.getArcLine());
                style.setArcText(oldStyle.getArcText());
                style.setBackground(oldStyle.getBackground());
                
                style.setBackground(c);
                refreshColor();
                boxStyle.setSelectedItem(GraphStyle.Personnalise);
            }
		}
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

}
