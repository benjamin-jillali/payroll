# Payroll

- [Introduction](#introduction)
- [Explication](#explication)
- [Technologies](#technologies)
- [Build](#build)
- [Run](#run)

## Introduction
Il s'agit d'une application Web qui gère les profils des employés
et des gestionnaires d'une entreprise et leurs salaires.
L'objectif est d'essayer d'apprendre et d'expérimenter avec des différentes fonctionnalités de Java EE.<br>
L'application utilise asynchronous Threads, un système de sécurité pour les RestPoints, Listeners, Loggers, filters et d'autres fonctionnalités.<br>

## Explication

   Le package "config" gère les configurations et les règles pour l'application et la configuration du serveur.<br> 
Les classe comme DynamicFilter.java est DynamicFilterFeature.java controle des communication avec des filtre.<br>
Les class de JaxRs gérer la configuration de serveur est les class des Secure.java est Security..<br> 
faire partie de la configuration de securite.<br>

  Le package "entities" contient des la structure des données et les règles de la base de données.<br>
Les différentes tables de la base de données sont créées avec les classes de ce package.<br>

  Le package "resource" contient les RestPoints. Qui gèrent les requêtes du client, filtrent les données <br>
envoyées ou reçues et récupèrent les requêtes correctes ou envoient des réponses avec un message d'erreur au client.<br>

  Le package "service" contient des services pour différentes parties de l'application. PersistenceService.java<br> 
conserve les données dans la base de données avec des méthodes pour mettre à jour, supprimer ou créer de nouvelles données.<br>
QueryService.java contient différentes requêtes MySQL pour communiquer avec la base de données. <br>
SecurityUtil.java Implémente des règles et des fonctionnalités pour la création, la vérification et le stockage d'un mot de passe. <br>
Il crée un mot de passe haché à stocker dans la base de données. Et vérifie si les mot de passe correspond.<br>

  Le package "websocket" contient plusieurs packages. qui crée et gère un petit serveur de chat. <br>
Qui utilise des asynchronous threads pour communiquer les messages de chat.<br>
Enfin il y a une petite interface client à l'emplacement src/main/webapp.<br>

## Technologies
+ Payara
+ Glassfish
+ MySQL
+ Javax
+ Maven
+ junit
+ jboss
+ jsonwebtoken
+ Docker

## Build
mvn clean package && docker build -t com.pedantic/payroll .

## Run

docker rm -f payroll || true && docker run -d -p 8080:8080 -p 4848:4848 --name payroll com.pedantic/payroll 
