# Rapport de projet – ETL Open Food Facts

## 1. Introduction

Ce projet a pour objectif de construire une application Java Spring Boot permettant de lire un fichier CSV Open Food Facts, de transformer les données et de les stocker dans une base de données relationnelle, puis de les exposer via une API REST.

Le travail s’inscrit dans le cadre d’un TP sur les optimisations de performances backend. L’enjeu n’était pas seulement de faire fonctionner l’import, mais aussi de rendre ce traitement plus efficace, plus structuré et plus réaliste d’un point de vue industriel.

---

## 2. Problématique

Le fichier fourni contient un grand volume de données produit. L’import manuel ou naïf de ces lignes serait lent, peu maintenable et difficile à optimiser.

Il fallait donc proposer une solution capable de :

- lire un fichier CSV volumineux,
- transformer les lignes en objets métier,
- éviter les redondances dans les recherches de données,
- améliorer les performances de traitement,
- rendre les données exploitables via une API REST.

---

## 3. Objectifs du projet

Le projet répond à plusieurs objectifs :

1. Implémenter un pipeline ETL simple et robuste.
2. Utiliser Spring Batch pour orchestrer l’import.
3. Optimiser les performances du traitement CSV.
4. Exposer des endpoints REST pour interroger les données.
5. Proposer un modèle de données cohérent avec l’application.

---

## 4. Choix techniques

### 4.1 Spring Boot

Spring Boot a été choisi parce qu’il permet de créer rapidement une application complète, prête à être exécutée, avec une architecture propre et simple à maintenir.

Il offre notamment :

- une configuration simplifiée,
- un support natif des services et contrôleurs,
- une intégration facile avec JPA, Batch et l’API REST.

### 4.2 Spring Batch

Le traitement de données du CSV a été intégré avec Spring Batch pour structurer l’import dans un flux métier clair.

Ce choix est pertinent car Spring Batch est conçu pour :

- orchestrer des traitements de données en étapes,
- gérer des traitements volumineux,
- rendre le processus plus modulaire et plus lisible.

Dans ce projet, l’import est géré comme une tâche unique, déclenchée à l’exécution de l’application selon un flag configuré.

### 4.3 JPA / Hibernate

La persistance des données se fait grâce à Spring Data JPA et Hibernate.

Cela permet de :

- représenter les données sous forme d’entités Java,
- éviter d’écrire du SQL manuel pour les opérations courantes,
- bénéficier d’un modèle objet propre et facile à étendre.

### 4.4 Java 21 et virtual threads

Le projet utilise Java 21, ce qui permet d’exploiter les virtual threads.

Cette approche a été retenue pour paralléliser le traitement des lignes du CSV sans créer un modèle de concurrence trop lourd ni trop complexe.

Le but est de réduire le temps d’import en traitant plusieurs lignes en parallèle pendant la phase de parsing.

### 4.5 Cache applicatif

Un cache a été ajouté pour éviter de refaire plusieurs fois les mêmes recherches de données de référence.

Par exemple, lors de l’import :

- un nom de catégorie peut apparaître de nombreuses fois,
- la même marque, le même ingrédient ou le même additif peut réapparaître dans plusieurs produits.

Sans cache, l’application ferait beaucoup de requêtes répétitives. Avec le cache, on réutilise les objets déjà reconnus, ce qui réduit la charge de travail et améliore les performances.

### 4.6 API REST

Une API REST a été ajoutée pour permettre l’exploitation des données après l’import.

Elle permet de faire des requêtes simples comme :

- retrouver les meilleurs produits selon une marque,
- retrouver les meilleurs produits selon une catégorie,
- obtenir les ingrédients, allergènes ou additifs les plus fréquents.

---

## 5. Architecture du projet

Le projet est organisé autour de plusieurs couches principales :

- controller : expose les endpoints REST,
- service : contient la logique métier,
- repository : gère l’accès aux données,
- entity : représente les objets métier persistés,
- etl : contient la logique d’import et d’optimisation,
- dto : contient les objets de transfert utilisés par l’API.

### Structure logique

- Les entités représentent les objets métier : produit, catégorie, marque, ingrédient, allergène, additif.
- Les services centralisent les opérations de création et de recherche.
- Les repositories sont utilisés pour interagir avec la base de données.
- Les classes d’ETL orchestrent l’import du fichier CSV.

---

## 6. Fonctionnement du projet

### 6.1 Chargement du fichier CSV

L’application lit un fichier CSV nommé Open Food Facts.

Chaque ligne du fichier est ensuite analysée et transformée en un objet métier correspondant à un produit.

### 6.2 Parsing des lignes

Chaque ligne est découpée et interprétée selon les colonnes du fichier.

Les informations extraites peuvent inclure :

- le nom du produit,
- son score nutritionnel,
- ses valeurs énergétiques,
- sa catégorie,
- sa marque,
- les ingrédients,
- les allergènes,
- les additifs.

### 6.3 Recherche ou création des entités liées

Lorsqu’un produit est créé, l’application vérifie si la catégorie, la marque, les ingrédients ou les allergènes existent déjà.

Si ce n’est pas le cas, ils sont créés. Sinon, on réutilise les objets déjà connus.

C’est ici que le cache apporte une valeur importante.

### 6.4 Persistance en lots

Les produits ne sont pas enregistrés un par un de façon totalement naïve. Ils sont regroupés puis enregistrés par lots.

Cela permet de limiter le coût des opérations de base de données et d’améliorer les performances globales.

### 6.5 Exécution du job d’import

L’import peut être déclenché au démarrage de l’application si l’option d’import est activée.

Le job Spring Batch s’exécute alors et traite l’ensemble du fichier CSV.

---

## 7. Pourquoi ce choix d’architecture ?

Ce choix a été fait pour plusieurs raisons :

- il correspond bien à l’objectif du TP : traiter un gros volume de données de manière performante,
- il est réaliste : un vrai pipeline ETL suit souvent cette logique,
- il est maintenable : chaque responsabilité est séparée,
- il est extensible : on peut facilement ajouter de nouvelles transformations ou de nouveaux endpoints.

En résumé, l’architecture ne sert pas uniquement à “faire marcher” le projet. Elle permet d’illustrer une vraie logique de traitement de données backend.

---

## 8. Modèle de données

Le modèle de données repose sur plusieurs entités principales :

- Produit
- Catégorie
- Marque
- Ingrédient
- Allergène
- Additif

Les relations principales sont les suivantes :

- un produit appartient à une catégorie,
- un produit appartient à une marque,
- un produit peut contenir plusieurs ingrédients,
- un produit peut contenir plusieurs allergènes,
- un produit peut contenir plusieurs additifs.

Le modèle est donc organisé autour d’un produit central, lié à plusieurs tables de références.

Une version SQL du modèle a été ajoutée dans le dossier de conception.

---

## 9. API REST disponible

L’application expose plusieurs endpoints permettant d’interroger les données importées.

### Produits

- GET /products/top-by-brand
- GET /products/top-by-category
- GET /products/top-by-brand-category

### Autres données

- GET /ingredients/top
- GET /allergens/top
- GET /additives/top

Ces endpoints retournent des résultats triés selon la fréquence ou le score nutritionnel, selon le type de requête.

---

## 10. Résultats de performance

Le projet a été pensé pour montrer une amélioration concrète des performances.

L’import a été validé sur un volume important de données, avec un traitement complet du fichier CSV.

Les points importants observés sont :

- traitement du CSV de façon structurée,
- utilisation de mécanismes d’optimisation adaptés,
- réduction des coûts liés aux recherches redondantes,
- meilleur comportement sur un volume de données significatif.

---

## 11. Comment lancer le projet

Depuis la racine du projet, il est possible de lancer l’application avec la commande suivante :

```powershell
./mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--etl.import.enabled=true --spring.main.web-application-type=none"
```

Cette commande déclenche l’import CSV et exécute le traitement principal.

Pour exécuter les tests :

```powershell
./mvnw.cmd test
```

---

## 12. Conclusion

Ce projet montre qu’il est possible de construire une application backend complète autour d’un traitement ETL, en combinant plusieurs technologies modernes et pertinentes.

Les choix réalisés sont cohérents avec les objectifs du TP :

- structurer l’import avec Spring Batch,
- améliorer les performances avec du cache et des virtual threads,
- exposer les données via une API REST,
- conserver une architecture claire et compréhensible.

Le résultat obtenu est donc à la fois fonctionnel, pédagogique et cohérent avec un projet de performance backend.
