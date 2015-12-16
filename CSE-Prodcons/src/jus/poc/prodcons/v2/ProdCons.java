package jus.poc.prodcons.v2;

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
	 * Semaphore des Producteurs
	 */
	private Semaphore prod = null;
	/**
	 * Semaphore des Consommateurs
	 */
	private Semaphore cons = null;

	/**
	 *
	 * @param taille
	 *            Taille du buffer utilisé pour stocker des Messages
	 */
	public ProdCons(int aTaille) {
		this.buffer = new Message[aTaille];
		this.prod = new Semaphore(aTaille);
		this.cons = new Semaphore(0);
	}

	@Override
	public synchronized int enAttente() {
		return nbPlein;
	}

	@Override
	public Message get(_Consommateur aConsommateur)
			throws InterruptedException, ControlException {
		this.cons.attendre();
		Message wMessage;
		synchronized (this) {
			wMessage = buffer[out];
			out = (out + 1) % taille();
			nbPlein--;

			/* Impression d'un message dans le log */
			SimpleLogger.out.logInfo(this, "<Consommation>",
					"%s consommé par Consommateur %d ", wMessage,
					aConsommateur.identification());
		}
		this.prod.reveiller();
		return wMessage;
	}

	@Override
	public void put(_Producteur aProducteur, Message aMessage)
			throws InterruptedException, ControlException {
		this.prod.attendre();
		synchronized (this) {
			buffer[in] = aMessage;
			in = (in + 1) % taille();
			nbPlein++;

			/* Impression d'un message dans le log */
			SimpleLogger.out.logInfo(this, "<Production>",
					"%s produit par Producteur %d ", aMessage,
					aProducteur.identification());
		}
		this.cons.reveiller();
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}
