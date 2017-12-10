package com.thrifleganger.alexa.scene.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thrifleganger.alexa.scene.configuration.EventfulEventProperties;
import com.thrifleganger.alexa.scene.exception.handler.RestResult;
import com.thrifleganger.alexa.scene.model.eventful.EventfulRequest;
import com.thrifleganger.alexa.scene.model.eventful.EventfulResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventfulRestService {

    private static final String SEPARATOR = "&";
    private static final String EQUALS = "=";

    private final RestTemplate restTemplate;
    private final EventfulEventProperties properties;
    private final HttpHeaders headers;

    public RestResult<EventfulResponse> callEventSearchEndpoint(EventfulRequest request) {

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    generateRestEndpointUrl(request),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class
            );
            log.info("Sending request to Eventful: " + generateRestEndpointUrl(request));
            return RestResult.success(
                    new ObjectMapper().readValue(
                            response.getBody(),
                            EventfulResponse.class
                    )
            );
        } catch(Exception exception) {
            return RestResult.error(exception);
        }
    }

    //Make sure no spaces
    private String generateRestEndpointUrl(EventfulRequest request) {

        StringBuilder urlString = new StringBuilder();
        urlString.append(properties.getBaseUrl())
                .append(properties.getAppKeyParam())
                .append(EQUALS)
                .append(properties.getApplicationKey());
        request.getCategory()
                .ifPresent(category ->
                        urlString.append(SEPARATOR)
                                .append(properties.getCategoryParam())
                                .append(EQUALS)
                                .append(eliminateWhiteSpace(category))
                );
        request.getDate()
                .ifPresent(date ->
                        urlString.append(SEPARATOR)
                                .append(properties.getDateParam())
                                .append(EQUALS)
                                .append(eliminateWhiteSpace(date))
                );
        request.getLocation()
                .ifPresent(location ->
                        urlString.append(SEPARATOR)
                                .append(properties.getLocationParam())
                                .append(EQUALS)
                                .append(eliminateWhiteSpace(location))
                );
        request.getKeywords()
                .ifPresent(keywords ->
                        urlString.append(SEPARATOR)
                                .append(properties.getKeywordsParam())
                                .append(EQUALS)
                                .append(eliminateWhiteSpace(keywords))
                );
        request.getSortBy()
                .ifPresent(sortBy ->
                        urlString.append(SEPARATOR)
                                .append(properties.getSortOrderParam())
                                .append(EQUALS)
                                .append(sortBy)
                );
        request.getPageSize()
                .ifPresent(pageSize ->
                        urlString.append(SEPARATOR)
                                .append(properties.getPageSizeParam())
                                .append(EQUALS)
                                .append(pageSize)
                );
        return urlString.toString();
    }

    private String eliminateWhiteSpace(final String string) {
        return string.replaceAll(" ", "+");
    }
}
