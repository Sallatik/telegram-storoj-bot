package com.github.sallatik.storoj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Component
public class MessagesRedis implements Messages{

    @Autowired
    private Jedis jedis;

    @Value("${messages.properties}")
    private String propertiesFilePath;

    @Value("${messages-key}")
    private String key;

    private Properties messages;

    @Override
    public String get(String key) {

        String message = jedis.hget(this.key, key);

        return message != null ?
                message :
                messages.getProperty(key, "no text");

    }

    @Override
    public void set(String key, String value) {

        jedis.hset(this.key, key, value);
    }

    @PostConstruct
    public void init() throws IOException {

        FileReader reader = new FileReader(propertiesFilePath);
        messages = new Properties();
        messages.load(reader);
    }
}
