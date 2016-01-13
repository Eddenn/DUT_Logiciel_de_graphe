package model;

import java.util.ArrayList;

import controller.IControlable;

public class ParcoursDijkstra implements IParcourable {
	private IControlable ctrl;

	private int sommetActif;
	private int sommetDepart;

	private int ligArcActif;
	private int colArcActif;

	private int[][] matrice;
	private char[] lstSommet;

	private int[][] tChemins;
	private boolean[] sommetsTraites;
	private int[][] filsPere;

	public ParcoursDijkstra(IControlable ctrl, int sommetDepart) {
		
		this.ctrl = ctrl;

		this.sommetActif = -1;
		this.ligArcActif = -1;
		this.colArcActif = -1;

		this.sommetDepart = sommetDepart;

		matrice = ctrl.getMatrice();
		lstSommet = ctrl.listeSommet();

		tChemins = new int[matrice.length][matrice.length];

		for (int i = 0; i < tChemins.length; i++) {
			for (int j = 0; j < tChemins.length; j++) {
				tChemins[i][j] = -1;
			}
		}

		sommetsTraites = new boolean[lstSommet.length];

		for (int i = 0; i < sommetsTraites.length; i++) {
			sommetsTraites[i] = false;
		}
		
		filsPere = new int[matrice.length][matrice.length];
		
		for (int i = 0; i < filsPere.length; i++) {
			filsPere[0][i] = i;
		}
	}

	public boolean sommetActif(char sommet) {
		return sommetActif != -1 && lstSommet[sommetActif] == sommet;
	}

	public boolean arcActif(char sommetOri, char sommetDest) {
		return this.colArcActif != -1 && this.ligArcActif != -1 && sommetOri == lstSommet[this.colArcActif]
				&& sommetDest == lstSommet[this.ligArcActif];
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void lancer() {
		sommetActif = sommetDepart;
		filsPere[1][sommetActif] = sommetActif;
		
		sommetsTraites[sommetActif] = true;

		// On place la valeur 0 sur toute la colonne du sommet de départ
		for (int i = 0; i < tChemins.length; i++) {
			tChemins[i][sommetActif] = 0;
		}

		// On parcourt le tableau ligne par ligne
		for (int i = 1; i < tChemins.length; i++) {
			// On récupére les voisins du sommet actif
			ArrayList<Integer> alVoisins = getVoisins(sommetActif);
			
			// On parcourt chaque sommet
			for (int iSommet = 0; iSommet < tChemins.length; iSommet++) {
				this.pauseSynchro();
				
				ligArcActif = i;
				colArcActif = sommetActif;
				
				if (alVoisins.contains(iSommet)) {
					if (!sommetsTraites[iSommet]) {
						// On vérifie qu'il n'y a aucun chemin existant
						if (tChemins[i - 1][iSommet] == -1) {
							// S'il n'y en a pas, on lui ajoute une valeur
							tChemins[i][iSommet] = getValeurArc(sommetActif, iSommet)
									+ tChemins[i - 1][sommetActif];
							
							filsPere[1][iSommet] = sommetActif;
						} else {
							if (tChemins[i - 1][sommetActif] + getValeurArc(sommetActif,
									iSommet) < tChemins[i - 1][iSommet]) {
								tChemins[i][iSommet] = tChemins[i - 1][sommetActif]
										+ getValeurArc(sommetActif, iSommet);
								
								filsPere[1][iSommet] = sommetActif;

							} else {
								tChemins[i][iSommet] = tChemins[i - 1][iSommet];
							}
						}
					}
				} else {
					if (tChemins[i - 1][iSommet] != -1) {
						tChemins[i][iSommet] = tChemins[i - 1][iSommet];
					}
				}
				
				ctrl.majIHM();
			}
			
			// On détermine le nouveau sommet actif
			int min = -1;

			for (int j = 0; j < tChemins.length; j++) {
				if ((min == -1 && !sommetsTraites[j] && tChemins[i][j] != -1)
						|| (tChemins[i][j] < min && tChemins[i][j] != -1 && !sommetsTraites[j])) {
					sommetActif = j;
					min = tChemins[i][j];
				}
			}

			sommetsTraites[sommetActif] = true;

			for (int iRow = i; iRow < tChemins.length; iRow++) {
				tChemins[iRow][sommetActif] = tChemins[i][sommetActif];
			}
		}
	
		sommetActif = -1;
		ligArcActif = -1;
		colArcActif = -1;
	}
	
	public int[][] getTChemins() {
		return tChemins;
	}
	
	private void pauseSynchro() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
	}

	private ArrayList<Integer> getVoisins(int iSommet) {
		ArrayList<Integer> alVoisins = new ArrayList<Integer>();

		for (int i = 0; i < matrice.length; i++) {
			Integer integer = new Integer(i);

			if (matrice[i][iSommet] != -1) {
				alVoisins.add(integer);
			}
		}

		return alVoisins;
	}

	private int getValeurArc(int sommetA, int sommetB) {
		return matrice[sommetB][sommetA];
	}

}
