# Payroll

## Introduction
Il s'agit d'une application Web qui gère les profils des employés
et des gestionnaires d'une entreprise et leurs salaires.
L'objectif est d'essayer d'apprendre et d'expérimenter avec des différentes fonctionnalités de Java EE.
L'application utilise asynchronous Threading, un système de sécurité pour les RestPoints, Listeners, Loggerss, filters et d'autres fonctionnalités.


## Build
mvn clean package && docker build -t com.pedantic/payroll .

## RUN

docker rm -f payroll || true && docker run -d -p 8080:8080 -p 4848:4848 --name payroll com.pedantic/payroll 
