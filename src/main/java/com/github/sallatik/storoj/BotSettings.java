package com.github.sallatik.storoj;

public interface BotSettings {

    long getChatId();

    void setChatId(long id);

    int getMaxVotes();
    void setMaxVotes(int maxVotes);

    boolean getVerticalKeyboard();

    void setVerticalKeyboard(boolean verticalKeyboard);

    void setMarkdownStyle(String markdownStyle);

    boolean getUseConstantLink();

    void setUseConstantLink(boolean useConstantLink);

    String getLink();

    void setLink(String link);

    String getMarkdownStyle();
}
