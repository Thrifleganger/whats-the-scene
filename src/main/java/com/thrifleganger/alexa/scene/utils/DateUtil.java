package com.thrifleganger.alexa.scene.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    private static LocalDateTime convertToLocalDateTime(String dateTime) {

        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String getFormattedDate(String dateTime) {

        return convertToLocalDateTime(dateTime).toLocalDate().toString();
    }

    public static String getFormattedTime(String dateTime) {

        return convertToLocalDateTime(dateTime).toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString();
    }
}
