package controller;
import model.Graph;
import view.HCI;

public class Controller {
	@SuppressWarnings("unused")
	private static HCI hci;
	private static Graph g;
	public static void main(String[] args) {
		new Controller();
	}	
	public Controller() {
		//Create graph
		g = new Graph(true, true);
		
		//Graph non valu� 
		/*g.addVertex("S1");
		g.addVertex("S2");
		g.addVertex("S3");
		g.addArc(g.getAlVertex().get(0), g.getAlVertex().get(0));
		g.addVertex("S4");
		for(int i=0; i<20 ;i++) {
			g.addVertex("A"+i);
		}
		for(int i=1; i<24 ;i++) {
			g.addArc(g.getAlVertex().get(i-1), g.getAlVertex().get(i));
		}*/
		
		//Graph valu� 
		g.addVertex("S1000");
		g.addVertex("S2");
		g.addArc(g.getAlVertex().get(0), g.getAlVertex().get(0),5);
		g.addVertex("S3");
		g.addVertex("S4");
		for(int i=0; i<20 ;i++) {
			g.addVertex("A"+i);
		}
		for(int i=1; i<24 ;i++) {
			g.addArc(g.getAlVertex().get(i-1), g.getAlVertex().get(i),i);
		}
		//Initialize the frame
		hci = new HCI(this);
	}
	
	public void addVertex(String nom){
		g.addVertex(nom);
		hci.addVertex(nom);
	}
	
	public static Graph getGraph() {return g;}
}
