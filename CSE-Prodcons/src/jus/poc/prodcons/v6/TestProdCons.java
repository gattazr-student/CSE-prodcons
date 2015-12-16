package jus.poc.prodcons.v6;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;
import jus.poc.prodcons.options.Properties;
import jus.poc.prodcons.utils.SimpleLogger;

public class TestProdCons extends Simulateur {
	/* Default LOG params */
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
		new TestProdCons(new ObservateurCtrl(), new Observateur(), args)
				.start();
	}

	/**
	 * Arguments the program is started with
	 */
	private String[] pArgs;

	/**
	 * Observateur
	 */
	private ObservateurCtrl pObsCtrl;

	/**
	 *
	 * @param aObservateur
	 *            Observateur
	 * @param aArgs
	 *            Program args
	 */
	public TestProdCons(ObservateurCtrl aObsCtrl, Observateur aObservateur,
			String[] aArgs) {
		super(aObservateur);
		this.pObsCtrl = aObsCtrl;
		this.pArgs = aArgs;
	}

	/**
	 * Creation of a SimpleLogger according to the apps params
	 */
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
				"jus/poc/prodcons/options/options.v6.xml");
		int wTProduction = wProperties.getInt("tempsMoyenProduction");
		int wDTProduction = wProperties.getInt("deviationTempsMoyenProduction");
		int wNbMessage = wProperties.getInt("nombreMoyenDeProduction");
		int wDNbMessage = wProperties
				.getInt("deviationNombreMoyenDeProduction");
		int wTConsommation = wProperties.getInt("tempsMoyenConsommation");
		int wDTConsommation = wProperties
				.getInt("deviationTempsMoyenConsommation");
		int wTailleBuffer = wProperties.getInt("nbBuffer");
		int wNbProd = wProperties.getInt("nbProd");
		int wNbCons = wProperties.getInt("nbCons");

		/* Appel aux Observateurs */
		this.pObsCtrl.init(wNbProd, wNbCons, wTailleBuffer);
		this.observateur.init(wNbProd, wNbCons, wTailleBuffer);

		ProdCons wProdCons = new ProdCons(this.pObsCtrl, this.observateur,
				wTailleBuffer);
		List<Producteur> wProducteurs = new LinkedList<Producteur>();
		List<Consommateur> wConsommateurs = new LinkedList<Consommateur>();

		/* Création des producteurs */
		SimpleLogger.out.logInfo(this, "<Main>", "%d Producteur(s) à créer",
				wNbProd);
		for (int wI = 0; wI < wNbProd; wI++) {
			SimpleLogger.out.logDebug(this, "<Main>",
					"Création du producteur %d", (wI + 1));
			Producteur wProducteur = new Producteur(this.pObsCtrl,
					this.observateur, wProdCons, wTProduction, wDTProduction,
					wNbMessage, wDNbMessage);
			/* Appel aux Observateurs */
			this.pObsCtrl.newProducteur(wProducteur);
			this.observateur.newProducteur(wProducteur);
			wProducteurs.add(wProducteur);
			wProducteur.start();
		}

		/* Création des consommateurs */
		SimpleLogger.out.logInfo(this, "<Main>", "%d Consommateur(s) à créer",
				wNbCons);
		for (int wI = 0; wI < wNbCons; wI++) {
			SimpleLogger.out.logDebug(this, "<Main>",
					"Création du consommateur %d", (wI + 1));
			Consommateur wConsommateur = new Consommateur(this.pObsCtrl,
					this.observateur, wProdCons, wTConsommation,
					wDTConsommation);
			/* Appel aux Observateurs */
			this.pObsCtrl.newConsommateur(wConsommateur);
			this.observateur.newConsommateur(wConsommateur);
			wConsommateurs.add(wConsommateur);
			wConsommateur.start();
		}

		for (Producteur wProducteur : wProducteurs) {
			wProducteur.join();
		}
		SimpleLogger.out.logInfo(this, "<Main>",
				"Production de message terminée");
		/*
		 * Quand cette boucle est terminé, tous les producteurs ont terminé
		 * leurs traitements
		 */

		do {
			Thread.yield();// Sorry :-)
		} while (wProdCons.enAttente() > 0);
		/*
		 * Quand cette boucle est terminé, il n'y a plus de messages a lire
		 */

		SimpleLogger.out.logInfo(this, "<Main>",
				"Lecture de tous les messages terminés");

		/* Interruption de tous les consommateurs */
		for (Consommateur wConsommateur : wConsommateurs) {
			wConsommateur.interrupt();
		}

		if (this.pObsCtrl.coherent()) {
			SimpleLogger.out.logInfo("Main", "<Main>",
					"La simulation est cohérente selon notre ObservateurCtrl");
		} else {
			SimpleLogger.out.logInfo("Main", "<Main>",
					"La simulation est incohérente selon ObservateurCtrl");
		}

		if (this.observateur.coherent()) {
			SimpleLogger.out.logInfo("Main", "<Main>",
					"La simulation est cohérente selon Observateur");
		} else {
			SimpleLogger.out.logInfo("Main", "<Main>",
					"La simulation est incohérente selon Observateur");
		}
	}

}
