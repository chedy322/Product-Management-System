package com.example.demo.Infrastructure.config.redis;


import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisConfig {
    public void connectBasic() {
        // RedisURI uri = RedisURI.Builder
        //         .redis()
        //         .withAuthentication()
        //         .build();
        // RedisClient client = RedisClient.create(uri);
        // StatefulRedisConnection<String, String> connection = client.connect();
        // RedisCommands<String, String> commands = connection.sync();

        // commands.set("foo", "bar");
        // String result = commands.get("foo");
        // System.out.println(result); // >>> bar

        // connection.close();

        // client.shutdown();
    }
}
