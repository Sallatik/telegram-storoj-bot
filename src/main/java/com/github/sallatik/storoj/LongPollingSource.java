package com.github.sallatik.storoj;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Set;

public abstract class LongPollingSource extends TelegramLongPollingBot implements UpdateSource {

    private Set<UpdateListener> listeners = new HashSet<>();

    @Override
    public void onUpdateReceived(Update update) {

        listeners.forEach(listener -> listener.onUpdate(update));
    }

    @Override
    public void addUpdateListener(UpdateListener listener) {

        listeners.add(listener);
    }
}
