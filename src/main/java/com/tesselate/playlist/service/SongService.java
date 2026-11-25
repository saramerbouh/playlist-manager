package com.tesselate.playlist.service;

import com.tesselate.playlist.model.Song;
import com.tesselate.playlist.repository.SongRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SongService {
    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Song find(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Song not found: " + id));
    }

    public List<Song> findAll() {
        return songRepository.findAll();
    }

    public Song create(Song song) {
        return songRepository.save(song);
    }

}
