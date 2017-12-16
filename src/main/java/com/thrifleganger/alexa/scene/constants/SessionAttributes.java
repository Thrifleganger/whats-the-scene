package com.thrifleganger.alexa.scene.constants;

public enum SessionAttributes {

    EVENTFUL_REQUEST("eventfulRequest"),
    EVENTFUL_RESPONSE("eventfulResponse"),
    TOTAL_NUMBER_RESULTS("totalNumberOfResuts"),
    CURRENT_PAGE_NUMBER("currentPageNumber"),
    CURRENT_PAGE_SIZE("currentPageSize"),
    MAX_PAGE_SIZE("maxPageSize"),
    PATH_STATE("pathState");

    private final String value;

    SessionAttributes(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
