package com.tesselate.playlist.shuffle;

import com.tesselate.playlist.model.Song;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class SmartShuffle implements ShuffleStrategy {

    @Override
    public String name() {
        return "smart";
    }


    @Override
    public List<Song> shuffle(List<Song> songs) {
        List<Song> mixed = new ArrayList<>(songs);
        Collections.shuffle(mixed);

        Map<String, Deque<Song>> byArtist = new LinkedHashMap<>();
        for (Song song : mixed) {
            byArtist.computeIfAbsent(song.getArtist(), artist -> new ArrayDeque<>()).add(song);
        }

        List<Song> result = new ArrayList<>();
        String last = null;
        while (!byArtist.isEmpty()) {
            String previous = last;
            String artist = byArtist.entrySet().stream()
                    .filter(entry -> !entry.getKey().equals(previous))
                    .max(Comparator.comparingInt(entry -> entry.getValue().size()))
                    .map(Map.Entry::getKey)
                    .orElse(previous);

            Deque<Song> queue = byArtist.get(artist);
            result.add(queue.poll());
            if (queue.isEmpty()) {
                byArtist.remove(artist);
            }
            last = artist;
        }
        return result;
    }
}
