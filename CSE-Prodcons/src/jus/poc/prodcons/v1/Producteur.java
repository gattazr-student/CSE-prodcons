package jus.poc.prodcons.v1;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur {

	protected Producteur(int aType, Observateur aObservateur, int aMoyenneTempsDeTraitement,
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
