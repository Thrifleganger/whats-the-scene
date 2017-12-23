package com.thrifleganger.alexa.scene.model.eventful.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Number {

    ONE("1", "one,first"),
    TWO("2", "two,second"),
    THREE("3", "three,third"),
    FOUR("4", "four,fourth"),
    FIVE("5", "five,fifth");

    private final String value;
    private final String tags;
}
