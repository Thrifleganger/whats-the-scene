package com.thrifleganger.alexa.scene.model.eventful;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDataModel {

    private String id;
    private String url;
    private String title;
    private String description;
    private String start_time;
    private String stop_time;
    private String venue_id;
    private String venue_url;
    private String venue_name;
    private Integer venue_display;
    private String venue_address;
    private String city_name;
    private String region_name;
    private String region_abbr;
    private String postal_code;
    private String country_name;
    private String country_abbr2;
    private String olson_path;
    private Integer all_day;
    private Float latitude;
    private Float longitude;
    private String geocode_type;

    private String created;
    private String modified;
    private String owner;

    private PerformerRootModel performers;
    private ImageRootModel image;
}
