package com.thrifleganger.alexa.scene.utils;

public class Ssml {

    public static final String SPEAK_START = " <speak> ";
    public static final String SPEAK_END = " </speak> ";
    public static final String SUMMARIZE_RESULT = "<p><s> %s. </s> " +      //Count
            "<s> %s, " +                                                                                        //Title
            "at %s, " +                                                                                         //Venue
            "on <say-as interpret-as=\"date\">%s</say-as>, " +                                                   //Date
            "at <say-as interpret-as=\"time\">%s</say-as>, " +                                                  //Time
            "in %s </s></p>";                                                                                   //City
    public static final String EXPAND_RESULT = "<speak><p><s> %s </s>" +
            " %s. " +
            "<s>This event is happening at %s, " +
            "on <say-as interpret-as=\"date\">%s</say-as> " +
            "at <say-as interpret-as=\"time\">%s</say-as>, " +
            "in %s </s>" +
            "<s>For more information on this event, check your Alexa app </s></p></speak>";
}
