package com.thrifleganger.alexa.scene.handler;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.ConfirmationStatus;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;
import com.amazon.speech.speechlet.dialog.directives.DialogIntent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thrifleganger.alexa.scene.exception.handler.RestResult;
import com.thrifleganger.alexa.scene.model.eventful.EventfulRequest;
import com.thrifleganger.alexa.scene.model.eventful.EventfulResponse;
import com.thrifleganger.alexa.scene.model.eventful.enumeration.Category;
import com.thrifleganger.alexa.scene.model.eventful.enumeration.Slot;
import com.thrifleganger.alexa.scene.model.eventful.enumeration.Sort;
import com.thrifleganger.alexa.scene.service.EventfulRestService;
import com.thrifleganger.alexa.scene.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventfulHandler {

    private final EventfulRestService eventfulRestService;
    private final EventfulHandlerUtils eventfulHandlerUtils;

    public SpeechletResponse handleWelcome(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        log.info("HelloIntent");
        return SpeechletResponse.newAskResponse(
                AlexaHelper.speech(Conversation.WELCOME),
                AlexaHelper.reprompt(Conversation.WELCOME_REPROMPT));
    }

    public SpeechletResponse handleSceneInvocationIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        //log.info(ToStringBuilder.reflectionToString(requestEnvelope.getRequest().getIntent()));
        /*try {
            log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(requestEnvelope.getRequest().getIntent()));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }*/
        IntentRequest request = requestEnvelope.getRequest();
        if(request.getDialogState().equals(IntentRequest.DialogState.COMPLETED)) {
            return handleGenericRequest(requestEnvelope, buildEventfulRequestAfterDialogEnds(request));
        }
        DialogIntent updatedIntent = new DialogIntent(request.getIntent());
        if(updatedIntent.getConfirmationStatus().equals(ConfirmationStatus.DENIED)) {
            return eventfulHandlerUtils.elicitResponseToRestartDialog(updatedIntent);
        }
        if(!isSlotValueNull(updatedIntent, Slot.CATEGORY) && !isSlotStatusConfirmed(updatedIntent, Slot.CATEGORY)) {
            if(isCategoryValid(updatedIntent)) {
                setTypeSafeCategoryAndConfirmStatus(updatedIntent);
            } else {
                return eventfulHandlerUtils.elicitResponseToReinitializeCategorySlot(updatedIntent);
            }
        }
        return eventfulHandlerUtils.delegateResponseToContinueDialog(updatedIntent);
    }



    public SpeechletResponse handleGigSearch(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        log.info("GigSearchIntent");
        /*try {
            log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(requestEnvelope.getRequest().getIntent()));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }*/
        IntentRequest request = requestEnvelope.getRequest();
        if(request.getDialogState().equals(IntentRequest.DialogState.COMPLETED)) {
            return handleGenericRequest(requestEnvelope, buildEventfulRequestAfterDialogEnds(request));
        }
        DialogIntent updatedIntent = new DialogIntent(request.getIntent());
        return eventfulHandlerUtils.delegateResponseToContinueDialog(updatedIntent);
        /*EventfulRequest eventfulRequest = buildDefaultRequest();
        eventfulRequest.setKeywords(
                replaceWhitespaces(AlexaHelper.getSlotValue(
                        requestEnvelope.getRequest().getIntent(),
                        "Band")
                )
        );
        eventfulRequest.setLocation(
                replaceWhitespaces(AlexaHelper.getSlotValue(
                        requestEnvelope.getRequest().getIntent(),
                        "Location")
                )
        );*/

        //return handleGenericRequest(requestEnvelope, eventfulRequest);
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

    private SpeechletResponse handleGenericRequest(
            SpeechletRequestEnvelope<IntentRequest> requestEnvelope,
            EventfulRequest eventfulRequest
    ) {

        RestResult<EventfulResponse> restResponse = eventfulRestService.callEventSearchEndpoint(eventfulRequest);
        if(restResponse.hasErrors() || !restResponse.getResultObject().isPresent()) {
            log.error(restResponse.getException().getMessage());
            return SpeechletResponse.newTellResponse(
                    AlexaHelper.speech(Conversation.ERROR_HITTING_ENDPOINT),
                    AlexaHelper.simpleCard(Conversation.ERROR_HITTING_ENDPOINT_TITLE, Conversation.ERROR_HITTING_ENDPOINT)
            );
        }

        final EventfulResponse eventfulResponse = restResponse.getResultObject().get();
        if(eventfulResponse.getTotal_items() == 0) {
            log.info("No results found.");
            return SpeechletResponse.newTellResponse(
                    AlexaHelper.speech(Conversation.NO_RESULTS_FOUND),
                    AlexaHelper.simpleCard(Conversation.NO_RESULTS_FOUND_TITLE, Conversation.NO_RESULTS_FOUND)
            );
        }

        return buildCascadedConversationResponse(eventfulResponse);
    }

    private SpeechletResponse buildCascadedConversationResponse(EventfulResponse eventfulResponse) {
        log.info(eventfulResponse.toString());
        final StringBuilder conversationBuilder = new StringBuilder()
                .append(Ssml.SPEAK_START);
        final StringBuilder cardTitleBuilder = new StringBuilder();
        final StringBuilder cardContentBuilder = new StringBuilder();

        handleSingleAndMultipleResponsesForConversation(conversationBuilder, eventfulResponse);
        handleSingleAndMultipleResponsesForCard(cardTitleBuilder, eventfulResponse);

        final AtomicInteger voiceCounter = new AtomicInteger();
        final AtomicInteger cardCounter = new AtomicInteger();
        eventfulResponse.getEvents().getEvent()
                .forEach(eventDataModel -> {
                    conversationBuilder.append(String.format(
                            Ssml.RESULT_EXPANDED,
                            voiceCounter.incrementAndGet(),
                            checkForNull(validateSsml(eventDataModel.getTitle()), Conversation.UNKNOWN_EVENT),
                            checkForNull(validateSsml(eventDataModel.getVenue_name()), Conversation.UNKNOWN_VENUE),
                            checkForNull(DateUtil.getFormattedDate(eventDataModel.getStart_time()), Conversation.UNKNOWN_DATE),
                            checkForNull(DateUtil.getFormattedTime(eventDataModel.getStart_time()), Conversation.UNKNOWN_TIME),
                            checkForNull(validateSsml(eventDataModel.getCity_name()), Conversation.UNKNOWN_LOCATION)
                        )
                    );
                    cardContentBuilder.append(String.format(
                            Conversation.RESULT_EXPANDED_CARD_DESC,
                            cardCounter.incrementAndGet(),
                            checkForNull(eventDataModel.getTitle(), Conversation.UNKNOWN_EVENT),
                            checkForNull(eventDataModel.getVenue_name(), Conversation.UNKNOWN_VENUE),
                            checkForNull(eventDataModel.getStart_time(), Conversation.UNKNOWN_DATE),
                            checkForNull(eventDataModel.getCity_name(), Conversation.UNKNOWN_LOCATION)
                        )
                    );
                }
        );
        conversationBuilder.append(Ssml.SPEAK_END);
        return SpeechletResponse.newTellResponse(
                AlexaHelper.ssmlSpeech(conversationBuilder.toString()),
                AlexaHelper.simpleCard(
                        cardTitleBuilder.toString(),
                        cardContentBuilder.toString()
                )
        );
    }

    private EventfulRequest buildDefaultRequest() {
        return EventfulRequest.builder()
                .sortBy(Optional.empty())
                .pageSize(Optional.empty())
                .date(Optional.empty())
                .category(Optional.empty())
                .keywords(Optional.empty())
                .location(Optional.empty())
                .build();
    }

    private Optional<String> replaceWhitespaces(Optional<String> string) {
        if(string.isPresent()) {
            log.info("Slot: " + string.get());
            return Optional.ofNullable(string.get().replaceAll(" ", "+"));
        }
        return Optional.empty();
    }

    private String checkForNull(Object object, String defaultValue) {
        if(object == null) return defaultValue;
        return object.toString();
    }

    private boolean isNull(Object object) {
        return object == null;
    }

    /*private String getImageUrl(EventfulResponse response) {
        StringBuilder url = new StringBuilder();
        response
                .getEvents()
                .getEvent()
                .stream()
                .findFirst()
                .ifPresent(eventDataModel -> url
                        .append(eventDataModel.getImage().getMedium().getUrl())
                );
        return url.toString();
    }*/

    private void handleSingleAndMultipleResponsesForConversation(StringBuilder string, EventfulResponse response) {
        if(!isNull(response.getTotal_items()) && response.getTotal_items() == 1) {
            string.append(Conversation.ONE_RESULT_FOUND);
        } else if(!isNull(response.getTotal_items())) {
            string.append(String.format(Conversation.FOUND_RESULTS, response.getTotal_items()))
                    .append(String.format(Conversation.READING_OUT_RESULTS,
                            checkForNull(
                                    response.getPage_items(),
                                    Integer.toString(response.getEvents().getEvent().size())
                            )
                    ));
        }
    }

    private void handleSingleAndMultipleResponsesForCard(StringBuilder string, EventfulResponse response) {
        if(!isNull(response.getTotal_items()) && response.getTotal_items() == 1) {
            string.append(Conversation.ONE_RESULT_FOUND);
        } else if(!isNull(response.getTotal_items())) {
            string.append(String.format(Conversation.FOUND_RESULTS, response.getTotal_items()));
        }
    }

    private EventfulRequest buildEventfulRequestAfterDialogEnds(IntentRequest intentRequest) {
        EventfulRequest eventfulRequest = buildDefaultRequest();
        final String keywords = intentRequest.getIntent().getSlot(Slot.KEYWORDS.getValue()).getValue();
        eventfulRequest.setKeywords(Optional.ofNullable(
                keywords.contains("anything") ? null : keywords
        ));
        eventfulRequest.setLocation(Optional.ofNullable(intentRequest.getIntent().getSlot(Slot.LOCATION.getValue()).getValue()));
        eventfulRequest.setDate(Optional.ofNullable(intentRequest.getIntent().getSlot(Slot.DATE.getValue()).getValue()));
        eventfulRequest.setCategory(Optional.ofNullable(intentRequest.getIntent().getSlot(Slot.CATEGORY.getValue()).getValue()));
        eventfulRequest.setSortBy(Optional.ofNullable(Sort.DATE));
        eventfulRequest.setPageSize(Optional.of(5));
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
                .map(category -> category.getKeyword())
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

    private String validateSsml(final String ssml) {
        return ssml.replaceAll("&", "and")
                .replaceAll("<", "")
                .replaceAll(">", "");

    }
}
