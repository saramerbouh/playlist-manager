# Playlist Manager

Spring Boot 3.5 / Java 17+ REST API for managing songs and playlists, with shuffle, recommendations, export, and a shared music player.

## Run

    mvn spring-boot:run

The server runs at http://localhost:8080. Twelve demo songs are loaded at startup into an in-memory H2 database.

## Swagger documentation

The interactive Swagger UI is available at http://localhost:8080/swagger-ui.html.
The OpenAPI JSON specification is available at http://localhost:8080/v3/api-docs.

## Structure

- `model`: JPA `Song` and `Playlist` entities
- `repository`: database access (Spring Data)
- `service`: business logic (`PlaylistService`, `RecommendationService`, `ShuffleService`, `ExportService`, `MusicPlayer`, `PlaylistWorkflowService`)
- `shuffle`: one class per shuffle algorithm (Strategy pattern)
- `export`: one class per export format (Strategy pattern)
- `controller`: REST endpoints
- `config`: demo data loading and OpenAPI configuration

## Endpoints

All endpoints are relative to `http://localhost:8080`. Use `Content-Type: application/json` when sending a request body. Path parameters `id`, `songId`, and `playlistId` are numeric identifiers.

### Songs

| Method | Path | Description |
|---|---|---|
| GET | `/songs` | List all songs in the catalog. |
| GET | `/songs/{id}` | Get a song by ID. |
| POST | `/songs` | Create a song from a JSON body such as `{"title":"Song","artist":"Artist","genre":"Rock"}`. Returns the new song. |

### Playlists

| Method | Path | Description |
|---|---|---|
| GET | `/playlists` | List all playlists with their songs. |
| GET | `/playlists/{id}` | Get a playlist and its ordered songs. |
| POST | `/playlists` | Create an empty playlist from `{"name":"Road trip"}`. |
| PUT | `/playlists/{id}` | Rename a playlist using a body such as `{"name":"New name"}`. |
| DELETE | `/playlists/{id}` | Delete a playlist. Returns `204 No Content`. |
| POST | `/playlists/{id}/songs/{songId}` | Add an existing song to a playlist; duplicate songs are rejected. |
| DELETE | `/playlists/{id}/songs/{songId}` | Remove a song from a playlist. |
| PUT | `/playlists/{id}/songs/order` | Set the song order using a JSON array containing every song ID exactly once, e.g. `[3,1,2]`. |
| GET | `/playlists/{id}/shuffle?strategy=random` | Return a shuffled song list without changing the saved order. Strategies: `random` (default), `smart`, or `genre`. |
| GET | `/playlists/{id}/recommendations?limit=5` | Recommend up to `limit` songs (default `5`) from genres already in the playlist, excluding songs it already contains. |
| GET | `/playlists/{id}/export?format=json` | Export a playlist as `json` (default) or `m3u`. |
| POST | `/playlists/quick` | Create a playlist, add the supplied songs and recommendations, and start playback. Body: `{"name":"Quick mix","songIds":[1,4],"recommendationCount":3}`. Returns the player state. |

### Player

The player is shared across the application. Player actions return its current state.

| Method | Path | Description |
|---|---|---|
| GET | `/player` | Get the current player state, including the current song and queue position. |
| POST | `/player/play/{playlistId}` | Start playing a non-empty playlist from its first song. |
| POST | `/player/pause` | Pause playback if currently playing. |
| POST | `/player/resume` | Resume playback if paused. |
| POST | `/player/next` | Advance to the next song, wrapping to the first song after the last. |
| POST | `/player/stop` | Stop playback and clear the queue. |
