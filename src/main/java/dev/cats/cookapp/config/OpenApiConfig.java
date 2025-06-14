package dev.cats.cookapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local"
                ),
                @Server(
                        url = "https://cats-cookapp-production.up.railway.app",
                        description = "Production"
                )
        }
)
public class OpenApiConfig {

}
