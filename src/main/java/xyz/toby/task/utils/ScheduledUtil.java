package xyz.toby.task.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 调度参数相关工具类
 *
 * @Author: zhangbin
 * @Date: 2020/12/17
 */
@UtilityClass
public class ScheduledUtil {
    /**
     * 计算任务执行实践
     *
     * @param delayedSec 延迟 秒
     * @return 执行时间
     */
    public static long getScheduledTime(int delayedSec) {
        return System.currentTimeMillis() + 1000L * delayedSec;
    }
}
