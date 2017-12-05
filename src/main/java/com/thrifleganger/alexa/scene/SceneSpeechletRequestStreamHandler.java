/*
package com.thrifleganger.alexa.scene;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import com.thrifleganger.alexa.scene.BeanConfiguration;
import com.thrifleganger.alexa.scene.configuration.EventfulEventProperties;
import com.thrifleganger.alexa.scene.handler.EventfulHandler;
import com.thrifleganger.alexa.scene.service.EventfulRestService;
import com.thrifleganger.alexa.scene.speechlet.SceneSpeechlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SceneSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {

    @Autowired
    SceneSpeechlet sceneSpeechlet;

    private static final Set<String> supportedApplicationIds;
    static {
        supportedApplicationIds = new HashSet<String>();
        supportedApplicationIds.add("amzn1.ask.skill.11fbb08b-fe35-4995-b585-7b888737ac5a");
    }

    public SceneSpeechletRequestStreamHandler() {

    }

    public SceneSpeechletRequestStreamHandler(SceneSpeechlet sceneSpeechlet, Set<String> set) {
        super(sceneSpeechlet, set);
    }


}
*/
