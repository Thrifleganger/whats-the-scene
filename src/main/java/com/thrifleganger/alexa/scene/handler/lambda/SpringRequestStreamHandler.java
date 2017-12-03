package com.thrifleganger.alexa.scene.handler.lambda;

import com.amazon.speech.speechlet.SpeechletRequestHandler;
import com.amazon.speech.speechlet.lambda.LambdaSpeechletRequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class SpringRequestStreamHandler implements RequestStreamHandler, ApplicationContextProvider {

    private final RequestStreamHandler handler;

    public SpringRequestStreamHandler() {
        this.handler = getApplicationContext().getBean(RequestStreamHandler.class);
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        handler.handleRequest(inputStream, outputStream, context);
    }
}
