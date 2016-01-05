package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import model.Arc;
import model.Vertex;

@SuppressWarnings("serial")
public class GraphPanel extends JPanel implements MouseListener,MouseMotionListener {

	private String strSelected;	//Selected Edge
	private String strEdgeMove; //For moving Edge
	private double iWidthEdge;	//Largeur
	private double iHeightEdge; //Hauteur
	private double iZoom;		//Zoom
	private HCI hci;

	public GraphPanel(HCI hci) {
		super();
		this.hci = hci;
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.iZoom = 1.0;
		this.iHeightEdge = 50*iZoom;
		this.iWidthEdge = 50*iZoom;
		this.strSelected = null;
		this.strEdgeMove = null;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	//Getters and Setters
	public double getiWidthEdge() {return iWidthEdge;}
	public double getiHeightEdge() {return iHeightEdge;}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		drawArcs(g2d);
		drawEdges(g2d);
		for(Vertex v : hci.getGraph().getAlVertex()) {
			if(v.getName().equals(strSelected)) {
				highlightEdge(g2d, v);
			}
		}
	}
	
	public String getStrSelected() 		 {return this.strSelected;}
	public void setStrSelected(String s) {this.strSelected = s;}
	
	public Point findPoint(Vertex v) {
		return HCI.hmVertex.get(v.getName());
	}
	
	public void highlightEdge(Graphics2D g2d, Vertex v) {
		g2d.setColor(new Color(20,20,255,50));
		g2d.fillOval((int)((findPoint(v).x-5)*iZoom) , (int)((findPoint(v).y-5)*iZoom) , (int)((iWidthEdge+10)*iZoom)  , (int)((iHeightEdge+10)*iZoom  ));
		g2d.setColor(Color.BLACK);
	}
	
	public void highlightArc(Graphics2D g2d, Vertex v, Arc arc) {
		Point c1 = HCI.hmVertex.get(v.getName());
		Point c2 = HCI.hmVertex.get(arc.getVertex().getName());
		
		//Coordonnées centrale des deux points en fonction du zoom
		Point pCenter1 = new Point( (int)((c1.getX()+iWidthEdge/2)*iZoom) , (int)((c1.getY()+iHeightEdge/2)*iZoom) );
		Point pCenter2 = new Point( (int)((c2.getX()+iWidthEdge/2)*iZoom) , (int)((c2.getY()+iHeightEdge/2)*iZoom) );		
		
		g2d.setColor(new Color(20,20,255,50));
		g2d.setStroke(new BasicStroke((float)iZoom+2));
		if(arc.getVertex() == v) {	//Arc sur lui même
			g2d.drawArc((int)((c2.getX()+12.5)*iZoom), (int)((c2.getY()+40)*iZoom), (int)(25*iZoom), (int)(25*iZoom), 150, 240);
	    	if(  hci.getGraph().isDirected() ) {	
				drawArrow(g2d, pCenter1.x-35, pCenter1.y+80, pCenter2.x, pCenter2.y, (int)(25*iZoom), (int)(10*iZoom));
			}
		} else {	//Arc entre deux points
			g2d.drawLine((int)pCenter1.x, (int)pCenter1.y, (int)pCenter2.x, (int)pCenter2.y);
	    	if(  hci.getGraph().isDirected() ) {	
				drawArrow(g2d, (int)pCenter1.x, (int)pCenter1.y, (int)pCenter2.x, (int)pCenter2.y, (int)(25*iZoom), (int)(10*iZoom));
			}
		}
		g2d.setColor(Color.BLACK);
	}
	
	public void drawEdges(Graphics2D g2d) {
		for(Point c : HCI.hmVertex.values()) {
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke((float)iZoom));
			g2d.drawOval((int)(c.getX()*iZoom-1), (int)(c.getY()*iZoom-1), (int)((iWidthEdge+2)*iZoom), (int)((iHeightEdge+2)*iZoom));
			g2d.setColor(Color.WHITE);
			g2d.fillOval((int)(c.getX()*iZoom)  , (int)(c.getY()*iZoom)  , (int)((iWidthEdge)*iZoom)  , (int)((iHeightEdge)*iZoom  ));
			// Find the key of this coordinate
			for (String s : HCI.hmVertex.keySet()) {
				if (HCI.hmVertex.get(s) == c) {
					g2d.setColor(Color.BLACK);
					g2d.drawString(HCI.centerStr(s,(int)((iWidthEdge*iZoom)/(3.5*iZoom))), (int)((c.getX()*iZoom)), (int)((c.getY()+iHeightEdge/2+(iHeightEdge/10))*iZoom));
				}
			}
		}
	}
	 /**
	   * Dessine une fleche vers le point 2 
	   * @param g the graphic component
	   * @param x1 x-position of first point
	   * @param y1 y-position of first point
	   * @param x2 x-position of second point
	   * @param y2 y-position of second point
	   * @param d  the width of the arrow
	   * @param h  the height of the arrow
	   */
    private void drawArrow(Graphics g, int x1, int y1, int x2, int y2, int d, int h){    	
    	//Distance entre les points
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx*dx + dy*dy);
        if(D>iWidthEdge) {
	        //---Calcul des positions des points M(xm;ym) et N(xn;yn)---
	        double xm = D - d, xn = xm, ym = h, yn = -h, x;
	        double sin = dy/D, cos = dx/D;
	        x = xm*cos - ym*sin + x1;
	        ym = xm*sin + ym*cos + y1;
	        xm = x;
	        x = xn*cos - yn*sin + x1;
	        yn = xn*sin + yn*cos + y1;
	        xn = x;
	        //----------------------------------------------------------
	
	        //Point central de MN
	        double x3 = (xn + xm)/ 2;
	        double y3 = (yn + ym)/ 2;
	        
	        //---Calcul des point MN en fonction du point calcule ci-dessus---
	        x2 = (int) x3;
	        y2 = (int) y3;
	        d = (int) (8*iZoom);
	        h = (int) (4*iZoom);
	        
	        dx = x2 - x1; dy = y2 - y1;
	        D = Math.sqrt(dx*dx + dy*dy);
	        xm = D - d; xn = xm; ym = h; yn = -h;
	        sin = dy/D; cos = dx/D;
	        
	        x = xm*cos - ym*sin + x1;
	        ym = xm*sin + ym*cos + y1;
	        xm = x;
	        
	        x = xn*cos - yn*sin + x1;
	        yn = xn*sin + yn*cos + y1;
	        xn = x;
	        //----------------------------------------------------------------
	        
	        //Dessine la fleche au bout du segment
	        g.drawLine((int)x3, (int)y3, (int)(xm), (int)(ym));
	        g.drawLine((int)x3, (int)y3, (int)(xn), (int)(yn));
        }
    }
	
	public void drawArcs(Graphics2D g2d) {	
		Point pCenter1;
		Point pCenter2;
		
		for(Vertex v : hci.getGraph().getAlVertex()) {
			//Recup les coordonnées associées à ce Vertex
			Point c1 = HCI.hmVertex.get(v.getName());
			
			for(Arc arc : v.getAlArcs()) {					
				Point c2 = HCI.hmVertex.get(arc.getVertex().getName());
				
				//Coordonnées centrale des deux points en fonction du zoom
				pCenter1 = new Point( (int)((c1.getX()+iWidthEdge/2)*iZoom) , (int)((c1.getY()+iHeightEdge/2)*iZoom) );
				pCenter2 = new Point( (int)((c2.getX()+iWidthEdge/2)*iZoom) , (int)((c2.getY()+iHeightEdge/2)*iZoom) );
				
		        //Dessine le segment entre les points
				g2d.setColor(Color.GRAY);
				g2d.setFont(g2d.getFont().deriveFont( Font.PLAIN, (float)( 11*iZoom)));
				
				if( arc.getVertex() == v ) {	//Arc sur lui même
					g2d.drawArc((int)((c2.getX()+12.5)*iZoom), (int)((c2.getY()+40)*iZoom), (int)(25*iZoom), (int)(25*iZoom), 150, 240);
			    	if( hci.getGraph().isDirected() ) {	
						g2d.setColor(Color.GRAY);
						drawArrow(g2d, pCenter1.x-35, pCenter1.y+80, pCenter2.x, pCenter2.y, (int)(25*iZoom), (int)(10*iZoom));
					}
					if(  hci.getGraph().isValued() ) {
						g2d.setColor(Color.BLACK);
						g2d.drawString( ""+arc.getIValue() , (int)(pCenter1.x-(2)*iZoom) , (int)(pCenter1.y+(40)*iZoom) );
					}
				} else {	//Arc entre deux points
					g2d.drawLine((int)pCenter1.x, (int)pCenter1.y, (int)pCenter2.x, (int)pCenter2.y);
			    	if(  hci.getGraph().isDirected() ) {	
						g2d.setColor(Color.GRAY);
						drawArrow(g2d, pCenter1.x, pCenter1.y, pCenter2.x, pCenter2.y, (int)(25*iZoom), (int)(10*iZoom));
					}
					if(  hci.getGraph().isValued() ) {
						g2d.setColor(Color.BLACK);
						g2d.drawString( ""+arc.getIValue() , (pCenter1.x+pCenter2.x)/2 , (pCenter1.y+pCenter2.y)/2 );
					}
				}
				g2d.setColor(Color.BLACK);
			}
		}
	}
	
	//Zoom in
	public double zoomIn() {
		if(iZoom<2)iZoom = iZoom+0.1;
		refreshPreferedSize();
		return iZoom;
	}
	//Zoom out
	public double zoomOut() {
		if(iZoom>0.5) iZoom = iZoom-0.1;
		refreshPreferedSize();
		return iZoom;
	}
	public double getZoom() { return iZoom; }
	
	
	@Override
	public void mouseClicked(MouseEvent e) {		
		if(e.getClickCount() > 1) {
			double centerX, centerY;
			for (Point c : HCI.hmVertex.values()) {
				centerX = (c.getX()+iWidthEdge/2)*iZoom;
				centerY = (c.getY()+iHeightEdge/2)*iZoom;
				
				//Si la souris est sur un sommet
				if (Math.pow(e.getX() - centerX, 2) + Math.pow(e.getY() - centerY, 2) <= (Math.pow(iWidthEdge/2*iZoom, 2))) {
					// Find the key of this coordinate
					for(String s : HCI.hmVertex.keySet()) {
						if (HCI.hmVertex.get(s) == c)
							strSelected = s;
					}
				}
			}
//			for(Vertex v1 : hci.getGraph().getAlVertex()) {
//				for(Arc arc : v1.getAlArcs()) {
//					Vertex v2 = arc.getVertex();
//					Point p1 = null,p2 = null;
//					for(String s : HCI.hmVertex.keySet()) {
//						if(v1.getName().equals(s))	p1 = HCI.hmVertex.get(s);
//						if(v2.getName().equals(s))	p2 = HCI.hmVertex.get(s);
//					}
//							
//					int aX = (int) ((p1.x+iWidthEdge/2)*iZoom);
//					int aY = (int) ((p1.y+iHeightEdge/2)*iZoom);
//					int bX = (int) ((p2.x+iWidthEdge/2)*iZoom);
//					int bY = (int) ((p2.y+iHeightEdge/2)*iZoom);
//					
//					double b = -(aX*bY-aX*bX)/(bX-aX) ;
//							
//					double a = (aX-b)/(aX);
//					
//					// Y=aX+b  -b=aX-Y   b=-aX+Y
//					// -aX=(b-Y)   X=-(b-Y)/a
//					
//					System.out.println(aX+":"+aY+" / "+bX+":"+bY);
//					System.out.println("Y="+a+"X+"+b);
//					System.out.println("Y="+(int)(a*e.getX()+b)+"X="+(int)(-(b-e.getY())/a));
//					System.out.println("X="+e.getX()+"Y="+e.getY());
//					
//					//Si la souris est sur un arc
//					if( ( (int)(a*e.getX()+b+5) <= e.getY() && e.getY() >= (int)(a*e.getX()+b-5) )&&( (int)(-(b-e.getY())/a+5) <= e.getX() && e.getX() >= (int)(-(b-e.getY())/a-5) ) ) {
//						System.out.println("test");
//					}
//				}
//			}
		}
		repaint();
	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		// (X-Xa)2+(Y-Ya)2 <= R2
		// Pb : Décalage => Résolu
		// x:100 et y:100 sont les positions en haut a gauche du cercle (le
		// point de depart)
		// Et width:50 et height:50 sont les diamètres* du cercle (*hauteur et
		// largeur)

		// Find the name of the selected vertex
		strEdgeMove = null;
		double centerX, centerY;
		for (Point c : HCI.hmVertex.values()) {
			centerX = (c.getX()+iWidthEdge/2)*iZoom;
			centerY = (c.getY()+iHeightEdge/2)*iZoom;
			if (Math.pow(e.getX() - centerX, 2) + Math.pow(e.getY() - centerY, 2) <= (Math.pow(iWidthEdge/2*iZoom, 2))) {
				// Find the key of this coordinate
				for(String s : HCI.hmVertex.keySet()) {
					if (HCI.hmVertex.get(s) == c)
						strEdgeMove = s;
				}
			}
		}	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		strEdgeMove = null;
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(strEdgeMove != null) {
			if( e.getX()/(iZoom)-(iWidthEdge/2)>0  && e.getX()+(iWidthEdge/2*iZoom) <this.getWidth()) {
				HCI.hmVertex.get(strEdgeMove).x =((int)(e.getX()/(iZoom)-(iWidthEdge/2)));
			}
			if( e.getY()/(iZoom)-(iHeightEdge/2)>0 && e.getY()+(iHeightEdge/2*iZoom)<this.getHeight()) {
				HCI.hmVertex.get(strEdgeMove).y =((int)(e.getY()/(iZoom)-(iHeightEdge/2)));
			}
			repaint();
			setCursor(new Cursor(Cursor.MOVE_CURSOR));
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {}
	
	public void refreshPreferedSize() {
		int xMax = 0;
		int yMax = 0;
		for(Point c : HCI.hmVertex.values()) {
			if(c.getX() > xMax) xMax = c.x;
			if(c.getY() > yMax) yMax = c.y;
		}
		setPreferredSize(new Dimension((int)((xMax+getiWidthEdge())*iZoom),(int)((yMax+getiHeightEdge())*iZoom)));
	}
}
