package dev.cats.cookapp.config.chatbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Slf4j
class ApiClientConfig {
    @Bean
    WebClient recommendationsModuleClient(@Value("${integration.recommendations.url}") final String base) {

        return WebClient.builder()
                .baseUrl(base)
                .build();
    }
}