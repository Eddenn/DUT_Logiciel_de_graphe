package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import model.Arc;
import model.Vertex;

@SuppressWarnings("serial")
public class GraphPanel extends JPanel implements MouseListener,MouseMotionListener,KeyListener {

	private ArrayList<String> alSelected;	//Selected Edge or Arc
	private Point saveMousePosition;
	private boolean bCtrlPressed;
	private double iWidthEdge;	//Largeur
	private double iHeightEdge; //Hauteur
	private double iZoom;		//Zoom
	private HCI hci;

	public GraphPanel(HCI hci) {
		super();
		this.hci = hci;
		this.saveMousePosition= new Point(0,0);
		this.bCtrlPressed = false;
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.iZoom = 1.0;
		this.iHeightEdge = 50*iZoom;
		this.iWidthEdge = 50*iZoom;
		this.alSelected = new ArrayList<String>();
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
			for(String s : alSelected) {
				if(v.getName().equals(s)) {
					highlightEdge(g2d, v);
				}
			}
		}
	}
	
	public ArrayList<String> getAlSelected() 	   {return this.alSelected;}
	public void setAlSelected(ArrayList<String> s) {this.alSelected = s;}
	
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
		boolean bMirroir=false;
		
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
					//Si A -> B et B -> A
					bMirroir = false;
					for(Arc arcTmp : arc.getVertex().getAlArcs()) {
						if(arcTmp.getVertex()==v) {
							bMirroir = true;
						}
					}
					if(bMirroir && hci.getGraph().isDirected()) {
						/*-----Cacul des points M et N pour placer les deux arcs par rapport à pCenter1*/
						int d = 0;
						int h = (int) (iWidthEdge/3*iZoom);
						int dx = (int)pCenter2.x - (int)pCenter1.x, dy = (int)pCenter2.y - (int)pCenter1.y;
				        double D = Math.sqrt(dx*dx + dy*dy);
				        double xm1 = D - d, xn1 = xm1, ym1 = h, yn1 = -h, x;
				        double sin = dy/D, cos = dx/D;
				        x = xm1*cos - ym1*sin + (int)pCenter1.x;
				        ym1 = xm1*sin + ym1*cos + (int)pCenter1.y;
				        xm1 = x;
				        x = xn1*cos - yn1*sin + (int)pCenter1.x;
				        yn1 = xn1*sin + yn1*cos + (int)pCenter1.y;
				        xn1 = x;
				        /*-----------------------------------------------------------------------------*/
						/*-----Cacul des points M et N pour placer les deux arcs par rapport à pCenter2*/
				        d = 0;
						h = (int) (iWidthEdge/3*iZoom);
						dx = (int)pCenter1.x - (int)pCenter2.x;
						dy = (int)pCenter1.y - (int)pCenter2.y;
				        D = Math.sqrt(dx*dx + dy*dy);
				        double xm2 = D - d;
				        double xn2 = xm2;
				        double ym2 = h;
				        double yn2 = -h;
				        x=0.0;
				        sin = dy/D;
				        cos = dx/D;
				        x = xm2*cos - ym2*sin + (int)pCenter2.x;
				        ym2 = xm2*sin + ym2*cos + (int)pCenter2.y;
				        xm2 = x;
				        x = xn2*cos - yn2*sin + (int)pCenter2.x;
				        yn2 = xn2*sin + yn2*cos + (int)pCenter2.y;
				        xn2 = x;
				        /*-----------------------------------------------------------------------------*/
				        
				        g2d.drawLine((int)xm1, (int)ym1, (int)xn2, (int)yn2);	
						drawArrow(g2d, (int)xm1, (int)ym1, (int)xn2, (int)yn2, (int)(22*iZoom), (int)(10*iZoom));
						if(  hci.getGraph().isValued() ) {
							g2d.setColor(Color.BLACK);
							g2d.drawString( ""+arc.getIValue() , (int)(xm1+xn2)/2 , (int)(ym1+yn2)/2 );
						}				        
					} else {
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
		/* Clic droit */
		int mod = e.getModifiers();

		if ((mod & InputEvent.BUTTON3_MASK) != 0)
			hci.getPopMenu().show(this, e.getX(), e.getY());
		/*------------*/
		
		/* Double clic */
		if(e.getClickCount() > 0) {
			double centerX, centerY;
			boolean bFind=false;
			for (Point c : HCI.hmVertex.values()) {
				centerX = (c.getX()+iWidthEdge/2)*iZoom;
				centerY = (c.getY()+iHeightEdge/2)*iZoom;
				
				//Si la souris est sur un sommet
				if (Math.pow(e.getX() - centerX, 2) + Math.pow(e.getY() - centerY, 2) <= (Math.pow(iWidthEdge/2*iZoom, 2))) {
					// Find the key of this coordinate
					for(String s : HCI.hmVertex.keySet()) {
						if (HCI.hmVertex.get(s) == c) {
							if(!bCtrlPressed) alSelected.clear();
							if(!alSelected.contains(s))	alSelected.add(s);
							bFind = true;
						}
					}
				}
			}
			if(!bFind) {alSelected.clear();}
		}
		/*-----------*/
		repaint();
	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		saveMousePosition = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mouseDragged(MouseEvent e) {	
		if(alSelected.size() != 0) {
			double centerX, centerY;
			//for (Point c : HCI.hmVertex.values()) {
				// Find the key of this coordinate
				for(String s : HCI.hmVertex.keySet()) {
					if (alSelected.contains(s)) {
						Point c = HCI.hmVertex.get(s);
						centerX = (c.getX()+iWidthEdge/2)*iZoom;
						centerY = (c.getY()+iHeightEdge/2)*iZoom;
						if (Math.pow(e.getX() - centerX, 2) + Math.pow(e.getY() - centerY, 2) <= (Math.pow(iWidthEdge/2*iZoom, 2))) {
							/*-----Déplacements des sommets sélectonnés----*/
							setCursor(new Cursor(Cursor.MOVE_CURSOR));
							//Coordonnées minimales des points sélectionnés
							Point minPosition = new Point(c.x,c.y);
							for(String edgeSelected : alSelected) {
								if( HCI.hmVertex.get(edgeSelected).x < minPosition.x) {minPosition.x = HCI.hmVertex.get(edgeSelected).x;}
								if( HCI.hmVertex.get(edgeSelected).y < minPosition.y) {minPosition.y = HCI.hmVertex.get(edgeSelected).y;}
							}
							//Coordonnées maximales des points sélectionnés
							Point maxPosition = new Point((int) (c.x+iWidthEdge*iZoom),(int) (c.y+iHeightEdge*iZoom));
							for(String edgeSelected : alSelected) {
								if( HCI.hmVertex.get(edgeSelected).x+iWidthEdge*iZoom > maxPosition.x) {maxPosition.x = (int) (HCI.hmVertex.get(edgeSelected).x+iWidthEdge*iZoom);}
								if( HCI.hmVertex.get(edgeSelected).y+iHeightEdge*iZoom > maxPosition.y) {maxPosition.y = (int) (HCI.hmVertex.get(edgeSelected).y+iHeightEdge*iZoom);}
							}
							//Calcul de la différence entre la dernière position de la souris et l'actuelle
							Point deplacement = new Point(e.getPoint().x-saveMousePosition.x,e.getPoint().y-saveMousePosition.y);
							
							System.out.println(minPosition +"\n"+ maxPosition +"\n"+ deplacement);
							System.out.println(this.getWidth()+":"+this.getHeight());
							/*--Déplacement--*/
							if( (minPosition.x==0 && deplacement.x>=0 || minPosition.x>0) && 
								(minPosition.y==0 && deplacement.y>=0 || minPosition.y>0) &&
								(maxPosition.x==this.getWidth()  && deplacement.x<=0 || maxPosition.x<this.getWidth() ) && 
								(maxPosition.y==this.getHeight() && deplacement.y<=0 || maxPosition.y<this.getHeight()) && 
								(minPosition.x+deplacement.x>=0) && minPosition.y+deplacement.y>=0 && maxPosition.x+deplacement.x<=this.getWidth() && maxPosition.y+deplacement.y<=this.getHeight() ) {
								//Sommet par sommet
								for(String edgeSelected : alSelected) {
//									if( (HCI.hmVertex.get(edgeSelected).x+deplacement.x)>=0 && (HCI.hmVertex.get(edgeSelected).x+iWidthEdge*iZoom+deplacement.x)<=this.getWidth() ) {
//									if( (HCI.hmVertex.get(edgeSelected).y+deplacement.y)>=0 && (HCI.hmVertex.get(edgeSelected).y+iHeightEdge*iZoom+deplacement.y)<=this.getHeight() ) {

									HCI.hmVertex.get(edgeSelected).x += deplacement.x;
									HCI.hmVertex.get(edgeSelected).y += deplacement.y;
									//Text qui affiche les coordonnées
									hci.getLabelCoord().setText("  X : " + (double) (HCI.hmVertex.get(edgeSelected).x +25) + "       Y : " + (double)(HCI.hmVertex.get(edgeSelected).y + 25));
									repaint();
								}
							}
							/*---------------------------------------------*/
						}
					}
				}
			//}			
		}
		saveMousePosition = e.getPoint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		hci.requestFocus();
		// Permet d'afficher les coordonnées du point dès que la souris passe dessus
		double centerX, centerY;
		for (Point c : HCI.hmVertex.values()) {
			centerX = (c.getX()+iWidthEdge/2)*iZoom;
			centerY = (c.getY()+iHeightEdge/2)*iZoom;
			if (Math.pow(e.getX() - centerX, 2) + Math.pow(e.getY() - centerY, 2) <= (Math.pow(iWidthEdge/2*iZoom, 2))) {
				hci.getLabelCoord().setText("  X : " + centerX + "       Y : " + centerY);
				break;
			}
			else {
				if (! hci.getLabelCoord().getText().equals(" "))
					hci.getLabelCoord().setText(" ");
			}
		}	
	}
	
	public void refreshPreferedSize() {
		int xMax = 0;
		int yMax = 0;
		for(Point c : HCI.hmVertex.values()) {
			if(c.getX() > xMax) xMax = c.x;
			if(c.getY() > yMax) yMax = c.y;
		}
		setPreferredSize(new Dimension((int)((xMax+getiWidthEdge())*iZoom),(int)((yMax+getiHeightEdge())*iZoom)));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==17) bCtrlPressed = true;
		
		if(e.getModifiersEx()==128 && e.getKeyCode()==67 ) {
			System.out.println("CTRL+C");
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()==17) bCtrlPressed = false;
	}
		
	@Override
	public void keyTyped(KeyEvent e) {}
}
