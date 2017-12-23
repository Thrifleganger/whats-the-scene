package com.thrifleganger.alexa.scene.test.end.to.end;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thrifleganger.alexa.scene.configuration.BeanConfiguration;
import com.thrifleganger.alexa.scene.speechlet.SceneSpeechlet;
import com.thrifleganger.alexa.scene.test.utils.TestUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FetchMoreResultsIntentTest {

    private static final String REQUEST_PATH = "SampleRequestsAndResponses/FetchMoreResultsIntent/";
    private static final String RESULT_PATH = "MockResults/";

    private ObjectMapper objectMapper;
    private SceneSpeechlet sceneSpeechlet;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        sceneSpeechlet = TestUtils.restTemplateMockedSpeechlet(restTemplate);
    }

    @Test
    public void fetchMoreResultsTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "FetchMoreResultsJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "FetchMoreResultsJsonResponse.json");
        Resource result = new ClassPathResource(RESULT_PATH + "EventfulResultPage2.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(new ResponseEntity<>(FileUtils.readFileToString(result.getFile()), HttpStatus.OK));
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }

    @Test
    public void fetchMoreResultsWhenNoMorePresentTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "FetchMoreResultsWhenNoMoreJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "FetchMoreResultsWhenNoMoreJsonResponse.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }
}
