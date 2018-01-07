package com.thrifleganger.alexa.scene.model.eventful.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Number {

    ONE("1", "one,first,1,1st"),
    TWO("2", "two,second,2,2nd"),
    THREE("3", "three,third,3,3rd"),
    FOUR("4", "four,fourth,4,4th"),
    FIVE("5", "five,fifth,5,5th");

    private final String value;
    private final String tags;
}
