package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.Controller;
import model.Vertex;

/**
 * Classe gérant le fenêtre utilisateur pour supprimer un arc.
 * @author Groupe 3
 * @version 2016-01-12
 */
public class PopupDeleteArc extends Popup {

	private static final long serialVersionUID = 2869913711173398321L;
	private JButton ok, annuler;
	@SuppressWarnings("rawtypes")
	private JComboBox boxDep, boxArr;
	
	/**
	 * Constructeur qui ouvre une fenêtre pop-up pour supprimer un arc.
	 * @param title le titre de la fenêtre
	 * @param modal
	 * @param ctrl le constructeur lancé
	 * @param hci le constructeur lancé
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PopupDeleteArc(String title, boolean modal, Controller ctrl, HCI hci) {
		super(title, modal, ctrl, hci);
		this.setSize(300, 165);
		setLocationRelativeTo(null);
		
		String[] tabVertex = new String[ctrl.getGraph().getAlVertex().size()];
		
		for (int i = 0; i < ctrl.getGraph().getAlVertex().size(); i++) {
			tabVertex[i] = (ctrl.getGraph().getAlVertex().get(i).getName());
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
		boxDep.addKeyListener(this);
		if(hci.getAlSelected().size() == 2) {
			boxDep.setSelectedItem(hci.getAlSelected().get(0));
		}
		panelComboBox.add(boxDep,"");
		
		// Gestion du sommet d'arrivé
		JLabel lArr = new JLabel("Arrivée : ");
		panelComboBox.add(lArr);
		boxArr = new JComboBox(tabVertex);
		boxArr.addKeyListener(this);
		if(hci.getAlSelected().size() == 2) {
			boxArr.setSelectedItem(hci.getAlSelected().get(1));
		}
		panelComboBox.add(boxArr);
		
		// Panel contenant le textField
		JPanel panelTextField = new JPanel(new BorderLayout());

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
					
		Vertex vDep = ctrl.getGraph().getAlVertex().get(vertexDep);
		Vertex vArr = ctrl.getGraph().getAlVertex().get(vertexArr);
		
		if (! ctrl.delArc(vDep, vArr)) {
			JOptionPane.showMessageDialog(null, "Il n'existe pas d'arc entre les sommets", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		else
			dispose();
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
