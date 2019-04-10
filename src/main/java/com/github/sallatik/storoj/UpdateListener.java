package com.github.sallatik.storoj;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateListener {

    void onUpdate(Update update);
}
