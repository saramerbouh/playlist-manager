package com.tesselate.playlist.export;

import com.tesselate.playlist.model.Playlist;
import com.tesselate.playlist.model.Song;
import org.springframework.stereotype.Component;

@Component
public class M3uExporter implements PlaylistExporter {

    @Override
    public String format() {
        return "m3u";
    }

    @Override
    public String contentType() {
        return "audio/x-mpegurl";
    }

    @Override
    public String export(Playlist playlist) {
        StringBuilder sb = new StringBuilder("#EXTM3U\n");
        for (Song song : playlist.getSongs()) {
            String label = song.getArtist() + " - " + song.getTitle();
            sb.append("#EXTINF:").append(',').append(label).append('\n');
            sb.append(label).append('\n');
        }
        return sb.toString();
    }
}
