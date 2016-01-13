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
 * Classe qui g�re la fen�tre utilisateur pour lancer l'algorithme de rechercher de chemin
 * @author Groupe 3
 * @version 2016-01-12
 *
 */
public class PopupAlgoRC extends Popup {

	private static final long serialVersionUID = 1L;
	private JButton ok, annuler;
	private JComboBox boxDep, boxArr;

	/**
	 * Constructeur qui instancie la fen�tre pop-up pour ajouter un arc
	 * @param title le titre de la fen�tre
	 * @param modal
	 * @param ctrl le controleur lanc�
	 * @param hci  le hci lanc�
	 */
	public PopupAlgoRC(String title, boolean modal, Controller ctrl, HCI hci) {
		super(title, modal, ctrl, hci);
		this.setSize(300, 120);
		setLocationRelativeTo(null);
		
		String[] tabVertex = new String[ctrl.getGraph().getAlVertex().size()];
		
		for (int i = 0; i < ctrl.getGraph().getAlVertex().size(); i++) {
			tabVertex[i] = ctrl.getGraph().getAlVertex().get(i).getName();
		}

		JLabel text = new JLabel("<html> Choisissez le sommet de d�part et le sommet d'arriv� : <br/><br/> </html>");
		text.setHorizontalAlignment(JLabel.CENTER);
		add(text, "North");
		
		// Panel contenant les JComboBox
		JPanel panelComboBox = new JPanel();
		
		// Gestion du sommet de d�part
		JLabel lDep = new JLabel("D�part : ");
		panelComboBox.add(lDep,"West");
		boxDep = new JComboBox(tabVertex);
		panelComboBox.add(boxDep,"");
		
		// Gestion du sommet d'arriv�e
		JLabel lArr = new JLabel("Arriv�e : ");
		panelComboBox.add(lArr);
		boxArr = new JComboBox(tabVertex);
		panelComboBox.add(boxArr);
				
		// Ajout des comboBox � la fenetre
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
		//Ajouter la m�thode � appeler en lui donnant en param�tre les valeurs des checkbox -> boxDep.getSelectedIndex(); boxArr.getSelectedIndex();
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
