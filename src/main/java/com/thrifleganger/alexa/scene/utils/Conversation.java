package com.thrifleganger.alexa.scene.utils;

public class Conversation {

    private Conversation() {}

    public static final String WELCOME = "Welcome to what's the scene. What kind of events are you looking for? ";
    public static final String ERROR_HITTING_ENDPOINT = "Sorry, something went wrong while fetching the data. ";
    public static final String NO_RESULTS_FOUND = "No results were found for this query. ";
    public static final String FOUND_RESULTS = "%s results found. ";
    public static final String ONE_RESULT_FOUND = "1 result found. ";
    public static final String READING_OUT_RESULTS = "Reading out the first %s results. ";
    public static final String RESULT_EXPANDED = "%s, %s at %s on %s in %s. ";

    //Reprompt
    public static final String WELCOME_REPROMPT = "I didn't get a response. Would you like me to list out a few categories for you? ";


    //Card Title text
    public static final String ERROR_HITTING_ENDPOINT_TITLE = "Error occured ";
    public static final String NO_RESULTS_FOUND_TITLE = "No results found ";
    public static final String RESULT_EXPANDED_CARD_DESC = "%s) Title: %s\nVenue: %s\nDate: %s\nCity: %s\n\n ";

    //Defaults for null
    public static final String UNKNOWN_NUMBER_OF = "unknown number of";
    public static final String FEW = "few";
    public static final String UNKNOWN_EVENT = "an unknown event";
    public static final String UNKNOWN_VENUE = "an unknown venue";
    public static final String UNKNOWN_DATE = "an unknown day";
    public static final String UNKNOWN_LOCATION = "an unknown location";
    public static final String UNKNOWN_TIME = "an unknown time";
}
