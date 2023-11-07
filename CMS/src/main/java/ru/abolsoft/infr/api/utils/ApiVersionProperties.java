package ru.abolsoft.infr.api.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.Set;

@Data
@ConfigurationProperties(prefix = "api.version")
public class ApiVersionProperties implements Serializable {

    private Type type = Type.URI;

    private String uriPrefix = "api";

    private String versionPrefix = "v";
    private Set<String> lastVersionAliases = Set.of("next", "v");

    private UriLocation uriLocation = UriLocation.BEGIN;

    private String header = "X-API-VERSION";

    private String param = "api_version";

    public enum Type {
        URI, HEADER, PARAM
    }

    public enum UriLocation {
        BEGIN, END
    }
}
