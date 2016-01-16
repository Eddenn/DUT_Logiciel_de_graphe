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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.Controller;
import model.Arc;
import model.Graph;
import model.Vertex;

@SuppressWarnings("serial")
/**
 * Classe qui gère le panneau contenant la représentation graphique du graphe.
 * 
 * @author Groupe 3
 * @version 2016-01-12
 */
public class GraphPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {

	final static float dash1[] = { 5.0f };
	final static BasicStroke pointille = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
			dash1, 0.0f);

	private boolean bClickOnVoid;
	private ArrayList<String> alSelected; // Sélection
	private HashMap<String, Point> clipBoardEdge;// Presse-Papier
	private Point saveMousePosition; // Sauvegarde de la dernière position de la
										// souris (Voir MouseDragged)
	private Point rectSelectionStartPoint;
	private Rectangle2D rectSelection;
	private boolean bDragged;
	private boolean bMoved;
	private boolean bCtrlPressed;
	private double iWidthEdge; // Largeur
	private double iHeightEdge; // Hauteur
	private double iZoom; // Zoom
	private HCI hci;
	private GraphStyle style;
	private Controller ctrl;

	/**
	 * Constructeur du panel de dessin
	 * 
	 * @param hci
	 *            le hci utilisé
	 * @param ctrl
	 *            le controleur utilisé
	 */
	public GraphPanel(HCI hci, Controller ctrl) {
		super();
		this.hci = hci;
		this.ctrl = ctrl;
		this.rectSelection = new Rectangle2D.Double();
		this.rectSelection.setRect(-1, -1, 0, 0);
		this.bClickOnVoid = false;
		this.style = GraphStyle.Basique;
		this.clipBoardEdge = new HashMap<String, Point>();
		this.saveMousePosition = new Point(0, 0);
		this.bCtrlPressed = false;
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.iZoom = 1.0;
		this.iHeightEdge = 50 * iZoom;
		this.iWidthEdge = 50 * iZoom;
		this.alSelected = new ArrayList<String>();
		this.bDragged = false;
		this.setBackground(style.getBackground());
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);

	}

	/**
	 * Méthode principale de dessin
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		paintVertexAndArc(g2d);

		g2d.setStroke(pointille);
		g2d.draw(rectSelection);
		g2d.setStroke(new BasicStroke((float) iZoom + 2));
	}

	/*--Getters and Setters--*/
	public double getiWidthEdge() {
		return iWidthEdge;
	}

	public double getiHeightEdge() {
		return iHeightEdge;
	}

	public ArrayList<String> getAlSelected() {
		return this.alSelected;
	}

	public void clearAlSelected() {
		alSelected.clear();
	}

	public void setAlSelected(ArrayList<String> s) {
		this.alSelected = s;
	}

	public GraphStyle getStyle() {
		return style;
	}

	public void setStyle(GraphStyle style) {
		this.style = style;
	}

	public Vertex getVertex(Point p) {
		for (String s : hci.getHmVertex().keySet()) {
			if (hci.getHmVertex().get(s) == p) {
				for (Vertex v : hci.getGraph().getAlVertex()) {
					if (v.getName().equals(s)) {
						return v;
					}
				}
			}
		}
		return null;
	}

	public Vertex getVertex(String s) {
		for (Vertex v : hci.getGraph().getAlVertex()) {
			if (v.getName().equals(s)) {
				return v;
			}
		}
		return null;
	}

	public double getZoom() {
		return iZoom;
	}

	/**
	 * 
	 * @param g2d
	 */
	public void paintVertexAndArc(Graphics2D g2d) {
		Graph graphLoaded = hci.getGraph();

		drawArcs(g2d);
		String strSelected = "";
		for (Vertex v : graphLoaded.getAlVertex()) {
			for (Arc a : v.getAlArcs()) {
				for (String s : alSelected) {
					if (graphLoaded.isValued()) {
						if (graphLoaded.isDirected()) {
							// Valué et orienté
							strSelected = HCI.centerStr(v.getName(), 5) + "------"
									+ HCI.centerStr("" + a.getIValue(), 7) + "----->"
									+ HCI.centerStr(a.getVertex().getName(), 5);
						} else {
							// Valué et non orienté
							strSelected = HCI.centerStr(v.getName(), 5) + "------"
									+ HCI.centerStr("" + a.getIValue(), 7) + "------"
									+ HCI.centerStr(a.getVertex().getName(), 5);
						}
					} else {
						if (graphLoaded.isDirected()) {
							// Non valué et orienté
							strSelected = HCI.centerStr(v.getName(), 5) + "-------->"
									+ HCI.centerStr(a.getVertex().getName(), 5);
						} else {
							// Non valué et non orienté
							strSelected = HCI.centerStr(v.getName(), 5) + "---------"
									+ HCI.centerStr(a.getVertex().getName(), 5);
						}
					}

					if (strSelected.equals(s)) {
						highlightArc(g2d, v, a);
					}
				}
			}
		}
		drawVertex(g2d);
		for (Vertex v : graphLoaded.getAlVertex()) {
			for (String s : alSelected) {
				if (v.getName().equals(s)) {
					highlightEdge(g2d, v);
				}
			}
		}

		g2d.setColor(getContrastColor(style.getBackground()));
		g2d.setStroke(pointille);
		g2d.draw(rectSelection);
		g2d.setStroke(new BasicStroke((float) iZoom + 2));
	}

	/**
	 * Méthode gérant le surlignage d'un sommet
	 * 
	 * @param g2d
	 * @param v
	 *            le sommet à surligner
	 */
	public void highlightEdge(Graphics2D g2d, Vertex v) {
		g2d.setColor(new Color(20, 20, 255, 50));

		g2d.fillOval((int) (hci.getHmVertex().get(v.getName()).x - 5 * iZoom),
				(int) (hci.getHmVertex().get(v.getName()).y - 5 * iZoom), (int) ((iWidthEdge + 10) * iZoom),
				(int) ((iHeightEdge + 10) * iZoom));
		g2d.setColor(Color.BLACK);
	}

	/**
	 * Méthode gérant le surlignage d'un arc
	 * 
	 * @param g2d
	 * @param v
	 * @param arc
	 */
	public void highlightArc(Graphics2D g2d, Vertex v, Arc arc) {
		Point c1 = hci.getHmVertex().get(v.getName());
		Point c2 = hci.getHmVertex().get(arc.getVertex().getName());

		boolean bMirroir = false;

		// Coordonnées centrale des deux points en fonction du zoom
		Point pCenter1 = new Point((int) (c1.getX() + iWidthEdge / 2 * iZoom),
				(int) (c1.getY() + iHeightEdge / 2 * iZoom));
		Point pCenter2 = new Point((int) (c2.getX() + iWidthEdge / 2 * iZoom),
				(int) (c2.getY() + iHeightEdge / 2 * iZoom));

		g2d.setColor(new Color(20, 20, 255, 50));

		g2d.setStroke(new BasicStroke((float) iZoom + 2));
		if (arc.getVertex() == v) { // Arc partant d'un sommet et pointant sur
									// lui-même
			g2d.drawArc((int) (c2.getX() + 12.5 * iZoom), (int) (c2.getY() + 40 * iZoom), (int) (25 * iZoom),
					(int) (25 * iZoom), 150, 240);
			if (hci.getGraph().isDirected()) {
				drawArrow(g2d, pCenter1.x - 35, pCenter1.y + 80, pCenter2.x, pCenter2.y, (int) (25 * iZoom),
						(int) (10 * iZoom));
			}
		} else { // Arc entre deux points
			// Si A -> B et B -> A
			bMirroir = false;
			for (Arc arcTmp : arc.getVertex().getAlArcs()) {
				if (arcTmp.getVertex() == v) {
					bMirroir = true;
				}
			}
			if (bMirroir && hci.getGraph().isDirected()) {
				/*-----Cacul des points M et N pour placer les deux arcs par rapport au pCenter1*/
				int d = 0;
				int h = (int) (iWidthEdge / 3 * iZoom);
				int dx = (int) pCenter2.x - (int) pCenter1.x, dy = (int) pCenter2.y - (int) pCenter1.y;
				double D = Math.sqrt(dx * dx + dy * dy);
				double xm1 = D - d, xn1 = xm1, ym1 = h, yn1 = -h, x;
				double sin = dy / D, cos = dx / D;
				x = xm1 * cos - ym1 * sin + (int) pCenter1.x;
				ym1 = xm1 * sin + ym1 * cos + (int) pCenter1.y;
				xm1 = x;
				x = xn1 * cos - yn1 * sin + (int) pCenter1.x;
				yn1 = xn1 * sin + yn1 * cos + (int) pCenter1.y;
				xn1 = x;
				/*-----------------------------------------------------------------------------*/
				/*-----Cacul des points M et N pour placer les deux arcs par rapport au Center2*/
				d = 0;
				h = (int) (iWidthEdge / 3 * iZoom);
				dx = (int) pCenter1.x - (int) pCenter2.x;
				dy = (int) pCenter1.y - (int) pCenter2.y;
				D = Math.sqrt(dx * dx + dy * dy);
				double xm2 = D - d;
				double xn2 = xm2;
				double ym2 = h;
				double yn2 = -h;
				x = 0.0;
				sin = dy / D;
				cos = dx / D;
				x = xm2 * cos - ym2 * sin + (int) pCenter2.x;
				ym2 = xm2 * sin + ym2 * cos + (int) pCenter2.y;
				xm2 = x;
				x = xn2 * cos - yn2 * sin + (int) pCenter2.x;
				yn2 = xn2 * sin + yn2 * cos + (int) pCenter2.y;
				xn2 = x;
				/*-----------------------------------------------------------------------------*/

				g2d.setColor(new Color(20, 20, 255, 50));
				g2d.drawLine((int) xm1, (int) ym1, (int) xn2, (int) yn2);
				drawArrow(g2d, (int) xn2, (int) yn2, (int) xm1, (int) ym1, (int) (22 * iZoom), (int) (10 * iZoom));
			} else {
				g2d.drawLine((int) pCenter1.x, (int) pCenter1.y, (int) pCenter2.x, (int) pCenter2.y);
				if (hci.getGraph().isDirected()) {
					drawArrow(g2d, (int) pCenter1.x, (int) pCenter1.y, (int) pCenter2.x, (int) pCenter2.y,
							(int) (25 * iZoom), (int) (10 * iZoom));
				}
			}
		}
		g2d.setColor(Color.BLACK);
	}

	/**
	 * Méthode qui dessine en fonction de hmVertex
	 */
	public void drawVertex(Graphics2D g2d) {
		for (Point c : hci.getHmVertex().values()) {
			g2d.setColor(style.getEdgeBorder());
			g2d.setStroke(new BasicStroke((float) iZoom));
			g2d.drawOval((int) (c.getX() - 1), (int) (c.getY() - 1), (int) ((iWidthEdge + 2) * iZoom),
					(int) ((iHeightEdge + 2) * iZoom));

			if (getVertex(c).getColor() == null) {
				g2d.setColor(style.getEdgeBackground());
			} else {
				g2d.setColor(getVertex(c).getColor());
			}

			g2d.fillOval((int) (c.getX()), (int) (c.getY()), (int) ((iWidthEdge) * iZoom),
					(int) ((iHeightEdge) * iZoom));
			// Find the key of this coordinate
			for (String s : hci.getHmVertex().keySet()) {
				if (hci.getHmVertex().get(s) == c) {
					g2d.setColor(style.getEdgeText());
					g2d.drawString(HCI.centerStr(s, (int) ((iWidthEdge * iZoom) / (3.5 * iZoom))), (int) (c.getX()),
							(int) (c.getY() + (iHeightEdge / 2 + (iHeightEdge / 10)) * iZoom));
				}
			}
		}
	}

	/**
	 * Méthode qui dessine une fleche du point 1 vers le point 2
	 * 
	 * @param g
	 *            Graphic component
	 * @param x1
	 *            x-position du point 1
	 * @param y1
	 *            y-position du point 1
	 * @param x2
	 *            x-position du point 2
	 * @param y2
	 *            y-position du point 2
	 * @param d
	 *            largeur de la flï¿½che
	 * @param h
	 *            hauteur de la flï¿½che
	 */
	public void drawArrow(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
		// Distance entre les points
		int dx = x2 - x1, dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		if (D > iWidthEdge) {
			// ---Calcul des positions des points M(xm;ym) et N(xn;yn)---
			double xm = D - d, xn = xm, ym = h, yn = -h, x;
			double sin = dy / D, cos = dx / D;
			x = xm * cos - ym * sin + x1;
			ym = xm * sin + ym * cos + y1;
			xm = x;
			x = xn * cos - yn * sin + x1;
			yn = xn * sin + yn * cos + y1;
			xn = x;
			// ----------------------------------------------------------

			// Point central de MN
			double x3 = (xn + xm) / 2;
			double y3 = (yn + ym) / 2;

			// ---Calcul des point MN en fonction du point calcule ci-dessus---
			x2 = (int) x3;
			y2 = (int) y3;
			d = (int) (8 * iZoom);
			h = (int) (4 * iZoom);

			dx = x2 - x1;
			dy = y2 - y1;
			D = Math.sqrt(dx * dx + dy * dy);
			xm = D - d;
			xn = xm;
			ym = h;
			yn = -h;
			sin = dy / D;
			cos = dx / D;

			x = xm * cos - ym * sin + x1;
			ym = xm * sin + ym * cos + y1;
			xm = x;

			x = xn * cos - yn * sin + x1;
			yn = xn * sin + yn * cos + y1;
			xn = x;
			// ----------------------------------------------------------------

			// Dessine la fleche au bout du segment
			g.drawLine((int) x3, (int) y3, (int) (xm), (int) (ym));
			g.drawLine((int) x3, (int) y3, (int) (xn), (int) (yn));
		}
	}

	/**
	 * Méthode qui dessine un arc
	 * 
	 * @param g2d
	 */
	public void drawArcs(Graphics2D g2d) {
		Point pCenter1;
		Point pCenter2;
		boolean bMirroir = false;

		for (Vertex v : hci.getGraph().getAlVertex()) {
			// Recup les coordonnees associees Ã  ce Vertex
			Point c1 = hci.getHmVertex().get(v.getName());

			for (Arc arc : v.getAlArcs()) {
				Point c2 = hci.getHmVertex().get(arc.getVertex().getName());

				// Coordonnees centrale des deux points en fonction du zoom
				pCenter1 = new Point((int) (c1.getX() + (iWidthEdge / 2) * iZoom),
						(int) (c1.getY() + (iHeightEdge / 2) * iZoom));
				pCenter2 = new Point((int) (c2.getX() + (iWidthEdge / 2) * iZoom),
						(int) (c2.getY() + (iHeightEdge / 2) * iZoom));

				// Dessine le segment entre les points
				g2d.setColor(style.getArcLine());
				g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, (float) (11 * iZoom)));

				if (arc.getVertex() == v) { // Arc sur lui mï¿½me
					g2d.drawArc((int) (c2.getX() + 12.5 * iZoom), (int) (c2.getY() + 40 * iZoom), (int) (25 * iZoom),
							(int) (25 * iZoom), 150, 240);
					if (hci.getGraph().isDirected()) {
						g2d.setColor(style.getArcLine());
						drawArrow(g2d, pCenter1.x - 35, pCenter1.y + 80, pCenter2.x, pCenter2.y, (int) (25 * iZoom),
								(int) (10 * iZoom));
					}
					if (hci.getGraph().isValued()) {
						g2d.setColor(style.getArcText());
						g2d.drawString("" + arc.getIValue(), (int) (pCenter1.x - (2) * iZoom),
								(int) (pCenter1.y + (40) * iZoom));
					}
				} else { // Arc entre deux points
					// Si A -> B et B -> A
					bMirroir = false;
					for (Arc arcTmp : arc.getVertex().getAlArcs()) {
						if (arcTmp.getVertex() == v) {
							bMirroir = true;
						}
					}
					if (bMirroir && hci.getGraph().isDirected()) {
						/*-----Cacul des points M et N pour placer les deux arcs par rapport au pCenter1*/
						int d = 0;
						int h = (int) (iWidthEdge / 3 * iZoom);
						int dx = (int) pCenter2.x - (int) pCenter1.x;
						int dy = (int) pCenter2.y - (int) pCenter1.y;
						double D = Math.sqrt(dx * dx + dy * dy);
						double xm1 = D - d, xn1 = xm1, ym1 = h, yn1 = -h, x;
						double sin = dy / D, cos = dx / D;
						x = xm1 * cos - ym1 * sin + (int) pCenter1.x;
						ym1 = xm1 * sin + ym1 * cos + (int) pCenter1.y;
						xm1 = x;
						x = xn1 * cos - yn1 * sin + (int) pCenter1.x;
						yn1 = xn1 * sin + yn1 * cos + (int) pCenter1.y;
						xn1 = x;
						/*-----------------------------------------------------------------------------*/
						/*-----Cacul des points M et N pour placer les deux arcs par rapport au Center2*/
						d = 0;
						h = (int) (iWidthEdge / 3 * iZoom);
						dx = (int) pCenter1.x - (int) pCenter2.x;
						dy = (int) pCenter1.y - (int) pCenter2.y;
						D = Math.sqrt(dx * dx + dy * dy);
						double xm2 = D - d;
						double xn2 = xm2;
						double ym2 = h;
						double yn2 = -h;
						x = 0.0;
						sin = dy / D;
						cos = dx / D;
						x = xm2 * cos - ym2 * sin + (int) pCenter2.x;
						ym2 = xm2 * sin + ym2 * cos + (int) pCenter2.y;
						xm2 = x;
						x = xn2 * cos - yn2 * sin + (int) pCenter2.x;
						yn2 = xn2 * sin + yn2 * cos + (int) pCenter2.y;
						xn2 = x;
						/*-----------------------------------------------------------------------------*/

						g2d.setColor(style.getArcLine());
						g2d.drawLine((int) xm1, (int) ym1, (int) xn2, (int) yn2);
						drawArrow(g2d, (int) xn1, (int) yn1, (int) xm2, (int) ym2, (int) (22 * iZoom),
								(int) (10 * iZoom));

						if (hci.getGraph().isValued()) {
							g2d.setColor(style.getArcText());
							g2d.drawString("" + arc.getIValue(), (int) (xm1 + xn2) / 2, (int) (ym1 + yn2) / 2);
						}
					} else {
						g2d.drawLine((int) pCenter1.x, (int) pCenter1.y, (int) pCenter2.x, (int) pCenter2.y);
						if (hci.getGraph().isDirected()) {
							g2d.setColor(style.getArcLine());
							drawArrow(g2d, pCenter1.x, pCenter1.y, pCenter2.x, pCenter2.y, (int) (25 * iZoom),
									(int) (10 * iZoom));
						}
						if (hci.getGraph().isValued()) {
							g2d.setColor(style.getArcText());
							g2d.drawString("" + arc.getIValue(), (pCenter1.x + pCenter2.x) / 2,
									(pCenter1.y + pCenter2.y) / 2);
						}
					}
				}
				g2d.setColor(Color.BLACK);
			}
		}
	}

	/**
	 * Méthode qui met à jour la taille du zoom
	 */
	public void refreshPreferedSize() {
		int xMax = 0;
		int yMax = 0;
		for (Point c : hci.getHmVertex().values()) {
			if (c.getX() > xMax)
				xMax = c.x;
			if (c.getY() > yMax)
				yMax = c.y;
		}
		setPreferredSize(new Dimension((int) ((xMax + iWidthEdge * iZoom)), (int) ((yMax + iHeightEdge * iZoom))));
	}

	/**
	 * Méthode qui gère l'agrandissement
	 * 
	 * @return
	 */
	public double zoomIn() {
		if (iZoom < 1.6) {
			for (Point c : hci.getHmVertex().values()) {
				c.x = (int) (c.x / iZoom * (iZoom + 0.2));
				c.y = (int) (c.y / iZoom * (iZoom + 0.2));
				iZoom = iZoom + 0.1;
			}
		}
		refreshPreferedSize();
		hci.getLabelZoom().setText(String.format("%3.0f", iZoom * 100) + "%");
		return iZoom;
	}

	/**
	 * Méthoque qui gère le rétrécissement
	 * 
	 * @return
	 */
	public double zoomOut() {
		if (iZoom > 0.6) {
			for (Point c : hci.getHmVertex().values()) {
				c.x = (int) (c.x / iZoom * (iZoom - 0.2));
				c.y = (int) (c.y / iZoom * (iZoom - 0.2));
				iZoom = iZoom - 0.1;
			}
		}
		refreshPreferedSize();
		hci.getLabelZoom().setText(String.format("%3.0f", iZoom * 100) + "%");
		return iZoom;
	}

	/*--Fonctions utiles--*/
	/**
	 * Méthode qui vérifie si le curseur se situe sur un sommet.
	 * 
	 * @param e
	 * @return le sommet sur lequel pointe le curseur
	 */
	public Point isOnEdge(MouseEvent e) {
		double centerX, centerY;
		for (String s : hci.getHmVertex().keySet()) {
			Point c = hci.getHmVertex().get(s);
			centerX = c.getX() + iWidthEdge / 2 * iZoom;
			centerY = c.getY() + iHeightEdge / 2 * iZoom;
			if (Math.pow(e.getX() - centerX, 2)
					+ Math.pow(e.getY() - centerY, 2) <= (Math.pow(iWidthEdge / 2 * iZoom, 2))) {
				return new Point((int) centerX, (int) centerY);
			}
		}
		return null;
	}

	/**
	 * Méthode utiliser lors de la selection multiple. Permet de définir la
	 * couleur du rectangle de sélection selon la couleur de font.
	 * 
	 * @param color
	 *            couleur de départ
	 * @return la couleur inverse
	 */
	public static Color getContrastColor(Color color) {
		double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
		return y >= 128 ? Color.black : Color.white;
	}

	/**
	 * Méthode utilisé pour supprimer plusieurs sommets.
	 */
	public void cutEdge() {
		if (alSelected.size() > 0)
			clipBoardEdge.clear();
		for (String s : alSelected) {
			clipBoardEdge.put(s, hci.getHmVertex().get(s));
		}
		ctrl.deleteMultipleVertex(this.getAlSelected());
	}

	/**
	 * Méthode utilisé pour copier plusieurs sommets
	 */
	public void copyEdge() {
		if (alSelected.size() > 0)
			clipBoardEdge.clear();
		for (String s : alSelected) {
			clipBoardEdge.put(s, hci.getHmVertex().get(s));
		}
	}

	/**
	 * Méthode permettant de sélectionnet tout le graphe
	 */
	public void selectAll() {
		alSelected.clear();
		for (String s : hci.getHmVertex().keySet()) {
			alSelected.add(s);
		}
	}

	/**
	 * Méthode permettant de coller les sommets copier
	 */
	public void pasteEdge() {
		for (String s : clipBoardEdge.keySet()) {
			int cpt = 1;
			while (hci.getHmVertex().containsKey(s.substring(0, 1) + "(" + cpt + ")")
					|| clipBoardEdge.containsKey(s.substring(0, 1) + "(" + cpt + ")")) {
				cpt++;
			}
			hci.getGraph().addVertex(s.substring(0, 1) + "(" + cpt + ")");
			hci.getHmVertex().put(s.substring(0, 1) + "(" + cpt + ")",
					new Point((int) (clipBoardEdge.get(s).x + iWidthEdge * iZoom),
							(int) (clipBoardEdge.get(s).y + iHeightEdge * iZoom)));
			hci.refresh();
		}
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
		if (e.getClickCount() > 0 && e.getButton() == MouseEvent.BUTTON1) {
			double centerX, centerY;
			boolean bFind = false;
			for (Point c : hci.getHmVertex().values()) {
				centerX = c.getX() + iWidthEdge / 2 * iZoom;
				centerY = c.getY() + iHeightEdge / 2 * iZoom;

				// Si la souris est sur un sommet
				if (Math.pow(e.getX() - centerX, 2)
						+ Math.pow(e.getY() - centerY, 2) <= (Math.pow(iWidthEdge / 2 * iZoom, 2))) {
					// Find the key of this coordinate
					for (String s : hci.getHmVertex().keySet()) {
						if (hci.getHmVertex().get(s) == c) {
							if (!bCtrlPressed)
								alSelected.clear();
							if (!alSelected.contains(s))
								alSelected.add(s);
							bFind = true;
						}
					}
				}
			}
			if (!bFind) {
				alSelected.clear();
			}
		}
		/*-----------*/
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point c = isOnEdge(e);
		if (c != null) {
			e.translatePoint((int) (c.getX() - e.getX()), (int) (c.getY() - e.getY()));
			bDragged = true;
		} else {
			bClickOnVoid = true;
		}
		saveMousePosition = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		bClickOnVoid = false;

		if (bDragged == true && bMoved == true) {
			ctrl.provSave();
			bDragged = false;
			bMoved = false;
			hci.setBSaved(false);
		}
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

		/*--Rectangle de sélection--*/
		if (rectSelectionStartPoint != null) {
			alSelected.clear();

			int xMin, yMin;
			int xMax, yMax;
			if (rectSelectionStartPoint.getX() < e.getPoint().x) {
				xMin = (int) rectSelectionStartPoint.getX();
			} else {
				xMin = e.getPoint().x;
			}
			if (rectSelectionStartPoint.getY() < e.getPoint().y) {
				yMin = (int) rectSelectionStartPoint.getY();
			} else {
				yMin = e.getPoint().y;
			}
			if (rectSelectionStartPoint.getX() > e.getPoint().x) {
				xMax = (int) rectSelectionStartPoint.getX();
			} else {
				xMax = e.getPoint().x;
			}
			if (rectSelectionStartPoint.getY() > e.getPoint().y) {
				yMax = (int) rectSelectionStartPoint.getY();
			} else {
				yMax = e.getPoint().y;
			}

			Point p1 = new Point(xMin, yMin);
			Point p2 = new Point(xMax, yMax);

			hci.getGraphPanel().getGraphics().drawRect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
			for (Point p : hci.getHmVertex().values()) {

				// Si p est dans le carré défini par p1 et p2
				if (p.x + iWidthEdge / 2 * iZoom > p1.x && p.x + iWidthEdge / 2 * iZoom < p2.x
						&& p.y + iHeightEdge / 2 * iZoom > p1.y && p.y + iHeightEdge / 2 * iZoom < p2.y) {
					for (String s : hci.getHmVertex().keySet()) {
						if (hci.getHmVertex().get(s).equals(p)) {
							alSelected.add(s);
						}
					}
				}
			}
			rectSelectionStartPoint = null;
			rectSelection.setRect(-1, -1, 0, 0);
			repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (isOnEdge(e) == null) {
			if (rectSelectionStartPoint == null) {
				rectSelectionStartPoint = e.getPoint();
			}
			bDragged = false;
			bMoved = false;
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} else
			bMoved = true;

		if (alSelected.size() != 0 && bDragged == true) {
			double centerX, centerY;
			// Find the key of this coordinate
			for (String s : hci.getHmVertex().keySet()) {
				if (alSelected.contains(s)) {
					Point c = hci.getHmVertex().get(s);
					centerX = c.getX() + iWidthEdge / 2 * iZoom;
					centerY = c.getY() + iHeightEdge / 2 * iZoom;
					if (Math.pow(e.getX() - centerX, 2)
							+ Math.pow(e.getY() - centerY, 2) <= (Math.pow(iWidthEdge / 2 * iZoom, 2))) {
						/*-----Déplacements des sommets sélectonnés----*/
						setCursor(new Cursor(Cursor.MOVE_CURSOR));
						// Coordonnées minimales des points sélectionnés
						Point minPosition = new Point(c.x, c.y);
						for (String edgeSelected : alSelected) {
							if (hci.getHmVertex().get(edgeSelected).x < minPosition.x) {
								minPosition.x = (int) (hci.getHmVertex().get(edgeSelected).x);
							}
							if (hci.getHmVertex().get(edgeSelected).y < minPosition.y) {
								minPosition.y = (int) (hci.getHmVertex().get(edgeSelected).y);
							}
						}
						// Coordonnées maximales des points sélectionnés
						Point maxPosition = new Point((int) (c.x + iWidthEdge * iZoom),
								(int) (c.y + iHeightEdge * iZoom));
						for (String edgeSelected : alSelected) {
							if ((hci.getHmVertex().get(edgeSelected).x + iWidthEdge) > maxPosition.x) {
								maxPosition.x = (int) ((hci.getHmVertex().get(edgeSelected).x + iWidthEdge));
							}
							if ((hci.getHmVertex().get(edgeSelected).y + iHeightEdge) > maxPosition.y) {
								maxPosition.y = (int) ((hci.getHmVertex().get(edgeSelected).y + iHeightEdge));
							}
						}
						// Calcul de la différence entre la derniere position de
						// la souris et l'actuelle
						Point deplacement = new Point(e.getPoint().x - saveMousePosition.x,
								e.getPoint().y - saveMousePosition.y);

						/*--Déplacement--*/
						if ((minPosition.x <= 0 && deplacement.x >= 0 || minPosition.x > 0)
								&& (minPosition.y <= 0 && deplacement.y >= 0 || minPosition.y > 0)
								&& (maxPosition.x >= this.getWidth() && deplacement.x <= 0
										|| maxPosition.x < this.getWidth())
								&& (maxPosition.y >= this.getHeight() && deplacement.y <= 0
										|| maxPosition.y < this.getHeight())
								&& (minPosition.x + deplacement.x >= 0) && minPosition.y + deplacement.y >= 0
								&& maxPosition.x + deplacement.x <= this.getWidth()
								&& maxPosition.y + deplacement.y <= this.getHeight()) {
							// Sommet par sommet
							for (String edgeSelected : alSelected) {
								hci.getHmVertex().get(edgeSelected).x += deplacement.x;
								hci.getHmVertex().get(edgeSelected).y += deplacement.y;

								// Texte qui affiche les coordonnees
								hci.getLabelCoord()
										.setText("  X : " + (double) (hci.getHmVertex().get(edgeSelected).x + 25)
												+ "       Y : "
												+ (double) (hci.getHmVertex().get(edgeSelected).y + 25));
								bDragged = true;
							}
						}
						/*---------------------------------------------*/
					}
				}
			}
		}
		if (rectSelectionStartPoint != null && bClickOnVoid) {

			int xMin, yMin;
			int xMax, yMax;
			if (rectSelectionStartPoint.getX() < e.getPoint().x) {
				xMin = (int) rectSelectionStartPoint.getX();
			} else {
				xMin = e.getPoint().x;
			}
			if (rectSelectionStartPoint.getY() < e.getPoint().y) {
				yMin = (int) rectSelectionStartPoint.getY();
			} else {
				yMin = e.getPoint().y;
			}
			if (rectSelectionStartPoint.getX() > e.getPoint().x) {
				xMax = (int) rectSelectionStartPoint.getX();
			} else {
				xMax = e.getPoint().x;
			}
			if (rectSelectionStartPoint.getY() > e.getPoint().y) {
				yMax = (int) rectSelectionStartPoint.getY();
			} else {
				yMax = e.getPoint().y;
			}

			Point p1 = new Point(xMin, yMin);
			Point p2 = new Point(xMax, yMax);

			rectSelection.setRect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
		}
		repaint();
		saveMousePosition = e.getPoint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		hci.requestFocus();
		// Permet d'afficher les coordonnees du point des que la souris passe
		// dessus
		Point c = isOnEdge(e);
		if (c != null) {
			hci.getLabelCoord().setText("  X : " + c.x + "       Y : " + c.y);
		} else {
			if (!hci.getLabelCoord().getText().equals(" "))
				hci.getLabelCoord().setText(" ");
		}
	}

	/*--KeyListener--*/
	@Override
	public void keyPressed(KeyEvent e) {
		// Suppr Pressed pour la supression des sommets sélectionnés
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			ctrl.deleteMultipleVertex(alSelected);
			setAlSelected(new ArrayList<String>());
			hci.refresh();
		}

		// CTRL Pressed pour la sélection
		else if (e.getKeyCode() == 17)
			bCtrlPressed = true;

		// CTRL+N
		else if (e.getModifiersEx() == 128 && e.getKeyCode() == KeyEvent.VK_N) {
			if (hci.bSaved == false) {
				// On propose à l'utilisateur de sauvegarder son travail avant
				// de continuer
				String[] tabVal = { "Enregistrer et continuer", "Continuer sans enregistrer", "Annuler" };
				int val = JOptionPane.showOptionDialog(this,
						"La création d'un nouveau graphe entrainera la perte du graphe actuel s'il n'a pas été sauvegardé. \nVoulez vous continuer ?",
						"Création d'un nouveau graphe", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
						tabVal, tabVal[0]);

				if (val == 0) {
					if (ctrl.getFile().equals(""))
						hci.saveDialog();
					else
						ctrl.saveFile("", "adjacence");
					new PopupNewGraph("Création d'un nouveau graphe", true, ctrl, hci);
				} else if (val == 1)
					new PopupNewGraph("Création d'un nouveau graphe", true, ctrl, hci);
			} else
				new PopupNewGraph("Création d'un nouveau graphe", true, ctrl, hci);

			// Reset style personnalise
			GraphStyle.Personnalise.setEdgeBorder(Color.BLACK);
			GraphStyle.Personnalise.setEdgeBackground(Color.WHITE);
			GraphStyle.Personnalise.setEdgeText(Color.BLACK);
			GraphStyle.Personnalise.setArcLine(Color.GRAY);
			GraphStyle.Personnalise.setArcText(Color.BLACK);
			GraphStyle.Personnalise.setBackground(new Color(238, 238, 238));
			this.setBackground(new Color(238, 238, 238));
		}
		// CTRL+O
		else if (e.getModifiersEx() == 128 && e.getKeyCode() == KeyEvent.VK_O) {
			JFileChooser dial = new JFileChooser(new File("."));
			if (dial.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				try {
					ctrl.loadFile(dial.getSelectedFile().getAbsolutePath());
				} catch (Exception exp) {
				}
			}
		}
		// CTRL+S
		else if (e.getModifiersEx() == 128 && e.getKeyCode() == KeyEvent.VK_S) {
			if (ctrl.getFile().equals("")) {
				hci.saveDialog();
			} else {
				ctrl.saveFile("", "adjacence");
			}
		}
		// CTRL+MAJ+S
		else if (e.getModifiersEx() == 192 && e.getKeyCode() == KeyEvent.VK_S) {
			hci.saveDialog();
		} else if (e.getKeyCode() == 27) {
			System.exit(0);
		}
		// CTRL+Z
		else if (e.getModifiersEx() == 128 && e.getKeyCode() == KeyEvent.VK_Z) {
			ctrl.undo();
			hci.refresh();
		}
		// CTRL+Y
		else if (e.getModifiersEx() == 128 && e.getKeyCode() == KeyEvent.VK_Y) {
			ctrl.redo();
			hci.refresh();
		}
		// CTRL+X
		else if (e.getModifiersEx() == 128 && e.getKeyCode() == KeyEvent.VK_X) {
			cutEdge();
		}
		// CTRL+C
		else if (e.getModifiersEx() == 128 && e.getKeyCode() == KeyEvent.VK_C) {
			copyEdge();
		}
		// CTRL+V
		else if (e.getModifiersEx() == 128 && e.getKeyCode() == KeyEvent.VK_V) {
			pasteEdge();
		}
		// CTRL+A
		else if (e.getModifiersEx() == 128 && e.getKeyCode() == KeyEvent.VK_A) {
			selectAll();
		}

		refreshPreferedSize();
		repaint();
		revalidate();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// CTRL released pour la sï¿½lection
		if (e.getKeyCode() == 17)
			bCtrlPressed = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	/*--MouseWheelListener--*/
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() == -1 && e.isControlDown()) {
			zoomIn();
		}
		if (e.getWheelRotation() == 1 && e.isControlDown()) {
			zoomOut();
		}

	}
}
