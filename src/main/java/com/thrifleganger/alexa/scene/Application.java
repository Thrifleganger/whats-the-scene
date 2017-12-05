
package com.thrifleganger.alexa.scene;

import com.amazon.speech.Sdk;
import com.thrifleganger.alexa.scene.configuration.EventfulEventProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(EventfulEventProperties.class)
public class Application {

    private ApplicationContext applicationContext;

    public void run() {
        applicationContext = SpringApplication.run(Application.class);
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void main(String[] args) {
        setAmazonProperties();
        SpringApplication.run(Application.class, args);
    }

    private static void setAmazonProperties() {
        System.setProperty(Sdk.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY, "true");
        System.setProperty(Sdk.SUPPORTED_APPLICATION_IDS_SYSTEM_PROPERTY, "amzn1.ask.skill.11fbb08b-fe35-4995-b585-7b888737ac5a");
        System.setProperty(Sdk.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY, "");
    }
}
