package com.thrifleganger.alexa.scene.handler;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.ConfirmationStatus;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;
import com.amazon.speech.speechlet.dialog.directives.DialogIntent;
import com.amazon.speech.speechlet.dialog.directives.ElicitSlotDirective;
import com.thrifleganger.alexa.scene.exception.handler.RestResult;
import com.thrifleganger.alexa.scene.model.eventful.EventfulRequest;
import com.thrifleganger.alexa.scene.model.eventful.EventfulResponse;
import com.thrifleganger.alexa.scene.service.EventfulRestService;
import com.thrifleganger.alexa.scene.utils.AlexaHelper;
import com.thrifleganger.alexa.scene.utils.Conversation;
import com.thrifleganger.alexa.scene.utils.DateUtil;
import com.thrifleganger.alexa.scene.utils.Ssml;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventfulHandler {

    private final EventfulRestService eventfulRestService;

    public SpeechletResponse handleWelcome(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        log.info("HelloIntent");
        return SpeechletResponse.newAskResponse(
                AlexaHelper.speech(Conversation.WELCOME),
                AlexaHelper.reprompt(Conversation.WELCOME_REPROMPT));
    }

    public SpeechletResponse handleSceneInvocationIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        IntentRequest request = requestEnvelope.getRequest();
        if(request.getDialogState() != IntentRequest.DialogState.COMPLETED) {

            DialogIntent updatedIntent = new DialogIntent(request.getIntent());
            if(updatedIntent.getConfirmationStatus() == ConfirmationStatus.DENIED) {
                clearSlotValuesForRetry(updatedIntent);
                ElicitSlotDirective elicitSlotDirective = new ElicitSlotDirective();
                elicitSlotDirective.setSlotToElicit("");
                //
            }
            DelegateDirective delegateDirective = new DelegateDirective();
            delegateDirective.setUpdatedIntent(updatedIntent);

            SpeechletResponse response = new SpeechletResponse();
            response.setDirectives(Collections.singletonList(delegateDirective));
            response.setNullableShouldEndSession(false);
            return response;
        } else {
            return handleGenericRequest(requestEnvelope, buildEventfulRequestAfterDialogEnds(request));
        }
    }

    public SpeechletResponse handleGigSearch(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        log.info("GigSearchIntent");
        EventfulRequest eventfulRequest = buildDefaultRequest();
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
        );

        return handleGenericRequest(requestEnvelope, eventfulRequest);
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
                            checkForNull(eventDataModel.getTitle(), Conversation.UNKNOWN_EVENT),
                            checkForNull(eventDataModel.getVenue_name(), Conversation.UNKNOWN_VENUE),
                            checkForNull(DateUtil.getFormattedDate(eventDataModel.getStart_time()), Conversation.UNKNOWN_DATE),
                            checkForNull(DateUtil.getFormattedTime(eventDataModel.getStart_time()), Conversation.UNKNOWN_TIME),
                            checkForNull(eventDataModel.getCity_name(), Conversation.UNKNOWN_LOCATION)
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

    private String getImageUrl(EventfulResponse response) {
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
    }

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
        log.info("Inside buildEventfulRequestAfterDialogEnds");
        EventfulRequest eventfulRequest = buildDefaultRequest();
        eventfulRequest.setKeywords(Optional.of(intentRequest.getIntent().getSlot("keywords").getValue()));
        eventfulRequest.setLocation(Optional.of(intentRequest.getIntent().getSlot("location").getValue()));
        eventfulRequest.setDate(Optional.of(intentRequest.getIntent().getSlot("date").getValue()));
        log.info(eventfulRequest.toString());
        return eventfulRequest;
    }

    private void clearSlotValuesForRetry(DialogIntent intent) {
        intent.getSlots().forEach(
                (s, dialogSlot) -> {
                    dialogSlot.setValue(null);
                    dialogSlot.setConfirmationStatus(ConfirmationStatus.NONE);
                }
        );
    }
}
