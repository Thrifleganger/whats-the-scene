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

    private String location;
    private String date;
    private String keywords;
    private String category;
    private Sort sortBy;
    private Integer pageSize;
    private Integer pageNumber;
}
