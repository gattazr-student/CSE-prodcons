package jus.poc.prodcons.v1;

import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

public class TestProdCons extends Simulateur {

	public TestProdCons(Observateur aObservateur) {
		super(aObservateur);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void run() throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * Entry point
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new TestProdCons(new Observateur()).start();
	}

}
