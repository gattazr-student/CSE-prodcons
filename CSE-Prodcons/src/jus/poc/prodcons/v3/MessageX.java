package jus.poc.prodcons.v3;

import jus.poc.prodcons.Message;

public class MessageX implements Message {

	int pEmmeteurId;
	int pNumeroMessage;

	public MessageX(int aCreateur, int aNumeroMessage) {
		this.pEmmeteurId = aCreateur;
		this.pNumeroMessage = aNumeroMessage;
	}

	@Override
	public String toString() {
		return String.format("[Producteur %d : Message %d-%d]",
				this.pEmmeteurId, this.pEmmeteurId, this.pNumeroMessage);
	}

}
