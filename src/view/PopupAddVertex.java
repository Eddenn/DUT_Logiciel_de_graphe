package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Controller;

/**
 * Classe qui gère la fenêtre utilisateur pour ajouter un sommet.
 * @author Groupe 3
 * @version 2016-01-12
 */
public class PopupAddVertex extends Popup implements ActionListener {

	private static final long serialVersionUID = -8234116112966360284L;
	private JTextField nom;
	private JButton ok, annuler;

	/** Méthode qui instancie la pop-up pour ajouter un sommet.
	 * @param title le titre de la pop-up.
	 * @param modal 
	 * @param ctrl le controleur utilisé.
	 * @param hci le hci utilisé.
	 */
	public PopupAddVertex(String title, boolean modal, Controller ctrl, HCI hci) {
		super(title, modal, ctrl, hci);
		this.hci = hci;
		this.ctrl = ctrl;
		this.setSize(300, 150);
		setLocationRelativeTo(null);
		
		// Contenu
		JPanel content = new JPanel();
		content.setBackground(Color.white);
		content.setPreferredSize(new Dimension(300, 70));
		content.setBorder(BorderFactory.createTitledBorder("Sommet"));
		JLabel nomL = new JLabel("Nom:");
		nom = new JTextField();
		nom.setPreferredSize(new Dimension(100, 25));
		content.add(nomL);
		content.add(nom);
		add(content);

		// Panel Bouton
		JPanel control = new JPanel();
		ok = new JButton("Ok");
		ok.addActionListener(this);
		annuler = new JButton("Annuler");
		annuler.addActionListener(this);
		control.add(ok);
		control.add(annuler);
		add(control, "South");

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			if(!ctrl.addVertex(nom.getText())){
				hci.getHmVertex().put(nom.getText(), new Point(0, 0));
				hci.getAlSelected().clear();
				hci.getAlSelected().add(nom.getText());
			}
			dispose();
		}
		if (e.getSource() == annuler) {
			dispose();
		}
		dispose();
	}

}
