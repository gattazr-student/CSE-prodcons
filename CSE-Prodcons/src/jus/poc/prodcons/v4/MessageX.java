package jus.poc.prodcons.v4;

import jus.poc.prodcons.Message;
import jus.poc.prodcons._Producteur;

public class MessageX implements Message {

	private _Producteur pProducteur;
	private int pNumeroMessage;
	private int pNbExemplaires;

	public MessageX(_Producteur aProducteur, int aNumeroMessage,
			int aNbExemplaires) {
		this.pProducteur = aProducteur;
		this.pNumeroMessage = aNumeroMessage;
		this.pNbExemplaires = aNbExemplaires;
	}

	public int getNbExemplaires() {
		return this.pNbExemplaires;
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
