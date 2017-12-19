package com.thrifleganger.alexa.scene.handler;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.ConfirmationStatus;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.dialog.directives.DialogIntent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thrifleganger.alexa.scene.constants.PathState;
import com.thrifleganger.alexa.scene.constants.SessionAttributes;
import com.thrifleganger.alexa.scene.model.eventful.EventDataModel;
import com.thrifleganger.alexa.scene.model.eventful.EventfulRequest;
import com.thrifleganger.alexa.scene.model.eventful.EventfulResponse;
import com.thrifleganger.alexa.scene.model.eventful.enumeration.Category;
import com.thrifleganger.alexa.scene.model.eventful.enumeration.Slot;
import com.thrifleganger.alexa.scene.model.eventful.enumeration.Sort;
import com.thrifleganger.alexa.scene.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventfulHandler {

    private final GenericRequestHandler genericRequestHandler;
    private final DialogModelHandler dialogModelHandler;
    private final EventfulHandlerUtils utils;

    public SpeechletResponse handleSceneInvocationIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        //log.info(utils.prettyPrint(requestEnvelope));
        IntentRequest request = requestEnvelope.getRequest();
        if(request.getDialogState().equals(IntentRequest.DialogState.COMPLETED)) {
            return genericRequestHandler.handle(
                    requestEnvelope,
                    buildEventfulRequestAfterDialogEnds(request, requestEnvelope)
            );
        }
        DialogIntent updatedIntent = new DialogIntent(request.getIntent());
        if(updatedIntent.getConfirmationStatus().equals(ConfirmationStatus.DENIED)) {
            return dialogModelHandler.elicitResponseToRestartDialog(updatedIntent);
        }
        if(!isSlotValueNull(updatedIntent, Slot.CATEGORY) && !isSlotStatusConfirmed(updatedIntent, Slot.CATEGORY)) {
            if(isCategoryValid(updatedIntent)) {
                setTypeSafeCategoryAndConfirmStatus(updatedIntent);
            } else {
                return dialogModelHandler.elicitResponseToReinitializeCategorySlot(updatedIntent);
            }
        }
        return dialogModelHandler.delegateResponseToContinueDialog(updatedIntent);
    }

    public SpeechletResponse handleGigSearch(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        final IntentRequest request = requestEnvelope.getRequest();
        if(request.getDialogState().equals(IntentRequest.DialogState.COMPLETED)) {
            return genericRequestHandler.handle(
                    requestEnvelope,
                    buildEventfulRequestAfterDialogEnds(request, requestEnvelope)
            );
        }
        DialogIntent updatedIntent = new DialogIntent(request.getIntent());
        return dialogModelHandler.delegateResponseToContinueDialog(updatedIntent);
    }

    public SpeechletResponse handleFetchMoreResults(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {

        if(utils.getCurrentPathState(requestEnvelope).equals(PathState.AWAITING_MORE_RESULTS_OR_MORE_DETAILS)) {
            log.info(utils.prettyPrint(utils.getSessionAttribute(requestEnvelope, SessionAttributes.EVENTFUL_REQUEST).orElse(null)));
            final EventfulRequest request = new ObjectMapper().convertValue(
                    utils.getSessionAttribute(requestEnvelope, SessionAttributes.EVENTFUL_REQUEST).orElse(EventfulRequest.builder().build()),
                    EventfulRequest.class);
            request.setPageNumber(utils.generateNextPageNumber(requestEnvelope));
            return  genericRequestHandler.handle(requestEnvelope, request);
        }
        return SpeechletResponse.newTellResponse(AlexaHelper.speech(Conversation.NO_MORE_RESULTS));
    }

    public SpeechletResponse handleExpandResults(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        final IntentRequest request = requestEnvelope.getRequest();
        final PathState pathState = utils.getCurrentPathState(requestEnvelope);
        if(request.getDialogState().equals(IntentRequest.DialogState.COMPLETED)) {
            if (pathState.equals(PathState.AWAITING_MORE_RESULTS_OR_MORE_DETAILS)
                    ||
                pathState.equals(PathState.AWAITING_MORE_DETAILS)
            ) {
                try {
                    Integer number = Integer.valueOf(AlexaHelper.getSlotValue(request.getIntent(), Slot.NUMBER.getValue()).orElse("0"));
                    if(number > 0 && number <= 5) {
                        final EventfulResponse eventfulResponse = new ObjectMapper().convertValue(
                                utils.getSessionAttribute(requestEnvelope, SessionAttributes.EVENTFUL_RESPONSE)
                                        .orElse(EventfulResponse.builder().build()),
                                EventfulResponse.class
                        );
                        final EventDataModel eventDataModel = eventfulResponse.getEvents().getEvent().get(number - 1);
                        return SpeechletResponse.newTellResponse(AlexaHelper.ssmlSpeech(
                                String.format(
                                        Ssml.EXPAND_RESULT,
                                        utils.validateSsml(utils.checkForNull(eventDataModel.getTitle(), Conversation.UNKNOWN_EVENT)),
                                        utils.validateSsml(utils.checkForNull(eventDataModel.getDescription(), Conversation.UNKNOWN_DESCRIPTION)),
                                        utils.validateSsml(utils.checkForNull(eventDataModel.getVenueName(), Conversation.UNKNOWN_VENUE)),
                                        utils.checkForNull(DateUtil.getFormattedDate(eventDataModel.getStartTime()), Conversation.UNKNOWN_DATE),
                                        utils.checkForNull(DateUtil.getFormattedTime(eventDataModel.getStartTime()), Conversation.UNKNOWN_TIME),
                                        utils.validateSsml(utils.checkForNull(eventDataModel.getCityName(), Conversation.UNKNOWN_LOCATION))
                                )
                        ));
                    }
                    return SpeechletResponse.newAskResponse(
                            AlexaHelper.speech("The number you said was not in range, could you try again? "),
                            AlexaHelper.reprompt("The number you said was not in range, could you try again? "));
                } catch (NumberFormatException ignore) {
                    return SpeechletResponse.newAskResponse(
                            AlexaHelper.speech("The number you said was not valid, could you try again? "),
                            AlexaHelper.reprompt("The number you said was not valid, could you try again? "));
                }
            }
            if (pathState.equals(PathState.AWAITING_MORE_DETAILS_FOR_SINGLE_RESULT)) {
                return SpeechletResponse.newTellResponse(AlexaHelper.speech("Expanding result. "));
            }
            return SpeechletResponse.newTellResponse(AlexaHelper.speech("This is not a valid response here. "));
        }
        return dialogModelHandler.delegateResponseToContinueDialog(new DialogIntent(request.getIntent()));
    }

    public SpeechletResponse handleStopEvent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        return SpeechletResponse.newTellResponse(
                AlexaHelper.speech(Conversation.GOODBYE)
        );
    }

    public SpeechletResponse handleHelpEvent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        return SpeechletResponse.newTellResponse(AlexaHelper.speech("Come on! You know what to do"));
    }

    public SpeechletResponse handleCancelEvent(SpeechletRequestEnvelope<IntentRequest> speechletRequestEnvelope) {
        return SpeechletResponse.newAskResponse(
                AlexaHelper.speech(Conversation.RESTART + Conversation.WELCOME),
                AlexaHelper.reprompt(Conversation.WELCOME_REPROMPT)
        );
    }

    private EventfulRequest buildDefaultRequest() {
        return EventfulRequest.builder()
                .build();
    }

    private EventfulRequest buildEventfulRequestAfterDialogEnds(
            final IntentRequest intentRequest,
            final SpeechletRequestEnvelope<IntentRequest> requestEnvelope
    ) {
        EventfulRequest eventfulRequest = buildDefaultRequest();
        final Optional<String> keywords = Optional.ofNullable(intentRequest.getIntent().getSlot(Slot.KEYWORDS.getValue()).getValue());
        eventfulRequest.setKeywords(
                keywords.filter(string -> !string.contains("anything")).orElse(null)
        );
        eventfulRequest.setLocation(intentRequest.getIntent().getSlot(Slot.LOCATION.getValue()).getValue());
        eventfulRequest.setDate(intentRequest.getIntent().getSlot(Slot.DATE.getValue()).getValue());
        eventfulRequest.setCategory(intentRequest.getIntent().getSlot(Slot.CATEGORY.getValue()).getValue());
        eventfulRequest.setSortBy(Sort.DATE);
        eventfulRequest.setPageSize(5);
        eventfulRequest.setPageNumber(utils.generateNextPageNumber(requestEnvelope));
        log.info(eventfulRequest.toString());
        return eventfulRequest;
    }

    private void setTypeSafeCategoryAndConfirmStatus(final DialogIntent updatedIntent) {
        updatedIntent.getSlots().entrySet()
                .stream()
                .filter(slotEntry -> Slot.CATEGORY.getValue().equals(slotEntry.getKey()))
                .forEach(slotEntry -> {
                    slotEntry.getValue().setValue(returnTypeSafeCategoryFor(slotEntry.getValue().getValue()));
                    slotEntry.getValue().setConfirmationStatus(ConfirmationStatus.CONFIRMED);
                });
    }

    private String returnTypeSafeCategoryFor(final String value) {
        return Arrays.stream(Category.values())
                .filter(category -> category.getTags().contains(value))
                .map(Category::getKeyword)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private boolean isCategoryValid(DialogIntent intent) {

        return intent.getSlots().entrySet()
                .stream()
                .filter(slotEntry -> Slot.CATEGORY.getValue().equals(slotEntry.getKey()))
                .map(slotEntry -> slotEntry.getValue().getValue())
                .anyMatch(slotValue ->
                    Arrays.stream(Category.values())
                            .filter(category -> category.getTags().contains(slotValue.toLowerCase()))
                            .collect(Collectors.toList()).size() == 1
                );
    }

    private boolean isSlotStatusConfirmed(final DialogIntent updatedIntent, final Slot slot) {
        return updatedIntent.getSlots().entrySet()
                .stream()
                .filter(slotEntry -> slot.getValue().equals(slotEntry.getKey()))
                .map(slotEntry -> slotEntry.getValue().getConfirmationStatus())
                .noneMatch(confirmationStatus -> confirmationStatus.equals(ConfirmationStatus.NONE));
    }

    private boolean isSlotValueNull(final DialogIntent intent, final Slot slot) {
        return intent.getSlots().entrySet()
                .stream()
                .filter(slotEntry -> slot.getValue().equals(slotEntry.getKey()))
                .anyMatch(slotEntry -> slotEntry.getValue().getValue() == null);
    }
}
