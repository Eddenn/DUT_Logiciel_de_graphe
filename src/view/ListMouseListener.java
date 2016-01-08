package view;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;

public class ListMouseListener extends MouseAdapter {
	
	private HCI hci;
	
	public ListMouseListener(HCI hci) {
		this.hci = hci;
	}
	
	public void mouseClicked(MouseEvent e){
		System.out.println(e.getButton());
		JList list = (JList)e.getSource();
		
		if(e.getButton()==MouseEvent.BUTTON3) {
			System.out.println("Clic");
			
			mouseClicked( new MouseEvent((JList)e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), 1, true, MouseEvent.BUTTON1));
		}
		int mod = e.getModifiers();
		if ((mod & InputEvent.BUTTON3_MASK) != 0) {
			hci.getPopMenu().show(list, e.getX(), e.getY());
		}
	}
}
