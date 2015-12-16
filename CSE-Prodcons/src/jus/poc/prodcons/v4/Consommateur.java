package jus.poc.prodcons.v4;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur {

	/**
	 * Nombre de messages lus par le consommateur
	 */
	private int pNbMessage = 0;
	/**
	 * ProdCons utilisé par le Consommateur pour récupérer des messages
	 */
	private ProdCons pProdCons;

	/**
	 *
	 * @param aObservateur
	 *            Observateur
	 * @param aProdCons
	 *            ProdCons dans lequel le consommateur va chercher des messages
	 * @param aMoyenneTempsDeTraitement
	 *            Temps moyen de traitement d'un message.
	 * @param aDeviationTempsDeTraitement
	 *            Déviation du temps moyen de traitement d'un message
	 * @throws ControlException
	 */
	protected Consommateur(Observateur aObservateur, ProdCons aProdCons,
			int aMoyenneTempsDeTraitement, int aDeviationTempsDeTraitement)
					throws ControlException {
		super(Acteur.typeConsommateur, aObservateur, aMoyenneTempsDeTraitement,
				aDeviationTempsDeTraitement);

		this.pProdCons = aProdCons;
	}

	@Override
	public int nombreDeMessages() {
		return this.pNbMessage;
	}

	/**
	 * Execution d'un Thread Consommateur. Un consommateur va récupérer tant
	 * qu'il le peut des messages dans ProdCons et effectuer un traitement sur
	 * ces messages.
	 *
	 * Le traitement d'un message est simulé par un appel à Thread.Wait. Les
	 * temps de traitements sont générés aléatoirement selon une loi uniforme de
	 * paramètres `moyenneTempsDeTraitement` et `deviationTempsDeTraitement
	 */
	@Override
	public void run() {
		try {
			int wAlea;
			Message wMessage = null;
			while (true) {

				/* Récupère un message */
				try {
					wMessage = this.pProdCons.get(this);
				} catch (ControlException e) {
					e.printStackTrace();
				}

				/* Calcul du temps de traitement */
				wAlea = Aleatoire.valeur(moyenneTempsDeTraitement(),
						deviationTempsDeTraitement());

				/* Appel à observateur */
				try {
					this.observateur.consommationMessage(this, wMessage, wAlea);
				} catch (ControlException e1) {
					e1.printStackTrace();
				}

				/* Attente active pour simuler un traitement */
				Thread.sleep(wAlea);

				this.pNbMessage++;
			}

		} catch (InterruptedException e) {
			/* Thread was interrupted to be stopped */
		}
	}
}
