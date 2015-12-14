package jus.poc.prodcons.v3;

import jus.poc.prodcons.Message;
import jus.poc.prodcons._Producteur;

public class MessageX implements Message {

	private _Producteur pProducteur;
	private int pNumeroMessage;

	public MessageX(_Producteur aProducteur, int aNumeroMessage) {
		this.pProducteur = aProducteur;
		this.pNumeroMessage = aNumeroMessage;
	}

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
