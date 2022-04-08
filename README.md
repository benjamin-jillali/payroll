# Payroll

## Introduction
Il s'agit d'une application Web qui gère les profils des employés
et des gestionnaires d'une entreprise et leurs salaires.
L'objectif est d'essayer d'apprendre et d'expérimenter avec des différentes fonctionnalités de Java EE.
L'application utilise asynchronous Threads, un système de sécurité pour les RestPoints, Listeners, Loggers, filters et d'autres fonctionnalités.

## Explication
   Le package         "config" gère les configurations et les règles pour l'application et la configuration du serveur.
Les classe comme DynamicFilter.java est DynamicFilterFeature.java controle des communication avec des filtre.
Les class de JaxRs gérer la configuration de serveur est les class des Secure.java est Security.. 
faire partie de la configuration de securite.
  Le package "entities" contient des la structure des données et les règles de la base de données.
Les différentes tables de la base de données sont créées avec les classes de ce package.
  Le package "resource" contient les RestPoints. Qui gèrent les requêtes du client, filtrent les données 
envoyées ou reçues et récupèrent les requêtes correctes ou envoient des réponses avec un message d'erreur au client.
  Le package "service" contient des services pour différentes parties de l'application. PersistenceService.java 
conserve les données dans la base de données avec des méthodes pour mettre à jour, supprimer ou créer de nouvelles données.
QueryService.java contient différentes requêtes MySQL pour communiquer avec la base de données. 
SecurityUtil.java Implémente des règles et des fonctionnalités pour la création, la vérification et le stockage d'un mot de passe. 
Il crée un mot de passe haché à stocker dans la base de données. Et vérifie si les mot de passe correspond.
  Le package "websocket" contient plusieurs packages. qui crée et gère un petit serveur de chat. 
Qui utilise des asynchronous threads pour communiquer les messages de chat.
  enfin il y a une petite interface client à l'emplacement src/main/webapp.

## Build
mvn clean package && docker build -t com.pedantic/payroll .

## RUN

docker rm -f payroll || true && docker run -d -p 8080:8080 -p 4848:4848 --name payroll com.pedantic/payroll 
