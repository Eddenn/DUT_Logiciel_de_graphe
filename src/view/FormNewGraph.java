package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;

public class FormNewGraph extends JDialog implements ActionListener {

	private static final long serialVersionUID = -1023319410573124162L;
	private JButton ok, annuler;
	@SuppressWarnings("rawtypes")
	private JComboBox boxOriented, boxValued;
	private Controller ctrl;
	private boolean bEnd;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FormNewGraph(HCI parent, String title, boolean modal, Controller ctrl){
		super(parent,title,modal);
		this.bEnd = false;
		this.setSize(300, 165);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) 
            {
    			System.exit(1);
            } 
         } ) ; 
		
		this.ctrl=ctrl;
		
		String[] tabRep = {"Oui","Non"};
		
		JLabel text = new JLabel("<html> Paramétrage du graphe : <br/><br/> </html>");
		text.setHorizontalAlignment(JLabel.CENTER);
		add(text, "North");
		
		// Panel contenant les JComboBox
		JPanel content = new JPanel();
		
		// Gestion de l'orienté
		JLabel lOriented = new JLabel("Orienté : ");
		content.add(lOriented);
		boxOriented = new JComboBox(tabRep);
		content.add(boxOriented);
		
		// Gestion du valué
		JLabel lValued = new JLabel("Valué : ");
		content.add(lValued);
		boxValued = new JComboBox(tabRep);
		content.add(boxValued);
		
		add(content);
		
		// Panel contenant les boutons
		JPanel control =new JPanel();
		ok = new JButton ("Valider");
		ok.addActionListener(this);
		annuler = new JButton ("Annuler");
		annuler.addActionListener(this);
		control.add(annuler);
		control.add(ok);
		add(control, "South");
		
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok ){
			boolean bValued = boxValued.getSelectedItem().equals("Oui");
			boolean bOriented = boxOriented.getSelectedItem().equals("Oui");
			ctrl.newGraph(bOriented, bValued);
		}
		this.bEnd=true;
		dispose();
	}
	
	public boolean getBEnd() {return this.bEnd;}

}
