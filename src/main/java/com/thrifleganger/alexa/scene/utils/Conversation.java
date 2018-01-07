package com.thrifleganger.alexa.scene.utils;

public class Conversation {

    private Conversation() {}

    public static final String WELCOME = "Welcome to event space. You can ask me something specific, or just say, What's going on, for an interactive walkthrough. ";
    public static final String RESTART = "Let's start over. ";
    public static final String ERROR_HITTING_ENDPOINT = "Sorry, something went wrong while fetching the data. ";
    public static final String NO_RESULTS_FOUND = "No results were found for this query. ";
    public static final String FOUND_RESULTS = "%s results found. ";
    public static final String ONE_RESULT_FOUND = "1 result found. ";
    public static final String READING_OUT_RESULTS = "Reading out the first %s results. ";
    public static final String READING_OUT_NEXT_PAGE = "Reading out the next %s results. ";
    public static final String GOODBYE = "Thank you for using event space. Good bye! ";
    public static final String FETCH_MORE_RESULTS = "Would you like me to fetch more results? Or, ";
    public static final String EXPAND_MANY_FURTHER = "just recite the number of the entry that you heard for a more detailed description. ";
    public static final String EXPAND_ONE_RESULT_FURTHER = "Would you like a more detailed description for this result? ";
    public static final String NO_MORE_RESULTS = "There are no more results. ";
    public static final String NUMBER_NOT_IN_RANGE = "The number you mentioned was not in range, could you try again? ";
    public static final String NUMBER_NOT_VALID = "The number you mentioned was not valid, could you try again with a number between one and %s? ";
    public static final String NOT_VALID_RESPONSE = "This is not a valid response here. You can try again, or just say Help, for a walkthrough. ";

    //Recovery
    public static final String SCENE_DIALOG_RESTART = "Let's try again. Which category of events are you looking for? ";
    public static final String CATEGORY_INVALID_RETRY = "I couldn't find that category. Here's a list of categories to choose from. " +
            "%s. Which category are you looking for? ";

    //Reprompt
    public static final String GENERIC_REPROMT = "I didn't get a response. ";
    public static final String WELCOME_REPROMPT = "I didn't get a response. You can ask me something specific, or just say, What's going on in town, for an interactive walkthrough. ";


    //Card text
    public static final String ERROR_HITTING_ENDPOINT_TITLE = "Error occured ";
    public static final String NO_RESULTS_FOUND_TITLE = "No results found ";
    public static final String RESULT_EXPANDED_CARD_DESC = "%s) Title: %s\nVenue: %s\nDate: %s\nCity: %s\n\n ";
    public static final String FOUND_RESULTS_WITH_PAGE_NUMBER = "%s results found - Page %s ";
    public static final String EXPAND_RESULT_CARD = "Description: %s\nVenue: %s\nDate: %s\nCity: %s\nURL: %s\n ";

    //Defaults for null
    public static final String UNKNOWN_NUMBER_OF = "unknown number of";
    public static final String FEW = "few";
    public static final String UNKNOWN_EVENT = "an unknown event";
    public static final String UNKNOWN_VENUE = "an unknown venue";
    public static final String UNKNOWN_DATE = "an unknown day";
    public static final String UNKNOWN_LOCATION = "an unknown location";
    public static final String UNKNOWN_TIME = "an unknown time";
    public static final String UNKNOWN_DESCRIPTION = "No description provided";
    public static final String UNKNOWN_URL = "No URL provided";

    //Help
    public static final String HELP_SKILL_INVOKED = "From here, you can take two approaches. The conversational approach is the " +
            "most accurate to begin with, where you can ask me, What's going on today? Or, what's happening in dublin tomorrow? " +
            "I will try to ask you all the relevant information to get the most relevant result back to you. The one shot approach " +
            "is where you can skip all the hassle and get your results fast. Just ask me something like, When is Taylor swift playing " +
            "in California? or Find gigs for Lord Huron. At any point, if you wish to come back to the main menu, just say Start over. ";
    public static final String HELP_MORE_DETAILS = "You can ask me to give you more details for a specific result that you heard " +
            "by saying, Expand number three, or, Give me more details for the second result. ";
    public static final String HELP_MORE_RESULTS = "You can also ask me to fetch the next page of results. Remember that all " +
            "results are sorted by date. Just say, fetch more results, or, go to the next page. ";
}
