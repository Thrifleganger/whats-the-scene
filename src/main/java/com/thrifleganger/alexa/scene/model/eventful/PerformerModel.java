package com.thrifleganger.alexa.scene.model.eventful;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PerformerModel {

    private String id;
    private String creator;
    private String linker;
    private String name;
    private String url;
    private String short_bio;
}
