package com.thrifleganger.alexa.scene.utils;

public class Conversation {

    private Conversation() {}

    public static final String WELCOME = "Welcome to what's the scene. You can ask me something specific, or just say, What's going on, for an interactive walkthrough. ";
    public static final String RESTART = "Let's start over. ";
    public static final String ERROR_HITTING_ENDPOINT = "Sorry, something went wrong while fetching the data. ";
    public static final String NO_RESULTS_FOUND = "No results were found for this query. ";
    public static final String FOUND_RESULTS = "%s results found. ";
    public static final String ONE_RESULT_FOUND = "1 result found. ";
    public static final String READING_OUT_RESULTS = "Reading out the first %s results. ";
    public static final String READING_OUT_NEXT_PAGE = "Reading out the next %s results. ";
    public static final String GOODBYE = "Thank you for using What's the scene. Sponsored by Wheatabix protein. Don't think, just eat. ";
    public static final String FETCH_MORE_RESULTS = "Would you like me to fetch more results? Or, ";
    public static final String EXPAND_MANY_FURTHER = "just recite the number of the entry that you heard for a more detailed description. ";
    public static final String EXPAND_ONE_RESULT_FURTHER = "Would you like a more detailed description for this result? ";
    public static final String NO_MORE_RESULTS = "There are no more results. ";

    //Recovery
    public static final String SCENE_DIALOG_RESTART = "Let's try again. Which category of events are you looking for? ";
    public static final String CATEGORY_INVALID_RETRY = "I couldn't find that category. Here's a list of categories to choose from. " +
            "%s. Which category are you looking for? ";

    //Reprompt
    public static final String GENERIC_REPROMT = "I didn't get a response. ";
    public static final String WELCOME_REPROMPT = "I didn't get a response. You can ask me something specific, or just say, What's going on in town, for an interactive walkthrough. ";


    //Card Title text
    public static final String ERROR_HITTING_ENDPOINT_TITLE = "Error occured ";
    public static final String NO_RESULTS_FOUND_TITLE = "No results found ";
    public static final String RESULT_EXPANDED_CARD_DESC = "%s) Title: %s\nVenue: %s\nDate: %s\nCity: %s\n\n ";
    public static final String FOUND_RESULTS_WITH_PAGE_NUMBER = "%s results found - Page %s ";

    //Defaults for null
    public static final String UNKNOWN_NUMBER_OF = "unknown number of";
    public static final String FEW = "few";
    public static final String UNKNOWN_EVENT = "an unknown event";
    public static final String UNKNOWN_VENUE = "an unknown venue";
    public static final String UNKNOWN_DATE = "an unknown day";
    public static final String UNKNOWN_LOCATION = "an unknown location";
    public static final String UNKNOWN_TIME = "an unknown time";
    public static final String UNKNOWN_DESCRIPTION = "No description provided";
}
