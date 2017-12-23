package com.thrifleganger.alexa.scene.handler;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thrifleganger.alexa.scene.constants.PathState;
import com.thrifleganger.alexa.scene.constants.SessionAttributes;
import com.thrifleganger.alexa.scene.model.eventful.EventDataModel;
import com.thrifleganger.alexa.scene.model.eventful.EventfulResponse;
import com.thrifleganger.alexa.scene.model.eventful.enumeration.Number;
import com.thrifleganger.alexa.scene.model.eventful.enumeration.Slot;
import com.thrifleganger.alexa.scene.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GenericResultExpanderHandler {

    private final EventfulHandlerUtils utils;

    SpeechletResponse handle(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        final PathState pathState = utils.getCurrentPathState(requestEnvelope);
        final EventfulResponse eventfulResponse = new ObjectMapper().convertValue(
                utils.getSessionAttribute(requestEnvelope, SessionAttributes.EVENTFUL_RESPONSE)
                        .orElse(EventfulResponse.builder().build()),
                EventfulResponse.class
        );
        if (pathState.equals(PathState.AWAITING_MORE_RESULTS_OR_MORE_DETAILS)
                ||
            pathState.equals(PathState.AWAITING_MORE_DETAILS)
        ) {
            try {
                String numberString = AlexaHelper.getSlotValue(requestEnvelope.getRequest().getIntent(), Slot.NUMBER.getValue())
                        .orElseThrow(IllegalStateException::new);
                Integer number = returnTypeSafeNumberFor(numberString);
                if(number > 0 && number <= eventfulResponse.getEvents().getEvent().size()) {
                    final EventDataModel eventDataModel = eventfulResponse.getEvents().getEvent().get(number - 1);
                    return generateTellResponse(eventDataModel);
                }
                return SpeechletResponse.newAskResponse(
                        AlexaHelper.speech(Conversation.NUMBER_NOT_IN_RANGE),
                        AlexaHelper.reprompt(Conversation.NUMBER_NOT_IN_RANGE));
            } catch (NumberFormatException ignore) {
                return SpeechletResponse.newAskResponse(
                        AlexaHelper.speech(String.format(Conversation.NUMBER_NOT_VALID,
                                String.valueOf(eventfulResponse.getEvents().getEvent().size()))),
                        AlexaHelper.reprompt(String.format(Conversation.NUMBER_NOT_VALID,
                                String.valueOf(eventfulResponse.getEvents().getEvent().size())))
                );
            }
        }
        if (pathState.equals(PathState.AWAITING_MORE_DETAILS_FOR_SINGLE_RESULT)) {
            final EventDataModel eventDataModel = eventfulResponse.getEvents().getEvent().get(0);
            return generateTellResponse(eventDataModel);
        }
        return SpeechletResponse.newAskResponse(
                AlexaHelper.speech(Conversation.NOT_VALID_RESPONSE),
                AlexaHelper.reprompt(Conversation.NOT_VALID_RESPONSE)
        );
    }

    private Integer returnTypeSafeNumberFor(final String value) {
        return Integer.valueOf(Arrays.stream(Number.values())
                .filter(number -> number.getTags().contains(value))
                .map(Number::getValue)
                .findFirst()
                .orElseThrow(NumberFormatException::new));
    }

    private SpeechletResponse generateTellResponse(final EventDataModel eventDataModel) {
        return SpeechletResponse.newTellResponse(
                AlexaHelper.ssmlSpeech(String.format(
                        Ssml.EXPAND_RESULT,
                        utils.validateSsml(utils.checkForNull(eventDataModel.getTitle(), Conversation.UNKNOWN_EVENT)),
                        utils.validateSsml(utils.checkForNull(eventDataModel.getDescription(), Conversation.UNKNOWN_DESCRIPTION)),
                        utils.validateSsml(utils.checkForNull(eventDataModel.getVenueName(), Conversation.UNKNOWN_VENUE)),
                        utils.checkForNull(DateUtil.getFormattedDate(eventDataModel.getStartTime()), Conversation.UNKNOWN_DATE),
                        utils.checkForNull(DateUtil.getFormattedTime(eventDataModel.getStartTime()), Conversation.UNKNOWN_TIME),
                        utils.validateSsml(utils.checkForNull(eventDataModel.getCityName(), Conversation.UNKNOWN_LOCATION))
                )),
                AlexaHelper.simpleCard(
                        utils.checkForNull(eventDataModel.getTitle(), Conversation.UNKNOWN_EVENT),
                        String.format(Conversation.EXPAND_RESULT_CARD,
                                utils.checkForNull(eventDataModel.getDescription(), Conversation.UNKNOWN_DESCRIPTION),
                                utils.checkForNull(eventDataModel.getVenueName(), Conversation.UNKNOWN_VENUE),
                                utils.checkForNull(eventDataModel.getStartTime(), Conversation.UNKNOWN_DATE),
                                utils.checkForNull(eventDataModel.getCityName(), Conversation.UNKNOWN_LOCATION),
                                utils.checkForNull(eventDataModel.getUrl(), Conversation.UNKNOWN_URL)
                        )
                )
        );
    }
}
