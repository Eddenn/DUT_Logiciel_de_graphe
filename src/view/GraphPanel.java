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
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import model.Arc;
import model.Vertex;

@SuppressWarnings("serial")
public class GraphPanel extends JPanel implements MouseListener,MouseMotionListener,KeyListener {

	final static float dash1[] = {5.0f};
	final static BasicStroke pointille = new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f, dash1, 0.0f);
	
	private ArrayList<String> alSelected;		//Sélection
	private HashMap<String,Point> clipBoardEdge;//Presse-Papier
	private Point saveMousePosition;			//Sauvegarde de la dernière position de la souris (Voir MouseDragged)
	private Point rectSelectionStartPoint;
	private Rectangle2D rectSelection;
	private boolean bDragged;
	private boolean bCtrlPressed;
	private double iWidthEdge;		//Largeur
	private double iHeightEdge; 	//Hauteur
	private double iZoom;			//Zoom
	private HCI hci;
	private GraphStyle style;

	/*--Constructeur du panel de dessin--*/
	public GraphPanel(HCI hci) {
		super();
		this.hci = hci;
		rectSelection = new Rectangle2D.Double();
		this.rectSelection.setRect(-1, -1, 0, 0);
		this.style = GraphStyle.Basique;
		this.clipBoardEdge = new HashMap<String,Point>();
		this.saveMousePosition= new Point(0,0);
		this.bCtrlPressed = false;
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.iZoom = 1.0;
		this.iHeightEdge = 50*iZoom;
		this.iWidthEdge = 50*iZoom;
		this.alSelected = new ArrayList<String>();
		this.bDragged = false;
		this.setBackground(style.getBackground());
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	/*--Méthode principale de dessin--*/
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		drawArcs(g2d);
		drawVertex(g2d);
		for(Vertex v : hci.getGraph().getAlVertex()) {
			for(String s : alSelected) {
				if(v.getName().equals(s)) {
					highlightEdge(g2d, v);
				}
			}
		}
		g2d.setStroke(pointille);
		g2d.draw(rectSelection);
		g2d.setStroke(new BasicStroke((float)iZoom+2));
	}
	
	/*--Getters and Setters--*/
	public double getiWidthEdge() 					{return iWidthEdge;}
	public double getiHeightEdge() 					{return iHeightEdge;}
	public ArrayList<String> getAlSelected() 	   	{return this.alSelected;}
	public void setAlSelected(ArrayList<String> s) 	{this.alSelected = s;}
	public GraphStyle getStyle() {return style;}
	public void setStyle(GraphStyle style) {this.style = style;}
	public Vertex getVertex(Point p) {
		for(String s : hci.getHmVertex().keySet()) {
			if(hci.getHmVertex().get(s)==p) {
				for(Vertex v : hci.getGraph().getAlVertex()) {
					if(v.getName().equals(s)) {
						return v;
					}
				}
			}
		}
		return null;
	}
	
	/*--Surlignage--*/
	public void highlightEdge(Graphics2D g2d, Vertex v) {
		g2d.setColor(new Color(20,20,255,50));
		g2d.fillOval((int)(hci.getHmVertex().get(v.getName()).x-5*iZoom) , (int)(hci.getHmVertex().get(v.getName()).y-5*iZoom) , (int)((iWidthEdge+10)*iZoom)  , (int)((iHeightEdge+10)*iZoom  ));
		g2d.setColor(Color.BLACK);
	}
	public void highlightArc(Graphics2D g2d, Vertex v, Arc arc) {
		Point c1 = hci.getHmVertex().get(v.getName());
		Point c2 = hci.getHmVertex().get(arc.getVertex().getName());
		
		//Coordonnï¿½es centrale des deux points en fonction du zoom
		Point pCenter1 = new Point( (int)(c1.getX()+iWidthEdge/2*iZoom) , (int)(c1.getY()+iHeightEdge/2*iZoom) );
		Point pCenter2 = new Point( (int)(c2.getX()+iWidthEdge/2*iZoom) , (int)(c2.getY()+iHeightEdge/2*iZoom) );		
		
		g2d.setColor(new Color(20,20,255,50));
		g2d.setStroke(new BasicStroke((float)iZoom+2));
		if(arc.getVertex() == v) {	//Arc sur lui mï¿½me
			g2d.drawArc((int)(c2.getX()+12.5*iZoom), (int)(c2.getY()+40*iZoom), (int)(25*iZoom), (int)(25*iZoom), 150, 240);
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
	
	/*--Dessin en fonction de hmVertex--*/
	public void drawVertex(Graphics2D g2d) {
		for(Point c : hci.getHmVertex().values()) {
			g2d.setColor(style.getEdgeBorder());
			g2d.setStroke(new BasicStroke((float)iZoom));
			g2d.drawOval((int)(c.getX()-1), (int)(c.getY()-1), (int)((iWidthEdge+2)*iZoom), (int)((iHeightEdge+2)*iZoom));
			
			if( getVertex(c).getColor()==null ) { 
				g2d.setColor(style.getEdgeBackground());
			} else {
				g2d.setColor(getVertex(c).getColor());
			}
			
			g2d.fillOval((int)(c.getX())  , (int)(c.getY())  , (int)((iWidthEdge)*iZoom)  , (int)((iHeightEdge)*iZoom  ));
			// Find the key of this coordinate
			for (String s : hci.getHmVertex().keySet()) {
				if (hci.getHmVertex().get(s) == c) {
					g2d.setColor(style.getEdgeText());
					g2d.drawString(HCI.centerStr(s,(int)((iWidthEdge*iZoom)/(3.5*iZoom))), (int)(c.getX()), (int)(c.getY()+(iHeightEdge/2+(iHeightEdge/10))*iZoom));
				}
			}
		}
	}
	 /**
	   * Dessine une fleche du point 1 vers le point 2 
	   * @param g Graphic component
	   * @param x1 x-position du point 1
	   * @param y1 y-position du point 1
	   * @param x2 x-position du point 2
	   * @param y2 y-position du point 2
	   * @param d  largeur de la flï¿½che
	   * @param h  hauteur de la flï¿½che
	   */
    public void drawArrow(Graphics g, int x1, int y1, int x2, int y2, int d, int h){    	
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
			//Recup les coordonnÃ©es associÃ©es Ã  ce Vertex
			Point c1 = hci.getHmVertex().get(v.getName());
			
			for(Arc arc : v.getAlArcs()) {					
				Point c2 = hci.getHmVertex().get(arc.getVertex().getName());
				
				//Coordonnï¿½es centrale des deux points en fonction du zoom
				pCenter1 = new Point( (int)(c1.getX()+(iWidthEdge/2)*iZoom) , (int)(c1.getY()+(iHeightEdge/2)*iZoom) );
				pCenter2 = new Point( (int)(c2.getX()+(iWidthEdge/2)*iZoom) , (int)(c2.getY()+(iHeightEdge/2)*iZoom) );
				
		        //Dessine le segment entre les points
				g2d.setColor(style.getArcLine());
				g2d.setFont(g2d.getFont().deriveFont( Font.PLAIN, (float)( 11*iZoom)));
				
				if( arc.getVertex() == v ) {	//Arc sur lui mï¿½me
					g2d.drawArc((int)(c2.getX()+12.5*iZoom), (int)(c2.getY()+40*iZoom), (int)(25*iZoom), (int)(25*iZoom), 150, 240);
			    	if( hci.getGraph().isDirected() ) {	
						g2d.setColor(style.getArcLine());
						drawArrow(g2d, pCenter1.x-35, pCenter1.y+80, pCenter2.x, pCenter2.y, (int)(25*iZoom), (int)(10*iZoom));
					}
					if(  hci.getGraph().isValued() ) {
						g2d.setColor(style.getArcText());
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
						/*-----Cacul des points M et N pour placer les deux arcs par rapport ï¿½ pCenter1*/
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
						/*-----Cacul des points M et N pour placer les deux arcs par rapport ï¿½ pCenter2*/
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
				        
				        g2d.setColor(style.getArcLine());
				        g2d.drawLine((int)xm1, (int)ym1, (int)xn2, (int)yn2);	
						drawArrow(g2d, (int)xm1, (int)ym1, (int)xn2, (int)yn2, (int)(22*iZoom), (int)(10*iZoom));
						if(  hci.getGraph().isValued() ) {
							g2d.setColor(style.getArcText());
							g2d.drawString( ""+arc.getIValue() , (int)(xm1+xn2)/2 , (int)(ym1+yn2)/2 );
						}				        
					} else {
						g2d.drawLine((int)pCenter1.x, (int)pCenter1.y, (int)pCenter2.x, (int)pCenter2.y);
						if(  hci.getGraph().isDirected() ) {	
							g2d.setColor(style.getArcLine());
							drawArrow(g2d, pCenter1.x, pCenter1.y, pCenter2.x, pCenter2.y, (int)(25*iZoom), (int)(10*iZoom));
						}
						if(  hci.getGraph().isValued() ) {
							g2d.setColor(style.getArcText());
							g2d.drawString( ""+arc.getIValue() , (pCenter1.x+pCenter2.x)/2 , (pCenter1.y+pCenter2.y)/2 );
						}
					}
				}
				g2d.setColor(Color.BLACK);
			}
		}
	}
	
	/*--Zoom--*/
	public void refreshPreferedSize() {
		int xMax = 0;
		int yMax = 0;
		for(Point c : hci.getHmVertex().values()) {
			if(c.getX() > xMax) xMax = c.x;
			if(c.getY() > yMax) yMax = c.y;
		}
		setPreferredSize(new Dimension((int)((xMax+iWidthEdge*iZoom)),(int)((yMax+iHeightEdge*iZoom))));
	}
	public double zoomIn() {
		if(iZoom<1.8) {
			for(Point c : hci.getHmVertex().values()) {
				c.x = (int) (c.x/iZoom*(iZoom+0.1));
				c.y = (int) (c.y/iZoom*(iZoom+0.1));
				iZoom = iZoom+0.1;
			}
		}
		refreshPreferedSize();
		return iZoom;
	}
	public double zoomOut() {
		if(iZoom>0.7) {
			for(Point c : hci.getHmVertex().values()) {
				c.x = (int) (c.x/iZoom*(iZoom-0.1));
				c.y = (int) (c.y/iZoom*(iZoom-0.1));
				iZoom = iZoom-0.1;
			}
		}
		refreshPreferedSize();
		return iZoom;
	}
	public double getZoom() { return iZoom; }
	
	/*--Fonctions utiles--*/
	public Point isOnEdge(MouseEvent e) {
		double centerX,centerY;
		for(String s : hci.getHmVertex().keySet()) {
			Point c = hci.getHmVertex().get(s);
			centerX = c.getX()+iWidthEdge/2*iZoom;
			centerY = c.getY()+iHeightEdge/2*iZoom;
			if (Math.pow(e.getX() - centerX, 2) + Math.pow(e.getY() - centerY, 2) <= (Math.pow(iWidthEdge/2*iZoom, 2))) {
				return new Point((int)centerX,(int)centerY);
			}
		}
		return null;
	}
	
	/*--MouseListener--*/
	@Override
	public void mouseClicked(MouseEvent e) {
		/* Clic droit */
		int mod = e.getModifiers();
		if ((mod & InputEvent.BUTTON3_MASK) != 0)
			hci.getPopMenu().show(this, e.getX(), e.getY());
		/*------------*/
		
		/* Sélection */
		if(e.getClickCount() > 0 && e.getButton()==MouseEvent.BUTTON1) {
			double centerX, centerY;
			boolean bFind=false;
			for (Point c : hci.getHmVertex().values()) {
				centerX = c.getX()+iWidthEdge/2*iZoom;
				centerY = c.getY()+iHeightEdge/2*iZoom;
				
				//Si la souris est sur un sommet
				if (Math.pow(e.getX() - centerX, 2) + Math.pow(e.getY() - centerY, 2) <= (Math.pow(iWidthEdge/2*iZoom, 2))) {
					// Find the key of this coordinate
					for(String s : hci.getHmVertex().keySet()) {
						if (hci.getHmVertex().get(s) == c) {
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
		Point c = isOnEdge(e);
		if(c!=null){
			e.translatePoint((int)(c.getX()-e.getX()), (int)(c.getY()-e.getY()));
			bDragged = true;
		}
		saveMousePosition = e.getPoint();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(bDragged == true) {
			hci.getController().provSave();
			bDragged = false;
		}
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
		/*--Rectangle de sélection--*/
		if(rectSelectionStartPoint!=null) {
			alSelected.clear();
			
			int xMin,yMin;
			int xMax,yMax;
			if( rectSelectionStartPoint.getX() < e.getPoint().x) {xMin = (int) rectSelectionStartPoint.getX();}
			else {xMin = e.getPoint().x;}
			if( rectSelectionStartPoint.getY() < e.getPoint().y) {yMin = (int) rectSelectionStartPoint.getY();}
			else {yMin = e.getPoint().y;}
			if( rectSelectionStartPoint.getX() > e.getPoint().x) {xMax = (int) rectSelectionStartPoint.getX();}
			else {xMax = e.getPoint().x;}
			if( rectSelectionStartPoint.getY() > e.getPoint().y) {yMax = (int) rectSelectionStartPoint.getY();}
			else {yMax = e.getPoint().y;}
			
			Point p1 = new Point(xMin,yMin);
			Point p2 = new Point(xMax,yMax);
		
//			Point p1 = new Point((int)rectSelectionStartPoint.getX(),(int)rectSelectionStartPoint.getY());
//			Point p2 = e.getPoint();
						
			hci.getGraphPanel().getGraphics().drawRect(p1.x, p1.y, p2.x-p1.x, p2.y-p1.y);
			for(Point p : hci.getHmVertex().values()) {
				
				//Si p est dans le carré défini par p1 et p2
				if(p.x+iWidthEdge*iZoom>p1.x && p.x+iWidthEdge*iZoom<p2.x && p.y+iHeightEdge*iZoom>p1.y && p.y+iHeightEdge*iZoom<p2.y) {
					for(String s : hci.getHmVertex().keySet()) {
						if( hci.getHmVertex().get(s).equals(p) ){
							alSelected.add(s);
						}
					}
				}
			}
			rectSelectionStartPoint = null;
			rectSelection.setRect(-1,-1,0,0);
			repaint();
		}
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		if(isOnEdge(e)==null) {
			if(rectSelectionStartPoint==null) {
				rectSelectionStartPoint = e.getPoint();
			}
			bDragged = false;
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		
		if(alSelected.size() != 0 && bDragged==true) {			
			double centerX, centerY;
			// Find the key of this coordinate
			for(String s : hci.getHmVertex().keySet()) {
				if (alSelected.contains(s)) {
					Point c = hci.getHmVertex().get(s);
					centerX = c.getX()+iWidthEdge/2*iZoom;
					centerY = c.getY()+iHeightEdge/2*iZoom;
					if (Math.pow(e.getX() - centerX, 2) + Math.pow(e.getY() - centerY, 2) <= (Math.pow(iWidthEdge/2*iZoom, 2))) {
						/*-----Déplacements des sommets sélectonnés----*/
						setCursor(new Cursor(Cursor.MOVE_CURSOR));
						//Coordonnées minimales des points sélectionnés
						Point minPosition = new Point(c.x,c.y);
						for(String edgeSelected : alSelected) {
							if( hci.getHmVertex().get(edgeSelected).x < minPosition.x) {minPosition.x = (int) (hci.getHmVertex().get(edgeSelected).x);}
							if( hci.getHmVertex().get(edgeSelected).y < minPosition.y) {minPosition.y = (int) (hci.getHmVertex().get(edgeSelected).y);}
						}
						//Coordonnées maximales des points sélectionnés
						Point maxPosition = new Point((int) (c.x+iWidthEdge*iZoom),(int) (c.y+iHeightEdge*iZoom));
						for(String edgeSelected : alSelected) {
							if( (hci.getHmVertex().get(edgeSelected).x+iWidthEdge)  > maxPosition.x) {maxPosition.x = (int) ((hci.getHmVertex().get(edgeSelected).x+iWidthEdge));}
							if( (hci.getHmVertex().get(edgeSelected).y+iHeightEdge) > maxPosition.y) {maxPosition.y = (int) ((hci.getHmVertex().get(edgeSelected).y+iHeightEdge));}
						}
						//Calcul de la différence entre la derniï¿½re position de la souris et l'actuelle
						Point deplacement = new Point(e.getPoint().x-saveMousePosition.x,e.getPoint().y-saveMousePosition.y);
						
						/*--Déplacement--*/
						if( (minPosition.x<=0 && deplacement.x>=0 || minPosition.x>0) && 
							(minPosition.y<=0 && deplacement.y>=0 || minPosition.y>0) &&
							(maxPosition.x>=this.getWidth()  && deplacement.x<=0 || maxPosition.x<this.getWidth() ) && 
							(maxPosition.y>=this.getHeight() && deplacement.y<=0 || maxPosition.y<this.getHeight()) && 
							(minPosition.x+deplacement.x>=0) && minPosition.y+deplacement.y>=0 && maxPosition.x+deplacement.x<=this.getWidth() && maxPosition.y+deplacement.y<=this.getHeight() ) {
							//Sommet par sommet
							for(String edgeSelected : alSelected) {
								hci.getHmVertex().get(edgeSelected).x += deplacement.x;
								hci.getHmVertex().get(edgeSelected).y += deplacement.y;
								//Texte qui affiche les coordonnï¿½es
								hci.getLabelCoord().setText("  X : " + (double) (hci.getHmVertex().get(edgeSelected).x +25) + "       Y : " + (double)(hci.getHmVertex().get(edgeSelected).y + 25));
								bDragged = true;
							}
						}
						/*---------------------------------------------*/
					}
				}
			}	
		}
		if(rectSelectionStartPoint!=null) {

			int xMin,yMin;
			int xMax,yMax;
			if( rectSelectionStartPoint.getX() < e.getPoint().x) {xMin = (int) rectSelectionStartPoint.getX();}
			else {xMin = e.getPoint().x;}
			if( rectSelectionStartPoint.getY() < e.getPoint().y) {yMin = (int) rectSelectionStartPoint.getY();}
			else {yMin = e.getPoint().y;}
			if( rectSelectionStartPoint.getX() > e.getPoint().x) {xMax = (int) rectSelectionStartPoint.getX();}
			else {xMax = e.getPoint().x;}
			if( rectSelectionStartPoint.getY() > e.getPoint().y) {yMax = (int) rectSelectionStartPoint.getY();}
			else {yMax = e.getPoint().y;}
			
			Point p1 = new Point(xMin,yMin);
			Point p2 = new Point(xMax,yMax);
			
			rectSelection.setRect(p1.x, p1.y, p2.x-p1.x, p2.y-p1.y);
		}
		repaint();
		saveMousePosition = e.getPoint();
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		hci.requestFocus();
		// Permet d'afficher les coordonnï¿½es du point dï¿½s que la souris passe dessus
		Point c = isOnEdge(e);
		if (c!=null) {
			hci.getLabelCoord().setText("  X : " + c.x + "       Y : " + c.y);
		}
		else {
			if (! hci.getLabelCoord().getText().equals(" "))
				hci.getLabelCoord().setText(" ");
		}	
	}	

	/*--KeyListener--*/
	@Override
	public void keyPressed(KeyEvent e) {
		//CTRL Pressed pour la sÃ©lection
		if(e.getKeyCode()==17) bCtrlPressed = true;
		
		//CTRL+C
		if(e.getModifiersEx()==128 && e.getKeyCode()==67 ) {
			clipBoardEdge.clear();
			for(String s : alSelected) {
				int cpt = 1;
				while(hci.getHmVertex().containsKey(s.substring(0,1)+"("+cpt+")") || clipBoardEdge.containsKey(s.substring(0,1)+"("+cpt+")")) {
					cpt++;
				}
				clipBoardEdge.put(s.substring(0,1)+"("+cpt+")", new Point((int)(hci.getHmVertex().get(s).x+iWidthEdge*iZoom),(int)(hci.getHmVertex().get(s).y+iHeightEdge*iZoom)));
			}
		}
		//CTRL+V
		if(e.getModifiersEx()==128 && e.getKeyCode()==86 ) {
			for(String s : clipBoardEdge.keySet()) {
				hci.getGraph().addVertex(s);
				hci.getHmVertex().put(s, clipBoardEdge.get(s));
				hci.refresh();
			}
			clipBoardEdge.clear();
		}
		//CTRL+Z
		if(e.getModifiersEx()==128 && e.getKeyCode()==90 ) {
			hci.getController().undo();
			hci.refresh();
		}
		//CTRL+Y
		if(e.getModifiersEx()==128 && e.getKeyCode()==89 ) {
			hci.getController().redo();
			hci.refresh();
		}
		refreshPreferedSize();
		repaint();
		revalidate();
	}
	@Override
	public void keyReleased(KeyEvent e) {
		//CTRL released pour la sï¿½lection
		if(e.getKeyCode()==17) bCtrlPressed = false;
	}	
	@Override
	public void keyTyped(KeyEvent e) {}
}
