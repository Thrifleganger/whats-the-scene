package com.thrifleganger.alexa.scene.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApplicationProperties {

    @Value("${rest.bands-in-town.upcoming-events.base-url}")
    private String bandsInTownUpcomingEventsBaseUrl;

    @Value("${alexa.application-id}")
    private String applicationId;
}
