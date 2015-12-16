package jus.poc.prodcons.v1;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.utils.SimpleLogger;

public class ProdCons implements Tampon {

	/**
	 * Prochain index d'écriture dans le buffer
	 */
	private int in = 0;
	/**
	 * Prochain index de lecture dans le buffer
	 */
	private int out = 0;
	/**
	 * Nombre de messages actuellement dans le buffer
	 */
	private int nbPlein = 0;

	/**
	 * Buffer contenant des Messages
	 */
	private Message[] buffer = null;

	/**
	 *
	 * @param taille
	 *            Taille du buffer utilisé pour stocker des Messages
	 */
	public ProdCons(int aTaille) {
		buffer = new Message[aTaille];
	}

	@Override
	public synchronized int enAttente() {
		return nbPlein;
	}

	@Override
	public synchronized Message get(_Consommateur aConsommateur)
			throws InterruptedException, ControlException {
		while (nbPlein <= 0) {
			wait();
		}

		Message msg = buffer[out];
		out = (out + 1) % taille();
		nbPlein--;

		/* Impression d'un message dans le log */
		SimpleLogger.out.logInfo(this, "<Consommation>",
				"%s consommé par Consommateur %d ", msg,
				aConsommateur.identification());

		notifyAll();
		return msg;

	}

	@Override
	public synchronized void put(_Producteur aProducteur, Message aMessage)
			throws InterruptedException, ControlException {
		while (nbPlein >= taille()) {
			wait();
		}
		buffer[in] = aMessage;
		in = (in + 1) % taille();
		nbPlein++;

		/* Impression d'un message dans le log */
		SimpleLogger.out.logInfo(this, "<Production>",
				"%s produit par Producteur %d ", aMessage,
				aProducteur.identification());

		notifyAll();
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}
