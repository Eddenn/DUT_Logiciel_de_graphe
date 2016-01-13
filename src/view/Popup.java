package view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JDialog;

import controller.Controller;

/**
 * Classe abstraite pour la création des pop-up utilisateur pour la gestion des composants du graphe
 * @author Groupe 3
 * @version 2016-01-12
 */
public abstract class Popup extends JDialog implements ActionListener, KeyListener{

	private static final long serialVersionUID = 9144781849668849155L;
	protected Controller ctrl;
	protected HCI hci;
	
	/**
	 * Méthode qui instancie la pop-up.
	 * @param title le titre de la pop-up
	 * @param modal 
	 * @param ctrl le controleur utilisé
	 * @param hci le hci utilisé
	 */
	public Popup(String title, boolean modal, Controller ctrl, HCI hci) {
		super(hci, title, modal);
		this.hci = hci;
		this.ctrl = ctrl;
		this.setTitle(title);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
	}

}
