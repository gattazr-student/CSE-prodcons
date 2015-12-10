package jus.poc.prodcons.v1;

import jus.poc.prodcons.Message;

public class MessageX implements Message {

	int pEmmeteurId;
	String pContenu;

	public MessageX(int aCreateur, String aContenu) {
		this.pEmmeteurId = aCreateur;
		this.pContenu = aContenu;
	}

	@Override
	public String toString() {
		return String.format("Message cree par le Producteur %d : %s",
				this.pEmmeteurId, this.pContenu);
	}

}
