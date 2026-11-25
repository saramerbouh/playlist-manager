package com.tesselate.playlist.config;

import com.tesselate.playlist.model.Song;
import com.tesselate.playlist.repository.SongRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final SongRepository songRepository;

    public DataLoader(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public void run(String... args) {
        songRepository.saveAll(List.of(
                new Song("La Vie en Rose", "Édith Piaf", "classique français"),
                new Song("Non, Je Ne Regrette Rien", "Édith Piaf", "classique français"),
                new Song("Les Champs-Élysées", "Joe Dassin", "classique français"),
                new Song("L'Été Indien", "Joe Dassin", "classique français"),
                new Song("Bande organisée", "13 Organisé", "rap"),
                new Song("La kiffance", "Naps", "rap"),
                new Song("Réseaux", "Niska", "rap"),
                new Song("Billie Jean", "Michael Jackson", "pop"),
                new Song("Thriller", "Michael Jackson", "pop"),
                new Song("Beat It", "Michael Jackson", "pop"),
                new Song("Smooth Criminal", "Michael Jackson", "pop"),
                new Song("Man in the Mirror", "Michael Jackson", "pop"),
                new Song("Heal the World", "Michael Jackson", "pop")
        ));
    }
}
