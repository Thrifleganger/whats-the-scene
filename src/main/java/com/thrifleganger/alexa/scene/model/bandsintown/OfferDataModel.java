package com.thrifleganger.alexa.scene.model.bandsintown;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class OfferDataModel {

    private String type;
    private String url;
    private String status;
}
