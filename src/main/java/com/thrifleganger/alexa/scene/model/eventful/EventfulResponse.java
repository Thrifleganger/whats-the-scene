package com.thrifleganger.alexa.scene.model.eventful;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class EventfulResponse {

    @JsonProperty("total_items")
    private Integer totalItems;
    @JsonProperty("page_size")
    private Integer pageSize;
    @JsonProperty("page_count")
    private Integer pageCount;
    @JsonProperty("page_number")
    private Integer pageNumber;
    @JsonProperty("page_items")
    private Integer pageItems;
    @JsonProperty("first_item")
    private Integer firstItem;
    @JsonProperty("last_item")
    private Integer lastItem;
    @JsonProperty("search_time")
    private Float searchTime;
    private EventsArrayModel events;

}
