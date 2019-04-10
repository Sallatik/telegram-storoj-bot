package com.github.sallatik.storoj;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import redis.clients.jedis.Jedis;

import javax.validation.constraints.AssertTrue;

@Configuration
@ComponentScan("com.github.sallat.guard")
@PropertySource("file:${user.dir}/bot.properties")
public class Config {

    @Autowired
    private ApplicationContext context;

    @Value("${redis.host}")
    private String host;

    @Value("${webhook.url}")
    private String url;

    @Value("${webhook.port}")
    private int port;

    @Value("${use-webhook}")
    private boolean useWebhook;

    @Value("${heroku}")
    private boolean heroku;

    @Bean
    public AbsSender sender() {
        return (AbsSender) context.getBean(useWebhook ? "webhookSourceBean" : "longPollingSourceBean");
    }

    @Bean
    public Jedis jedis() {

        System.out.println("heroku: " + heroku);
        return new Jedis(heroku ? System.getenv("REDIS_URL") : host);
    }

    @Bean
    public Gson gson() {

        return new Gson();
    }

    @Bean
    public TelegramBotsApi api() throws TelegramApiException {

        ApiContextInitializer.init();

        int usePort = heroku ?
                Integer.parseInt(System.getenv("PORT")) :
                port;

        if (useWebhook)
            return new TelegramBotsApi(url, "https://0.0.0.0:" + usePort + "/");
        else
            return new TelegramBotsApi();
    }
}
