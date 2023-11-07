package ru.abolsoft.infr.api.utils;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    @NonNull
    private ApiVersionProperties apiVersionProperties;
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setPathMatcher(versionedAntPathMatcher());
    }

    @Bean
    public PathMatcher versionedAntPathMatcher() {
        return new VersionedAntPathMatcher(apiVersionProperties);
    }
}
