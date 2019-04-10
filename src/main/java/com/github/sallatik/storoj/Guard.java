package com.github.sallatik.storoj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static com.github.sallatik.storoj.LoggingUtils.*;
import java.util.logging.Logger;

import static com.github.sallatik.storoj.UserData.UserStatus.*;

@Component
public class Guard implements UpdateListener {

    @Autowired
    private TelegramFacade telegramFacade;

    @Autowired
    private UserStore userStore;

    @Autowired
    private BotSettings botSettings;

    Logger logger = Logger.getLogger("guard.Guard");

    private void onPrivateMessage(Message message) {

        User user = message.getFrom();
        UserData userData = userStore.getData(user);

        logger.fine("New message from " + user(user) +
                "\nCurrent status: " + userStatus(userData) +
                "\nMessage text: \"" + nullSafe(message.getText()) + "\"");

        if (userData == null) {

            telegramFacade.askForPresentation(user);
            userData = new UserData(user);
            userStore.storeData(user, userData);

            logger.fine("New user registered, waiting for presentation");
        } else if (userData.getStatus() == WAITING)

            if (validatePresentation(message)) {

                String presentation = message.getText();
                userData.setPresentation(presentation);
                userData.setStatus(ON_POLL);
                telegramFacade.startPoll(userData);
                userStore.storeData(user, userData);

                logger.fine("User status changed to ON_POLL");
            } else {
                telegramFacade.presentationInvalid(user);
                logger.fine("Invalid presentation, user status stays the same");
            }
    }

    private boolean validatePresentation(Message message) {
        // todo: extend validation rules
        return message.hasText();
    }

    private void onNewMember(User user) {

        if (!user.getBot()) {

            UserData userData = userStore.getData(user);

            if (userData == null || userData.getStatus()!= UserData.UserStatus.ALLOWED) {
                logger.warning("Unauthorised user joined the chat: " + user(user) +
                        "\nUser status: " + userStatus(userData));
                telegramFacade.kick(user);
            } else
                logger.info("Authorised user joined the chat: " + user(user));

        }
    }

    private void onCallbackQuery(CallbackQuery callbackQuery) {

        long id = 0;
        String vote = "";

        try {

            String[] parts = callbackQuery.getData().split(":");

            id = Long.parseLong(parts[1]);
            vote = parts[0];

        } catch (RuntimeException e) {

            logger.warning("Malformed callback query: " + callbackQuery.getData());
            return;
        }

        UserData userData = userStore.getData(id);

        if (userData != null && userData.getStatus() == ON_POLL) {
            String queryId = callbackQuery.getId();

            long voterId = callbackQuery.getFrom().getId();

            switch (vote) {

                case "for":
                    userData.voteFor(voterId);
                    logger.finer("User " + user(callbackQuery.getFrom()) +
                            " voted for " + user(userData) + ": " +
                            userData.getVotesFor() + " of " + botSettings.getMaxVotes());
                    telegramFacade.answerVote(queryId, true, userData);
                    break;

                case "against":
                    userData.voteAgainst(voterId);
                    logger.finer("User " + user(callbackQuery.getFrom()) +
                            " voted against " + user(userData) + ": " +
                            userData.getVotesAgainst() + " of " + botSettings.getMaxVotes());
                    telegramFacade.answerVote(queryId, false, userData);
                    break;
            }

            Message message = callbackQuery.getMessage();

            if (userData.getVotesFor() == botSettings.getMaxVotes()) {

                userData.setStatus(ALLOWED);
                logger.info("User " + user(userData) + " is now authorised to join the chat");
                telegramFacade.invite(id);
                telegramFacade.endPoll(message, userData, true);

            } else if (userData.getVotesAgainst() == botSettings.getMaxVotes()) {

                userData.setStatus(REJECTED);
                logger.info("User " + user(userData) + " is denied permission to join the chat");
                telegramFacade.reject(id);
                telegramFacade.endPoll(message, userData, false);

            } else
                telegramFacade.updatePoll(message, userData);

            userStore.storeData(id, userData);
        }
    }

    @Override
    public void onUpdate(Update update) {

        if (update.hasMessage()) {

            Message message = update.getMessage();

            if (message.isUserMessage())
                onPrivateMessage(message);

            else {

                if (botSettings.getChatId() != message.getChatId()) {
                    botSettings.setChatId(message.getChatId());

                    logger.info("New chat detected: changing the working chat to " + message.getChat().getTitle());
                }

                if (message.getNewChatMembers() != null)

                    message.getNewChatMembers().forEach(this::onNewMember);
            }

        } else if (update.hasCallbackQuery())
            onCallbackQuery(update.getCallbackQuery());
    }
}
