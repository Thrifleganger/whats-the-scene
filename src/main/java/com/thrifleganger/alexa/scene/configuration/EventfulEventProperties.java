package com.thrifleganger.alexa.scene.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "rest.eventful.events")
public class EventfulEventProperties {

    private String baseUrl;
    private String applicationKey;
    private String appKeyParam;
    private String keywordsParam;
    private String locationParam;
    private String dateParam;
    private String categoryParam;
    private String pageSizeParam;
    private String sortOrderParam;
}
