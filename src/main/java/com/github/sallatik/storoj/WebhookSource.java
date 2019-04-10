package com.github.sallatik.storoj;

import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Set;

public abstract class WebhookSource extends TelegramWebhookBot implements UpdateSource {

    private Set<UpdateListener> listeners = new HashSet<>();

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {

        listeners.forEach(listener -> listener.onUpdate(update));
        return null;
    }

    @Override
    public void addUpdateListener(UpdateListener listener) {

        listeners.add(listener);
    }
}
