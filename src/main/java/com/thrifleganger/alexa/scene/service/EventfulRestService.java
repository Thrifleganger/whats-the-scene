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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventfulRestService {

    private static final String SEPARATOR = "&";
    private static final String EQUALS = "=";
    private static final String APP_KEY = "app_key";
    private static final String CATEGORY = "category";
    private static final String DATE = "date";
    private static final String KEYWORDS = "keywords";
    private static final String LOCATION = "location";
    private static final String PAGE_SIZE = "page_size";
    private static final String PAGE_NUMBER = "page_number";
    private static final String SORT_ORDER = "sort_order";

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
                .append(APP_KEY)
                .append(EQUALS)
                .append(properties.getApplicationKey());
        if(!Objects.isNull(request.getCategory())) {
            urlString.append(SEPARATOR)
                    .append(CATEGORY)
                    .append(EQUALS)
                    .append(eliminateWhiteSpace(request.getCategory()));
        }
        if(!Objects.isNull(request.getDate())) {
            urlString.append(SEPARATOR)
                    .append(DATE)
                    .append(EQUALS)
                    .append(eliminateWhiteSpace(request.getDate()));
        }
        if(!Objects.isNull(request.getLocation())) {
            urlString.append(SEPARATOR)
                    .append(LOCATION)
                    .append(EQUALS)
                    .append(eliminateWhiteSpace(request.getLocation()));
        }
        if(!Objects.isNull(request.getKeywords())) {
            urlString.append(SEPARATOR)
                    .append(KEYWORDS)
                    .append(EQUALS)
                    .append(eliminateWhiteSpace(request.getKeywords()));
        }
        if(!Objects.isNull(request.getSortBy())) {
            urlString.append(SEPARATOR)
                    .append(SORT_ORDER)
                    .append(EQUALS)
                    .append(request.getSortBy());
        }
        if(!Objects.isNull(request.getPageSize())) {
            urlString.append(SEPARATOR)
                    .append(PAGE_SIZE)
                    .append(EQUALS)
                    .append(request.getPageSize());
        }
        if(!Objects.isNull(request.getPageNumber())) {
            urlString.append(SEPARATOR)
                    .append(PAGE_NUMBER)
                    .append(EQUALS)
                    .append(request.getPageNumber());
        }
        return urlString.toString();
    }

    private String eliminateWhiteSpace(final String string) {
        return string.replaceAll(" ", "+");
    }
}
