package com.thrifleganger.alexa.scene.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

//@ConfigurationProperties(prefix = "rest.eventful.events")
@Getter
@Setter
@Builder
public class EventfulEventProperties {

    private String baseUrl;
    private String applicationKey;
}
