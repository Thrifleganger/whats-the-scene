package com.thrifleganger.alexa.scene.handler;

import com.amazon.speech.slu.ConfirmationStatus;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;
import com.amazon.speech.speechlet.dialog.directives.DialogIntent;
import com.amazon.speech.speechlet.dialog.directives.ElicitSlotDirective;
import com.thrifleganger.alexa.scene.model.eventful.enumeration.Category;
import com.thrifleganger.alexa.scene.model.eventful.enumeration.Slot;
import com.thrifleganger.alexa.scene.utils.AlexaHelper;
import com.thrifleganger.alexa.scene.utils.Conversation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class DialogModelHandler {

    SpeechletResponse elicitResponseToReinitializeCategorySlot(final DialogIntent updatedIntent) {
        ElicitSlotDirective elicitSlotDirective = new ElicitSlotDirective();
        elicitSlotDirective.setSlotToElicit(Slot.CATEGORY.getValue());
        elicitSlotDirective.setUpdatedIntent(updatedIntent);
        SpeechletResponse response = new SpeechletResponse();
        response.setNullableShouldEndSession(false);
        response.setDirectives(Collections.singletonList(elicitSlotDirective));
        response.setOutputSpeech(AlexaHelper.speech(String.format(Conversation.CATEGORY_INVALID_RETRY, fetchAllCategoryDescription())));
        return response;
    }

    SpeechletResponse elicitResponseToRestartDialog(final DialogIntent updatedIntent) {
        updatedIntent.setConfirmationStatus(ConfirmationStatus.NONE);
        clearSlotValuesForRetry(updatedIntent);
        ElicitSlotDirective elicitSlotDirective = new ElicitSlotDirective();
        elicitSlotDirective.setSlotToElicit(Slot.CATEGORY.getValue());
        elicitSlotDirective.setUpdatedIntent(updatedIntent);
        SpeechletResponse response = new SpeechletResponse();
        response.setNullableShouldEndSession(false);
        response.setDirectives(Collections.singletonList(elicitSlotDirective));
        response.setOutputSpeech(AlexaHelper.speech(Conversation.SCENE_DIALOG_RESTART));
        return response;
    }

    SpeechletResponse delegateResponseToContinueDialog(final DialogIntent updatedIntent) {
        DelegateDirective delegateDirective = new DelegateDirective();
        delegateDirective.setUpdatedIntent(updatedIntent);

        SpeechletResponse response = new SpeechletResponse();
        response.setDirectives(Collections.singletonList(delegateDirective));
        response.setNullableShouldEndSession(false);
        return response;
    }

    private void clearSlotValuesForRetry(final DialogIntent intent) {
        intent.getSlots().forEach(
                (s, dialogSlot) -> {
                    dialogSlot.setValue(null);
                    dialogSlot.setConfirmationStatus(ConfirmationStatus.NONE);
                }
        );
    }

    private String fetchAllCategoryDescription() {
        return StringUtils.join(
                Arrays.stream(Category.values())
                        .map(Category::getDescription)
                        .collect(Collectors.toList()),
                ", "
        );
    }
}
