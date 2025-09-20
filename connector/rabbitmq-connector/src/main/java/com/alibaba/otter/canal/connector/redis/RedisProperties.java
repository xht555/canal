package com.alibaba.otter.canal.connector.redis;

import com.alibaba.otter.canal.common.utils.PropertiesUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Properties;

/**
 * Redis服务器属性配置
 * @Author 许亮
 * @Mailto xht555@163.com, xuliang@poweroak.net
 * @Create 2025-09-19 09:22:30
 */
public class RedisProperties {
    private String host     = "localhost";
    private int port        = 6379;
    private String password;
    private int database    = 0;
    private int timeout     = 30000;

    public RedisProperties(Properties properties) {
        String property = PropertiesUtils.getProperty(properties, "spring.redis.host");
        if (!StringUtils.isEmpty(property)) {
            this.host = property;
        }

        property = PropertiesUtils.getProperty(properties, "spring.redis.port");
        if (!StringUtils.isEmpty(property)) {
            this.port = Integer.parseInt(property);
        }

        property = PropertiesUtils.getProperty(properties, "spring.redis.password");
        if (!StringUtils.isEmpty(property)) {
            this.password = property;
        }

        property = PropertiesUtils.getProperty(properties, "spring.redis.database");
        if (!StringUtils.isEmpty(property)) {
            this.database = Integer.parseInt(property);
        }

        property = PropertiesUtils.getProperty(properties, "spring.redis.timeout");
        if (!StringUtils.isEmpty(property)) {
            this.timeout = Integer.parseInt(property);
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public int getDatabase() {
        return database;
    }

    public int getTimeout() {
        return timeout;
    }
}
