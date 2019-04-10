package com.github.sallatik.storoj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Launcher {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Guard guard;

    @Autowired
    private TelegramBotsApi api;

    @Value("${use-webhook}")
    private boolean useWebhook;

    public void launch() throws TelegramApiException {

        UpdateSource source = context.getBean(
                useWebhook ? "webhookSourceBean" : "longPollingSourceBean",
                UpdateSource.class
        );

        source.addUpdateListener(guard);

        if (useWebhook)
            api.registerBot((TelegramWebhookBot) source);
        else
            api.registerBot((TelegramLongPollingBot) source);
    }

    public static void main(String [] args) {

        ApiContextInitializer.init();

        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        System.out.println("Beans loaded successfully");

        try {

            context.getBean("launcher", Launcher.class)
                    .launch();
            System.out.println("Bot launched successfully");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
