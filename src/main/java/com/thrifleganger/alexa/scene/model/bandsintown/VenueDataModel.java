package com.thrifleganger.alexa.scene.model.bandsintown;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class VenueDataModel {

    private String name;
    private String latitude;
    private String longitude;
    private String city;
    private String region;
    private String country;
}
