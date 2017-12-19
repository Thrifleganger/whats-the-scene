package com.thrifleganger.alexa.scene.test.end.to.end;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
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

public class ExpandResultIntentTest {

    private static final String REQUEST_PATH = "SampleRequestsAndResponses/ExpandResultIntent/";

    private ObjectMapper objectMapper;
    private SceneSpeechlet sceneSpeechlet;

    @Before
    public void setup() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        objectMapper = new ObjectMapper();
        sceneSpeechlet = new SceneSpeechlet(beanConfiguration.getEventfulHandler());
    }

    @Test
    public void expandResultTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "ExpandResultJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "ExpandResultJsonResponse.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }

    @Test
    public void expandResultAtAnInvalidEntryPointTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "ExpandResultInvalidEntryPointJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "ExpandResultInvalidEntryPointJsonResponse.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }

    @Test
    public void expandResultWhenNumberIsInvalidTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "ExpandResultNumberInvalidJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "ExpandResultNumberInvalidJsonResponse.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }

    @Test
    public void expandResultWhenNimberIsOutOfRangeTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "ExpandResultNumberOutOfRangeJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "ExpandResultNumberOutOfRangeJsonResponse.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }
}
