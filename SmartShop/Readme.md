# SmartShop - Système de Gestion Commerciale B2B

## 📋 Description

SmartShop est une application web backend REST API destinée à **MicroTech Maroc**, distributeur B2B de matériel informatique basé à Casablanca. L'application permet de gérer un portefeuille de 650 clients actifs avec un système de fidélité à remises progressives et des paiements fractionnés multi-moyens.

## 🎯 Objectifs du Projet

- Gestion complète des clients et produits
- Système de fidélité automatique avec remises progressives
- Gestion des commandes multi-produits
- Système de paiements fractionnés multi-moyens (Espèces, Chèque, Virement)
- Traçabilité complète des transactions financières
- Authentification par session HTTP (ADMIN/CLIENT)

## 🛠️ Technologies Utilisées

- **Framework**: Spring Boot
- **Langage**: Java 8+
- **Base de données**: PostgreSQL / MySQL
- **ORM**: Spring Data JPA / Hibernate
- **Validation**: Bean Validation (Hibernate Validator)
- **Mapping**: MapStruct
- **Simplification du code**: Lombok
- **Tests**: JUnit, Mockito
- **Documentation API**: Swagger / Postman
- **Gestion de projet**: JIRA

## 📁 Architecture du Projet

```
src/main/java/com/microtech/smartshop/
├── controller/          # Couche contrôleur (REST endpoints)
├── service/            # Couche service (logique métier)
│   └── impl/          # Implémentations des services
├── repository/         # Couche d'accès aux données (JPA)
├── entity/            # Entités JPA
├── dto/               # Data Transfer Objects
├── mapper/            # MapStruct mappers
├── enums/             # Énumérations (UserRole, OrderStatus, etc.)
├── exception/         # Exceptions personnalisées
├── config/            # Configuration Spring
```

## 🗄️ Modèle de Données

### Entités Principales

- **User**: Gestion des utilisateurs (ADMIN/CLIENT)
- **Client**: Informations clients avec niveau de fidélité
- **Product**: Catalogue produits avec gestion du stock
- **Order**: Commandes avec calcul automatique des remises et TVA
- **OrderItem**: Lignes de commande (produit + quantité)
- **Payment**: Paiements multi-moyens avec traçabilité

### Énumérations

- `UserRole`: ADMIN, CLIENT
- `CustomerTier`: BASIC, SILVER, GOLD, PLATINUM
- `OrderStatus`: PENDING, CONFIRMED, CANCELED, REJECTED
- `PaymentStatus`: EN_ATTENTE, ENCAISSÉ, REJETÉ

## 🚀 Installation et Configuration

### Prérequis

- Java 17
- Maven 3.6+
- PostgreSQL 12+ 
- Postman ou Swagger pour tester l'API

### Configuration de la Base de Données

1. Créer une base de données:
```sql
CREATE DATABASE smartshop_db;
```

2. Modifier `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/smartshop_db
spring.datasource.username=votre_username
spring.datasource.password=votre_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Lancement de l'Application

```bash
# Cloner le repository
git clone https://github.com/ifital/SmartShop.git
cd smartshop

# Compiler et lancer
mvn clean install
mvn spring-boot:run
```

L'application sera accessible sur: `http://localhost:8080`

## 📊 Fonctionnalités Principales

### 1. Gestion des Clients
- Création, consultation, modification et suppression
- Suivi automatique des statistiques (nombre de commandes, montant cumulé)
- Historique complet des commandes

### 2. Système de Fidélité Automatique

Le niveau de fidélité est calculé automatiquement selon:

| Niveau | Conditions | Remise | Seuil |
|--------|-----------|---------|--------|
| **BASIC** | Par défaut | 0% | - |
| **SILVER** | 3 commandes OU 1,000 DH | 5% | ≥ 500 DH |
| **GOLD** | 10 commandes OU 5,000 DH | 10% | ≥ 800 DH |
| **PLATINUM** | 20 commandes OU 15,000 DH | 15% | ≥ 1,200 DH |

### 3. Gestion des Produits
- CRUD complet avec validation
- Gestion du stock en temps réel
- Soft delete pour les produits liés à des commandes

### 4. Gestion des Commandes
- Création multi-produits avec quantités
- Validation automatique du stock
- Calcul automatique: Sous-total HT → Remises → TVA 20% → Total TTC
- Application des codes promo (format: PROMO-XXXX)
- Gestion des statuts: PENDING → CONFIRMED/CANCELED/REJECTED

### 5. Système de Paiements Multi-Moyens

| Moyen | Limite | Statuts | Informations |
|-------|--------|---------|--------------|
| **ESPÈCES** | 20,000 DH max | Encaissé | Reçu |
| **CHÈQUE** | Illimitée | En attente / Encaissé / Rejeté | Numéro, banque, échéance |
| **VIREMENT** | Illimitée | En attente / Encaissé / Rejeté | Référence, banque |

**Règle importante**: Une commande doit être totalement payée (montant_restant = 0) avant validation par l'ADMIN.

## 🔐 Authentification et Autorisations

### Types d'Utilisateurs

**CLIENT (Entreprise cliente)**
- Consulter son profil et niveau de fidélité
- Voir son historique de commandes
- Consulter le catalogue produits (lecture seule)

**ADMIN (Employé MicroTech)**
- Accès complet à toutes les fonctionnalités
- Gestion CRUD complète (clients, produits, commandes)
- Validation et annulation des commandes
- Enregistrement des paiements

### Endpoints d'Authentification

```http
POST /api/auth/login
POST /api/auth/logout
GET  /api/auth/session
```

## 📡 Endpoints Principaux

### Clients
```http
POST   /api/clients              # Créer un client
GET    /api/clients/{id}         # Consulter un client
PUT    /api/clients/{id}         # Modifier un client
DELETE /api/clients/{id}         # Supprimer un client
GET    /api/clients/{id}/orders  # Historique des commandes
```

### Produits
```http
POST   /api/products             # Ajouter un produit
GET    /api/products             # Liste des produits (avec filtres)
GET    /api/products/{id}        # Consulter un produit
PUT    /api/products/{id}        # Modifier un produit
DELETE /api/products/{id}        # Supprimer un produit
```

### Commandes
```http
POST   /api/orders               # Créer une commande
GET    /api/orders/{id}          # Consulter une commande
PUT    /api/orders/{id}/confirm  # Valider une commande (ADMIN)
PUT    /api/orders/{id}/cancel   # Annuler une commande (ADMIN)
```

### Paiements
```http
POST   /api/payments             # Enregistrer un paiement
GET    /api/payments/order/{id}  # Liste des paiements d'une commande
PUT    /api/payments/{id}/status # Modifier le statut d'un paiement
```

## ⚠️ Règles Métier Critiques

1. **Stock**: La quantité demandée doit être ≤ stock disponible
2. **Arrondis**: Tous les montants à 2 décimales
3. **Codes promo**: Format strict `PROMO-XXXX`, usage unique possible
4. **TVA**: 20% par défaut (paramétrable)
5. **Paiement complet**: Obligatoire avant validation de commande
6. **Espèces**: Limite légale de 20,000 DH par paiement

## 🧪 Tests

```bash
# Exécuter tous les tests
mvn test

# Tests avec couverture
mvn test jacoco:report
```

## 🔍 Gestion des Erreurs

L'application utilise une gestion centralisée des exceptions avec `@ControllerAdvice`:

| Code HTTP | Signification |
|-----------|---------------|
| 400 | Erreur de validation |
| 401 | Non authentifié |
| 403 | Accès refusé |
| 404 | Ressource inexistante |
| 422 | Règle métier violée |
| 500 | Erreur interne |

Chaque réponse d'erreur contient: timestamp, code HTTP, type d'erreur, message et chemin de la requête.


## Diagramme de classe

![SmartShop.svg](../../SmartShop.svg)

---
