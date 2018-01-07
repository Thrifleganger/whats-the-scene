package com.thrifleganger.alexa.scene;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import com.thrifleganger.alexa.scene.configuration.BeanConfiguration;
import com.thrifleganger.alexa.scene.handler.EventfulHandler;
import com.thrifleganger.alexa.scene.service.EventfulRestService;
import com.thrifleganger.alexa.scene.speechlet.SceneSpeechlet;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SceneSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {

    private static final Set<String> supportedApplicationIds;
    static {
        supportedApplicationIds = new HashSet<String>();
        supportedApplicationIds.add("amzn1.ask.skill.d57c5c5a-f619-4b1f-8fca-a5b5a3dc9c71");
    }

    public SceneSpeechletRequestStreamHandler() {
        super(new SceneSpeechlet(
                new BeanConfiguration().getEventfulHandler()),
                supportedApplicationIds
        );
    }


}
