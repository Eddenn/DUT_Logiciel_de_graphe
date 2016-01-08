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

import com.itextpdf.text.pdf.TextField;

import controller.Controller;
import model.Arc;
import model.Vertex;

public class FormUpdateArc extends JDialog implements ActionListener {

	private static final long serialVersionUID = 2869913711173398321L;
	private JButton ok, annuler;
	@SuppressWarnings("rawtypes")
	private JComboBox boxDep, boxArr;
	private JTextField valArc;
	private Controller ctrl;
	private JTextField textfield;

	public FormUpdateArc(HCI parent, String title, boolean modal, Controller ctrl) {
		super(parent, title, modal);
		this.setSize(300, 165);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		JComboBox boxDep, boxArr;
		
		this.ctrl = ctrl;

		String[] tabVertex = new String[ctrl.getGraph().getAlVertex().size()];

		for (int i = 0; i < ctrl.getGraph().getAlVertex().size(); i++) {
			tabVertex[i] = ctrl.getGraph().getAlVertex().get(i).getName();
		}

		JLabel text = new JLabel("<html> Saisissez les donnees de l'arc : <br/><br/> </html>");
		text.setHorizontalAlignment(JLabel.CENTER);
		add(text, "North");

		// Panel contenant les JComboBox et le textfield
		JPanel content = new JPanel(new BorderLayout());

		// Panel contenant les JComboBox
		JPanel panelComboBox = new JPanel();

		// Gestion du sommet de d�part
		JLabel lDep = new JLabel("Depart : ");
		panelComboBox.add(lDep, "West");
		boxDep = new JComboBox(tabVertex);
		panelComboBox.add(boxDep, "");

		// Gestion du sommet d'arriv�
		JLabel lArr = new JLabel("Arrivee : ");
		panelComboBox.add(lArr);
		boxArr = new JComboBox(tabVertex);
		panelComboBox.add(boxArr);

		// Panel contenant le textField
		JPanel panelTextField = new JPanel(new BorderLayout());
		textfield = new JTextField();
		panelTextField.add(textfield);

		// Ajout des comboBox et textField a la fenetre
		content.add(panelComboBox, "North");
		content.add(panelTextField);
		add(content);

		// Panel contenant les boutons
		JPanel control = new JPanel();
		ok = new JButton("Valider");
		ok.addActionListener(this);
		annuler = new JButton("Annuler");
		annuler.addActionListener(this);
		control.add(ok);
		control.add(annuler);
		add(control, "South");

		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Code de gestion de la modification de l'arc.
				if (e.getSource() == ok) {
					Vertex vOrigine = null;
					Vertex vDest = null;

					try {
						for (Vertex v : ctrl.getGraph().getAlVertex()) {
							if (v.getName().equals("a")) {
								vOrigine = v;
							}
						}
						for (Vertex v : ctrl.getGraph().getAlVertex()) {
							if (v.getName().equals("b")) {
								vDest = v;
							}
						}

						for (Arc a : vOrigine.getAlArcs()) {
							for (Arc b : vDest.getAlArcs()) {
								if (a.getVertex() == vDest || b.getVertex() == vOrigine || true) {
									int value = Integer.parseInt(textfield.getText());
									a.setValue(value);
									b.setValue(value);
								}
							}
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}
		dispose();
	}
}