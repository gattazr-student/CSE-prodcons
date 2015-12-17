CSE-prodcons
=============

- Quentin DUNAND
- Rémi GATTAZ

Projet court du cours Système d'exploitation de RICM4. Ce dépôt contient une solution envisageable au problème des producteurs et des consommateurs.

---
<!--==========================  Producteur Consommateur ==========================-->
## Problème des Producteurs et des Consommateurs
Le problème des Producteurs et des Consommateurs est un problème informatique classique dans la gestion des applications multi-threads. Le principe est le suivant. Il existe un ensemble de producteurs qui produisent des messages, et les placent dans un buffer de taille limité. Dans le même temps, des consommateurs récupèrent ces messages et vident le buffer.

Pour le problème que nous traitons dans ce projet, les conditions suivantes ont été rajoutés :
- Le nombre de message créé par les producteurs est défini lors de leurs initialisation.
- Le nombre de message récupéré par les consommateur n'est pas défini. Tant que des messages sont présents dans le buffer, les consommateurs les récupérerons donc.

Une solution acceptable à ce problème est une solution dans laquelle les conditions suivantes sont respectées.
1. Le buffer est utilisé de façon optimal. C'est à dire que le buffer est utilisé au maximum de sa capacité ou de la capacité des producteurs
2. Le programme ne termine que lorsque la production des messages par les producteurs et terminés et que tous les messages ont été consommés.
3. Un message produit et déposé à la date T dans le buffer sera toujours consommé par une consommateur avant un message déposé à la date T+n.
4. Le consommateur se présentant à la date T pourra récupérer un message avant le consommateur se présentant à la date T+n.


---
<!--==========================  SORTIES ==========================-->
## Sorties
Les sorties du programmes sont toutes gérées par la classe ``SimpleLogger``.

Les paramètres suivants permettent d'activer les sorties du programme.
- ``LOGLEVEL<LEVEL>`` : Niveau de détail de la log. ex: ``LOGLEVELFINE``, ``LOGLEVELINFO`` ...
- ``LOGFILE`` : Les sorties dans un fichier de log sont activées. Les logs se trouvent dans le dossier ``logs/``
- ``LOGCONSOLE`` : Les sorties dans la console sont activées. Le console ne supporte pas les niveaux de logs supérieurs à INFO. Si
un niveau supérieur est spécifié, les sorties de niveaux supérieurs ne seront pas affichées dans la console. Elles se trouveront
tout de même dans le fichier de log si il a été activé.

---
<!--==========================  Terminaison ==========================-->
## Terminaison
La condition de terminaison de ces programmes a été évoqués dans le point **2** des conditions d'acceptabilité d'une solution au problème des producteurs et des consommateurs. Après avoir créé tous les producteurs et consommateurs et démarré tous les threads, nous avons géré la terminaison de nos programmes de la façon suivante :

Le thread "Main" effectue tout d'abord des opérations de jointures (``Thread.join(Thread aThread)``) sur tous les threads Producteurs. Lorsque cette première boucle termine, alors cela implique que la production de tous les producteurs est terminé.

Ensuite, nous attendons que le buffer soit vide. Pour cela, nous utilisons la méthode nbPlein de ProdCons. Tant que cette valeur est supérieure à 0, le buffer n'est pas encore vide. Pour éviter une attente active, tant que nous détectons que le buffer n'est pas vide, nous effectuons un ``Thread.yield()`` pour commuter.

Enfin, le signal ``interrupt`` est envoyé à tous les processus Consommateurs. Ceci va déclencher une exception du type ``InterruptedException`` qui, va emmener le fil d'execution du thread vers la fin de la méthode ``run()``, terminant donc le Thread.


Cette implémentation de la terminaison nous permet d'arreter le programme de façon propre.

---
<!--==========================  Launch app ==========================-->
## Lancer l'application:
Il est énoncé dans le point ``Sorties`` que nous avons mis en place une gestion des paramètres au programmes afin de gérer les sorties. Voici donc un exemple de comment lancer notre programme

```
# java jus.poc.prodcons.vX.TestProdCons LOGLEVELINFO LOGCONSOLE LOGFILE
```

Le fichier XML contenant les paramètres pour un exécution de la version vX de notre simulateur est le fichier xml jus.poc.prodcons.options.options.vX.xml

---
<!--==========================  OBJECTIF 1 ==========================-->
## Objectif 1:
Dans la première version de ce problème, nous avons implémenté une solution "naive" au problème . Utilisant les appels ``wait()`` et ``notifyAll()``, cette solution ne satisfait cependant pas la condition d'admissibilité (4). On observer en effet beaucoup de vol de cycles.

### Résumé des opérations:
- Implémentation naïve du problème Producteur/Consommateur

---
<!--==========================  OBJECTIF 2 ==========================-->
## Objectif 2:
Dans cette seconde version du programme, nous avons créé une classe Sémaphore. Le but premier de cette classe est de pouvoir créer 2 objets distincts dans lequel sont gérer les listes d'attentes des producteurs et des consommateurs. Ainsi, les évènements "Réveiller Consommateur" et "Réveiller Producteur" peuvent se séparer. Au lieu de réveiller tous les processus, induisant des problèmes de vol de cycle, nous avons alors été capable de réveiller les threads un par un et contrôler les date d'accès à nos ressources.

### Résumé des opérations:
- Création de la classe Sémaphore
- Utilisation de deux sémaphores dans ProdCons
	- Sémaphore(0) 'bloquante' à la création pour les Consommateurs
	- Sémaphore(tailleBuffer) 'ouverte' à la création pour les Producteurs. Le résidu dans la Sémaphore représente le nombre libre de blocs restant dans le buffer.

---
<!--==========================  OBJECTIF 3 ==========================-->
## Objectif 3:
Passer de la version de l'objectif 2 à l'objectif 3 a été très simple. En effet, il a simplement fallu détecter où les méthodes de l'objet ``Observateur`` devait être appelé et rajouter ces appels.

### Résumé des opérations:
- Ajout des appels aux méthodes de l'Observateur
- Modification du constructeur de ProdCons (ajout du paramètres Observateur)

---
<!--==========================  OBJECTIF 4 ==========================-->
## Objectif 4:
Pour cet objectif, nous avons rajouté une condition supplémentaire au programme.
- Un producteur est mis en attente tant que le dernier message qu'il a déposé n'a pas été retiré du buffer
- Un message est retiré du buffer lorsqu'il a été consommé N fois. N étant défini à la création du message.

Pour répondre à cet objectif, nous avons donc modifié la classe ``MessageX`` et y avons ajouté un attribut ``nbExemplaire``. Lors du dépôt d'un message, le ``Producteur`` est bloqué sur une sémaphore (stocké dans une ``Map`` donc la clé est l'identifiant du ``Producteur``) et le nombre d'exemplaire du Message est stocké dans une autre Map avec le même ``Identifiant``. La valeur dans cette map est ensuite décrémenté à chaque consommation du message et lorsque cette valeur atteint 0, le message est alors retiré du buffer et le producteur débloqué.


Dans cette version, nous avons utilisé deux objets ``Map``. Ce choix a été fait pour éviter de mettre dans ProdCons des informations qui ne sont pas nécessaire à son fonctionnement. Si nous avions décidé de faire des tableaux, nous aurions alors été obligé de donner au constructeur de ``ProdCons``. Hors cette information n'est pas nécessaire au fonctionnement de ProdCons. Nous avons donc décidé d'opter pour une solution ne nécessitant pas cette information. Nous aurions également pu utiliser des ``Listes`` mais les ``Maps`` semblaient plus adaptés étant donnée que nous avons des identifiants uniques (le numéro d'identification des producteurs et consommateurs).

Pour effectuer cette version, nous avons été obligés de caster les Messages en MessageX dans les fonctions ``get`` et ``put``de ``Prodcons``. Ce n'est pas très joli mais nous pour pouvoir récupérer les informations sur le producteur du message et le nombre d'exemplaire du message nous n'avons pas le choix.

### Résumés des opérations:
- Ajout d'un champ nbExemplaire dans MessageX
	- La fonction MessageX.getNbExemplaires() retourne le nombre d'exemplaires du message.
	- La fonction MessageX.getProducteurId() retourne l'identifiant du Producteur d'un message.
- Ajout d'une Map<Integer, Semaphore> dans Prodcons. Cette map stocke pour chaque producteur la sémaphore qui lui est associé lorsqu'il est en attente de la consommation complète de son Message. Nous utilisons une map car cela nous permet de ne pas avoir connaissance du nombre de producteur. La clé de cette Map est l'identifiant du Producteur du message.

---
<!--==========================  OBJECTIF 5 ==========================-->
## Objectif 5:
Cette version a été effectué sur la base de l'objectif 3. Le but était de remplacer les Sémaphores issues de la classe que nous avons définis par des objets provenant de la bibliothèque java.util.concurrent.

Nous avons donc supprimé la classe Sémaphore et utilisé à la place des objets Lock et Condition.

### Résumé des opérations:
- Suppression de la classe Sémaphore
- Utilisation d'un object Lock (ReentrantLock) pour empécher la modification concurrente du buffer dans ProdCons
- Utilisation d'objets Condition pour bloquer les Producteurs ou Consommateurs si le buffer dans ProdCons est plein ou vide

---
<!--==========================  OBJECTIF 6 ==========================-->
## Objectif 6:
Comme pour la version de l'objectif 5, cette version a été effectué sur la base de l'objectif 3. Le but était ici de remplacer la classe Obervateur fournie par notre propre version afin de nous assurer que certaines propriétés du protocole pour l’objectif n°3 soient toujours respectées.

Pour ce faire, une nouvelle classe ObservateurCtrl à été créée. Toutes les méthodes que l'on pouvait trouver dans la classe Observateur originale ont donc été recréée afin de répondre à notre besoin.

Il semble évident que les méthodes de prodcons ne doivent jamais être exécutées en parrallèle. Nous avons donc tout d'abord pensé rendre toutes les méthodes de cet objet synchronised. Cepepdant, plusieurs appels à ces méthodes sont déja dans des blocs de ce type. Pour ne pas imbriquer de blocs synchronised, nous avons donc utilisé un objet de type Lock.

### Résumé des opérations:
- Création de la classe ObservateurCtrl.
- Utilisation de la classe ObservateurCtrl dans le reste du programme afin de remplacer la classe Observateur existante.


---
<!--==========================  Problèmes ==========================-->
## Problèmes

### Sémaphore non bloquante
Nous avons un problème lors de l'éxécution des solutions v2, v3 et v6 que nous n'avons pas su régler. En effet, pour une raison que nous ne comprenons pas, la valeur résidu dans le sémaphore devient incohérente est le nombre de consommateurs et rédéacteurs bloqués devient alors incorrect. Nous pouvons donc avoir plusieurs fois la consommation d'un même message ou la production d'un message dans le buffer qui remplace un message non consommé.

#### Résolution
Ce problème a été résolu lorsque nous avons remplacé l'attribut ``int pResidu`` dans la classe ``Semaphore`` par un objet du type ``AtomicInteger``.

Le problème que nous détections provenait du fait que la valeur de résidu dans Sémaphore n'était pas atomique. Malgré l'utilisation de blocs synchronised, il pouvait donc arriver que à une instant T la valeur en question d'un objet soit différente dans deux threads.
