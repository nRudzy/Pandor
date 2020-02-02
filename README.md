# Projet Transversal Master 2019

[![pipeline status](https://forge.univ-lyon1.fr/p1408459/projet-transversal-master-2019/badges/master/pipeline.svg)](https://forge.univ-lyon1.fr/p1408459/projet-transversal-master-2019/commits/master)

**Lors de la réalisation des rendus intermédiaires, nous n’avions pas vu qu’il fallait faire une
page sur le Wiki avec un lien vers les fichiers, ces derniers était dans des dossiers dans notre
répertoire. Nous nous excusons de ne pas avoir respecté les consignes et vous prions de prendre
en considération notre travail. Ils sont désormais bien référencés dans la page "Fichiers" de
notre Wiki, elle même référencé en haut de la page d’accueil.**

## Création d'une branche de travail
* Aller dans l'onglet "Repository > Branches", cliquer sur "New Branch" puis créer sa branche
en la nommant sous la forme "quelquechose" ou "quelquechose-quelquechose". Pas d'underscore
et de majuscules en début de phrase.

* Travailler sur sa branche autant que nécessaire pour traiter une issue,
 commit et push compris.

* Une fois l'issue traitée dans la branche, créer une "Merge request" ou "Demande de fusion"
et l'associer à la branche et à l'issue(s) concernée(s).

###### La demande de fusion sera validée ou non par le "maintener", chargé du code review.

## Installation et utilisation

Le projet est hebergé à l'adresse suivante : [http://192.168.74.221:8080/Pandor/](http://192.168.74.221:8080/Pandor/)

Un compte admin est disponible avec le compte : Administrateur Administrateur

**Attention** Il arrive que la BD reset la connexion (c'est arrivé une fois, en une semaine, puis plus jamais) pour relancer le site il faut :
* Aller à l'adresse : [http://192.168.74.221:8080](http://192.168.74.221:8080)
* Accéder à l'App mangager avec le compte : etudiant etudiant
* Arrêter et redémarrer l'app Pandor

#### Cloner le projet
```bash
$ git clone https://forge.univ-lyon1.fr/p1408459/projet-transversal-master-2019.git
```

#### Lancer l'application

* Après modifications de l'architecture de l'application, 
il faut à présent se placer à la racine du projet et exécuter :

```bash
$ mvn tomcat7:run
```

* L'application sera donc disponible sous l'adresse localhost:9090.

* Si des erreurs apparaissent, bien penser à exécuter la commande ci-dessous pour 
"nettoyer" et appliquer les nouveaux changemements à Maven :

```bash
$ mvn clean install
```


#### Lancer les tests

* Se placer à la racine du projet et exécuter :

```bash
$ mvn test
```

#### Modifications sur pom.xml

* Si le fichier [pom.xml](pom.xml) venait à être modifié, Maven doit tenir compte de ces changements.
Pour cela, il faut exécuter : 

```bash
$ mvn clean install
```

#### Adapter la base de données à votre machine

* Il faut modifier le ficher [persistence.xml](src/main/resources/META-INF/persistence.xml)
et modifier les valeurs suivantes par le nom d'utilisateur créé pour postgres sur votre machine
ainsi que son mot de passe associé.

* Lien de guide [ici](https://www.digitalocean.com/community/tutorials/how-to-install-and-use-postgresql-on-ubuntu-16-04)
pour créer un user dans postgres (suivre les parties création d'utilisateur)
et installation (ubuntu).

```bash
<property name="hibernate.connection.user" value="leNomDeLuserCree" />
<property name="hibernate.connection.password" value="mdpAssocie" />
```

#### Dépendances

- **javax** : javaee-web-api (7.0)
- **joda-time** : joda-time (2.10.5)
- **org.postgresql** : postgresql (42.2.0)
- **org.hibernate** : hibernate-core (4.3.6.Final)
- **org.hibernate** : hibernate-entitymanager (4.3.6.Final)
- **javax.xml.bind** : jaxb-api (2.2.11)
- **com.sun.xml.bind** : jaxb-core (2.3.0.1)
- **com.sun.xml.bind** : jaxb-impl (2.4.0-b180830.0438)
- **javax.activation** : activation (1.1.1)
- **junit** : junit (4.12)
- **javax.servlet** : jstl (1.2)

## Environnements de travail collectifs

#### Trello

* Un Trello est disponible à cette [adresse](https://trello.com/invite/b/TS4ModUn/761980d7775d5cf0af5ee71e253b91a4/projet-transversal).
Outil utile au projet notamment pour créer des user-stories ou pour intéragir avec un SCRUM board.

#### Google Drive

* Penser à nous faire un dossier commun de travail et à le linker ici (pour le draw.io et autres)