package com.thrifleganger.alexa.scene.model.eventful;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class EventsArrayModel {

    List<EventDataModel> event;
}
