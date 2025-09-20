package com.alibaba.otter.canal.connector.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Properties;

/**
 * @Author 许亮
 * @Mailto xht555@163.com, xuliang@poweroak.net
 * @Create 2025-09-18 16:06:40
 */
public class RedisClient {
    private static JedisPool jedisPool;
    private static RedisProperties redisProperties;

    public static void init(Properties properties) {
        if (jedisPool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(128);    // 最大连接数
            config.setMaxIdle(64);      // 最大空闲连接数
            config.setMinIdle(16);      // 最小空闲连接数

            redisProperties = new RedisProperties(properties);
            jedisPool = new JedisPool(config, redisProperties.getHost(), redisProperties.getPort(), redisProperties.getTimeout());
        }
    }

    public static Jedis jedis() {
        try (Jedis jedis = jedisPool.getResource()) {
            if (redisProperties == null) {
                throw new RuntimeException("请先初始化RedisClient#init。");
            }

            jedis.auth(redisProperties.getPassword());
            jedis.select(redisProperties.getDatabase());
            return jedis;
        }
    }

    public static void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
            jedisPool.close();
        }
    }
}
