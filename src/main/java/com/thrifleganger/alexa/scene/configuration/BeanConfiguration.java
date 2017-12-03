package com.thrifleganger.alexa.scene.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    public HttpHeaders httpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpHeaders.add("Accept-Encoding", "gzip, deflate, br");
        httpHeaders.add("Accpet-Language", "en-US,en;q=0.9");
        httpHeaders.add("Connection", "keep-alive");
        httpHeaders.add("Cookie", "_ga=GA1.2.1397757772.1512214977; _gid=GA1.2.796396849.1512214977");
        httpHeaders.add("Host", "rest.bandsintown.com");
        httpHeaders.add("Upgrade-Insecure-Requests", "1");
        httpHeaders.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        return httpHeaders;
    }

    @Bean
    public EventfulEventProperties eventfulPropertiesBuilder() {
        return EventfulEventProperties.builder()
                .appKeyParam("app_key")
                .applicationKey("PVGpX9Rmnhx6nbsq")
                .baseUrl("http://api.eventful.com/json/events/search?")
                .categoryParam("category")
                .dateParam("date")
                .keywordsParam("keywords")
                .locationParam("location")
                .pageSizeParam("page_size")
                .sortOrderParam("sort_order")
                .build();
    }

}
