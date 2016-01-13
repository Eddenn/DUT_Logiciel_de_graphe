package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Controller;

/**
 * Classe qui gère la fenêtre utilisateur pour lancer l'algorithme de rechercher de chemin
 * @author Groupe 3
 * @version 2016-01-12
 *
 */
public class PopupAlgoRC extends Popup {

	private static final long serialVersionUID = 1L;
	private JButton ok, annuler;
	private JComboBox boxDep, boxArr;

	/**
	 * Constructeur qui instancie la fenêtre pop-up pour ajouter un arc
	 * @param title le titre de la fenêtre
	 * @param modal
	 * @param ctrl le controleur lancé
	 * @param hci  le hci lancé
	 */
	public PopupAlgoRC(String title, boolean modal, Controller ctrl, HCI hci) {
		super(title, modal, ctrl, hci);
		this.setSize(300, 120);
		setLocationRelativeTo(null);
		
		String[] tabVertex = new String[ctrl.getGraph().getAlVertex().size()];
		
		for (int i = 0; i < ctrl.getGraph().getAlVertex().size(); i++) {
			tabVertex[i] = ctrl.getGraph().getAlVertex().get(i).getName();
		}

		JLabel text = new JLabel("<html> Choisissez le sommet de départ et le sommet d'arrivé : <br/><br/> </html>");
		text.setHorizontalAlignment(JLabel.CENTER);
		add(text, "North");
		
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
				
		// Ajout des comboBox à la fenetre
		add(panelComboBox,"North");
		
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
	
	public void valider(){
		//Ajouter la méthode à appeler en lui donnant en paramètre les valeurs des checkbox -> boxDep.getSelectedIndex(); boxArr.getSelectedIndex();
		dispose();
		super.ctrl.startParcoursLienExiste(boxDep.getSelectedIndex(),boxArr.getSelectedIndex());
	}

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
