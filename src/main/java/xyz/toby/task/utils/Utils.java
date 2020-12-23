package xyz.toby.task.utils;

import org.springframework.util.Assert;
import xyz.toby.task.model.TaskData;

import java.util.UUID;

/**
 * @Author: zhangbin
 * @Date: 2020/12/23
 */
public class Utils {
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String shortId(TaskData data) {
        return shortId(data.getTaskId());
    }

    public static String shortId(String uuid) {
        Assert.notNull(uuid, "uuid is null");
        return uuid.length() <= 8 ? uuid : uuid.substring(0, 8);
    }
}
