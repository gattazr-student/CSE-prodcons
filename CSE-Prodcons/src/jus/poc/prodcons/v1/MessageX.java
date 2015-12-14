package jus.poc.prodcons.v1;

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
	 *
	 * @param aProducteur
	 *            Producteur du message
	 * @param aNumeroMessage
	 *            Identifiant du message
	 */
	public MessageX(_Producteur aProducteur, int aNumeroMessage) {
		this.pProducteur = aProducteur;
		this.pNumeroMessage = aNumeroMessage;
	}

	@Override
	public String toString() {
		int wProducteurId = pProducteur.identification();
		return String.format("[Producteur %d : Message %d-%d]", wProducteurId,
				wProducteurId, this.pNumeroMessage);
	}

}
