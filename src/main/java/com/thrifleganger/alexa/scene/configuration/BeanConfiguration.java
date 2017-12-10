package com.thrifleganger.alexa.scene.configuration;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.speechlet.servlet.SpeechletServlet;
import com.thrifleganger.alexa.scene.handler.EventfulHandler;
import com.thrifleganger.alexa.scene.service.EventfulRestService;
import com.thrifleganger.alexa.scene.speechlet.SceneSpeechlet;
import com.thrifleganger.alexa.scene.utils.EventfulHandlerUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Properties;

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
        /*Resource resource =  new ClassPathResource("/application.yml");
        Properties properties = PropertiesLoaderUtils.loadProperties(resource);*/
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

    @Bean
    public EventfulHandler getEventfulHandler() {
        return new EventfulHandler(
                new EventfulRestService(
                        restTemplate(),
                        eventfulPropertiesBuilder(),
                        httpHeaders()),
                new EventfulHandlerUtils()
        );
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean bean =
                new ServletRegistrationBean(createServlet(new SceneSpeechlet(getEventfulHandler())), "/hello");
        bean.setLoadOnStartup(1);
        return bean;
    }

    private static SpeechletServlet createServlet(final SpeechletV2 speechlet) {
        SpeechletServlet servlet = new SpeechletServlet();
        servlet.setSpeechlet(speechlet);
        return servlet;
    }

}
