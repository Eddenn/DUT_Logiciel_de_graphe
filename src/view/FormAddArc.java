package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Controller;

public class FormAddArc extends JDialog implements ActionListener {

	private static final long serialVersionUID = 2869913711173398321L;
	private JButton ok, annuler;
	@SuppressWarnings("rawtypes")
	private JComboBox boxDep, boxArr;
	private JTextField valArc;
	private Controller ctrl;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FormAddArc(HCI parent, String title, boolean modal, Controller ctrl){
		super(parent,title,modal);
		this.setSize(300, 165);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		
		this.ctrl=ctrl;
		
		String[] tabVertex = new String[ctrl.getGraph().getAlVertex().size()];
		
		for (int i = 0; i < ctrl.getGraph().getAlVertex().size(); i++) {
			tabVertex[i] = ctrl.getGraph().getAlVertex().get(i).getName();
		}
		
		JLabel text = new JLabel("<html> Saisissez les donn�es de l'arc : <br/><br/> </html>");
		text.setHorizontalAlignment(JLabel.CENTER);
		add(text, "North");
		
		// Panel contenant les JComboBox et le textfield
		JPanel content = new JPanel(new BorderLayout());
		
		// Panel contenant les JComboBox
		JPanel panelComboBox = new JPanel();
		
		// Gestion du sommet de d�part
		JLabel lDep = new JLabel("D�part : ");
		panelComboBox.add(lDep,"West");
		boxDep = new JComboBox(tabVertex);
		panelComboBox.add(boxDep,"");
		
		// Gestion du sommet d'arriv�
		JLabel lArr = new JLabel("Arriv� : ");
		panelComboBox.add(lArr);
		boxArr = new JComboBox(tabVertex);
		panelComboBox.add(boxArr);
		
		// Panel contenant le textField
		JPanel panelTextField = new JPanel(new BorderLayout());
		
		// Gestion du TextField
		panelTextField.add(new JLabel("Valeur de l'arc : "),"West");
		valArc = new JTextField();
		
		if (! parent.getGraph().isValued())
			valArc.setEditable(false);
		
		panelTextField.add(valArc);
		
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok ){
			int vertexDep = boxDep.getSelectedIndex();
			int vertexArr = boxArr.getSelectedIndex();
			
			if (this.ctrl.getGraph().isValued()) {
				if (valArc.getText().matches("[0-9]+")) {
					ctrl.addArc(ctrl.getGraph().getAlVertex().get(vertexDep), ctrl.getGraph().getAlVertex().get(vertexArr), Integer.parseInt(valArc.getText()));
					dispose();
				}
				else
					valArc.setText("Valeur erron�e");
			}
			else {
				ctrl.addArc(ctrl.getGraph().getAlVertex().get(vertexDep), ctrl.getGraph().getAlVertex().get(vertexArr));
				dispose();
			}
		}
		
		else
			dispose();
	}
	

}
