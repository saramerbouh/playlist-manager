package com.tesselate.playlist.shuffle;

import com.tesselate.playlist.model.Song;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShuffleStrategiesTest {

    private final List<Song> songs = List.of(
            new Song("a1", "A", "pop"), new Song("a2", "A", "pop"), new Song("a3", "A", "pop"),
            new Song("b1", "B", "rock"), new Song("b2", "B", "rock"), new Song("b3", "B", "rock"),
            new Song("c1", "C", "jazz"), new Song("c2", "C", "jazz"), new Song("c3", "C", "jazz"),
            new Song("d1", "D", "electro"), new Song("d2", "D", "electro"), new Song("d3", "D", "electro")
    );

    @Test
    void allStrategiesKeepTheSameSongs() {
        List<ShuffleStrategy> strategies = List.of(new RandomShuffle(), new SmartShuffle(), new GenreBalancedShuffle());
        for (ShuffleStrategy strategy : strategies) {
            assertThat(strategy.shuffle(songs)).containsExactlyInAnyOrderElementsOf(songs);
        }
    }

    @Test
    void smartShuffleNeverPlaysSameArtistTwiceInARow() {
        for (int i = 0; i < 100; i++) {
            List<Song> result = new SmartShuffle().shuffle(songs);
            for (int j = 1; j < result.size(); j++) {
                assertThat(result.get(j).getArtist()).isNotEqualTo(result.get(j - 1).getArtist());
            }
        }
    }

    @Test
    void genreBalancedShuffleCyclesThroughAllGenres() {
        List<Song> result = new GenreBalancedShuffle().shuffle(songs);
        for (int start = 0; start < result.size(); start += 4) {
            long distinctGenres = result.subList(start, start + 4).stream().map(Song::getGenre).distinct().count();
            assertThat(distinctGenres).isEqualTo(4);
        }
    }

    @Test
    void smartShuffleStillWorksWhenOneArtistDominates() {
        List<Song> dominated = List.of(
                new Song("x1", "X", "pop"), new Song("x2", "X", "pop"), new Song("x3", "X", "pop"),
                new Song("y1", "Y", "pop"));
        assertThat(new SmartShuffle().shuffle(dominated)).hasSize(4);
    }
}
