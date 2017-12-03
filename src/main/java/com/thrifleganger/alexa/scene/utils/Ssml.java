package com.thrifleganger.alexa.scene.utils;

public class Ssml {

    public static final String SPEAK_START = " <speak> ";
    public static final String SPEAK_END = " </speak> ";
    public static final String RESULT_EXPANDED = "<p><s> %s. </s> " +      //Count
            "<s> %s, " +                                                                                        //Title
            "at %s, " +                                                                                         //Venue
            "on <say-as interpret-as=\"date\">%s</say-as> " +                                                   //Date
            "at <say-as interpret-as=\"time\">%s</say-as>, " +                                                  //Time
            "in %s </s></p>";                                                                                   //City
}
