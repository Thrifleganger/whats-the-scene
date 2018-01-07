package com.thrifleganger.alexa.scene.utils;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.thrifleganger.alexa.scene.constants.PathState;
import com.thrifleganger.alexa.scene.constants.SessionAttributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class EventfulHandlerUtils {

    public Integer generateNextPageNumber(final  SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        return (Integer) getSessionAttribute(requestEnvelope, SessionAttributes.CURRENT_PAGE_NUMBER).orElse(0) + 1;
    }

    public String checkForNull(final Object object, final String defaultValue) {
        if(object == null) return defaultValue;
        return object.toString();
    }

    public Optional<Object> getSessionAttribute(
            final SpeechletRequestEnvelope<IntentRequest> requestEnvelope,
            final SessionAttributes sessionAttribute
    ) {
        return Optional.ofNullable(requestEnvelope.getSession().getAttribute(sessionAttribute.getValue()));
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

    public PathState getCurrentPathState(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        return PathState.valueOf(
                (String) getSessionAttribute(requestEnvelope, SessionAttributes.PATH_STATE)
                        .orElse(PathState.UNDETERMINED.name())
        );
    }

    public String validateSsml(final String ssml) {
        if(Objects.isNull(ssml))
            return null;
        return ssml.replaceAll("&", "and")
                .replaceAll("<", "")
                .replaceAll(">", "");

    }
}
