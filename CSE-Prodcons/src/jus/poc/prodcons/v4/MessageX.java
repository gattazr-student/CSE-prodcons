package jus.poc.prodcons.v4;

import jus.poc.prodcons.Message;
import jus.poc.prodcons._Producteur;

/**
 * MessageX
 *
 */
public class MessageX implements Message {

	/**
	 * Producteur du message
	 */
	private _Producteur pProducteur;
	/**
	 * Identifiant du message
	 */
	private int pNumeroMessage;
	/**
	 * Nombre d'exemplaires du message
	 */
	private int pNbExemplaires;

	/**
	 *
	 * @param aProducteur
	 *            Producteur du message
	 * @param aNumeroMessage
	 *            Identifiant du message
	 * @param aNbExemplaires
	 *            Nombre d'exemplaires du message
	 */
	public MessageX(_Producteur aProducteur, int aNumeroMessage,
			int aNbExemplaires) {
		this.pProducteur = aProducteur;
		this.pNumeroMessage = aNumeroMessage;
		this.pNbExemplaires = aNbExemplaires;
	}

	/**
	 * Retourne le nombre d'exmplaires du Message
	 *
	 * @return int
	 */
	public int getNbExemplaires() {
		return this.pNbExemplaires;
	}

	/**
	 * Retourne l'identifiant du Producteur du message
	 *
	 * @return int
	 */
	public int getProducteurId() {
		return this.pProducteur.identification();
	}

	@Override
	public String toString() {
		int wProducteurId = pProducteur.identification();
		return String.format("[Producteur %d : Message %d-%d]", wProducteurId,
				wProducteurId, this.pNumeroMessage);
	}

}
