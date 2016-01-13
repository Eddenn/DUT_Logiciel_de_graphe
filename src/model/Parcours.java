package model;

import controller.IControlable;

public class Parcours implements IParcourable {
	private IControlable ctrl;

	private int sommetActif; // on pourrait aussi avoir un tableau

	private int ligArcActif; // on pourrait aussi avoir un tableau
	private int colArcActif;

	private boolean next;
	private String message;

	private int[][] matrice;
	private char[] lstSommet;

	public Parcours(IControlable ctrl) {
		this.ctrl = ctrl;
		this.sommetActif = -1;
		this.ligArcActif = -1;
		this.colArcActif = -1;
		this.message = "";

		matrice = ctrl.getMatrice();
		lstSommet = ctrl.listeSommet();

		this.next = false;

	}

	public boolean sommetActif(char sommet) {
		return sommetActif != -1 && lstSommet[sommetActif] == sommet;
	}

	public boolean arcActif(char sommetOri, char sommetDest) {
		return this.colArcActif != -1 && this.ligArcActif != -1 && sommetOri == lstSommet[this.colArcActif]
				&& sommetDest == lstSommet[this.ligArcActif];
	}

	public void lancer() {

		int etape = 1;

		// recherche de l'arc le plus grand
		int max = -1;
		
		for (int col = 0; col < matrice.length; col++) {
			this.sommetActif = col;
			for (int lig = 0; lig < matrice.length; lig++) {

				if (matrice[lig][col] != -1) {
					this.pauseSynchro();

					this.ligArcActif = lig;
					this.colArcActif = col;

					if (matrice[lig][col] > max) {
						max = matrice[lig][col];
						this.message = "etape " + etape + "   nouveau max : " + max;
					} else {
						this.message = "etape " + etape + "           max : " + max;
					}
					ctrl.majIHM();
					
					etape++;
				}
			}
		}

		if (max == -1) {
			this.message = "Aucune valeur n'a été trouvée";
		}
		else {
			message = "Le max ainsi trouvé vaut " + max;
		}

		this.sommetActif = -1;
		this.colArcActif = -1;
		this.ligArcActif = -1;
	}

	public String getMessage() {
		return this.message;
	}

	private void pauseSynchro() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
	}

	// méthode ne pouvant fonctionner que dans un contexte asynchrone
	public void pause() {
		next = false;
		while (!next) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
	}

	// méthode ne pouvant fonctionner que dans un contexte asynchrone,
	// permettant de déactiver la pause
	public void next() {
		next = true;

	}

}