package com.fds.rest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fds")
public class FdsProperties {
private final Urls urls = new Urls();

    @Data
    public static class Urls {
        private String baseUrl;
        private String topHighUrl;
        private String topGreatChange;
    }

    public Urls getUrls() {
        return urls;
    }

}
