package com.tesselate.playlist.repository;

import com.tesselate.playlist.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
}
