package com.thrifleganger.alexa.scene.model.eventful;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@EqualsAndHashCode
public class PerformerRootModel {

    private PerformerModel performer;
}
