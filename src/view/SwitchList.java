package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.*;

public class SwitchList extends JPanel{
	/** Serial Version
	  */
	private static final long serialVersionUID = -967795867440790085L;
	@SuppressWarnings("rawtypes")
	private JList listOfObject;
	private JButton jbSwitch;
	private JLabel lTitle;
	private int state = 1;
	private HCI hci;
	
	@SuppressWarnings("rawtypes")
	public SwitchList(HCI hci) {
		this.hci = hci;
		setLayout(new BorderLayout());
		
		/*--Header of the JList--*/
		JPanel jpTop = new JPanel();
	    jpTop.setBackground(Color.WHITE);
		//Button used to switch 
		ImageIcon iSwitch = new ImageIcon( getClass().getResource( "/Farm-Fresh_arrow_refresh_16.png"));
		jbSwitch = new JButton(iSwitch);
		jbSwitch.setContentAreaFilled(false);
		jbSwitch.setToolTipText("Changer de liste");
		jbSwitch.addActionListener(hci);
		jpTop.add(jbSwitch);
		lTitle = new JLabel("Liste des Sommets");
	    jpTop.add(lTitle);
	    add(jpTop,BorderLayout.NORTH);
	    /*-----------------------*/
	    
	    //JList
	    listOfObject = new JList();
	    DefaultListCellRenderer renderer =  (DefaultListCellRenderer)listOfObject.getCellRenderer();  //Center string in the list
	    renderer.setHorizontalAlignment(JLabel.CENTER); 	
	    // -----------------------
	    listOfObject.addMouseListener(new ListMouseListener(hci));
	    listOfObject.addListSelectionListener(hci);
	    JScrollPane jscrPanel = new JScrollPane(listOfObject);
	    add(jscrPanel,BorderLayout.CENTER);
	    
	    this.setPreferredSize(new Dimension(200,500));
	}
	//Getters and Setters
	@SuppressWarnings("unchecked")
	public JList<String> getListOfObject() {return this.listOfObject;}
	public JButton getJBSwitch()   {return this.jbSwitch;}
	
	@SuppressWarnings("unchecked")
	public void refresh() {
		Graph graphLoaded = hci.getGraph();
		if(state == 0) {	//State of the SwitchList (0 = Sommets)
			String[] tabVertex = new String[graphLoaded.getAlVertex().size()];
			int cpt = 0;
			for(Vertex v : graphLoaded.getAlVertex()) {
				tabVertex[cpt] = v.getName();
				cpt++;
			}
			listOfObject.setListData(tabVertex);
		} else if(state == 1) { 	//State of the SwitchList (1 = Arcs)
			int nbArc = 0;
			for(Vertex v : graphLoaded.getAlVertex()) {
				for(@SuppressWarnings("unused") Arc a : v.getAlArcs()) {
					nbArc++;
				}
			}
			String[] tabArc = new String[nbArc];
			int cpt = 0;
			boolean bFound = false;
			for(Vertex v : graphLoaded.getAlVertex()) {
				if(graphLoaded.isValued()) {	
					if (graphLoaded.isDirected()) {
						//Valué et orienté
						for(Arc a : v.getAlArcs()) {
							tabArc[cpt] = HCI.centerStr(v.getName(),5)+"------"+HCI.centerStr(""+a.getIValue(),7)+"----->"+HCI.centerStr(a.getVertex().getName(),5);
							cpt++;
						}
					} else {
						//Valué et non orienté
						for(Arc a : v.getAlArcs()) {
							bFound = false;
							for(String s : tabArc) {
								if( s!=null && (s.equals(HCI.centerStr(a.getVertex().getName(),5)+"------"+HCI.centerStr(""+a.getIValue(),7)+"------"+HCI.centerStr(v.getName(),5)))) {
									bFound = true;
								}
							}
							if(!bFound) {
								tabArc[cpt] = HCI.centerStr(v.getName(),5)+"------"+HCI.centerStr(""+a.getIValue(),7)+"------"+HCI.centerStr(a.getVertex().getName(),5);
								cpt++;
							}
							
						}
					}
				} else {
					if (graphLoaded.isDirected()) {
						//Non valué et orienté
						for(Arc a : v.getAlArcs()) {
							tabArc[cpt] = HCI.centerStr(v.getName(),5)+"-------->"+HCI.centerStr(a.getVertex().getName(),5);
							cpt++;
						}
					} else {
						//Non valué et non orienté
						for(Arc a : v.getAlArcs()) {
							bFound = false;
							for(String s : tabArc) {
								if( s!=null && s.equals(HCI.centerStr(a.getVertex().getName(),5)+"---------"+HCI.centerStr(v.getName(),5))) {
									bFound = true;
								}
							}
							if(!bFound) {
								tabArc[cpt] = HCI.centerStr(v.getName(),5)+"---------"+HCI.centerStr(a.getVertex().getName(),5);
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
	
	//Set state of the SwitchList
	public void switchState() {
		if(state == 0) {
			lTitle.setText("Liste des Arcs");
			state=1;
		} else if(state == 1) {
			lTitle.setText("Liste des Sommets");
			state=0;
		}
		refresh();
	}
}
