package com.tesselate.playlist.service;

import com.tesselate.playlist.export.PlaylistExporter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExportService {

    private final Map<String, PlaylistExporter> exporters;


    public ExportService(List<PlaylistExporter> exporterList) {
        this.exporters = exporterList.stream()
                .collect(Collectors.toMap(PlaylistExporter::format, Function.identity()));
    }

    public PlaylistExporter getExporter(String format) {
        PlaylistExporter exporter = exporters.get(format.toLowerCase());
        if (exporter == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown format: " + format + ". Available: " + exporters.keySet());
        }
        return exporter;
    }
}
