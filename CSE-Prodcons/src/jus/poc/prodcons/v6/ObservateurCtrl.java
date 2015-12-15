package jus.poc.prodcons.v6;

import java.util.HashSet;
import java.util.Hashtable;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ObservateurCtrl {

	int nbProd;
	int nbCons;
	int nbBuff;

	HashSet<_Producteur> producteurs;
	HashSet<_Consommateur> consommateurs;
	HashSet<Message> msgsBuffer;

	Hashtable<_Producteur, Message> msgsNotPut;
	Hashtable<_Consommateur, Message> msgsNotGet;

	public void consommationMessage(_Consommateur c, Message m,
			int tempsDeTraitement) throws ControlException {

		// On test que le consommateur existe bien
		if (!consommateurs.contains(c)) {
			throw new ControlException(c.getClass(), "consommationMessage");
		}

		Message msgTemp = msgsNotGet.remove(c);

		// On vérifie si le message est bien celui retirer avant
		if (msgTemp != m) {
			throw new ControlException(c.getClass(), "consommationMessage");
		}

		// Le consommateur n'as pas consommé de message
		if (msgTemp == null) {
			throw new ControlException(c.getClass(), "consommationMessage");
		}

	}

	public void depotMessage(_Producteur p, Message m) throws ControlException {
		if (!producteurs.contains(p)) {
			throw new ControlException(p.getClass(), "depotMessage");
		}

		Message msgTemp = msgsNotPut.remove(p);

		// On vérifie si le producteur à bien deposer un message
		if (msgTemp != m) {
			throw new ControlException(p.getClass(), "depotMessage");
		}

		// On vérifie si le producteur p à bien produit un message
		if (msgTemp == null) {
			throw new ControlException(p.getClass(), "depotMessage");
		}

		if (msgsBuffer.size() > nbBuff) {
			throw new ControlException(p.getClass(), "depotMessage");
		}

		// On dépose le message dans le buffer
		this.msgsBuffer.add(m);

	}

	public void init(int nbProducteurs, int nbConsommateurs, int nbBuffers)
			throws ControlException {
		this.nbProd = nbProducteurs;
		this.nbCons = nbConsommateurs;
		this.nbBuff = nbBuffers;
		this.producteurs = new HashSet<_Producteur>();
		this.consommateurs = new HashSet<_Consommateur>();
		this.msgsBuffer = new HashSet<Message>();
		this.msgsNotGet = new Hashtable<_Consommateur, Message>();
		this.msgsNotPut = new Hashtable<_Producteur, Message>();
	}

	public void newConsommateur(_Consommateur C) throws ControlException {
		consommateurs.add(C);
		// On verifie si on a pas trop de consommateur
		if (consommateurs.size() > nbCons) {
			throw new ControlException(this.getClass(), "newConsommateur");
		}
	}

	public void newProducteur(_Producteur P) throws ControlException {
		producteurs.add(P);
		// On vérifie si on a pas trop de producteurs
		if (producteurs.size() > nbProd) {
			throw new ControlException(this.getClass(), "newProducteur");
		}
	}

	public void productionMessage(_Producteur p, Message m,
			int tempsDeTraitement) throws ControlException {
		if (!producteurs.contains(p)) {
			throw new ControlException(p.getClass(), "productionMessage");
		}

		// On verifie si le producteur n'as pas deposer le message qui vient
		// d'être produit
		if (msgsNotPut.containsKey(p)) {
			throw new ControlException(p.getClass(), "productionMessage");
		}

		// Une fois produit, on met le message dans la hashmap des message à
		// deposer
		msgsNotPut.put(p, m);
	}

	public void retraitMessage(_Consommateur c, Message m)
			throws ControlException {
		if (!consommateurs.contains(c)) {
			throw new ControlException(c.getClass(), "retraitMessage");
		}

		Boolean msgWasPresent = msgsBuffer.remove(m);

		// On check si le message était bien dans le tampon
		if (!msgWasPresent) {
			throw new ControlException(c.getClass(), "retraitMessage");
		}

		// On verifie si le consommateur à bien consommer le message qu'il
		// retire avant
		if (msgsNotGet.containsKey(c)) {
			throw new ControlException(c.getClass(), "retraitMessage");
		}

		// Une fois retirer, on met le message dans notre hashmap des non
		// consommé
		msgsNotGet.put(c, m);

	}

}
