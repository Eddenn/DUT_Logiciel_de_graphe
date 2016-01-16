package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Controller;

/**
 * Classe qui gèrent la fenêtre utilisateur pour modifier un arc.
 * 
 * @author Groupe 3
 * @version 2016-01-12
 */
public class PopupUpdateArc extends Popup {

	private static final long serialVersionUID = 2869913711173398321L;
	private JButton ok, annuler;
	@SuppressWarnings("rawtypes")
	private JComboBox boxDep, boxArr;
	private JTextField textfield;

	/**
	 * Méthode qui instancie la pop-up pour modifier un arc.
	 * 
	 * @param title
	 *            le titre de la pop-up.
	 * @param modal
	 * @param ctrl
	 *            le controleur utilisé.
	 * @param hci
	 *            le hci utilisé.
	 */
	public PopupUpdateArc(String title, boolean modal, Controller ctrl, HCI hci) {
		super(title, modal, ctrl, hci);
		this.setSize(300, 165);
		setLocationRelativeTo(null);

		String[] tabVertex = new String[ctrl.getGraph().getAlVertex().size()];

		for (int i = 0; i < ctrl.getGraph().getAlVertex().size(); i++) {
			tabVertex[i] = ctrl.getGraph().getAlVertex().get(i).getName();
		}

		JLabel text = new JLabel("<html> Saisissez les donnees de l'arc : <br/><br/> </html>");
		text.setHorizontalAlignment(JLabel.CENTER);
		add(text, "North");

		// Panel contenant les JComboBox et le textfield
		JPanel content = new JPanel(new BorderLayout());

		// Panel contenant les JComboBox
		JPanel panelComboBox = new JPanel();

		// Gestion du sommet de dï¿½part
		JLabel lDep = new JLabel("Depart : ");
		panelComboBox.add(lDep, "West");
		boxDep = new JComboBox(tabVertex);
		if (hci.getAlSelected().size() == 2) {
			boxDep.setSelectedItem(hci.getAlSelected().get(0));
		}
		panelComboBox.add(boxDep, "");

		// Gestion du sommet d'arrivï¿½
		JLabel lArr = new JLabel("Arrivee : ");
		panelComboBox.add(lArr);
		boxArr = new JComboBox(tabVertex);
		if (hci.getAlSelected().size() == 2) {
			boxArr.setSelectedItem(hci.getAlSelected().get(1));
		}
		panelComboBox.add(boxArr);

		// Panel contenant le textField
		JPanel panelTextField = new JPanel(new BorderLayout());
		textfield = new JTextField();
		textfield.addKeyListener(this);
		panelTextField.add(textfield);

		// Ajout des comboBox et textField a la fenetre
		content.add(panelComboBox, "North");
		content.add(panelTextField);
		add(content);

		// Panel contenant les boutons
		JPanel control = new JPanel();
		ok = new JButton("Valider");
		ok.addActionListener(this);
		annuler = new JButton("Annuler");
		annuler.addActionListener(this);
		control.add(ok);
		control.add(annuler);
		add(control, "South");

		setVisible(true);

	}

	private void valider() {
		int vertexDep = boxDep.getSelectedIndex();
		int vertexArr = boxArr.getSelectedIndex();

		if (this.ctrl.getNbSommet() > 0) {
			int value;
			try {
				value = Integer.parseInt(textfield.getText());

				if (ctrl.getGraph().isDirected()) {
					if (ctrl.updateArc(ctrl.getGraph().getAlVertex().get(vertexDep),
							ctrl.getGraph().getAlVertex().get(vertexArr), value))
						dispose();
					else
						JOptionPane.showMessageDialog(null, "Il n'existe pas d'arc entre les sommets", "Erreur",
								JOptionPane.ERROR_MESSAGE);
				} else {
					if (ctrl.updateArc(ctrl.getGraph().getAlVertex().get(vertexDep),
							ctrl.getGraph().getAlVertex().get(vertexArr), value)
							&& ctrl.updateArc(ctrl.getGraph().getAlVertex().get(vertexArr),
									ctrl.getGraph().getAlVertex().get(vertexDep), value))
						dispose();
					else
						JOptionPane.showMessageDialog(null, "Il n'existe pas d'arc entre les sommets", "Erreur",
								JOptionPane.ERROR_MESSAGE);
				}

			} catch (Exception exc) {
				textfield.setText("Valeur Erronée");
			}
		} else
			JOptionPane.showMessageDialog(null, "Un arc doit appartenir à deux sommets", "Erreur",
					JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Code de gestion de la modification de l'arc.
		if (e.getSource() == ok)
			valider();
		else
			dispose();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == 10 || arg0.getKeyCode() == 13) {
			valider();
		} else if (arg0.getKeyCode() == 27)
			dispose();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}