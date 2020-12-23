package xyz.toby.task.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redis配置
 *
 * @Author: zhangbin
 * @Date: 2020/12/16
 */
@Data
@ConfigurationProperties(prefix = "transaction.async.task.redis")
public class RedisProperties {

    private String host;
    private int port;
    private int database;
    private String password;
}
