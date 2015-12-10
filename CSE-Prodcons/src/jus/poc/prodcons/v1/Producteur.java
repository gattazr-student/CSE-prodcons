package jus.poc.prodcons.v1;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur {

	private int pNbMessage = 0;
	private ProdCons pProdCons;

	protected Producteur(Observateur aObservateur, ProdCons aProdCons,
			int aMoyenneTempsDeTraitement, int aDeviationTempsDeTraitement,
			int aMoyenneNbMessages, int aDeviationNbMessages)
					throws ControlException {
		super(Acteur.typeProducteur, aObservateur, aMoyenneTempsDeTraitement,
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
		int wI = 1;
		int wAlea;
		Message wMessage = null;
		while (nombreDeMessages() > 0) {
			/* Attente active */
			wAlea = Aleatoire.valeur(moyenneTempsDeTraitement(),
					deviationTempsDeTraitement());
			try {
				Thread.sleep(wAlea);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			/* Création d'un message */
			wMessage = new MessageX(this.identification(),
					String.format("Je suis le message %d", wI));

			/* Dépose le message */
			try {
				this.pProdCons.put(this, wMessage);
				/* Impression d'un message de log */
				/* TODO: Change this to use a real logger */
				System.out.println(wMessage);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			this.pNbMessage--;
			wI++;
		}
	}

}
