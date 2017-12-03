package com.thrifleganger.alexa.scene.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thrifleganger.alexa.scene.configuration.ApplicationProperties;
import com.thrifleganger.alexa.scene.model.bandsintown.EventDataModel;
import com.thrifleganger.alexa.scene.model.eventful.EventfulResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BandsInTownRestService {

    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    private final ApplicationProperties properties;

    public EventfulResponse getUpcomingEventsForArtist(String artist) {
        String url = properties.getBandsInTownUpcomingEventsBaseUrl()
                + artist.replaceAll(" ", "%20")
                + "/events?app_id=whats-the-scene";
        url = "http://api.eventful.com/json/events/search?app_key=PVGpX9Rmnhx6nbsq&keywords=lord+huron&location=dublin&date=Future";
        /*ResponseEntity<List<EventDataModel>> eventResponse =
            restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<String>("parameters", httpHeaders),
                new ParameterizedTypeReference<List<EventDataModel>>() {}
            );*/
        ResponseEntity<String> eventResponse =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        //new HttpEntity<String>("parameters", httpHeaders),
                        new HttpEntity<>("parameters", httpHeaders),
                        String.class
                );
        try {
            return new ObjectMapper().readValue(eventResponse.getBody(), EventfulResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
