package com.thrifleganger.alexa.scene.configuration;

import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.speechlet.servlet.SpeechletServlet;
import com.thrifleganger.alexa.scene.handler.DialogModelHandler;
import com.thrifleganger.alexa.scene.handler.EventfulHandler;
import com.thrifleganger.alexa.scene.handler.GenericRequestHandler;
import com.thrifleganger.alexa.scene.service.EventfulRestService;
import com.thrifleganger.alexa.scene.speechlet.SceneSpeechlet;
import com.thrifleganger.alexa.scene.utils.EventfulHandlerUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class BeanConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    public HttpHeaders httpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

    @Bean
    public EventfulEventProperties eventfulPropertiesBuilder() {
        return EventfulEventProperties.builder()
                .applicationKey(System.getenv("EVENTFUL_APPKEY"))
                .baseUrl(System.getenv("EVENTFUL_BASE_URL"))
                .build();
    }

    @Bean
    public EventfulHandler getEventfulHandler() {
        return new EventfulHandler(
                genericRequestHandler(),
                dialogModelHandler(),
                eventfulHandlerUtils()
        );
    }

    @Bean
    public DialogModelHandler dialogModelHandler() {
        return new DialogModelHandler();
    }

    @Bean
    public GenericRequestHandler genericRequestHandler() {
        return new GenericRequestHandler(
                eventfulRestService(),
                eventfulHandlerUtils()
        );
    }

    @Bean
    public EventfulHandlerUtils eventfulHandlerUtils() {
        return new EventfulHandlerUtils();
    }

    @Bean
    public EventfulRestService eventfulRestService() {
        return new EventfulRestService(
                restTemplate(),
                eventfulPropertiesBuilder(),
                httpHeaders()
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
