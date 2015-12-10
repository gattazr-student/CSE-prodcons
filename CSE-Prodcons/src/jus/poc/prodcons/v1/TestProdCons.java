package jus.poc.prodcons.v1;

import java.util.LinkedList;
import java.util.List;

import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;
import jus.poc.prodcons.options.Properties;

public class TestProdCons extends Simulateur {

	public TestProdCons(Observateur aObservateur) {
		super(aObservateur);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void run() throws Exception {
		Properties wProperties = new Properties(
				"jus/poc/prodcons/options/options.v1.xml");
		int wTProduction = wProperties.getInt("tempsMoyenProduction");
		int wDTProduction = wProperties.getInt("deviationTempsMoyenProduction");
		int wNbMessage = wProperties.getInt("nombreMoyenDeProduction");
		int wDNbMessage = wProperties
				.getInt("deviationNombreMoyenDeProduction");
		int wTConsommation = wProperties.getInt("tempsMoyenConsommation");
		int wDTConsommation = wProperties
				.getInt("deviationTempsMoyenConsommation");
		int wNbExemplaire = wProperties.getInt("nombreMoyenNbExemplaire");;
		int wDNbExemplaire = wProperties
				.getInt("deviationNombreMoyenNbExemplaire");
		int wTailleBuffer = wProperties.getInt("nbBuffer");
		int wNbProd = wProperties.getInt("nbProd");
		int wNbCons = wProperties.getInt("nbCons");

		ProdCons wProdCons = new ProdCons(wTailleBuffer);
		List<Producteur> wProducteurs = new LinkedList<Producteur>();

		/* Création des producteurs */
		/* TODO: use a real logger */
		System.out.println("Producteur à creer : " + wNbProd);
		for (int wI = 0; wI < wNbProd; wI++) {
			/* TODO: use a real logger */
			System.out.println("Creation producteur " + (wI + 1));
			Producteur wProducteur = new Producteur(this.observateur, wProdCons,
					wTProduction, wDTProduction, wNbMessage, wDNbMessage);
			wProducteurs.add(wProducteur);
			wProducteur.start();
		}

		/* Création des consommateurs */
		/* TODO: use a real logger */
		System.out.println("Consommateur à creer : " + wNbCons);
		for (int wI = 0; wI < wNbCons; wI++) {
			/* TODO: use a real logger */
			System.out.println("Creation consommateur " + (wI + 1));
			new Consommateur(this.observateur, wProdCons, wTConsommation,
					wDTConsommation, wNbExemplaire, wDNbExemplaire).start();
		}

		for (Producteur wProducteur : wProducteurs) {
			wProducteur.join();
		}
		/* TODO: use a real logger */
		System.out.println("Production de message terminée");
		/*
		 * Quand cette boucle est terminé, tous les producteurs ont terminé
		 * leurs traitements
		 */

		do {
			Thread.sleep(500);// Sorry :-)
		} while (wProdCons.enAttente() > 0);
		/* Quand cette boucle est terminé, il n'y a plus de messages a lire */
		/* TODO: use a real logger */
		System.out.println("Consommation de message terminée");
		System.exit(0);

	}

	/**
	 * Entry point
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new TestProdCons(new Observateur()).start();
	}

}
