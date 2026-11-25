package com.tesselate.playlist.controller;

import com.tesselate.playlist.model.Song;
import com.tesselate.playlist.service.SongService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songs")
@Tag(name = "Songs", description = "Browse and create songs in the catalog.")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping
    @Operation(summary = "List songs", description = "Returns all songs in the catalog.")
    public List<Song> findAll() {
        return songService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a song", description = "Returns a song by its ID.")
    public Song find(@PathVariable Long id) {
        return songService.find(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a song", description = "Adds a song to the catalog.")
    public Song create(@Valid @RequestBody Song song) {
        return songService.create(song);
    }
}
