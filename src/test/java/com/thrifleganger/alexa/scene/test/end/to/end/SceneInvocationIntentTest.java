package com.thrifleganger.alexa.scene.test.end.to.end;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thrifleganger.alexa.scene.speechlet.SceneSpeechlet;
import com.thrifleganger.alexa.scene.test.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class SceneInvocationIntentTest {

    private static final String REQUEST_PATH = "SampleRequestsAndResponses/SceneInvocationIntent/";
    private static final String RESULT_PATH = "MockResults/";

    private ObjectMapper objectMapper;
    private SceneSpeechlet sceneSpeechlet;

    @Mock
    private RestTemplate restTemplate;


    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        sceneSpeechlet = TestUtils.restTemplateMockedSpeechlet(restTemplate);

        //objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    }

    @Test
    public void SceneInvocationStartedTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "SceneInvocationStartedJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "SceneInvocationStartedJsonResponse.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }

    @Test
    public void SceneInvocationCategoryConfirmTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "SceneInvocationCategoryConfirmJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "SceneInvocationCategoryConfirmJsonResponse.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }

    @Test
    public void SceneInvocationDateConfirmTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "SceneInvocationDateConfirmJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "SceneInvocationDateConfirmJsonResponse.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }

    @Test
    public void SceneInvocationCityConfirmTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "SceneInvocationCityConfirmJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "SceneInvocationCityConfirmJsonResponse.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }

    @Test
    public void SceneInvocationKeywordConfirmTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "SceneInvocationKeywordConfirmJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "SceneInvocationKeywordConfirmJsonResponse.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }

    @Test
    public void SceneInvocationIntentConfirmTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "SceneInvocationIntentConfirmJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "SceneInvocationIntentConfirmJsonResponse.json");
        Resource result = new ClassPathResource(RESULT_PATH + "EventfulResultPage1.json");
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
    public void SceneInvocationCategoryInvalidTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "SceneInvocationCategoryInvalidJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "SceneInvocationCategoryInvalidJsonResponse.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }

    @Test
    public void SceneInvocationIntentDeniedTest() throws IOException {
        Resource requestResource = new ClassPathResource(REQUEST_PATH + "SceneInvocationIntentDeniedJsonRequest.json");
        Resource responseResource = new ClassPathResource(REQUEST_PATH + "SceneInvocationIntentDeniedJsonResponse.json");
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope =
                objectMapper.readValue(requestResource.getFile(), new TypeReference<SpeechletRequestEnvelope<IntentRequest>>(){});
        JSONAssert.assertEquals(
                FileUtils.readFileToString(responseResource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onIntent(requestEnvelope)),
                false
        );
    }
}
