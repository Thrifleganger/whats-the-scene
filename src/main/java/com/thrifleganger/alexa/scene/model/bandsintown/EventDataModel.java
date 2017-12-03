package com.thrifleganger.alexa.scene.model.bandsintown;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class EventDataModel {

    private String id;
    private String artist_id;
    private String url;
    private String on_sale_datetime;
    private String datetime;
    private String description;
    private VenueDataModel venue;
    private List<OfferDataModel> offers;
    private List<String> lineup;
}
