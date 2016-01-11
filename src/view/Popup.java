package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import controller.Controller;

public abstract class Popup extends JDialog implements ActionListener{

	private static final long serialVersionUID = 9144781849668849155L;
	protected Controller ctrl;
	protected HCI hci;
	
	public Popup(String title, boolean modal, Controller ctrl, HCI hci) {
		super(hci, title, modal);
		this.hci = hci;
		this.ctrl = ctrl;
		this.setTitle(title);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
	}
	@Override
	public abstract void actionPerformed(ActionEvent e);

}
