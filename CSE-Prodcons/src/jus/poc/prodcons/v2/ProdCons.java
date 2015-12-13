package jus.poc.prodcons.v2;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	int in = 0;
	int out = 0;
	int nbPlein = 0;

	Message[] buffer = null;
	Semaphore prod = null;
	Semaphore cons = null;

	/**
	 *
	 * @param taille
	 *            La taille de notre buffer
	 */
	public ProdCons(int aTaille) {
		this.buffer = new Message[aTaille];
		this.prod = new Semaphore(1);
		this.cons = new Semaphore(1);
	}

	@Override
	public synchronized int enAttente() {
		return nbPlein;
	}

	@Override
	public Message get(_Consommateur aConsommateur)
			throws Exception, InterruptedException {
		this.cons.attendre();
		Message wMessage;
		synchronized (this) {
			wMessage = buffer[out];
			out = (out + 1) % taille();
			nbPlein--;
		}
		this.prod.reveiller();
		return wMessage;
	}

	@Override
	public void put(_Producteur aProducteur, Message aMessage)
			throws Exception, InterruptedException {
		this.prod.attendre();
		synchronized (this) {
			buffer[in] = aMessage;
			in = (in + 1) % taille();
			nbPlein++;
		}
		this.cons.reveiller();
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}
