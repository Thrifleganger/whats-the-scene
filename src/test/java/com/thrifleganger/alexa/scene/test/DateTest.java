package com.thrifleganger.alexa.scene.test;

import com.thrifleganger.alexa.scene.model.eventful.enumeration.Category;
import com.thrifleganger.alexa.scene.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Slf4j
public class DateTest {

    @Test
    public void dateTest() {

        String date = "2008-03-26 14:05:23";
        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        log.info(dateTime.toLocalDate().toString());
        log.info(dateTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString());

        log.info(DateUtil.getFormattedDate(date));
        log.info(DateUtil.getFormattedTime(date));

    }

    @Test
    public void containsTest() {
        String music = "gig";
        log.info(Category.MUSIC.getTags());
        log.info(String.valueOf(Category.MUSIC.getTags().contains(music)));
    }
}
