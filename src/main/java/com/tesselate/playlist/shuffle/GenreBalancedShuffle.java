package com.tesselate.playlist.shuffle;

import com.tesselate.playlist.model.Song;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class GenreBalancedShuffle implements ShuffleStrategy {

    @Override
    public String name() {
        return "genre";
    }


    @Override
    public List<Song> shuffle(List<Song> songs) {
        Map<String, List<Song>> byGenre = new LinkedHashMap<>();
        for (Song song : songs) {
            byGenre.computeIfAbsent(song.getGenre(), genre -> new ArrayList<>()).add(song);
        }

        List<List<Song>> groups = new ArrayList<>(byGenre.values());
        groups.forEach(Collections::shuffle);
        Collections.shuffle(groups);

        List<Song> result = new ArrayList<>();
        for (int round = 0; result.size() < songs.size(); round++) {
            for (List<Song> group : groups) {
                if (round < group.size()) {
                    result.add(group.get(round));
                }
            }
        }
        return result;
    }
}
