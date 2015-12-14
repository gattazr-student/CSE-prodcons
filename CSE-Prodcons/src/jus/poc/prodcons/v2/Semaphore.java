package jus.poc.prodcons.v2;

/**
 * Implémentation simple de Semaphore
 *
 */
public class Semaphore {

	/**
	 * Résidu
	 */
	private int pResidu;

	/**
	 *
	 * @param aResidu
	 *            résidu
	 */
	public Semaphore(int aResidu) {
		this.pResidu = aResidu;
	}

	/**
	 * Appeler attendre est bloquant si le résidu est à 0. Résidu est décrémenté
	 * lors de la sortie de la fonction.
	 */
	public synchronized void attendre() {
		if (this.pResidu == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.pResidu--;

	}

	/**
	 * Réveiller incrémente le réside et réveille le premier thread qui s'est
	 * bloqué sur attendre
	 */
	public synchronized void reveiller() {
		this.pResidu++;
		notify();
	}

}
