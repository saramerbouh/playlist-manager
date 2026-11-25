package com.tesselate.playlist.shuffle;

import com.tesselate.playlist.model.Song;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class RandomShuffle implements ShuffleStrategy {

    @Override
    public String name() {
        return "random";
    }

    @Override
    public List<Song> shuffle(List<Song> songs) {
        List<Song> result = new ArrayList<>(songs);
        Collections.shuffle(result);
        return result;
    }
}
