package com.thrifleganger.alexa.scene.speechlet;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.thrifleganger.alexa.scene.handler.EventfulHandler;
import com.thrifleganger.alexa.scene.utils.AlexaHelper;
import com.thrifleganger.alexa.scene.utils.Conversation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SceneSpeechlet implements SpeechletV2 {

    private final EventfulHandler eventfulHandler;

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> speechletRequestEnvelope) {
        log.info("Session started.");
    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> speechletRequestEnvelope) {
        return SpeechletResponse.newAskResponse(AlexaHelper.speech(Conversation.WELCOME),
                AlexaHelper.reprompt(Conversation.WELCOME_REPROMPT));
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> speechletRequestEnvelope) {
        Intent incomingIntent = speechletRequestEnvelope.getRequest().getIntent();
        log.info("Intent triggered: " + incomingIntent.getName());
        switch (incomingIntent.getName()) {
            case "SceneInvocationIntent":
                return eventfulHandler.handleSceneInvocationIntent(speechletRequestEnvelope);
            case "GigSearchIntent":
                return eventfulHandler.handleGigSearch(speechletRequestEnvelope);
            case "AMAZON.StopIntent":
                return eventfulHandler.handleStopEvent(speechletRequestEnvelope);
            case "AMAZON.HelpIntent":
                return eventfulHandler.handleHelpEvent(speechletRequestEnvelope);
            case "AMAZON.CancelIntent":
                return eventfulHandler.handleCancelEvent(speechletRequestEnvelope);
            default:
                throw new IllegalArgumentException("Unknown Intent: " + incomingIntent.getName());
        }
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> speechletRequestEnvelope) {
        log.info("Session ended.");
    }
}
