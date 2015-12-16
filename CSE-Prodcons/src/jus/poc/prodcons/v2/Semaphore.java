package jus.poc.prodcons.v2;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implémentation simple de Semaphore
 *
 */
public class Semaphore {

	/**
	 * Résidu
	 */
	private AtomicInteger pResidu;

	/**
	 *
	 * @param aResidu
	 *            résidu
	 */
	public Semaphore(int aResidu) {
		this.pResidu = new AtomicInteger(aResidu);
	}

	/**
	 * Appeler attendre est bloquant si le résidu est à 0. Résidu est décrémenté
	 * lors de la sortie de la fonction.
	 */
	public synchronized void attendre() throws InterruptedException {
		if (this.pResidu.getAndDecrement() <= 0) {
			wait();
		}

	}

	/**
	 * Réveiller incrémente le réside et réveille le premier thread qui s'est
	 * bloqué sur attendre
	 */
	public synchronized void reveiller() {
		this.pResidu.incrementAndGet();
		notify();
	}

}
