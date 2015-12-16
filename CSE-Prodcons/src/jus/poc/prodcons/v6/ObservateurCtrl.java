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

	private boolean pCoherent = true;

	private int nbProd;
	private int nbCons;
	private int nbBuff;

	private HashSet<_Producteur> producteurs;
	private HashSet<_Consommateur> consommateurs;

	private Queue<Message> msgQueue;

	private Hashtable<_Producteur, Message> msgCreated;
	private Hashtable<_Consommateur, Message> msgWithdrawn;

	public boolean coherent() {
		return this.pCoherent;
	}

	public void consommationMessage(_Consommateur c, Message m,
			int tempsDeTraitement) throws ControlException {
		String wMethodName = "consommationMessage";

		/* ArgumentsValides */
		if ((c == null) || (m == null) || (tempsDeTraitement <= 0)) {
			throwControlException(wMethodName);
		}

		/* Test de l'existence du consommateur */
		if (consommateurs.contains(c) == false) {
			throwControlException(wMethodName);
		}

		/*
		 * On vérifie qu'un message a été retiré par c et qu'il s'agit du
		 * message m. Le message est retiré de la hashtable si c'est le cas
		 */
		if (msgWithdrawn.remove(c, m) == false) {
			throwControlException(wMethodName);
		}
	}

	public void depotMessage(_Producteur p, Message m) throws ControlException {
		String wMethodName = "depotMessage";
		/* ArgumentsValides */
		if ((p == null) || (m == null)) {
			throwControlException(wMethodName);
		}

		/* On vérifie si le message m a été produit précemment par p */
		if (msgCreated.remove(p, m) == false) {
			throwControlException(wMethodName);
		}

		/* Ajoute le message dans la queue des "messages dans le buffer" */
		try {
			msgQueue.add(m);
		} catch (Exception e) {
			throwControlException(wMethodName);
		}

		/*
		 * On vérifie que l'on ne dépasse pas la taille du buffer. Si la
		 * capacité a été dépassé, le catch précédent aurait du retourner une
		 * exception
		 */
		if (msgQueue.size() > nbBuff) {
			throwControlException(wMethodName);
		}
	}

	public void init(int nbProducteurs, int nbConsommateurs, int nbBuffers)
			throws ControlException {
		String wMethodName = "init";
		/* ArgumentsValides */
		if ((nbProducteurs <= 0) || (nbConsommateurs <= 0)
				|| (nbBuffers <= 0)) {
			throwControlException(wMethodName);
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
		String wMethodName = "newConsommateur";
		/* ArgumentsValides */
		if (c == null) {
			throwControlException(wMethodName);
		}

		consommateurs.add(c);

		/* On verifie qu'il n'y a pas trop de consommateur */
		if (consommateurs.size() > nbCons) {
			throwControlException(wMethodName);
		}
	}

	public void newProducteur(_Producteur p) throws ControlException {
		String wMethodName = "newProducteur";
		/* ArgumentsValides */
		if (p == null) {
			throwControlException(wMethodName);
		}

		producteurs.add(p);

		/* On vérifie qu'il n'y a pas trop de producteurs */
		if (producteurs.size() > nbProd) {
			throwControlException(wMethodName);
		}
	}

	public void productionMessage(_Producteur p, Message m,
			int tempsDeTraitement) throws ControlException {
		String wMethodName = "productionMessage";
		/* ArgumentsValides */
		if ((p == null) || (m == null) || (tempsDeTraitement <= 0)) {
			throwControlException(wMethodName);
		}

		/* Test de l'existence du producteur */
		if (producteurs.contains(p) == false) {
			throwControlException(wMethodName);
		}

		/* Ajout du message dans la Table des messages produits */
		this.msgCreated.put(p, m);
	}

	public void retraitMessage(_Consommateur c, Message m)
			throws ControlException {
		String wMethodName = "retraitMessage";
		/* ArgumentsValides */
		if ((c == null) || (m == null)) {
			throwControlException(wMethodName);
		}

		if (!consommateurs.contains(c)) {
			throwControlException(wMethodName);
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
			throwControlException(wMethodName);
		}
	}

	private void throwControlException(String aMethodName)
			throws ControlException {
		this.pCoherent = false;
		throw new ControlException(getClass(), aMethodName);
	}

}
