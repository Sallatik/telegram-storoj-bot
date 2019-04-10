package com.github.sallatik.storoj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.ExportChatInviteLink;
import org.telegram.telegrambots.meta.api.methods.groupadministration.KickChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.UnbanChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

@Component
public class TelegramFacadeImpl implements TelegramFacade {

    @Autowired
    private AbsSender sender;

    @Autowired
    private BotSettings botSettings;

    @Autowired
    Messages messages;

    @Override
    public void askForPresentation(long userId) {

        sendMessage(userId, messages.get(Messages.HELLO));
    }

    @Override
    public void presentationInvalid(long userId) {

        sendMessage(userId, messages.get(Messages.INVALID));
    }

    @Override
    public void startPoll(UserData userData) {

        //todo: Notify the user!
        String text = prepareText(userData, Messages.POLL_TEMPLATE);
        InlineKeyboardMarkup keyboard = prepareKeyboard(userData.getId(), 0, 0);

        SendMessage message = new SendMessage()
                .setParseMode(botSettings.getMarkdownStyle())
                .setText(text)
                .setReplyMarkup(keyboard)
                .setChatId(botSettings.getChatId());

        tryExecute(message);
    }

    private String prepareText(UserData userData, String key) {

        String text = messages.get(key)
                .replace("%firstname%", userData.getFirstName())
                .replace("%presentation%", userData.getPresentation());

        if (userData.getLastName() != null)
            text = text.replace("%lastname%", userData.getLastName());

        if (userData.getUsername() != null)
            text = text.replace("%username%", userData.getUsername());

        return text;
    }

    private InlineKeyboardMarkup prepareKeyboard(long userId, int forVotes, int againstVotes) {

        String forQuery = "for:" + userId;
        String againstQuery = "against:" + userId;

        InlineKeyboardButton forButton = new InlineKeyboardButton()
                .setCallbackData(forQuery)
                .setText(messages.get(Messages.FOR_BUTTON) + forVotes);

        InlineKeyboardButton againstButton = new InlineKeyboardButton()
                .setCallbackData(againstQuery)
                .setText(messages.get(Messages.AGAINST_BUTTON) + againstVotes);

        List<List<InlineKeyboardButton>> keyboard;

        if (botSettings.getVerticalKeyboard())
            keyboard = list(list(forButton), list(againstButton));
        else
            keyboard = list(list(forButton, againstButton));

        return new InlineKeyboardMarkup()
                .setKeyboard(keyboard);

    }

    private <T> List<T> list(T... elements) {
        return Arrays.asList(elements);
    }

    @Override
    public void answerVote(String queryId, boolean votedFor, UserData userData) {

        String text = prepareText(userData,
                votedFor ? Messages.VOTED_FOR_PUSH : Messages.VOTED_AGAINST_PUSH);

        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery()
                .setCallbackQueryId(queryId)
                .setText(text);

        tryExecute(answerCallbackQuery);
    }

    @Override
    public void invite(long id) {

        UnbanChatMember unbanChatMember = new UnbanChatMember()
                .setChatId(botSettings.getChatId())
                .setUserId((int) id);

        tryExecute(unbanChatMember);

        ExportChatInviteLink exportChatInviteLink = new ExportChatInviteLink()
                .setChatId(botSettings.getChatId());

        try {

            String link = botSettings.getUseConstantLink() ?
                    botSettings.getLink():
                    sender.execute(exportChatInviteLink);

            String text = messages.get(Messages.INVITE_TEMPLATE)
                    .replace("%link%", link);

            sendMessage(id, text);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endPoll(long messageId, UserData userData, boolean isAllowed) {

        String text = prepareText(userData,
                isAllowed ? Messages.POLL_FINAL_ALLOWED : Messages.POLL_FINAL_REJECTED);

        EditMessageText editMessageText = new EditMessageText()
                .setParseMode(botSettings.getMarkdownStyle())
                .setText(text)
                .setChatId(botSettings.getChatId())
                .setMessageId((int) messageId);

        tryExecute(editMessageText);
    }

    @Override
    public void updatePoll(long messageId, UserData userData) {

        InlineKeyboardMarkup keyboard = prepareKeyboard(userData.getId(), userData.getVotesFor(), userData.getVotesAgainst());

        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup()
                .setReplyMarkup(keyboard)
                .setChatId(botSettings.getChatId())
                .setMessageId((int) messageId);

        tryExecute(editMessageReplyMarkup);
    }

    @Override
    public void kick(long userId) {

        KickChatMember kickChatMember = new KickChatMember()
                .setUserId((int) userId)
                .setChatId(botSettings.getChatId());

        tryExecute(kickChatMember);
    }

    @Override
    public void reject(long userId) {

        sendMessage(userId, messages.get(Messages.REJECT));
    }

    private void sendMessage(long id, String text) {

        SendMessage sendMessage = new SendMessage()
                .setParseMode(botSettings.getMarkdownStyle())
                .setChatId(id)
                .setText(text);

        System.out.println("Sending:\n" + text);

        tryExecute(sendMessage);
    }

    private void tryExecute(BotApiMethod<?> method) {

        try {
            sender.execute(method);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
