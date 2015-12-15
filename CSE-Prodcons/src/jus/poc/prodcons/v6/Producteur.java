package jus.poc.prodcons.v6;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur {

	private int pNbMessage = 0;
	private ProdCons pProdCons;
	private ObservateurCtrl obsCtrl;

	protected Producteur(ObservateurCtrl aObsCtrl, Observateur aObservateur,
			ProdCons aProdCons, int aMoyenneTempsDeTraitement,
			int aDeviationTempsDeTraitement, int aMoyenneNbMessages,
			int aDeviationNbMessages) throws ControlException {
		super(Acteur.typeProducteur, aObservateur, aMoyenneTempsDeTraitement,
				aDeviationTempsDeTraitement);

		this.pNbMessage = Aleatoire.valeur(aMoyenneNbMessages,
				aDeviationNbMessages);
		this.pProdCons = aProdCons;
		this.obsCtrl = aObsCtrl;
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
			/* Création d'un messageX */
			wMessage = new MessageX(this.identification(), wI);

			/* Calcul du temps de traitement */
			wAlea = Aleatoire.valeur(moyenneTempsDeTraitement(),
					deviationTempsDeTraitement());

			/* Appel à observateur */
			try {
				this.obsCtrl.productionMessage(this, wMessage, wAlea);
				this.observateur.productionMessage(this, wMessage, wAlea);
			} catch (ControlException e1) {
				e1.printStackTrace();
			}

			/* Attente active pour simuler un traitement */
			try {
				Thread.sleep(wAlea);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			/* Dépose le message */
			try {
				this.pProdCons.put(this, wMessage);
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
