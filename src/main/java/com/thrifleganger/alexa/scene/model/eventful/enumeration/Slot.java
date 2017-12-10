package com.thrifleganger.alexa.scene.model.eventful.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Slot {

    CATEGORY("category"),
    KEYWORDS("keywords"),
    LOCATION("location"),
    DATE("date");

    private final String value;
}
