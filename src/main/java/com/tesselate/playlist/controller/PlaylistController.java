package com.tesselate.playlist.controller;

import com.tesselate.playlist.export.PlaylistExporter;
import com.tesselate.playlist.model.Playlist;
import com.tesselate.playlist.model.Song;
import com.tesselate.playlist.service.ExportService;
import com.tesselate.playlist.service.MusicPlayer.PlayerState;
import com.tesselate.playlist.service.PlaylistService;
import com.tesselate.playlist.service.PlaylistWorkflowService;
import com.tesselate.playlist.service.RecommendationService;
import com.tesselate.playlist.service.ShuffleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/playlists")
@Tag(name = "Playlists", description = "Manage playlists, recommendations, shuffle, and export.")
public class PlaylistController {

    @io.swagger.v3.oas.annotations.media.Schema(description = "Playlist name.")
    public record PlaylistRequest(@NotBlank String name) {
    }

    @io.swagger.v3.oas.annotations.media.Schema(description = "Quick playlist creation parameters.")
    public record QuickRequest(@NotBlank String name, List<Long> songIds, int recommendationCount) {
        public QuickRequest {
            if (songIds == null) {
                songIds = List.of();
            }
        }
    }

    private final PlaylistService playlistService;
    private final ShuffleService shuffleService;
    private final RecommendationService recommendationService;
    private final ExportService exportService;
    private final PlaylistWorkflowService workflowService;

    public PlaylistController(PlaylistService playlistService,
                              ShuffleService shuffleService,
                              RecommendationService recommendationService,
                              ExportService exportService,
                              PlaylistWorkflowService workflowService) {
        this.playlistService = playlistService;
        this.shuffleService = shuffleService;
        this.recommendationService = recommendationService;
        this.exportService = exportService;
        this.workflowService = workflowService;
    }

    @GetMapping
    @Operation(summary = "List playlists", description = "Returns all playlists and their songs.")
    public List<Playlist> findAll() {
        return playlistService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a playlist", description = "Returns a playlist and its songs.")
    public Playlist find(@PathVariable Long id) {
        return playlistService.find(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a playlist", description = "Creates an empty playlist with the given name.")
    public Playlist create(@Valid @RequestBody PlaylistRequest request) {
        return playlistService.create(request.name());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Rename a playlist", description = "Updates the name of an existing playlist.")
    public Playlist update(@PathVariable Long id, @Valid @RequestBody PlaylistRequest request) {
        return playlistService.update(id, request.name());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a playlist", description = "Deletes the specified playlist.")
    public void delete(@PathVariable Long id) {
        playlistService.delete(id);
    }

    @PostMapping("/{id}/songs/{songId}")
    @Operation(summary = "Add a song", description = "Adds an existing song to the playlist.")
    public Playlist addSong(@PathVariable Long id, @PathVariable Long songId) {
        return playlistService.addSong(id, songId);
    }

    @DeleteMapping("/{id}/songs/{songId}")
    @Operation(summary = "Remove a song", description = "Removes a song from the playlist.")
    public Playlist removeSong(@PathVariable Long id, @PathVariable Long songId) {
        return playlistService.removeSong(id, songId);
    }

    @PutMapping("/{id}/songs/order")
    @Operation(summary = "Reorder songs",
            description = "Replaces the song order. The request body must contain exactly the playlist's song IDs.")
    public Playlist reorder(@PathVariable Long id, @RequestBody List<Long> orderedSongIds) {
        return playlistService.reorder(id, orderedSongIds);
    }


    @GetMapping("/{id}/shuffle")
    @Operation(summary = "Shuffle a playlist",
            description = "Returns a shuffled play order without changing the saved order. "
                    + "Available strategies: random, smart, and genre.")
    public List<Song> shuffle(@PathVariable Long id, @RequestParam(defaultValue = "random") String strategy) {
        return shuffleService.shuffle(playlistService.getSongs(id), strategy);
    }

    @GetMapping("/{id}/recommendations")
    @Operation(summary = "Get song recommendations",
            description = "Returns songs from the catalog that are not already in the playlist.")
    public List<Song> recommendations(@PathVariable Long id, @RequestParam(defaultValue = "5") int limit) {
        return recommendationService.recommend(id, limit);
    }

    @GetMapping("/{id}/export")
    @Operation(summary = "Export a playlist",
            description = "Exports a playlist in JSON or M3U format.")
    public ResponseEntity<String> export(@PathVariable Long id, @RequestParam(defaultValue = "json") String format) {
        PlaylistExporter exporter = exportService.getExporter(format);
        String content = exporter.export(playlistService.find(id));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(exporter.contentType()))
                .body(content);
    }

    @PostMapping("/quick")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create, complete, and play a playlist",
            description = "Creates a playlist, adds the provided songs, fills it with recommendations, "
                    + "and starts playback.")
    public PlayerState quick(@Valid @RequestBody QuickRequest request) {
        return workflowService.createRecommendAndPlay(request.name(), request.songIds(), request.recommendationCount());
    }
}
