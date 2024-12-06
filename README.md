# Grpc_Android_SpringBoot
# TP : Application Android avec gRPC et Spring Boot

## Description

Ce TP consiste à développer une application complète utilisant **Android** comme client, **Spring Boot** comme serveur backend, et **gRPC** pour la communication entre les deux.  
L'objectif principal est de gérer des comptes bancaires, où le client Android permet à l'utilisateur d'effectuer des actions telles que la création, la récupération et la gestion des comptes via une interface graphique.

---
## Vedio demo
[Vedio demo GRPC](https://github.com/user-attachments/assets/c3cc75d0-d494-40c7-bd44-ae5b6cb7379b
d
)
## Fonctionnalités

### Client Android
- Interface utilisateur moderne basée sur **Material Design**.
- Gestion des comptes :
  - Création d’un compte avec type, solde et date.
  - Consultation de la liste des comptes avec leurs détails.
  - Visualisation de statistiques globales (total, moyenne, nombre de comptes).
- Communication via **gRPC** avec le serveur Spring Boot.

### Serveur Spring Boot
- Implémentation de l’API gRPC définie dans le fichier `.proto`.
- Gestion de la base de données pour stocker les informations des comptes.
- Exposition des méthodes gRPC pour la gestion des comptes :
  - `CreateCompte`
  - `GetComptes`
  - `GetStats`.

---

## Architecture du Projet

### Fichier `.proto`
Le fichier `compte.proto` définit les messages et les services pour gRPC. Voici un exemple des définitions principales :
