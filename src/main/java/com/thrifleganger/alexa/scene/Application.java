package com.thrifleganger.alexa.scene;

import com.amazon.speech.Sdk;
import com.thrifleganger.alexa.scene.configuration.EventfulEventProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info(System.getenv("EVENTFUL_APPKEY"));
        log.info(System.getenv("EVENTFUL_BASE_URL"));
    }
}
