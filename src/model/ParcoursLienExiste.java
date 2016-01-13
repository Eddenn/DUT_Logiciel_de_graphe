package model;

import java.util.ArrayList;

import controller.IControlable;

public class ParcoursLienExiste implements IParcourable {
	private IControlable ctrl;

	private int sommetActif;

	private int sommetDepart;
	private int sommetArrive;

	private int ligArcActif;
	private int colArcActif;

	private String message;

	private int[][] matrice;
	private char[] lstSommet;

	private ArrayList<Integer> alSommetsTraites;

	private ListeFilsDuPere lfdpArrive;

	private int iValeurChemin;

	public ParcoursLienExiste(IControlable ctrl, int sommetDepart, int sommetArrive) {
		this.ctrl = ctrl;

		this.sommetDepart = sommetDepart;
		this.sommetArrive = sommetArrive;

		this.sommetActif = -1;
		this.ligArcActif = -1;
		this.colArcActif = -1;

		this.message = "";

		matrice = ctrl.getMatrice();
		lstSommet = ctrl.listeSommet();

		alSommetsTraites = new ArrayList<Integer>();

		lfdpArrive = null;
		iValeurChemin = -1;
	}

	@Override
	public boolean sommetActif(char sommet) {
		return sommetActif != -1 && lstSommet[sommetActif] == sommet;
	}

	@Override
	public boolean arcActif(char sommetOri, char sommetDest) {
		return this.colArcActif != -1 && this.ligArcActif != -1 && sommetOri == lstSommet[this.colArcActif]
				&& sommetDest == lstSommet[this.ligArcActif];
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void lancer() {
		sommetActif = sommetDepart;

		ligArcActif = sommetActif;
		colArcActif = sommetActif;
		
		ListeFilsDuPere lfdp = new ListeFilsDuPere(sommetActif, null);

		boolean bContinu = true;

		bContinu = recurr(lfdp);

		sommetActif = lfdpArrive.getSommet();
		ligArcActif = lfdpArrive.getSommet();
		colArcActif = lfdpArrive.getPere().getSommet();
		
		if (lfdpArrive != null) {
			lfdp = lfdpArrive;

			while (lfdp != null) {
				if (lfdp.getPere() != null) {
					if (iValeurChemin == -1) {
						iValeurChemin = 0;
					}

					iValeurChemin += matrice[lfdp.getSommet()][lfdp.getPere().getSommet()];
				}

				lfdp = lfdp.getPere();
			}
		}

		ctrl.majIHM();
		
		if (iValeurChemin == -1) {
			message = "Il n'y a pas de chemin entre " + lstSommet[sommetDepart] + " et " + lstSommet[sommetArrive]
					+ ".";
		} else {
			message = "Il existe un chemin partant de " + lstSommet[sommetDepart] + " vers " + lstSommet[sommetArrive] + " vallant "
					+ iValeurChemin;
		}
	}

	private boolean recurr(ListeFilsDuPere lfdp) {
		ctrl.majIHM();
		boolean bContinu = true;

		for (Integer integer : getFils(sommetActif)) {
			lfdp.ajouterFils(integer.intValue());
		}
		
		while (bContinu) {
			this.pauseSynchro();
			
			if (sommetArrive == lfdp.getFilsCourant().getSommet()) {
				lfdpArrive = lfdp.getFilsCourant();

				bContinu = false;
			} else {
				sommetActif = lfdp.getFilsCourant().getSommet();
				ligArcActif = lfdp.getFilsCourant().getSommet();
				colArcActif = lfdp.getSommet();
				ctrl.majIHM();
				
				lfdp = lfdp.getFilsCourantIncremente();
				bContinu = recurr(lfdp);
			}
		}

		lfdp = lfdp.getFilsCourant();

		return bContinu;
	}

	private ArrayList<Integer> getFils(int iSommet) {
		if (iSommet != sommetArrive) {
			alSommetsTraites.add(new Integer(iSommet));
		}
		
		ArrayList<Integer> alInteger = new ArrayList<Integer>();

		for (int i = 0; i < matrice.length; i++) {
			Integer integer = new Integer(i);

			if (matrice[i][iSommet] != -1 && !alSommetsTraites.contains(integer)) {
				alInteger.add(integer);
			}
		}

		return alInteger;
	}
	
	private void pauseSynchro() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
	}
}