package jus.poc.prodcons.v3;

public class Semaphore {

	private int pResidu;

	public Semaphore(int aResidu) {
		this.pResidu = aResidu;
	}

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

	public synchronized void reveiller() {
		this.pResidu++;
		notify();
	}

}
