package com.thrifleganger.alexa.scene.exception.handler;

import java.util.Optional;

public class RestResult<T> {

    private T result;
    private Throwable exception;

    public static <T> RestResult<T> success(T object) {
        return new RestResult<T>(object, null);
    }

    public static <T> RestResult<T> error(Throwable exception) {
        return new RestResult<T>(null, exception);
    }

    public boolean hasErrors() {
        return this.exception != null || this.result == null;
    }

    public Optional<T> getResultObject() {
        return Optional.ofNullable(result);
    }

    public Throwable getException() {
        return exception;
    }

    private RestResult(T result, Throwable exception) {
        this.result = result;
        this.exception = exception;
    }
}
