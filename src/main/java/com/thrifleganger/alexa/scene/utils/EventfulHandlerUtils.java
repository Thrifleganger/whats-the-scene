package com.thrifleganger.alexa.scene.utils;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.thrifleganger.alexa.scene.constants.SessionAttributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventfulHandlerUtils {

    public Integer generateNextPageNumber(final  SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        if(isNull(requestEnvelope.getSession().getAttribute(SessionAttributes.CURRENT_PAGE_NUMBER.getValue()))) {
            return 1;
        } else {
            return (Integer) requestEnvelope.getSession().getAttribute(SessionAttributes.CURRENT_PAGE_NUMBER.getValue()) + 1;
        }
    }

    public String checkForNull(final Object object, final String defaultValue) {
        if(object == null) return defaultValue;
        return object.toString();
    }

    public boolean isNull(final Object object) {
        return object == null;
    }

    public String prettyPrint(final Object object){
        try {
            return new ObjectMapper()
                    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            return ex.getMessage();
        }
    }
}
