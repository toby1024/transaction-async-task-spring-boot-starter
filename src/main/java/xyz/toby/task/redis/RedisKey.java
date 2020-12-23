package xyz.toby.task.redis;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author: zhangbin
 * @Date: 2020/12/16
 */
public enum RedisKey {
    /**
     * RedisKeys
     */
    TASK_ID("task.id", RedisDataType.SET),
    READY("ready", RedisDataType.LIST),
    DELAYED("delayed", RedisDataType.ZSET),
    DEPENDENT_WAIT("dependent.wait", RedisDataType.HASH),
    WORKING("working", RedisDataType.HASH),
    RETRY("retry", RedisDataType.ZSET),
    SUCCESS("success", RedisDataType.LIST),
    DEAD("dead", RedisDataType.LIST),
    INDEX_FINISHED_DEPENDENT_ID("index:finished.dependent.id", RedisDataType.SET),
    TRANSFERABLE_DATA("transferable.data", RedisDataType.HASH),
    COUNT_SUCCESS("count.success", RedisDataType.VALUE),
    COUNT_DEAD("count.failed", RedisDataType.VALUE),
    LOCK_DEPENDENT("lock:dependent", RedisDataType.STRING),
    LOCK_TIMEOUT_CHECK("lock:timeout.check", RedisDataType.STRING),
    PREFIX_CLIENT("client:", RedisDataType.STRING),
    // the old key name is 'clients'
    CLIENTS("detailed.clients", RedisDataType.HASH);

    private String name;

    @Getter
    private RedisDataType type;

    RedisKey(String name, RedisDataType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        String keyName;
        if (this != DEAD && this != SUCCESS) {
            keyName = this.name;
        } else {
            keyName = keyWithDateSuffix(this.name, LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        }
        return keyName;
    }

    public static String successKeyName(String dateSuffix) {
        return keyWithDateSuffix(SUCCESS.name, dateSuffix);
    }

    public static String deadKeyName(String dateSuffix) {
        return keyWithDateSuffix(DEAD.name, dateSuffix);
    }

    private static String keyWithDateSuffix(String keyName, String date) {
        return keyName + ":" + date;
    }
}
