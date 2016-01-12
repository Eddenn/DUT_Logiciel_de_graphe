import java.util.ArrayList;

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

	private String message;

	public ParcoursDijkstra(IControlable ctrl, int sommetDepart) {
		this.ctrl = ctrl;

		this.sommetActif = -1;
		this.ligArcActif = -1;
		this.colArcActif = -1;

		this.sommetDepart = sommetDepart;

		this.message = "";

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
			// On parcourt les arcs voisins du sommet actif
			for (Integer iVoisins : getVoisins(sommetActif)) {
				// On vérifie que le sommet n'a pas été traité.
				if (!sommetsTraites[iVoisins.intValue()]) {
					// On vérifie qu'il n'y a aucun chemin existant
					if (tChemins[i - 1][iVoisins.intValue()] == -1) {
						// S'il n'y en a pas, on lui ajoute une valeur
						tChemins[i][iVoisins.intValue()] = getValeurArc(sommetActif, iVoisins.intValue())
								+ tChemins[i - 1][sommetActif];
						
						filsPere[1][iVoisins.intValue()] = sommetActif;
					} else {
						if (tChemins[i - 1][sommetActif] + getValeurArc(sommetActif,
								iVoisins.intValue()) < tChemins[i - 1][iVoisins.intValue()]) {
							tChemins[i][iVoisins.intValue()] = tChemins[i - 1][sommetActif]
									+ getValeurArc(sommetActif, iVoisins.intValue());
							
							filsPere[1][iVoisins.intValue()] = sommetActif;

						} else {
							tChemins[i][iVoisins.intValue()] = tChemins[i - 1][iVoisins.intValue()];
						}
					}
				}
			}

			for (int iRow = 0; iRow < tChemins.length; iRow++) {
				if (tChemins[i][iRow] == -1 && tChemins[i - 1][iRow] != -1) {
					tChemins[i][iRow] = tChemins[i - 1][iRow];
				}
			}

			// On détermine le nouveau sommet actif
			int min = -1;
			int indice = sommetActif;

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
