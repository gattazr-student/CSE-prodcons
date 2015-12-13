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

## Objectif 3:
- Ajout des appels aux méthodes de l'Observateur
- Modification du constructeur de ProdCons (ajout du paramètres Observateur)
