package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;
import model.Vertex;

public class PopupDeleteArc extends Popup implements ActionListener {

	private static final long serialVersionUID = 2869913711173398321L;
	private JButton ok, annuler;
	@SuppressWarnings("rawtypes")
	private JComboBox boxDep, boxArr;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PopupDeleteArc(String title, boolean modal, Controller ctrl, HCI hci) {
		super(title, modal, ctrl, hci);
		this.setSize(300, 165);
		
		String[] tabVertex = new String[ctrl.getGraph().getAlVertex().size()];
		
		for (int i = 0; i < ctrl.getGraph().getAlVertex().size(); i++) {
			tabVertex[i] = ctrl.getGraph().getAlVertex().get(i).getName();
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
		panelComboBox.add(boxDep,"");
		
		// Gestion du sommet d'arrivé
		JLabel lArr = new JLabel("Arrivée : ");
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
			
			int indiceDep = -1;
			int indiceArr = -1;
			
			System.out.println("indiceDep : " + indiceDep + " ; indiceArr : " + indiceArr );
			
			for (int i = 0; i < vDep.getAlArcs().size(); i++){
				if (vDep.getAlArcs().get(i).equals(vArr)){
					indiceDep = i;
					break;
				}
				if (vArr.getAlArcs().get(i).equals(vDep)){
					indiceArr = i;
					break;
				}
			}
			
			if (indiceDep != -1 && indiceArr != -1) {
				ctrl.getGraph().getAlVertex().get(vertexDep).getAlArcs().remove(ctrl.getGraph().getAlVertex().get(vertexDep).getAlArcs().get(indiceArr));
				ctrl.getGraph().getAlVertex().get(vertexArr).getAlArcs().remove(ctrl.getGraph().getAlVertex().get(vertexDep).getAlArcs().get(indiceDep));
			}
		}
		
		dispose();
	}
	

}
