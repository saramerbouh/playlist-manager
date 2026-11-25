package com.tesselate.playlist.export;

import com.tesselate.playlist.model.Playlist;

public interface PlaylistExporter {

    String format();

    String contentType();

    String export(Playlist playlist);
}
