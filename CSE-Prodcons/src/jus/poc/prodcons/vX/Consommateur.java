package jus.poc.prodcons.vX;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur {

	protected Consommateur(int aType, Observateur aObservateur, int aMoyenneTempsDeTraitement,
			int aDeviationTempsDeTraitement) throws ControlException {
		super(aType, aObservateur, aMoyenneTempsDeTraitement, aDeviationTempsDeTraitement);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int nombreDeMessages() {
		// TODO Auto-generated method stub
		return 0;
	}
}
