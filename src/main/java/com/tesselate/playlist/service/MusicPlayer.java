package com.tesselate.playlist.service;

import com.tesselate.playlist.model.Song;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MusicPlayer {

    public enum Status { STOPPED, PLAYING, PAUSED }

    public record PlayerState(Status status, Long playlistId, Song currentSong, int index, int queueSize) {
    }

    private final PlaylistService playlistService;

    private Status status = Status.STOPPED;
    private Long playlistId;
    private List<Song> queue = List.of();
    private int index;

    public MusicPlayer(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    public synchronized PlayerState play(Long playlistId) {
        List<Song> songs = playlistService.getSongs(playlistId);
        if (songs.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Playlist is empty: " + playlistId);
        }
        this.playlistId = playlistId;
        this.queue = songs;
        this.index = 0;
        this.status = Status.PLAYING;
        return state();
    }

    public synchronized PlayerState pause() {
        if (status == Status.PLAYING) {
            status = Status.PAUSED;
        }
        return state();
    }

    public synchronized PlayerState resume() {
        if (status == Status.PAUSED) {
            status = Status.PLAYING;
        }
        return state();
    }

    public synchronized PlayerState next() {
        if (status == Status.STOPPED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Nothing is playing");
        }
        index = (index + 1) % queue.size();
        status = Status.PLAYING;
        return state();
    }

    public synchronized PlayerState stop() {
        status = Status.STOPPED;
        playlistId = null;
        queue = List.of();
        index = 0;
        return state();
    }

    public synchronized PlayerState state() {
        Song current = status == Status.STOPPED ? null : queue.get(index);
        return new PlayerState(status, playlistId, current, index, queue.size());
    }
}
