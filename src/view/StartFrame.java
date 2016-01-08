package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.Controller;

public class StartFrame {
	
	public StartFrame(Controller ctrl, HCI hci) {
	
		/*-----Choix de l'utilisateur----*/
		//Frame pour le logo
		JFrame logoFrame = new JFrame();
		logoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		JPanel pLogo = new JPanel(){
			public void paintComponent(Graphics g){
				Image logo = null;
				try {
					logo = ImageIO.read(new File("images/Logo_LGP.png"));
				} catch (IOException e) {}
				g.drawImage(logo, 0, 0, null);
			}
		};
		pLogo.revalidate();
		pLogo.repaint();
		pLogo.setBackground(new Color(0,0,0,0));
		pLogo.setOpaque(false);
		logoFrame.add(pLogo);
		logoFrame.setUndecorated(true);
		logoFrame.setSize(530, 530);
		logoFrame.setLocationRelativeTo(null);
		logoFrame.setBackground(new Color(0,0,0,0));
		
		logoFrame.setVisible(true);
		FormNewGraph nouveauGraph = new FormNewGraph(hci, "Création d'un nouveau graphe", true, ctrl);
		//Attend la fin de la saisie des parametres du graphe
		while(!nouveauGraph.getBEnd()){}
		if(nouveauGraph.getBClose()) {
			System.exit(0);
		}
		logoFrame.dispose();
		hci.setVisible(true);
		
	}

}
