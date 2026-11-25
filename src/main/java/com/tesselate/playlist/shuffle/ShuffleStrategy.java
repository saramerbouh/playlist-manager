package com.tesselate.playlist.shuffle;

import com.tesselate.playlist.model.Song;

import java.util.List;

public interface ShuffleStrategy {

    String name();

    List<Song> shuffle(List<Song> songs);
}
