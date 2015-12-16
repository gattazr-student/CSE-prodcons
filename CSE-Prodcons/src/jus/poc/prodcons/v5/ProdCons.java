package jus.poc.prodcons.v5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
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
	 * Observateur
	 */
	private Observateur pObservateur;

	/**
	 * Buffer contenant des Messages
	 */
	private Message[] buffer = null;

	/**
	 * Lock permettant de bloquer la lecture ou l'écriture
	 */
	private final Lock pLock = new ReentrantLock();
	/**
	 * Condition bloquant l'écriture lorsque le buffer est plein
	 */
	private final Condition pFull = pLock.newCondition();
	/**
	 * Condition bloquant l'écriture lorsque le buffer est plein
	 */
	private final Condition pEmpty = pLock.newCondition();

	/**
	 *
	 * @param taille
	 *            Taille du buffer utilisé pour stocker des Messages
	 */
	public ProdCons(Observateur aObservateur, int aTaille) {
		this.pObservateur = aObservateur;
		this.buffer = new Message[aTaille];
	}

	@Override
	public synchronized int enAttente() {
		return nbPlein;
	}

	@Override
	public Message get(_Consommateur aConsommateur)
			throws InterruptedException, ControlException {
		this.pLock.lock();

		try {
			Message wMessage;

			/* Stop si il n'y a pas de message */
			while (nbPlein <= 0) {
				this.pEmpty.await();
			}

			wMessage = buffer[out];
			out = (out + 1) % taille();
			nbPlein--;

			/* Impression d'un message dans le log */
			SimpleLogger.out.logInfo(this, "<Consommation>",
					"%s consommé par Consommateur %d ", wMessage,
					aConsommateur.identification());
			/* Appel à observateur */
			pObservateur.retraitMessage(aConsommateur, wMessage);

			/* Reveille un producteur */
			this.pFull.signal();
			return wMessage;
		} finally {
			this.pLock.unlock();
		}
	}

	@Override
	public void put(_Producteur aProducteur, Message aMessage)
			throws InterruptedException, ControlException {
		this.pLock.lock();

		try {
			/* Stop si il n'y a pas de place dans le buffer */
			while (nbPlein >= taille()) {
				this.pFull.await();
			}

			buffer[in] = aMessage;
			in = (in + 1) % taille();
			nbPlein++;

			/* Impression d'un message dans le log */
			SimpleLogger.out.logInfo(this, "<Production>",
					"%s produit par Producteur %d ", aMessage,
					aProducteur.identification());

			/* Appel à observateur */
			pObservateur.depotMessage(aProducteur, aMessage);

			/* Reveille un consommateur */
			this.pEmpty.signal();
		} finally {
			this.pLock.unlock();
		}
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}
