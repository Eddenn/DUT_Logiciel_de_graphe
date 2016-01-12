package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Controller;

/**
 * Classe gérant la fenêtre utilisateur d'ajout d'arc
 * @author Groupe 3
 * @version 2016-01-12
 */
public class PopupAddArc extends Popup {

	private static final long serialVersionUID = 2869913711173398321L;
	private JButton ok, annuler;
	@SuppressWarnings("rawtypes")
	private JComboBox boxDep, boxArr;
	private JTextField valArc;
	
	
	/**
	 * Constructeur qui instancie la fenêtre pop-up pour ajouter un arc
	 * @param title le titre de la fenêtre
	 * @param modal
	 * @param ctrl le controleur lancé
	 * @param hci  le hci lancé
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PopupAddArc(String title, boolean modal, Controller ctrl, HCI hci) {
		super(title, modal, ctrl, hci);
		this.setSize(300, 165);
		setLocationRelativeTo(null);
		
		String[] tabVertex = new String[ctrl.getGraph().getAlVertex().size()];
		
		for (int i = 0; i < ctrl.getGraph().getAlVertex().size(); i++) {
			tabVertex[i] = (ctrl.getGraph().getAlVertex().get(i).getName() + "        ").substring(0, 4);
		}

		JLabel text = new JLabel("<html> Saisissez les données de l'arc : <br/><br/> </html>");
		text.setHorizontalAlignment(JLabel.CENTER);
		add(text, "North");
		
		// Panel contenant les JComboBox et le textfield
		JPanel content = new JPanel(new BorderLayout());
		
		// Panel contenant les JComboBox
		JPanel panelComboBox = new JPanel();
		
		// Gestion du sommet de départ
		JLabel lDep = new JLabel("Départ : ");
		panelComboBox.add(lDep,"West");
		boxDep = new JComboBox(tabVertex);
		panelComboBox.add(boxDep,"");
		
		// Gestion du sommet d'arrivée
		JLabel lArr = new JLabel("Arrivée : ");
		panelComboBox.add(lArr);
		boxArr = new JComboBox(tabVertex);
		panelComboBox.add(boxArr);
		
		// Panel contenant le textField
		JPanel panelTextField = new JPanel(new BorderLayout());
		
		// Gestion du TextField
		panelTextField.add(new JLabel("Valeur de l'arc : "),"West");
		valArc = new JTextField();
		valArc.addKeyListener(this);
		
		if (! hci.getGraph().isValued())
			valArc.setEditable(false);
		
		panelTextField.add(valArc);
		
		// Ajout des comboBox et textField a la fenetre
		content.add(panelComboBox,"North");
		content.add(panelTextField);
		add(content);
		
		// Panel contenant les boutons
		JPanel control =new JPanel();
		ok = new JButton ("Valider");
		ok.addActionListener(this);
		annuler = new JButton ("Annuler");
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
			if (this.ctrl.getGraph().isValued()) {
				if (valArc.getText().matches("[0-9]+")) {
					ctrl.addArc(ctrl.getGraph().getAlVertex().get(vertexDep), ctrl.getGraph().getAlVertex().get(vertexArr), Integer.parseInt(valArc.getText()));
					dispose();
				}
				else
					valArc.setText("Valeur erronée");
			}
			else {
				ctrl.addArc(ctrl.getGraph().getAlVertex().get(vertexDep), ctrl.getGraph().getAlVertex().get(vertexArr));
				dispose();
			}
		}
		else
			JOptionPane.showMessageDialog(null, "Un arc doit appartenir à deux sommets", "Erreur", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok )
			valider();
		else
			dispose();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode()==10 || arg0.getKeyCode()==13) {
			valider();
		}
		else if (arg0.getKeyCode() == 27)
			dispose();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	

}
