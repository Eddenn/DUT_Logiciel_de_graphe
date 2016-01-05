package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;

public class FormAddArc extends JDialog implements ActionListener {

	private JButton ok, annuler;
	private JComboBox boxDep, boxArr;
	private Controller ctrl;
	
	public FormAddArc(HCI parent, String title, boolean modal, Controller ctrl){
		super(parent,title,modal);
		this.ctrl=ctrl;
		this.setSize(300, 165);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		String[] tabVertex = new String[ctrl.getGraph().getAlVertex().size()];
		
		for (int i = 0; i < ctrl.getGraph().getAlVertex().size(); i++) {
			tabVertex[i] = ctrl.getGraph().getAlVertex().get(i).getName();
		}
		
		JLabel text = new JLabel("<html> Saisissez les données de l'arc : <br/><br/> </html>");
		text.setHorizontalAlignment(JLabel.CENTER);
		add(text, "North");
		
		// Panel contenant les JComboBox
		JPanel content = new JPanel();
		
		// Gestion du sommet de départ
		JLabel lDep = new JLabel("Départ : ");
		content.add(lDep);
		boxDep = new JComboBox(tabVertex);
		content.add(boxDep);
		
		// Gestion du sommet d'arrivé
		JLabel lArr = new JLabel("Arrivé : ");
		content.add(lArr);
		boxArr = new JComboBox(tabVertex);
		content.add(boxArr);
		
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == ok ){
			int vertexDep = boxDep.getSelectedIndex();
			int vertexArr = boxArr.getSelectedIndex();
			
			ctrl.addArc(ctrl.getGraph().getAlVertex().get(vertexDep), ctrl.getGraph().getAlVertex().get(vertexArr));
		}
		dispose();
	}
	

}
