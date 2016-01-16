package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.*;

/**
 * Classe permettant d'afficher la liste des sommets et des arcs
 * 
 * @author Groupe 3
 * @version 2016-01-13
 */

public class SwitchList extends JPanel {

	private static final long serialVersionUID = -967795867440790085L;
	@SuppressWarnings("rawtypes")
	private JList listOfObject;
	private JButton jbSwitch, buttonMatrix;
	private JLabel lTitle, lInfo;
	private int state = 1;
	private HCI hci;

	/**
	 * Constructeur permettant de cr�er la liste
	 * 
	 * @param hci
	 *            l'IHM du programme
	 */
	@SuppressWarnings("rawtypes")
	public SwitchList(HCI hci) {
		this.hci = hci;
		setLayout(new BorderLayout());

		/*--Haut de la liste--*/
		JPanel jpTop = new JPanel();
		jpTop.setBackground(Color.WHITE);
		// Button used to switch
		ImageIcon iSwitch = new ImageIcon(getClass().getResource("/switch.png"));
		jbSwitch = new JButton(iSwitch);
		jbSwitch.setContentAreaFilled(false);
		jbSwitch.setBorderPainted(false);
		jbSwitch.setRolloverIcon(new ImageIcon("images/switch_rollover.png"));
		jbSwitch.setMargin(new Insets(0, 0, 0, 0));
		jbSwitch.setBackground(new Color(255, 255, 255));
		jbSwitch.setForeground(new Color(255, 255, 255));
		jbSwitch.setToolTipText("Changer de liste");
		jbSwitch.addActionListener(hci);
		jpTop.add(jbSwitch);
		lTitle = new JLabel("Liste des Sommets");
		jpTop.add(lTitle);
		add(jpTop, BorderLayout.NORTH);
		/*-----------------------*/

		// JList
		listOfObject = new JList();
		DefaultListCellRenderer renderer = (DefaultListCellRenderer) listOfObject.getCellRenderer(); // Center
																										// string
																										// in
																										// the
																										// list
		renderer.setHorizontalAlignment(JLabel.CENTER);
		// -----------------------
		listOfObject.addMouseListener(new ListMouseListener(hci));
		listOfObject.addListSelectionListener(hci);
		JScrollPane jscrPanel = new JScrollPane(listOfObject);
		add(jscrPanel, BorderLayout.CENTER);

		/*--Bas de la liste --*/
		JPanel jpBottom = new JPanel();
		jpBottom.setBackground(Color.WHITE);
		lInfo = new JLabel("");
		jpBottom.add(lInfo);
		add(jpBottom, BorderLayout.SOUTH);

		this.setPreferredSize(new Dimension(200, 500));
	}

	// Getters and Setters
	@SuppressWarnings("unchecked")
	public JList<String> getListOfObject() {
		return this.listOfObject;
	}

	public JButton getJBSwitch() {
		return this.jbSwitch;
	}

	public Object getJBMatrix() {
		return this.buttonMatrix;
	}

	/**
	 * M�thode mettant � jour la JList pr�sente dans SwitchList en fonction du
	 * graphe
	 */
	@SuppressWarnings("unchecked")
	public void refresh() {
		Graph graphLoaded = hci.getGraph();
		if (state == 0) { // State of the SwitchList (0 = Sommets)
			String[] tabVertex = new String[graphLoaded.getAlVertex().size()];
			int cpt = 0;
			for (Vertex v : graphLoaded.getAlVertex()) {
				tabVertex[cpt] = v.getName();
				cpt++;
			}
			listOfObject.setListData(tabVertex);
		} else if (state == 1) { // State of the SwitchList (1 = Arcs)
			int nbArc = 0;
			for (Vertex v : graphLoaded.getAlVertex()) {
				for (@SuppressWarnings("unused")
				Arc a : v.getAlArcs()) {
					nbArc++;
				}
			}
			String[] tabArc = new String[nbArc];
			int cpt = 0;
			boolean bFound = false;
			for (Vertex v : graphLoaded.getAlVertex()) {
				if (graphLoaded.isValued()) {
					if (graphLoaded.isDirected()) {
						// Valu� et orient�
						for (Arc a : v.getAlArcs()) {
							tabArc[cpt] = HCI.centerStr(v.getName(), 5) + "------"
									+ HCI.centerStr("" + a.getIValue(), 7) + "----->"
									+ HCI.centerStr(a.getVertex().getName(), 5);
							cpt++;
						}
					} else {
						// Valu� et non orient�
						for (Arc a : v.getAlArcs()) {
							bFound = false;
							for (String s : tabArc) {
								if (s != null && (s.equals(HCI.centerStr(a.getVertex().getName(), 5) + "------"
										+ HCI.centerStr("" + a.getIValue(), 7) + "------"
										+ HCI.centerStr(v.getName(), 5)))) {
									bFound = true;
								}
							}
							if (!bFound) {
								tabArc[cpt] = HCI.centerStr(v.getName(), 5) + "------"
										+ HCI.centerStr("" + a.getIValue(), 7) + "------"
										+ HCI.centerStr(a.getVertex().getName(), 5);
								cpt++;
							}

						}
					}
				} else {
					if (graphLoaded.isDirected()) {
						// Non valu� et orient�
						for (Arc a : v.getAlArcs()) {
							tabArc[cpt] = HCI.centerStr(v.getName(), 5) + "-------->"
									+ HCI.centerStr(a.getVertex().getName(), 5);
							cpt++;
						}
					} else {
						// Non valu� et non orient�
						for (Arc a : v.getAlArcs()) {
							bFound = false;
							for (String s : tabArc) {
								if (s != null && s.equals(HCI.centerStr(a.getVertex().getName(), 5) + "---------"
										+ HCI.centerStr(v.getName(), 5))) {
									bFound = true;
								}
							}
							if (!bFound) {
								tabArc[cpt] = HCI.centerStr(v.getName(), 5) + "---------"
										+ HCI.centerStr(a.getVertex().getName(), 5);
								cpt++;
							}
						}
					}
				}
			}
			listOfObject.setListData(tabArc);
			// Resultat voulu
			// S1 ----- S2
			// S1 --4-- S2
			// S1 --4-> S2
			// S1 ----> S2

		}
	}

	/**
	 * M�thode permettant de changer l'�tat de la liste entre l'�tat "Sommet" et
	 * l'�tat "Arc"
	 */
	// Set state of the SwitchList
	public void switchState() {
		if (state == 0) {
			lTitle.setText("Liste des Arcs");
			state = 1;
		} else if (state == 1) {
			lTitle.setText("Liste des Sommets");
			state = 0;
		}
		refresh();
	}

	/**
	 * M�thode permettant d'afficher les param�tres du graphe (Orient� / valu�)
	 * 
	 * @param strInfo
	 *            La chaine � afficher
	 */
	public void setInfo(String strInfo) {
		lInfo.setText(strInfo);
	}

}
