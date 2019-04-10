package com.github.sallatik.storoj;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PersistentSettings {

    @Value("${settings.max-votes}")
    private int maxVotes;
    @Value("${settings.vertical-keyboard}")
    private boolean verticalKeyboard;
    @Value("${settings.markdown-style}")
    private String markdownStyle;
    @Value("${settings.constant-link}")
    private boolean useConstantLink;
    @Value("${link}")
    private String link;

    private long chatId = 0;

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public int getMaxVotes() {
        return maxVotes;
    }

    public void setMaxVotes(int maxVotes) {
        this.maxVotes = maxVotes;
    }

    public boolean getVerticalKeyboard() {
        return verticalKeyboard;
    }

    public void setVerticalKeyboard(boolean verticalKeyboard) {
        this.verticalKeyboard = verticalKeyboard;
    }

    public String getMarkdownStyle() {
        return markdownStyle;
    }

    public void setMarkdownStyle(String markdownStyle) {
        this.markdownStyle = markdownStyle;
    }

    public boolean getUseConstantLink() {
        return useConstantLink;
    }

    public void setUseConstantLink(boolean useConstantLink) {
        this.useConstantLink = useConstantLink;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
