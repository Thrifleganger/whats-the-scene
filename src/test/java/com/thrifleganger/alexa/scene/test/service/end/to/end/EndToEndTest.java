package com.thrifleganger.alexa.scene.test.service.end.to.end;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thrifleganger.alexa.scene.configuration.BeanConfiguration;
import com.thrifleganger.alexa.scene.speechlet.SceneSpeechlet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

@Slf4j
public class EndToEndTest {

    private ObjectMapper objectMapper;
    private SceneSpeechlet sceneSpeechlet;
    private BeanConfiguration beanConfiguration;

    @Before
    public void setup() {
        beanConfiguration = new BeanConfiguration();
        objectMapper = new ObjectMapper();
        sceneSpeechlet = new SceneSpeechlet(beanConfiguration.getEventfulHandler());

        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    }

    @Test
    public void test() throws IOException {
        /*Resource resource = new ClassPathResource("SampleRequests/LaunchJsonRequest.json");
        SpeechletRequestEnvelope<LaunchRequest> requestEnvelope =
                objectMapper.readValue(resource.getFile(), new TypeReference<SpeechletRequestEnvelope<LaunchRequest>>(){});
        log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(sceneSpeechlet.onLaunch(requestEnvelope)));

        JSONAssert.assertEquals(
                FileUtils.readFileToString(resource.getFile()),
                objectMapper.writeValueAsString(sceneSpeechlet.onLaunch(requestEnvelope)),
                false);*/
    }
}
