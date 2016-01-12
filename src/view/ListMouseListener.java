package view;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;

/**
 * Classe qui gère le clique droit sur le liste des composants
 * @author Groupe 3
 * @version 2016-01-12
 */
public class ListMouseListener extends MouseAdapter {
	
	private HCI hci;

	public ListMouseListener(HCI hci) {
		this.hci = hci;
	}
	
	@SuppressWarnings("rawtypes")
	public void mouseClicked(MouseEvent e){
		JList list = (JList)e.getSource();
		int mod = e.getModifiers();
		if ((mod & InputEvent.BUTTON3_MASK) != 0) {
			hci.getPopMenu().show(list, e.getX(), e.getY());
		}
	}
}
