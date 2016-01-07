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
import model.Vertex;

public class FormDeleteArc extends JDialog implements ActionListener {

	private static final long serialVersionUID = 2869913711173398321L;
	private JButton ok, annuler;
	@SuppressWarnings("rawtypes")
	private JComboBox boxDep, boxArr;
	private Controller ctrl;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FormDeleteArc(HCI parent, String title, boolean modal, Controller ctrl){
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
		JLabel lDep = new JLabel("Depart : ");
		panelComboBox.add(lDep,"West");
		boxDep = new JComboBox(tabVertex);
		panelComboBox.add(boxDep,"");
		
		// Gestion du sommet d'arriv�
		JLabel lArr = new JLabel("Arrivee : ");
		panelComboBox.add(lArr);
		boxArr = new JComboBox(tabVertex);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok ){
			int vertexDep = boxDep.getSelectedIndex();
			int vertexArr = boxArr.getSelectedIndex();
			
			System.out.println("vertexDep : " + vertexDep + " ; vertexArr : " + vertexArr );
			
			Vertex vDep = ctrl.getGraph().getAlVertex().get(vertexDep);
			Vertex vArr = ctrl.getGraph().getAlVertex().get(vertexDep);
			
			int indiceDep = 0;
			int indiceArr = 0;
			
			System.out.println("indiceDep : " + indiceDep + " ; indiceArr : " + indiceArr );
			
			for (int i = 0; i < vDep.getAlArcs().size(); i++){
				if (vDep.getAlArcs().get(i).equals(vArr)){
					indiceDep = i;
					break;
				}
			}
			
			for (int i = 0; i < vArr.getAlArcs().size(); i++){
				if (vArr.getAlArcs().get(i).equals(vDep)){
					indiceArr = i;
					break;
				}
			}
			
			try{
				ctrl.getGraph().getAlVertex().get(vertexDep).getAlArcs().remove(ctrl.getGraph().getAlVertex().get(vertexDep).getAlArcs().get(indiceArr));
				ctrl.getGraph().getAlVertex().get(vertexArr).getAlArcs().remove(ctrl.getGraph().getAlVertex().get(vertexDep).getAlArcs().get(indiceDep));
			} catch (Exception e1){e1.printStackTrace();}

		}
		
		dispose();
	}
	

}
