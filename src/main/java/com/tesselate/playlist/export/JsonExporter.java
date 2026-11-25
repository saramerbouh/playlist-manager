package com.tesselate.playlist.export;

import com.tesselate.playlist.model.Playlist;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonExporter implements PlaylistExporter {

    private final ObjectMapper objectMapper;

    public JsonExporter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String format() {
        return "json";
    }

    @Override
    public String contentType() {
        return "application/json";
    }

    @Override
    public String export(Playlist playlist) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(playlist);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("JSON export failed", e);
        }
    }
}
