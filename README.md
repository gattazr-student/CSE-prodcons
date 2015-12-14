CSE-prodcons
=============

Projet court du cours Système d'exploitation de RICM4. Ce dépot contient une solution envisageable au problème des producteurs et des consommateurs.


## Sorties
les sorties du programmes sont toutes gérés par la classe SimpleLogger.

les paramètres suivants permettent d'activer les sorties :
- ``LOGLEVEL<LEVEL>`` : Niveau de détail de la log. ex: ``LOGLEVELFINE``, ``LOGLEVELINFO`` ...
- ``LOGFILE`` : Les sorties dans un fichier de log sont activiés. Les logs se trouvent dans le dossier ``logs/``
- ``LOGCONSOLE`` : Les sorties dans la console sont activiés. Le console ne supporte pas les niveau de logs supérieur à INFO. Si
un niveau supérieur est spécifié, les sorties de nieau supérieur ne seront pas affichés dans la console. Elles se trouveront
tout de même dans le fichier de log si il a été activé.

## Objectif 1:
- Implémentation "naïve" du problème Producteur/Consommateur

## Objectif 2:
- Création de la classe Sémaphore
- Utilisation de deux sémaphores dans ProdCons
	- Sémaphore(0) 'bloquante' à la création pour les Consommateurs
	- Sémaphore(tailleBuffer) 'ouverte' à la création pour les Producteurs. Le résidu dans la Sémaphore représente le nombre libre de blocs réstant dans le buffer.

## Objectif 3:
- Ajout des appels aux méthodes de l'Observateur
- Modification du constructeur de ProdCons (ajout du paramètres Observateur)

## Objectif 4:
- Ajout d'un champ nbExemplaire dans MessageX
	- La fonction MessageX.getNbExemplaires() retourne le nombre d'exemplaires du message.
	- La fonction MessageX.getProducteurId() retourne l'identifiant du Producteur d'un message.

> Nous aurions préféré ne pas mettre ajouter de champ nbExemplaires dans le message car elle n'est pas pertinente pour le consommateur.

> Nous somme obligés de caster les Messages en MessageX. Ce n'est pas très joli mais nous pour pouvoir récupérer les informations sur le producteur du message nous n'avons pas le choix.

- Ajout d'une Map<Integer, Semaphore> dans Prodcons. Cette map stocke pour chaque producteur la sémaphore qui lui est associé lorsqu'il est en attente de la consommation complète de son Message. Nous utilisons une map car cela nous permet de ne pas avoir connaissance du nombre de producteur. La clé de cette Map est l'identifiant du Producteur du message.

> Pour que cela fonctionne correctement, nous assurons que deux Producteurs ont un identifiant différent.
