# ETL-OFF

## Objectif 3 - Optimisation des performances

L'import Open Food Facts est activable a la demande avec :

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--etl.import.enabled=true --spring.main.web-application-type=none"
```

Optimisations mises en place :

- Cache applicatif incremental avec des `Map` pendant l'import pour eviter les recherches totalement redondantes sans precharger toute la base.
- Traitement parallele des lignes CSV avec des Virtual Threads Java 21.
- Persistance progressive des produits afin de conserver un traitement lisible et realiste pour le TP.
- Rapport de performance : temps total, temps CPU du processus, memoire heap et nombre de taches virtual-thread.

Mesure locale avec la version tres optimisee precedente :

- 13 432 lignes lues
- 13 432 produits importes
- 0 ligne rejetee
- parsing : 1 174 ms
- persistance : 17 905 ms
- total applicatif : 19 204 ms
- CPU process : 22 984 ms
- memoire heap : 40 MB -> 595 MB
- taches virtual-thread : 13 432

La version actuelle est volontairement moins agressive : elle garde les Virtual Threads et un cache incremental, mais ne fait plus de resolution globale en masse ni de sauvegarde groupee des produits.

## Objectif 4 - API REST

Endpoints disponibles :

```text
GET /products/top-by-brand?brand=X&limit=N
GET /products/top-by-category?category=X&limit=N
GET /products/top-by-brand-category?brand=X&category=Y&limit=N
GET /ingredients/top?limit=N
GET /allergens/top?limit=N
GET /additives/top?limit=N
```

Les endpoints produits retournent les meilleurs produits selon le score nutritionnel, de `A` vers `F`.
Les endpoints ingredients, allergenes et additifs retournent les valeurs les plus frequentes dans les produits.

Le parametre `limit` est optionnel, vaut `10` par defaut et est borne a `100`.
