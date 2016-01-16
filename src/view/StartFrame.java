package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.Controller;

/**
 * Classe qui gère la fenêtre de démarrage du programme
 * 
 * @author Groupe 3
 * @version 2016-01-12
 */
public class StartFrame {

	/**
	 * Constructeur qui instancie la fenêtre de démarrage
	 * 
	 * @param ctrl
	 *            le controleur utilisé
	 * @param hci
	 *            le hci utilisé
	 */
	public StartFrame(Controller ctrl, HCI hci) {

		/*-----Choix de l'utilisateur----*/
		// Frame pour le logo
		JFrame logoFrame = new JFrame();
		logoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		@SuppressWarnings("serial")
		JPanel pLogo = new JPanel() {
			public void paintComponent(Graphics g) {
				Image logo = null;
				ImageIcon img = new ImageIcon(getClass().getResource("/Logo_LGP.png"));
				logo = iconToImage(img);
				g.drawImage(logo, 0, 0, null);
			}
		};
		pLogo.revalidate();
		pLogo.repaint();
		pLogo.setBackground(new Color(0, 0, 0, 0));
		pLogo.setOpaque(false);
		logoFrame.add(pLogo);
		logoFrame.setUndecorated(true);
		logoFrame.setSize(530, 530);
		logoFrame.setLocationRelativeTo(null);
		logoFrame.setBackground(new Color(0, 0, 0, 0));

		logoFrame.setVisible(true);
		PopupNewGraph nouveauGraph = new PopupNewGraph("Création d'un nouveau graphe", true, ctrl, hci);
		// Attend la fin de la saisie des parametres du graphe
		while (!nouveauGraph.getBEnd()) {
		}
		if (nouveauGraph.getBClose()) {
			System.exit(0);
		}
		logoFrame.dispose();
		hci.setVisible(true);

	}

	static Image iconToImage(Icon icon) {
		if (icon instanceof ImageIcon) {
			return ((ImageIcon) icon).getImage();
		} else {
			int w = icon.getIconWidth();
			int h = icon.getIconHeight();
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gd = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gd.getDefaultConfiguration();
			BufferedImage image = gc.createCompatibleImage(w, h);
			Graphics2D g = image.createGraphics();
			icon.paintIcon(null, g, 0, 0);
			g.dispose();
			return image;
		}
	}

}
