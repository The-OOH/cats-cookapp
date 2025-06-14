package dev.cats.cookapp.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CaffeineCacheManager caffeineCacheManager() {
        final CaffeineCacheManager mgr = new CaffeineCacheManager("preferences", "recipeFilters");
        mgr.setAsyncCacheMode(true);
        mgr.setAllowNullValues(false);
        mgr.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofHours(1))
                        .maximumSize(10_000)
        );
        return mgr;
    }
}
