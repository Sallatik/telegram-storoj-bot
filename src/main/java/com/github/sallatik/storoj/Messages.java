package com.github.sallatik.storoj;

public interface Messages {

    String HELLO = "hello";
    String INVALID = "invalid";
    String POLL_FINAL_ALLOWED = "poll-final-allowed";
    String POLL_FINAL_REJECTED = "poll-final-rejected";
    String POLL_TEMPLATE = "poll-template";
    String FOR_BUTTON = "for-button";
    String AGAINST_BUTTON = "against-button";
    String INVITE_TEMPLATE = "invite-template";
    String VOTED_FOR_PUSH = "voted-for-push";
    String VOTED_AGAINST_PUSH = "voted-against-push";
    String REJECT = "reject";

    public String get(String key);

    public void set(String key, String value);
}
