package jus.poc.prodcons.vX;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	int			in		= 0;
	int			out		= 0;
	int			nbPlein	= 0;

	Message[]	buffer	= new Message[taille()];

	/**
	 * 
	 * @param taille
	 *            La taille de notre buffer
	 */
	public ProdCons(int taille) {
		buffer = new Message[taille];
	}

	/**
	 * Nombre de messages present dans le buffer ??
	 */
	@Override
	public int enAttente() {
		return nbPlein;
	}

	@Override
	public synchronized Message get(_Consommateur aConsommateur) throws Exception, InterruptedException {
		while (nbPlein <= 0) {
			wait();
		}

		Message msg = buffer[out];
		out += 1 % taille();
		nbPlein--;
		notifyAll();
		return msg;

	}

	@Override
	public synchronized void put(_Producteur aProducteur, Message aMessage) throws Exception, InterruptedException {
		while (nbPlein >= taille()) {
			wait();
		}
		buffer[in] = aMessage;
		in = (in + 1) % taille();
		nbPlein++;
		notifyAll();
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}