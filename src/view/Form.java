package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Controller;
import model.Vertex;

public class Form extends JDialog implements ActionListener {

	private static final long serialVersionUID = -8234116112966360284L;
	private JTextField nom;
	private JButton ok, annuler;
	private Controller ctrl;
	private HCI hci;

	public Form(HCI hci, String title, boolean modal, Controller ctrl) {
		super(hci, title, modal);
		this.hci = hci;
		this.ctrl = ctrl;
		this.setSize(300, 150);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

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
		if (this.getTitle().equals("Ajouter un sommet")) {
			if (e.getSource() == ok) {
				ctrl.addVertex(nom.getText());

				HCI.hmVertex.put(nom.getText(), new Point(0, 0));
				hci.setStrSelected(nom.getText());

				dispose();
			}
			if (e.getSource() == annuler) {
				setVisible(false);
			}
		}

		if (this.getTitle().equals("Modifier un sommet")) {
			if (e.getSource() == ok && hci.getStrSelected() != null) {
				for (Vertex v : ctrl.getGraph().getAlVertex()) {
					if (v.getName().equals(hci.getStrSelected())) {
						v.setName(nom.getText());
						HCI.hmVertex.put( nom.getText() , HCI.hmVertex.get(hci.getStrSelected()) );
						HCI.hmVertex.remove(hci.getStrSelected());
						hci.setStrSelected(nom.getText());
						hci.refresh();
					}
				}
<<<<<<< HEAD

=======
				
				HCI.hmVertex.put(nom.getText(), HCI.hmVertex.get(hci.getStrSelected()));
				HCI.hmVertex.remove(hci.getStrSelected());
				hci.setStrSelected(nom.getText());
				hci.refresh();
>>>>>>> 485fca41d8e0737060526a340e07162f087ebfec
				dispose();
			}
			if (e.getSource() == annuler) {
				setVisible(false);
			}
		}
	}

}
