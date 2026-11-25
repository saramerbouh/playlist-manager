# Playlist Manager

API REST Spring Boot 3.5 / Java 17+ : playlists, shuffle, recommandations, export, lecteur global.

## Lancer

    mvn spring-boot:run

Serveur sur http://localhost:8080. 12 chansons de démo sont chargées au démarrage (base H2 en mémoire).
Les requêtes d'exemple sont dans `requests.http` (IntelliJ ou VS Code REST Client).

## Swagger documentation

The interactive Swagger UI is available at http://localhost:8080/swagger-ui.html.
The OpenAPI JSON specification is available at http://localhost:8080/v3/api-docs.

## Structure

- `model` : entités JPA `Song` et `Playlist`
- `repository` : accès base de données (Spring Data)
- `service` : logique métier (`PlaylistService`, `RecommendationService`, `ShuffleService`, `ExportService`, `MusicPlayer`, `PlaylistWorkflowService`)
- `shuffle` : une classe par algorithme (Strategy)
- `export` : une classe par format (Strategy)
- `controller` : endpoints REST
- `config` : chargement des données de démo

## Endpoints

    GET/POST        /songs
    GET/POST        /playlists
    GET/PUT/DELETE  /playlists/{id}
    POST/DELETE     /playlists/{id}/songs/{songId}
    PUT             /playlists/{id}/songs/order        body: [3,1,2]
    GET             /playlists/{id}/shuffle?strategy=random|smart|genre
    GET             /playlists/{id}/recommendations?limit=5
    GET             /playlists/{id}/export?format=json|m3u
    POST            /playlists/quick                   create + recommend + play
    GET             /player
    POST            /player/play/{playlistId} | pause | resume | next | stop
