package jus.poc.prodcons.v3;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.utils.SimpleLogger;

public class ProdCons implements Tampon {

	private int in = 0;
	private int out = 0;
	private int nbPlein = 0;
	private Observateur pObservateur;

	Message[] buffer = null;
	Semaphore prod = null;
	Semaphore cons = null;

	/**
	 *
	 * @param taille
	 *            La taille de notre buffer
	 */
	public ProdCons(Observateur aObservateur, int aTaille) {
		this.pObservateur = aObservateur;
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

			/* Impression d'un message dans le log */
			SimpleLogger.out.logInfo(this, "<Consommation>",
					"%s consommé par Consommateur %d ", wMessage,
					aConsommateur.identification());
			/* Appel à observateur */
			pObservateur.retraitMessage(aConsommateur, wMessage);
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

			/* Impression d'un message dans le log */
			SimpleLogger.out.logInfo(this, "<Production>",
					"%s produit par Producteur %d ", aMessage,
					aProducteur.identification());

			/* Appel à observateur */
			pObservateur.depotMessage(aProducteur, aMessage);
		}
		this.cons.reveiller();
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}
