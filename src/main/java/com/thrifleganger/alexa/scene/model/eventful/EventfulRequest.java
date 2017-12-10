package com.thrifleganger.alexa.scene.model.eventful;

import com.thrifleganger.alexa.scene.model.eventful.enumeration.Category;
import com.thrifleganger.alexa.scene.model.eventful.enumeration.Sort;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class EventfulRequest {

    private Optional<String> location;
    private Optional<String> date;
    private Optional<String> keywords;
    private Optional<String> category;
    private Optional<Sort> sortBy;
    private Optional<Integer> pageSize;
}
