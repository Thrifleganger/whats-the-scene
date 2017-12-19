package com.thrifleganger.alexa.scene.handler;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.thrifleganger.alexa.scene.constants.PathState;
import com.thrifleganger.alexa.scene.constants.SessionAttributes;
import com.thrifleganger.alexa.scene.exception.handler.RestResult;
import com.thrifleganger.alexa.scene.model.eventful.EventfulRequest;
import com.thrifleganger.alexa.scene.model.eventful.EventfulResponse;
import com.thrifleganger.alexa.scene.service.EventfulRestService;
import com.thrifleganger.alexa.scene.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class GenericRequestHandler {

    private final EventfulRestService eventfulRestService;
    private final EventfulHandlerUtils utils;

    SpeechletResponse handle(
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
        if(eventfulResponse.getTotalItems() == 0) {
            log.info("No results found.");
            return SpeechletResponse.newTellResponse(
                    AlexaHelper.speech(Conversation.NO_RESULTS_FOUND),
                    AlexaHelper.simpleCard(Conversation.NO_RESULTS_FOUND_TITLE, Conversation.NO_RESULTS_FOUND)
            );
        }

        saveRestResultToSession(requestEnvelope, eventfulRequest, eventfulResponse);

        return buildCascadedConversationResponse(requestEnvelope, eventfulResponse);
    }

    private SpeechletResponse buildCascadedConversationResponse(
            SpeechletRequestEnvelope<IntentRequest> requestEnvelope,
            EventfulResponse eventfulResponse
    ) {
        log.info(eventfulResponse.toString());
        final StringBuilder repromptBuilder = new StringBuilder();
        final StringBuilder conversationBuilder = new StringBuilder()
                .append(Ssml.SPEAK_START);
        final StringBuilder cardTitleBuilder = new StringBuilder();
        final StringBuilder cardContentBuilder = new StringBuilder();

        handleSingleAndMultipleResponsesForConversation(conversationBuilder, eventfulResponse, requestEnvelope);
        handleSingleAndMultipleResponsesForCard(cardTitleBuilder, eventfulResponse, requestEnvelope);

        final AtomicInteger voiceCounter = new AtomicInteger();
        final AtomicInteger cardCounter = new AtomicInteger();
        eventfulResponse.getEvents().getEvent()
                .forEach(eventDataModel -> {
                            conversationBuilder.append(String.format(
                                    Ssml.SUMMARIZE_RESULT,
                                    voiceCounter.incrementAndGet(),
                                    utils.checkForNull(utils.validateSsml(eventDataModel.getTitle()), Conversation.UNKNOWN_EVENT),
                                    utils.checkForNull(utils.validateSsml(eventDataModel.getVenueName()), Conversation.UNKNOWN_VENUE),
                                    utils.checkForNull(DateUtil.getFormattedDate(eventDataModel.getStartTime()), Conversation.UNKNOWN_DATE),
                                    utils.checkForNull(DateUtil.getFormattedTime(eventDataModel.getStartTime()), Conversation.UNKNOWN_TIME),
                                    utils.checkForNull(utils.validateSsml(eventDataModel.getCityName()), Conversation.UNKNOWN_LOCATION)
                                    )
                            );
                            cardContentBuilder.append(String.format(
                                    Conversation.RESULT_EXPANDED_CARD_DESC,
                                    cardCounter.incrementAndGet(),
                                    utils.checkForNull(eventDataModel.getTitle(), Conversation.UNKNOWN_EVENT),
                                    utils.checkForNull(eventDataModel.getVenueName(), Conversation.UNKNOWN_VENUE),
                                    utils.checkForNull(eventDataModel.getStartTime(), Conversation.UNKNOWN_DATE),
                                    utils.checkForNull(eventDataModel.getCityName(), Conversation.UNKNOWN_LOCATION)
                                    )
                            );
                        }
                );
        repromptBuilder.append(Conversation.GENERIC_REPROMT);
        if(moreResultsAvailable(requestEnvelope)) {
            savePathStateToSession(requestEnvelope, PathState.AWAITING_MORE_RESULTS_OR_MORE_DETAILS);
            conversationBuilder.append(Conversation.FETCH_MORE_RESULTS);
            repromptBuilder.append(Conversation.FETCH_MORE_RESULTS);
        }
        if(eventfulResponse.getEvents().getEvent().size() > 1) {
            if(!moreResultsAvailable(requestEnvelope)) {
                savePathStateToSession(requestEnvelope, PathState.AWAITING_MORE_DETAILS);
            }
            conversationBuilder.append(Conversation.EXPAND_MANY_FURTHER);
            repromptBuilder.append(Conversation.EXPAND_MANY_FURTHER);
        }
        if(eventfulResponse.getEvents().getEvent().size() == 1) {
            savePathStateToSession(requestEnvelope, PathState.AWAITING_MORE_DETAILS_FOR_SINGLE_RESULT);
            conversationBuilder.append(Conversation.EXPAND_ONE_RESULT_FURTHER);
            repromptBuilder.append(Conversation.EXPAND_ONE_RESULT_FURTHER);
        }
        conversationBuilder.append(Ssml.SPEAK_END);
        return SpeechletResponse.newAskResponse(
                AlexaHelper.ssmlSpeech(conversationBuilder.toString()),
                AlexaHelper.reprompt(repromptBuilder.toString()),
                AlexaHelper.simpleCard(
                        cardTitleBuilder.toString(),/**/
                        cardContentBuilder.toString()
                )
        );
    }

    private boolean moreResultsAvailable(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {

        final Integer total = (Integer) utils.getSessionAttribute(
                requestEnvelope, SessionAttributes.TOTAL_NUMBER_RESULTS).orElse(0);
        final Integer maxPageSize = (Integer) utils.getSessionAttribute(
                requestEnvelope, SessionAttributes.MAX_PAGE_SIZE).orElse(0);
        final Integer currentPageNumber = (Integer) utils.getSessionAttribute(
                requestEnvelope, SessionAttributes.CURRENT_PAGE_NUMBER).orElse(0);
        return total - (maxPageSize * currentPageNumber) > 0;
    }

    private void saveRestResultToSession(
            final SpeechletRequestEnvelope<IntentRequest> requestEnvelope,
            final EventfulRequest eventfulRequest,
            final EventfulResponse eventfulResponse
    ) {
        requestEnvelope.getSession().setAttribute(SessionAttributes.EVENTFUL_REQUEST.getValue(), eventfulRequest);
        requestEnvelope.getSession().setAttribute(SessionAttributes.EVENTFUL_RESPONSE.getValue(), eventfulResponse);
        requestEnvelope.getSession().setAttribute(SessionAttributes.TOTAL_NUMBER_RESULTS.getValue(), eventfulResponse.getTotalItems());
        requestEnvelope.getSession().setAttribute(SessionAttributes.CURRENT_PAGE_NUMBER.getValue(), utils.generateNextPageNumber(requestEnvelope));
        requestEnvelope.getSession().setAttribute(SessionAttributes.CURRENT_PAGE_SIZE.getValue(), eventfulResponse.getEvents().getEvent().size());
        requestEnvelope.getSession().setAttribute(SessionAttributes.MAX_PAGE_SIZE.getValue(), 5);
    }

    private void handleSingleAndMultipleResponsesForConversation(
            final StringBuilder string,
            final EventfulResponse response,
            final SpeechletRequestEnvelope<IntentRequest> requestEnvelope
    ) {
        if(utils.getCurrentPathState(requestEnvelope).equals(PathState.AWAITING_MORE_RESULTS_OR_MORE_DETAILS)) {
            string.append(String.format(Conversation.READING_OUT_NEXT_PAGE, getNumberOfResultsOnCurrentPage(response)));
        } else if(!utils.isNull(response.getTotalItems()) && response.getTotalItems() == 1) {
            string.append(Conversation.ONE_RESULT_FOUND);
        } else if(!utils.isNull(response.getTotalItems())) {
            string.append(String.format(Conversation.FOUND_RESULTS, response.getTotalItems()))
                    .append(String.format(Conversation.READING_OUT_RESULTS, getNumberOfResultsOnCurrentPage(response)));
        }
    }

    private void handleSingleAndMultipleResponsesForCard(
            final StringBuilder string,
            final EventfulResponse response,
            final SpeechletRequestEnvelope<IntentRequest> requestEnvelope
    ) {
        if(!utils.isNull(response.getTotalItems()) && response.getTotalItems() == 1) {
            string.append(Conversation.ONE_RESULT_FOUND);
        } else if(!utils.isNull(response.getTotalItems())) {
            string.append(String.format(
                    Conversation.FOUND_RESULTS_WITH_PAGE_NUMBER,
                    response.getTotalItems(),
                    utils.getSessionAttribute(requestEnvelope, SessionAttributes.CURRENT_PAGE_NUMBER).orElse(1)
            ));
        }
    }

    private void savePathStateToSession(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope,
                                       final PathState pathState
    ) {
        requestEnvelope.getSession().setAttribute(SessionAttributes.PATH_STATE.getValue(), pathState);
    }

    private String getNumberOfResultsOnCurrentPage(final EventfulResponse eventfulResponse) {
        return utils.checkForNull(
                eventfulResponse.getPageItems(),
                Integer.toString(eventfulResponse.getEvents().getEvent().size())
        );
    }
    
}
