package jus.poc.prodcons.vX;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	@Override
	public int enAttente() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Message get(_Consommateur aConsommateur) throws Exception, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(_Producteur aProducteur, Message aMessage) throws Exception, InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public int taille() {
		// TODO Auto-generated method stub
		return 0;
	}

}
