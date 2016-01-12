package view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;

/**
 * Classe qui gère la fenêtre utilisateur pour lancer l'algorithme de Dijkstra-Moore
 * @author Groupe 3
 * @version 2016-01-12
 */
public class PopupAlgoDM extends Popup {

	private static final long serialVersionUID = 1L;
	private JButton ok, annuler;
	private JComboBox boxDep;
	
	/**
	 * Constructeur qui instancie la fenêtre pop-up pour lancer l'algorithme
	 * @param title le titre de la fenêtre
	 * @param modal
	 * @param ctrl le controleur lancé
	 * @param hci  le hci lancé
	 */

	public PopupAlgoDM(String title, boolean modal, Controller ctrl, HCI hci) {
		super(title, modal, ctrl, hci);
		this.setSize(300, 120);
		setLocationRelativeTo(null);
		
		String[] tabVertex = new String[ctrl.getGraph().getAlVertex().size()];
		
		for (int i = 0; i < ctrl.getGraph().getAlVertex().size(); i++) {
			tabVertex[i] = ctrl.getGraph().getAlVertex().get(i).getName();
		}

		JLabel text = new JLabel("<html> Choisissez le sommet de départ: <br/><br/> </html>");
		text.setHorizontalAlignment(JLabel.CENTER);
		add(text, "North");
		
		// Panel contenant le JComboBox
		JPanel panelComboBox = new JPanel();
		
		// Gestion du sommet de départ
		JLabel lDep = new JLabel("Départ : ");
		panelComboBox.add(lDep,"West");
		boxDep = new JComboBox(tabVertex);
		panelComboBox.add(boxDep,"");
						
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
		//Ajouter la méthode à appeler en lui donnant en paramètre la valeurs du checkbox -> boxDep.getSelectedIndex(); 
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
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
