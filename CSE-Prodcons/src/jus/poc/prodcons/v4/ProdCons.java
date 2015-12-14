package jus.poc.prodcons.v4;

import java.util.HashMap;
import java.util.Map;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.utils.SimpleLogger;
import jus.poc.prodcons.v2.Semaphore;

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
	 * Semaphores bloquant les producteurs pendant que leurs message est consomé
	 */
	private Map<Integer, Semaphore> prodAttente = null;
	/**
	 * Stocke le nombre de fois le message produit par le producteur dont
	 * l'identifiant est la clé de la map doit encore être consommé
	 */
	private Map<Integer, Integer> messageRestants = null;

	/**
	 * Semaphore des Producteurs
	 */
	private Semaphore prod = null;
	/**
	 * Semaphore des Consommateurs
	 */
	private Semaphore cons = null;

	/**
	 *
	 * @param taille
	 *            Taille du buffer utilisé pour stocker des Messages
	 */
	public ProdCons(Observateur aObservateur, int aTaille) {
		this.pObservateur = aObservateur;
		this.buffer = new Message[aTaille];
		this.prod = new Semaphore(aTaille);
		this.cons = new Semaphore(0);

		/*
		 * Utilisation d'une map pour ne pas empecher l'apparition éventuelle de
		 * nouveau producteurs dans une autre version
		 */
		prodAttente = new HashMap<Integer, Semaphore>();
		messageRestants = new HashMap<Integer, Integer>();
	}

	@Override
	public synchronized int enAttente() {
		return nbPlein;
	}

	@Override
	public Message get(_Consommateur aConsommateur)
			throws Exception, InterruptedException {
		this.cons.attendre();

		MessageX wMessageX;
		boolean wReveilleConsommateur;

		synchronized (this) {
			wMessageX = (MessageX) buffer[out];

			/* Décrémentation du nombre d'exemplaires restant du message */
			int wRestants = this.messageRestants
					.get(wMessageX.getProducteurId());
			this.messageRestants.put(wMessageX.getProducteurId(), --wRestants);

			/*
			 * Retire le message du buffer et reveille les producteurs si aucun
			 * exemplaires restants
			 */
			if (wRestants == 0) {
				out = (out + 1) % taille();
				nbPlein--;
				wReveilleConsommateur = false;
			} else {
				/* Réveille un consommateur sinon */
				wReveilleConsommateur = true;
			}

			/* Impression d'un message dans le log */
			SimpleLogger.out.logInfo(this, "<Consommation>",
					"%s (%d restants) consommé par Consommateur %d ", wMessageX,
					wRestants, aConsommateur.identification());
			/* Appel à observateur */
			pObservateur.retraitMessage(aConsommateur, wMessageX);
		}

		if (wReveilleConsommateur) {
			/* Réveille le prochain consommateur */
			this.cons.reveiller();
		} else {
			/*
			 * Réveille le producteur en attente ainsi que le prochain
			 * producteur
			 */
			/*
			 * Ce premier appel pourrait poser problème car prodAttente est
			 * manipulé par put. Aucune problème n'a cependant été détecté.
			 */
			prodAttente.get(wMessageX.getProducteurId()).reveiller();
			this.prod.reveiller();
		}
		return wMessageX;
	}

	@Override
	public void put(_Producteur aProducteur, Message aMessage)
			throws Exception, InterruptedException {
		this.prod.attendre();

		MessageX wMessageX = ((MessageX) aMessage);

		synchronized (this) {
			buffer[in] = aMessage;
			in = (in + 1) % taille();
			nbPlein++;

			/* Récupère le nombre d'exemplaire de ce message */
			int wNbExemplaires = wMessageX.getNbExemplaires();
			/* Conserver le nombre d'exemplaire restant à distribuer */
			this.messageRestants.put(aProducteur.identification(),
					wNbExemplaires);

			/* Impression d'un message dans le log */
			SimpleLogger.out.logInfo(this, "<Production>",
					"%s (%d restants) produit par Producteur %d ", wMessageX,
					wNbExemplaires, aProducteur.identification());

			/* Appel à observateur */
			pObservateur.depotMessage(aProducteur, aMessage);
		}
		this.cons.reveiller();
		/* Création de la Sémaphore permettant l'attente de la fin de conso */
		Semaphore wSemaphore = new Semaphore(0);
		prodAttente.put(wMessageX.getProducteurId(), wSemaphore);
		wSemaphore.attendre();
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}
