package com.github.sallatik.storoj;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public interface TelegramFacade {

    void askForPresentation(long userId);

    void presentationInvalid(long userId);

    void startPoll(UserData userData);

    void invite(long userId);

    void reject(long userId);

    void endPoll(long messageId, UserData userData, boolean isAllowed);

    void updatePoll(long messageId, UserData userData);

    void kick(long userId);

    void answerVote(String queryId, boolean votedFor, UserData userData);

    default void askForPresentation(User user) {
        askForPresentation(user.getId());
    }

    default void presentationInvalid(User user) {
        presentationInvalid(user.getId());
    }

    default void endPoll(Message message, UserData userData, boolean isAllowed) {
        endPoll(message.getMessageId(), userData, isAllowed);
    }

    default void updatePoll(Message message, UserData userData) {
        updatePoll(message.getMessageId(), userData);
    }

    default void kick(User user) {
        kick(user.getId());
    }

}
