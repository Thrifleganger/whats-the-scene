package com.thrifleganger.alexa.scene;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import me.ccampo.spring.aws.lambda.ApplicationContextProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class CustomRequestStreamHandler implements RequestStreamHandler, ApplicationContextProvider {

    private final RequestStreamHandler handler = (RequestStreamHandler)this.getApplicationContext().getBean(RequestStreamHandler.class);

    public CustomRequestStreamHandler() {
    }

    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
            this.handler.handleRequest(input, output, context);
    }
}
