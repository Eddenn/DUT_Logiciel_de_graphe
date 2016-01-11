package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Controller;
import model.Vertex;

public class PopupColorizeVertex extends Popup implements ActionListener {

	private static final long serialVersionUID = -8234116112966360284L;
	private JButton buttonChoice,buttonDelete;
	private JButton ok, annuler;
	private Color c;

	public PopupColorizeVertex(String title, boolean modal, Controller ctrl, HCI hci) {
		super(title, modal, ctrl, hci);
		this.hci = hci;
		this.ctrl = ctrl;
		this.setSize(300, 150);
		setLocationRelativeTo(null);
		
		c = Color.WHITE;
		
		// Contenu
		JPanel content = new JPanel(new GridLayout(2,1));
		content.setBackground(Color.white);
		content.setPreferredSize(new Dimension(300, 70));
		content.setBorder(BorderFactory.createTitledBorder("Sommet"));
		buttonChoice = new JButton("Choix de\n la colorisation");
		buttonChoice.addActionListener(this);
		content.add(buttonChoice);
		buttonDelete = new JButton("Supprimer \nla colorisation");
		buttonDelete.addActionListener(this);
		content.add(buttonDelete);
		add(content);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == buttonChoice) {
			c = JColorChooser.showDialog(null,"Sélection de couleur", null);
			for (String s : hci.getGraphPanel().getAlSelected()) {
				for(Vertex v : hci.getGraph().getAlVertex()) {
					if(v.getName().equals(s)) {
						v.setColor(c);
					}
				}
				hci.refresh();
			}
		}
		
		if (e.getSource() == buttonDelete ) {
			for (String s : hci.getGraphPanel().getAlSelected()) {
				for(Vertex v : hci.getGraph().getAlVertex()) {
					if(v.getName().equals(s)) {
						v.setColor(null);
					}
				}
				hci.refresh();
			}
		}
		dispose();
	}

}
