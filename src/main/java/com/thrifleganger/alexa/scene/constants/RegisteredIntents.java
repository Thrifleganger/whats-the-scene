package com.thrifleganger.alexa.scene.constants;

public enum RegisteredIntents {

    SCENE_INVOCATION("SceneInvocationIntent"),
    GIG_SEARCH("GigSearchIntent"),
    FETCH_MORE_RESULTS("FetchMoreResultsIntent"),
    EXPAND_RESULTS("ExpandResultsIntent"),
    HELP("AMAZON.HelpIntent"),
    CANCEL("AMAZON.CancelIntent"),
    STOP("AMAZON.StopIntent");


    private final String value;

    RegisteredIntents(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RegisteredIntents findByValue(String value) {
        for(RegisteredIntents registeredIntent : values()) {
            if(registeredIntent.getValue().equals(value))
                return registeredIntent;
        }
        throw new IllegalStateException("No enum constant for " + value);
    }
}
