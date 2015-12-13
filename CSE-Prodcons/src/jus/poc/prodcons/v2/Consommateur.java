package jus.poc.prodcons.v2;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons.utils.SimpleLogger;

public class Consommateur extends Acteur implements _Consommateur {

	private int pNbMessage = 0;
	private ProdCons pProdCons;

	protected Consommateur(Observateur aObservateur, ProdCons aProdCons,
			int aMoyenneTempsDeTraitement, int aDeviationTempsDeTraitement,
			int aMoyenneNbMessages, int aDeviationNbMessages)
					throws ControlException {
		super(Acteur.typeConsommateur, aObservateur, aMoyenneTempsDeTraitement,
				aDeviationTempsDeTraitement);

		this.pNbMessage = Aleatoire.valeur(aMoyenneNbMessages,
				aDeviationNbMessages);
		this.pProdCons = aProdCons;
	}

	@Override
	public int nombreDeMessages() {
		return this.pNbMessage;
	}

	@Override
	public void run() {
		int wAlea;
		Message wMessage = null;
		while (nombreDeMessages() > 0) {
			/* Récupère un message */
			try {
				wMessage = this.pProdCons.get(this);
				/* Impression d'un message dans le log */
				SimpleLogger.out.logInfo(this, "run",
						"Message consomme par Consommateur %d -> %s",
						identification(), wMessage);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			/* Attente active */
			wAlea = Aleatoire.valeur(moyenneTempsDeTraitement(),
					deviationTempsDeTraitement());
			try {
				Thread.sleep(wAlea);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.pNbMessage--;
		}
	}
}
