package com.tesselate.playlist.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Playlist Manager API",
        version = "1.0.0",
        description = "REST API for managing songs and playlists, with playback, recommendations, "
                + "shuffle, and export."
))
public class OpenApiConfig {
}
