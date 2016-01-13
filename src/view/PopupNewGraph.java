package view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;

/**
 * 
 * @author Groupe 3 info
 *
 */
public class PopupNewGraph extends Popup  {

	private static final long serialVersionUID = -1023319410573124162L;
	private JButton ok, annuler;
	@SuppressWarnings("rawtypes")
	private JComboBox boxOriented, boxValued;
	private boolean bEnd;
	private boolean bClose;
	
	/**
	* Méthode qui instancie la pop-up pour créer un nouveau graphe.
	 * @param title le titre de la pop-up
	 * @param modal 
	 * @param ctrl le controleur utilisé
	 * @param hci le hci utilisé
	 */
	public PopupNewGraph(String title, boolean modal, Controller ctrl, HCI hci) {
		super(title, modal, ctrl, hci);
		this.bEnd = false;
		this.bClose = false;
		this.setSize(300, 165);
		setLocationRelativeTo(null);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {bEnd=true;bClose=true;} 
         } ) ; 
		
		String[] tabRep = {"Oui","Non"};
		
		JLabel text = new JLabel("<html> Paramétrage du graphe : <br/><br/> </html>");
		text.setHorizontalAlignment(JLabel.CENTER);
		add(text, "North");
		
		// Panel contenant les JComboBox
		JPanel content = new JPanel();
		
		// Gestion de l'orienté
		JLabel lOriented = new JLabel("Orienté : ");
		content.add(lOriented);
		boxOriented = new JComboBox<String>(tabRep);
		boxOriented.addKeyListener(this);;
		content.add(boxOriented);
		
		// Gestion du valué
		JLabel lValued = new JLabel("Valué : ");
		content.add(lValued);
		boxValued = new JComboBox<String>(tabRep);
		boxValued.addKeyListener(this);
		content.add(boxValued);
		
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

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ok ){
			boolean bValued = boxValued.getSelectedItem().equals("Oui");
			boolean bOriented = boxOriented.getSelectedItem().equals("Oui");
			ctrl.newGraph(bOriented, bValued);
		}
		if(e.getSource() == annuler){
			bClose=true;
		}
		this.bEnd=true;
		dispose();
	}
	
	/*--Getters et Setters--*/
	public boolean getBEnd() {return this.bEnd;}
	public boolean getBClose() {return this.bClose;}

	
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode()==10 || arg0.getKeyCode()==13){
			boolean bValued = boxValued.getSelectedItem().equals("Oui");
			boolean bOriented = boxOriented.getSelectedItem().equals("Oui");
			ctrl.newGraph(bOriented, bValued);
			this.bEnd=true;
			dispose();
		}
		if(arg0.getKeyCode() == 27){
			bClose=true;
			this.bEnd=true;
			dispose();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

}
