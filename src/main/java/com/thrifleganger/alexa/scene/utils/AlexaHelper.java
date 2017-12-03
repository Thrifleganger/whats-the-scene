package com.thrifleganger.alexa.scene.utils;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.ui.*;

import java.util.Optional;

public class AlexaHelper {

    private AlexaHelper() {}

    public static Optional<String> getSlotValue(Intent intent, String slotName) {
        return Optional.ofNullable(intent.getSlot(slotName).getValue());
    }

    public static PlainTextOutputSpeech speech(String text) {
        PlainTextOutputSpeech result = new PlainTextOutputSpeech();
        result.setText(text);
        return result;
    }

    public static Reprompt reprompt(String question) {
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech(question));
        return reprompt;
    }

    public static SsmlOutputSpeech ssmlSpeech(String ssml) {
        SsmlOutputSpeech result = new SsmlOutputSpeech();
        result.setSsml(ssml);
        return result;
    }

    public static SimpleCard simpleCard(String title, String content) {
        SimpleCard simpleCard = new SimpleCard();
        simpleCard.setTitle(title);
        simpleCard.setContent(content);
        return simpleCard;
    }

    public static StandardCard standardCard(
            String title,
            String content,
            String smallImageUrl,
            String largeImageUrl
    ) {
        StandardCard standardCard = new StandardCard();
        Image image = new Image();
        standardCard.setTitle(title);
        standardCard.setText(content);
        if(smallImageUrl != null) image.setSmallImageUrl(smallImageUrl);
        if(largeImageUrl != null) image.setLargeImageUrl(largeImageUrl);
        standardCard.setImage(image);
        return standardCard;
    }
}
