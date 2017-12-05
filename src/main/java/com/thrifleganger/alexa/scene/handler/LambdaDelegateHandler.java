package com.thrifleganger.alexa.scene.handler;

import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletRequestHandler;
import com.amazon.speech.speechlet.SpeechletRequestHandlerException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.thrifleganger.alexa.scene.speechlet.SceneSpeechlet;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LambdaDelegateHandler implements RequestStreamHandler {

    private final SceneSpeechlet speechlet;
    private final SpeechletRequestHandler speechletRequestHandler;

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        byte[] serializedSpeechletRequest = IOUtils.toByteArray(input);

        byte[] outputBytes;
        try {
            outputBytes = this.speechletRequestHandler.handleSpeechletCall(this.speechlet, serializedSpeechletRequest);
        } catch (SpeechletException | SpeechletRequestHandlerException var7) {
            throw new RuntimeException(var7);
        }

        output.write(outputBytes);
    }
}
