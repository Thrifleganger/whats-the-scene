package com.thrifleganger.alexa.scene.test.utils;

import com.thrifleganger.alexa.scene.configuration.BeanConfiguration;
import com.thrifleganger.alexa.scene.handler.EventfulHandler;
import com.thrifleganger.alexa.scene.handler.GenericRequestHandler;
import com.thrifleganger.alexa.scene.service.EventfulRestService;
import com.thrifleganger.alexa.scene.speechlet.SceneSpeechlet;
import org.springframework.web.client.RestTemplate;

public class TestUtils {

    public static SceneSpeechlet restTemplateMockedSpeechlet(final RestTemplate restTemplate) {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        return new SceneSpeechlet(
                new EventfulHandler(
                        new GenericRequestHandler(
                                new EventfulRestService(
                                        restTemplate,
                                        beanConfiguration.eventfulPropertiesBuilder(),
                                        beanConfiguration.httpHeaders()
                                ),
                                beanConfiguration.eventfulHandlerUtils()
                        ),
                        beanConfiguration.dialogModelHandler(),
                        beanConfiguration.eventfulHandlerUtils()
                )
        );
    }
}
