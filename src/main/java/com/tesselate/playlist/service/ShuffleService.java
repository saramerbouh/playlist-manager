package com.tesselate.playlist.service;

import com.tesselate.playlist.model.Song;
import com.tesselate.playlist.shuffle.ShuffleStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShuffleService {

    private final Map<String, ShuffleStrategy> strategies;


    public ShuffleService(List<ShuffleStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(ShuffleStrategy::name, Function.identity()));
    }

    public List<Song> shuffle(List<Song> songs, String strategyName) {
        ShuffleStrategy strategy = strategies.get(strategyName.toLowerCase());
        if (strategy == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown strategy: " + strategyName + ". Available: " + strategies.keySet());
        }
        return strategy.shuffle(songs);
    }
}
