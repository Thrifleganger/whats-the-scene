package com.thrifleganger.alexa.scene.handler;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import org.springframework.stereotype.Component;

@Component
public class BandsInTownHandler implements GigsHandler {

    public SpeechletResponse handleHello(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        return null;
    }

    public SpeechletResponse handleGigSearch(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        return null;
    }
}
