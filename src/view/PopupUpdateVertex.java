package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Controller;
import model.Vertex;

public class PopupUpdateVertex extends Popup implements ActionListener {

	private static final long serialVersionUID = -8234116112966360284L;
	private JTextField nom;
	private JButton ok, annuler;

	public PopupUpdateVertex(String title, boolean modal, Controller ctrl, HCI hci) {
		super(title, modal, ctrl, hci);
		this.hci = hci;
		this.ctrl = ctrl;
		this.setSize(300, 150);
		setLocationRelativeTo(null);
		
		// Contenu
		JPanel content = new JPanel();
		content.setBackground(Color.white);
		content.setPreferredSize(new Dimension(300, 70));
		if (this.getTitle().equals("Modifier un sommet"))
			content.setBorder(BorderFactory.createTitledBorder("Modification du Sommet " + hci.getAlSelected().get(0)));
		else
			content.setBorder(BorderFactory.createTitledBorder("Sommet"));
		JLabel nomL = new JLabel("Nom:");
		nom = new JTextField();
		nom.setText(hci.getAlSelected().get(0));
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
		if (e.getSource() == ok && !nom.getText().isEmpty()) {
			for (Vertex v : ctrl.getGraph().getAlVertex()) {
				if (v.getName().equals(hci.getAlSelected().get(0))) {
					v.setName(nom.getText());
					hci.hmVertex.put(nom.getText(), hci.hmVertex.get(hci.getAlSelected().get(0)));
					hci.hmVertex.remove(hci.getAlSelected().get(0));
					hci.getAlSelected().clear();
					hci.getAlSelected().add(nom.getText());
					hci.refresh();
				}
			}
			dispose();
		}
<<<<<<< HEAD:src/view/Form.java

		else if (this.getTitle().equals("Modifier un sommet")) {
			if (e.getSource() == ok && !nom.getText().isEmpty()) {
				for (Vertex v : ctrl.getGraph().getAlVertex()) {
					if (v.getName().equals(hci.getAlSelected().get(0))) {
						v.setName(nom.getText());
						hci.hmVertex.put(nom.getText(), hci.hmVertex.get(hci.getAlSelected().get(0)));
						hci.hmVertex.remove(hci.getAlSelected().get(0));
						hci.getAlSelected().clear();
						hci.getAlSelected().add(nom.getText());
						hci.refresh();
					}
				}
				dispose();
			}
			else if (e.getSource() == annuler)
				dispose();
			else
				JOptionPane.showMessageDialog(null, "Le nom du sommet ne doit pas être vide", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
=======
		if (e.getSource() == annuler) {
			setVisible(false);
		}
		dispose();
>>>>>>> 6b2fc14b19b8ae6e71e30e7185a318164173fb6b:src/view/PopupUpdateVertex.java
	}

}
