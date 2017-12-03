package com.thrifleganger.alexa.scene.model.eventful;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ImageRootModel {

    private Integer width;
    private Integer height;
    private String url;
    private String caption;
    private ImageModel small;
    private ImageModel medium;
    private ImageModel thumb;

}
