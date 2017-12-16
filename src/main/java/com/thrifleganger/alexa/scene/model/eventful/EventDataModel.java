package com.thrifleganger.alexa.scene.model.eventful;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("start_time")
    private String startTime;
    @JsonProperty("stop_time")
    private String stopTime;
    @JsonProperty("venue_id")
    private String venueId;
    @JsonProperty("venue_url")
    private String venueUrl;
    @JsonProperty("venue_name")
    private String venueName;
    @JsonProperty("venue_display")
    private Integer venueDisplay;
    @JsonProperty("venue_address")
    private String venueAddress;
    @JsonProperty("city_name")
    private String cityName;
    @JsonProperty("region_name")
    private String regionName;
    @JsonProperty("region_abbr")
    private String regionAbbr;
    @JsonProperty("postal_code")
    private String postalCode;
    @JsonProperty("country_name")
    private String countryName;
    @JsonProperty("country_abbr2")
    private String countryAbbr2;
    @JsonProperty("olson_path")
    private String olsonPath;
    @JsonProperty("all_day")
    private Integer allDay;
    private Float latitude;
    private Float longitude;
    @JsonProperty("geocode_type")
    private String geocodeType;

    private String created;
    private String modified;
    private String owner;

    //private PerformerRootModel performers;
    //private ImageRootModel image;
}
