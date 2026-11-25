package com.tesselate.playlist.service;

import com.tesselate.playlist.model.Playlist;
import com.tesselate.playlist.model.Song;
import com.tesselate.playlist.service.MusicPlayer.PlayerState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlaylistWorkflowService {

    private final PlaylistService playlistService;
    private final RecommendationService recommendationService;
    private final MusicPlayer musicPlayer;

    public PlaylistWorkflowService(PlaylistService playlistService,
                                   RecommendationService recommendationService,
                                   MusicPlayer musicPlayer) {
        this.playlistService = playlistService;
        this.recommendationService = recommendationService;
        this.musicPlayer = musicPlayer;
    }


    @Transactional
    public PlayerState createRecommendAndPlay(String name, List<Long> songIds, int recommendationCount) {
        Playlist playlist = playlistService.create(name);
        Long playlistId = playlist.getId();

        for (Long songId : songIds) {
            playlistService.addSong(playlistId, songId);
        }
        for (Song song : recommendationService.recommend(playlistId, recommendationCount)) {
            playlistService.addSong(playlistId, song.getId());
        }
        return musicPlayer.play(playlistId);
    }
}
