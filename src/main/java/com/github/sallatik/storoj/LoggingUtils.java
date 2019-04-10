package com.github.sallatik.storoj;

import org.telegram.telegrambots.meta.api.objects.User;

public class LoggingUtils {

    public static String userStatus(UserData data) {

        return data == null ?
                "unknown" :
                data.getStatus().toString();
    }

    // instead of User.toString()
    public static String user(User user) {

        return user.getFirstName() + " " +
                nullSafe(user.getLastName()) +
                "(" + nullSafe(user.getUserName()) +")" +
                "[" + user.getId() + "]";
    }

    public static String user(UserData userData) {

        return userData.getFirstName() + " " +
                nullSafe(userData.getLastName()) +
                "(" + nullSafe(userData.getUsername()) +")" +
                "[" + userData.getId() + "]";
    }

    public static String nullSafe(String str) {
        return str == null ? "" : str;
    }
}
