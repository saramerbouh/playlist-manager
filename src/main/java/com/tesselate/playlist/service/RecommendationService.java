package com.tesselate.playlist.service;

import com.tesselate.playlist.model.Playlist;
import com.tesselate.playlist.model.Song;
import com.tesselate.playlist.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final SongRepository songRepository;
    private final PlaylistService playlistService;

    public RecommendationService(SongRepository songRepository, PlaylistService playlistService) {
        this.songRepository = songRepository;
        this.playlistService = playlistService;
    }


    public List<Song> recommend(Long playlistId, int limit) {
        Playlist playlist = playlistService.find(playlistId);

        Map<String, Long> genreCount = playlist.getSongs().stream()
                .collect(Collectors.groupingBy(Song::getGenre, Collectors.counting()));
        Set<Long> inPlaylist = playlist.getSongs().stream()
                .map(Song::getId)
                .collect(Collectors.toSet());

        return songRepository.findAll().stream()
                .filter(song -> !inPlaylist.contains(song.getId()))
                .filter(song -> genreCount.containsKey(song.getGenre()))
                .sorted(Comparator.comparing((Song song) -> genreCount.get(song.getGenre())).reversed())
                .limit(limit)
                .toList();
    }
}
