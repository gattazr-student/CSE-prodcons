package jus.poc.prodcons.v2;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

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
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			/* Calcul du temps de traitement */
			wAlea = Aleatoire.valeur(moyenneTempsDeTraitement(),
					deviationTempsDeTraitement());

			/* Attente active pour simuler un traitement */
			try {
				Thread.sleep(wAlea);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.pNbMessage--;
		}
	}
}
