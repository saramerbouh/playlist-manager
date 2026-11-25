package com.tesselate.playlist.service;

import com.tesselate.playlist.model.Playlist;
import com.tesselate.playlist.model.Song;
import com.tesselate.playlist.repository.PlaylistRepository;
import com.tesselate.playlist.repository.SongRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;

    public PlaylistService(PlaylistRepository playlistRepository, SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
    }

    public List<Playlist> findAll() {
        return playlistRepository.findAll();
    }

    public Playlist find(Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Playlist not found: " + id));
    }

    public List<Song> getSongs(Long id) {
        return new ArrayList<>(find(id).getSongs());
    }

    public Playlist create(String name) {
        return playlistRepository.save(new Playlist(name));
    }

    @Transactional
    public Playlist update(Long id, String name) {
        Playlist playlist = find(id);
        playlist.setName(name);
        return playlistRepository.save(playlist);
    }

    public void delete(Long id) {
        playlistRepository.delete(find(id));
    }

    @Transactional
    public Playlist addSong(Long playlistId, Long songId) {
        Playlist playlist = find(playlistId);
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Song not found: " + songId));
        boolean alreadyIn = playlist.getSongs().stream().anyMatch(s -> s.getId().equals(songId));
        if (alreadyIn) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Song already in playlist: " + songId);
        }
        playlist.getSongs().add(song);
        return playlistRepository.save(playlist);
    }

    @Transactional
    public Playlist removeSong(Long playlistId, Long songId) {
        Playlist playlist = find(playlistId);
        boolean removed = playlist.getSongs().removeIf(s -> s.getId().equals(songId));
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Song not in playlist: " + songId);
        }
        return playlistRepository.save(playlist);
    }

    @Transactional
    public Playlist reorder(Long playlistId, List<Long> orderedSongIds) {
        Playlist playlist = find(playlistId);
        Map<Long, Song> byId = playlist.getSongs().stream()
                .collect(Collectors.toMap(Song::getId, Function.identity()));

        boolean samePermutation = orderedSongIds.size() == byId.size()
                && new HashSet<>(orderedSongIds).equals(byId.keySet());
        if (!samePermutation) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Order must contain exactly the songs of the playlist");
        }

        List<Song> reordered = orderedSongIds.stream().map(byId::get).toList();
        playlist.getSongs().clear();
        playlist.getSongs().addAll(reordered);
        return playlistRepository.save(playlist);
    }
}
