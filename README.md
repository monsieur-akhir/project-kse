# Auth Service - ASSURE-PLUS

## Démarrage rapide avec Docker

### 1. Build de l'image
```bash
docker build -t assureplus-auth-service .
```

### 2. Lancement du conteneur
```bash
docker run -d -p 8080:8080 --name auth-service \
  -e SPRING_PROFILES_ACTIVE=prod \
  assureplus-auth-service
```

> **Remarque** :
> - Pense à configurer l'accès à la base PostgreSQL et à Redis (variables d'environnement ou `application.yml`).
> - Pour la connexion à une base distante, tu peux ajouter des variables d'environnement `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, etc.

---

## Autres commandes utiles

- Voir les logs :
  ```bash
  docker logs -f auth-service
  ```
- Arrêter le conteneur :
  ```bash
  docker stop auth-service
  ```
- Supprimer le conteneur :
  ```bash
  docker rm auth-service
  ``` 