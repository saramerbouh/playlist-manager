package com.tesselate.playlist.controller;

import com.tesselate.playlist.service.MusicPlayer;
import com.tesselate.playlist.service.MusicPlayer.PlayerState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/player")
@Tag(name = "Player", description = "Control the application's shared music player.")
public class PlayerController {

    private final MusicPlayer musicPlayer;

    public PlayerController(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    @GetMapping
    @Operation(summary = "Get player state", description = "Returns the current playback state.")
    public PlayerState state() {
        return musicPlayer.state();
    }

    @PostMapping("/play/{playlistId}")
    @Operation(summary = "Start playback", description = "Starts playing the specified playlist.")
    public PlayerState play(@PathVariable Long playlistId) {
        return musicPlayer.play(playlistId);
    }

    @PostMapping("/pause")
    @Operation(summary = "Pause playback", description = "Pauses playback if a song is currently playing.")
    public PlayerState pause() {
        return musicPlayer.pause();
    }

    @PostMapping("/resume")
    @Operation(summary = "Resume playback", description = "Resumes playback if the player is paused.")
    public PlayerState resume() {
        return musicPlayer.resume();
    }

    @PostMapping("/next")
    @Operation(summary = "Skip to the next song", description = "Advances to the next song in the queue.")
    public PlayerState next() {
        return musicPlayer.next();
    }

    @PostMapping("/stop")
    @Operation(summary = "Stop playback", description = "Stops playback and clears the player queue.")
    public PlayerState stop() {
        return musicPlayer.stop();
    }
}
