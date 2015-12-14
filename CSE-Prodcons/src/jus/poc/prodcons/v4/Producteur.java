package jus.poc.prodcons.v4;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur {

	/**
	 * Nombre de message le producteur doit produire
	 */
	private int pNbMessage = 0;
	/**
	 * ProdCons utilisé par le Producteur pour partager des messages
	 */
	private ProdCons pProdCons;
	/**
	 * Suite de nombre aléatoire tiré selon une loi Gausienne
	 */
	private Aleatoire pNbExemplaires;

	/**
	 *
	 *
	 * @throws ControlException
	 */

	/**
	 *
	 * @param aObservateur
	 *            Observateur
	 * @param aProdCons
	 *            ProdCons dans lequel le producteur va déposer des messages
	 * @param aMoyenneTempsDeTraitement
	 *            Temps moyen de traitement d'un message.
	 * @param aDeviationTempsDeTraitement
	 *            Déviation du temps moyen de traitement d'un message
	 * @param aMoyenneNbMessages
	 *            Nombre moyen de messages à produire
	 * @param aDeviationNbMessages
	 *            Déviation du nombre moyen de messages à produire
	 * @param aNbExemplaire
	 *            Nombre de fois un message doit être consommé avant d'être
	 *            retiré du buffer
	 * @param aDeviationNbExemplaire
	 *            Déviation du nombre de fois un message doit être consommé
	 *            avant d'être retiré du buffer
	 * @throws ControlException
	 */
	protected Producteur(Observateur aObservateur, ProdCons aProdCons,
			int aMoyenneTempsDeTraitement, int aDeviationTempsDeTraitement,
			int aMoyenneNbMessages, int aDeviationNbMessages, int aNbExemplaire,
			int aDeviationNbExemplaire) throws ControlException {
		super(Acteur.typeProducteur, aObservateur, aMoyenneTempsDeTraitement,
				aDeviationTempsDeTraitement);

		this.pNbMessage = Aleatoire.valeur(aMoyenneNbMessages,
				aDeviationNbMessages);
		this.pNbExemplaires = new Aleatoire(aNbExemplaire,
				aDeviationNbExemplaire);
		this.pProdCons = aProdCons;
	}

	@Override
	public int nombreDeMessages() {
		return this.pNbMessage;
	}

	/**
	 * Execution d'un Thread Producteur. Un consommateur va produire
	 * `pNbMessage` messages dans ProdCons après avoir effectuer un traitement
	 * sur ces messages.
	 *
	 * Le traitement d'un message est simulé par un appel à Thread.Wait. Les
	 * temps de traitements sont générés aléatoirement selon une loi uniforme de
	 * paramètres `moyenneTempsDeTraitement` et `deviationTempsDeTraitement
	 *
	 * Chaque mesage est conservé dans le buffer de ProdCons un nombre de fois
	 * généré aléatoire selon une loi gaussienne de paramètres
	 * aNbExemplaireaNbExemplaire` et aDeviationTempsDeTraitement`, deux
	 * paramètres passé en paramètres au constructeur de cette classe.
	 */
	@Override
	public void run() {
		int wI = 1;
		int wAlea;
		MessageX wMessage = null;
		while (nombreDeMessages() > 0) {
			/* Création d'un messageX */
			wMessage = new MessageX(this, wI, this.pNbExemplaires.next());

			/* Calcul du temps de traitement */
			wAlea = Aleatoire.valeur(moyenneTempsDeTraitement(),
					deviationTempsDeTraitement());

			/* Appel à observateur */
			try {
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
