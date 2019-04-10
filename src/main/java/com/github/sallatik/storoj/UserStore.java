package com.github.sallatik.storoj;

import org.telegram.telegrambots.meta.api.objects.User;

public interface UserStore {

    UserData getData(long id);

    void storeData(long id, UserData data);

    default UserData getData(User user) {

        long id = user.getId();
        return getData(id);
    }

    default void storeData(User user, UserData data) {

        long id = user.getId();
        storeData(id, data);
    }
}
