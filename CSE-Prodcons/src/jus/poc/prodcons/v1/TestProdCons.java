package jus.poc.prodcons.v1;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;
import jus.poc.prodcons.options.Properties;
import jus.poc.prodcons.utils.SimpleLogger;

public class TestProdCons extends Simulateur {

	private static String LOG_LEVEL = "INFO";
	private static String LOG_FOLDER = "logs/";
	private static boolean LOG_IN_FILE = false;
	private static boolean LOG_IN_CONSOLE = false;

	/**
	 * Entry point
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		new TestProdCons(new Observateur(), args).start();
	}

	/* Arguments the program is started with */
	private String[] pArgs;

	public TestProdCons(Observateur aObservateur, String[] aArgs) {
		super(aObservateur);
		this.pArgs = aArgs;
	}

	public void createLogger() {
		/* Default params for log */
		String wLevel = LOG_LEVEL;
		boolean wLogInConsole = LOG_IN_CONSOLE;
		boolean wLogInFile = LOG_IN_FILE;

		/* Loads params from console args */
		for (int wArgIdx = 0; wArgIdx < this.pArgs.length; wArgIdx++) {
			String wArgValue = this.pArgs[wArgIdx];

			if (SimpleLogger.out.isValidLevel(wArgValue)) {
				wLevel = wArgValue;
			} else if (SimpleLogger.out.isValidFileFlag(wArgValue)) {
				wLogInFile = true;
			} else if (SimpleLogger.out.isValidConsoleFlag(wArgValue)) {
				wLogInConsole = true;
			}
		}
		/* Create the logger */
		try {
			SimpleLogger.out.setLogFolderPath(LOG_FOLDER);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		SimpleLogger.out.open(this.getClass().getSimpleName(), wLevel,
				wLogInConsole, wLogInFile);
	}

	@Override
	protected void run() throws Exception {
		/* Create the logger */
		createLogger();

		/* Read properties in the option file */
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
		SimpleLogger.out.logInfo(this, "run", "%d Producteur(s) à créer",
				wNbProd);
		for (int wI = 0; wI < wNbProd; wI++) {
			SimpleLogger.out.logDebug(this, "run", "Création du producteur %d",
					(wI + 1));
			Producteur wProducteur = new Producteur(this.observateur, wProdCons,
					wTProduction, wDTProduction, wNbMessage, wDNbMessage);
			wProducteurs.add(wProducteur);
			wProducteur.start();
		}

		/* Création des consommateurs */
		SimpleLogger.out.logInfo(this, "run", "%d Consommateur(s) à créer",
				wNbCons);
		for (int wI = 0; wI < wNbCons; wI++) {
			SimpleLogger.out.logDebug(this, "run",
					"Création du consommateur %d", (wI + 1));
			new Consommateur(this.observateur, wProdCons, wTConsommation,
					wDTConsommation, wNbExemplaire, wDNbExemplaire).start();
		}

		for (Producteur wProducteur : wProducteurs) {
			wProducteur.join();
		}
		SimpleLogger.out.logInfo(this, "run", "Production de message terminée");
		/*
		 * Quand cette boucle est terminé, tous les producteurs ont terminé
		 * leurs traitements
		 */

		do {
			Thread.sleep(500);// Sorry :-)
		} while (wProdCons.enAttente() > 0);
		/* Quand cette boucle est terminé, il n'y a plus de messages a lire */

		SimpleLogger.out.logInfo(this, "run",
				"Lecture de tous les messages terminés");
		System.exit(0);

	}

}
