package com.github.sallatik.storoj;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;

@Component
public class BotSettingsRedis implements BotSettings {

    @Autowired
    private Jedis jedis;

    @Autowired
    private Gson gson;

    @Autowired
    private PersistentSettings settings;

    @Value("${settings.key}")
    private String key;

    private void persistSettings() {
        jedis.set(key, gson.toJson(settings));
    }

    @PostConstruct
    private void loadSettings() {

        String json = jedis.get(key);

        if (json != null)
            settings = gson.fromJson(json, PersistentSettings.class);
    }

    @Override
    public long getChatId() {
        return settings.getChatId();
    }

    @Override
    public void setChatId(long id) {
        settings.setChatId(id);
        persistSettings();
    }

    @Override
    public int getMaxVotes() {
        return settings.getMaxVotes();
    }

    @Override
    public void setMaxVotes(int maxVotes) {
        settings.setMaxVotes(maxVotes);
        persistSettings();
    }

    @Override
    public boolean getVerticalKeyboard() {
        return settings.getVerticalKeyboard();
    }

    @Override
    public void setVerticalKeyboard(boolean verticalKeyboard) {
        settings.setVerticalKeyboard(verticalKeyboard);
        persistSettings();
    }

    @Override
    public void setMarkdownStyle(String markdownStyle) {
        settings.setMarkdownStyle(markdownStyle);
        persistSettings();
    }

    @Override
    public String getMarkdownStyle() {
        return settings.getMarkdownStyle();
    }

    @Override
    public boolean getUseConstantLink() {
        return settings.getUseConstantLink();
    }

    @Override
    public void setUseConstantLink(boolean useConstantLink) {
        settings.setUseConstantLink(useConstantLink);
        persistSettings();
    }

    @Override
    public String getLink() {
        return settings.getLink();
    }

    @Override
    public void setLink(String link) {
        settings.setLink(link);
        persistSettings();
    }
}
