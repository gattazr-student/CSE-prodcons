package jus.poc.prodcons.v6;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ObservateurCtrl {

	private int nbProd;
	private int nbCons;
	private int nbBuff;

	private HashSet<_Producteur> producteurs;
	private HashSet<_Consommateur> consommateurs;

	private Queue<Message> msgQueue;

	private Hashtable<_Producteur, Message> msgCreated;
	private Hashtable<_Consommateur, Message> msgWithdrawn;

	public void consommationMessage(_Consommateur c, Message m,
			int tempsDeTraitement) throws ControlException {
		/* ArgumentsValides */
		if ((c == null) || (m == null) || (tempsDeTraitement <= 0)) {
			throw new ControlException(getClass(), "consommationMessage");
		}

		/* Test de l'existence du consommateur */
		if (consommateurs.contains(c) == false) {
			throw new ControlException(getClass(), "consommationMessage");
		}

		/*
		 * On vérifie qu'un message a été retiré par c et qu'il s'agit du
		 * message m. Le message est retiré de la hashtable si c'est le cas
		 */
		if (msgWithdrawn.remove(c, m) == false) {
			throw new ControlException(getClass(), "consommationMessage");
		}
	}

	public void depotMessage(_Producteur p, Message m) throws ControlException {
		/* ArgumentsValides */
		if ((p == null) || (m == null)) {
			throw new ControlException(getClass(), "depotMessage");
		}

		/* On vérifie si le message m a été produit précemment par p */
		if (msgCreated.remove(p, m) == false) {
			throw new ControlException(getClass(), "depotMessage");
		}

		/* Ajoute le message dans la queue des "messages dans le buffer" */
		try {
			msgQueue.add(m);
		} catch (Exception e) {
			throw new ControlException(getClass(), "depotMessage");
		}

		/*
		 * On vérifie que l'on ne dépasse pas la taille du buffer. Si la
		 * capacité a été dépassé, le catch précédent aurait du retourner une
		 * exception
		 */
		if (msgQueue.size() > nbBuff) {
			throw new ControlException(p.getClass(), "depotMessage");
		}
	}

	public void init(int nbProducteurs, int nbConsommateurs, int nbBuffers)
			throws ControlException {
		/* ArgumentsValides */
		if ((nbProducteurs <= 0) || (nbConsommateurs <= 0)
				|| (nbBuffers <= 0)) {
			throw new ControlException(getClass(), "init");
		}
		this.nbProd = nbProducteurs;
		this.nbCons = nbConsommateurs;
		this.nbBuff = nbBuffers;

		this.producteurs = new HashSet<_Producteur>();
		this.consommateurs = new HashSet<_Consommateur>();
		this.msgQueue = new ArrayBlockingQueue<Message>(nbBuffers);

		this.msgCreated = new Hashtable<_Producteur, Message>();
		this.msgWithdrawn = new Hashtable<_Consommateur, Message>();
	}

	public void newConsommateur(_Consommateur c) throws ControlException {
		/* ArgumentsValides */
		if (c == null) {
			throw new ControlException(getClass(), "newConsommateur");
		}

		consommateurs.add(c);

		/* On verifie qu'il n'y a pas trop de consommateur */
		if (consommateurs.size() > nbCons) {
			throw new ControlException(getClass(), "newConsommateur");
		}
	}

	public void newProducteur(_Producteur p) throws ControlException {
		/* ArgumentsValides */
		if (p == null) {
			throw new ControlException(getClass(), "newProducteur");
		}

		producteurs.add(p);

		/* On vérifie qu'il n'y a pas trop de producteurs */
		if (producteurs.size() > nbProd) {
			throw new ControlException(getClass(), "newProducteur");
		}
	}

	public void productionMessage(_Producteur p, Message m,
			int tempsDeTraitement) throws ControlException {
		/* ArgumentsValides */
		if ((p == null) || (m == null) || (tempsDeTraitement <= 0)) {
			throw new ControlException(getClass(), "productionMessage");
		}

		/* Test de l'existence du producteur */
		if (producteurs.contains(p) == false) {
			throw new ControlException(p.getClass(), "productionMessage");
		}

		/* Ajout du message dans la Table des messages produits */
		this.msgCreated.put(p, m);
	}

	public void retraitMessage(_Consommateur c, Message m)
			throws ControlException {
		/* ArgumentsValides */
		if ((c == null) || (m == null)) {
			throw new ControlException(c.getClass(), "retraitMessage");
		}

		if (!consommateurs.contains(c)) {
			throw new ControlException(c.getClass(), "retraitMessage");
		}

		/* Retire la tête de la queue */
		Message msgTemp = msgQueue.poll();

		/* Ajoute le message retiré dans la table des messages retirés */
		msgWithdrawn.put(c, m);

		/*
		 * On vérifie que le message retiré de notre queue est le même que donné
		 * en paramètre
		 */
		if (msgTemp != m) {
			throw new ControlException(c.getClass(), "retraitMessage");
		}
	}

}
