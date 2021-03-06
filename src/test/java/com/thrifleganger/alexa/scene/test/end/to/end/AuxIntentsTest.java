package com.thrifleganger.alexa.scene.test.end.to.end;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thrifleganger.alexa.scene.configuration.BeanConfiguration;
import com.thrifleganger.alexa.scene.speechlet.SceneSpeechlet;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class AuxIntentsTest {

    private static final String REQUEST_PATH = "SampleRequestsAndResponses/AuxIntents/";

    private ObjectMapper objectMapper;
    private SceneSpeechlet sceneSpeechlet;

    @Before
    public void setup() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        objectMapper = new ObjectMapper();
        sceneSpeechlet = new SceneSpeechlet(beanConfiguration.getEventfulHandler());
    }

    @Test
    public void launchIntentTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "LaunchJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "LaunchJsonResponse.json");
        SpeechletRequestEnvelope<LaunchRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<LaunchRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onLaunch(requestEnvelope)),
                false
        );
    }

    @Test
    public void stopIntentTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "StopJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "StopJsonResponse.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }

    @Test
    public void cancelIntentTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "CancelJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "CancelJsonResponse.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }
}
