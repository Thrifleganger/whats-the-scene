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

public class HelpIntent {

    private static final String REQUEST_PATH = "SampleRequestsAndResponses/HelpIntent/";

    private ObjectMapper objectMapper;
    private SceneSpeechlet sceneSpeechlet;

    @Before
    public void setup() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        objectMapper = new ObjectMapper();
        sceneSpeechlet = new SceneSpeechlet(beanConfiguration.getEventfulHandler());
    }

    @Test
    public void HelpIntentAfterSkillInvokedTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "HelpAfterSkillInvokedJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "HelpAfterSkillInvokedJsonResponse.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }

    @Test
    public void HelpIntentAfterResultsReadOutWithMoreResultsAvailableTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "HelpAfterResultsReadOutWithMoreResultsAvailableJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "HelpAfterResultsReadOutWithMoreResultsAvailableJsonResponse.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }

    @Test
    public void HelpIntentAfterResultsReadOutAwaitingExpansionTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "HelpAfterResultsReadOutAwaitingExpansionJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "HelpAfterResultsReadOutAwaitingExpansionJsonResponse.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }
}
