package com.github.sallatik.storoj;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class UserStoreRedis implements UserStore {

    @Autowired
    private Gson gson;

    @Autowired
    private Jedis jedis;

    @Value("${user-store.key}")
    private String key;

    @Override
    public UserData getData(long id) {

        String json = jedis.hget(key, String.valueOf(id));
        UserData userData = gson.fromJson(json, UserData.class);
        return userData;
    }

    @Override
    public void storeData(long id, UserData data) {

        String json = gson.toJson(data);
        jedis.hset(key, String.valueOf(id), json);
    }
}
