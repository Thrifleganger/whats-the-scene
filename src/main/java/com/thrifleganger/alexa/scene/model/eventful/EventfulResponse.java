package com.thrifleganger.alexa.scene.model.eventful;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class EventfulResponse {

    private Integer total_items;
    private Integer page_size;
    private Integer page_count;
    private Integer page_number;
    private Integer page_items;
    private Integer first_item;
    private Integer last_item;
    private Float search_time;
    private EventsArrayModel events;

}
