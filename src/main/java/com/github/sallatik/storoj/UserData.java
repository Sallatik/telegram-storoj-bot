package com.github.sallatik.storoj;

import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashSet;
import java.util.Set;

public class UserData {

    private long id;

    private String firstName;
    private String lastName;
    private String username;

    private UserStatus status;

    private String presentation;

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    private Set<Long> votersFor = new HashSet<>();
    private Set<Long> votersAgainst = new HashSet<>();

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public int getVotesFor() {
        return votersFor.size();
    }

    public int getVotesAgainst() {
        return votersAgainst.size();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void voteFor(long id) {
        votersAgainst.remove(id);
        votersFor.add(id);
    }

    public void voteAgainst(long id) {
        votersFor.remove(id);
        votersAgainst.add(id);
    }

    public UserData(User user) {

        setId(user.getId());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setUsername(user.getUserName());

        setStatus(UserStatus.WAITING);
    }

    public enum UserStatus {
        WAITING, ON_POLL, ALLOWED, REJECTED
    }
}
