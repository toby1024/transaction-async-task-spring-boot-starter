package xyz.toby.task.redis;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import xyz.toby.task.exception.TransactionAsyncTaskException;

import java.io.IOException;

/**
 * redis lua 脚本
 *
 * @Author: zhangbin
 * @Date: 2020/12/16
 */
public class Scripts {
    private Scripts() {
    }

    private static final String PREFIX = "META-INF/trans-async-task-script/";

    public static final RedisScript<Long> submitTaskToList;

    public static final RedisScript<Long> submitTaskToZset;

    public static final RedisScript<Long> submitTaskToHash;

    public static final RedisScript<String> tryMoveRetryToReady;

    public static final RedisScript<String> tryMoveDelayedToReady;

    public static final RedisScript<Boolean> moveDependentToReady;

    public static final RedisScript<Boolean> moveDependentToDead;

    public static final RedisScript<String> tryMoveReadyToWorking;

    public static final RedisScript<Boolean> moveWorkingToSuccess;

    public static final RedisScript<Boolean> moveWorkingToDead;

    public static final RedisScript<Boolean> moveWorkingToRetry;

    public static final RedisScript<Boolean> updateScheduledTime;

    public static final RedisScript<Boolean> deleteDelayedTask;

    public static final RedisScript<Boolean> resumeDeadTask;

    static {
        submitTaskToList = script("submitTaskToList.lua", Long.class);
        submitTaskToZset = script("submitTaskToZset.lua", Long.class);
        submitTaskToHash = script("submitTaskToHash.lua", Long.class);
        tryMoveRetryToReady = script("tryMoveRetryToReady.lua", String.class);
        tryMoveDelayedToReady = script("tryMoveDelayedToReady.lua", String.class);
        moveDependentToReady = script("moveDependentToReady.lua", Boolean.class);
        moveDependentToDead = script("moveDependentToDead.lua", Boolean.class);
        tryMoveReadyToWorking = script("tryMoveReadyToWorking.lua", String.class);
        moveWorkingToSuccess = script("moveWorkingToSuccess.lua", Boolean.class);
        moveWorkingToDead = script("moveWorkingToDead.lua", Boolean.class);
        moveWorkingToRetry = script("moveWorkingToRetry.lua", Boolean.class);
        updateScheduledTime = script("updateScheduledTime.lua", Boolean.class);
        deleteDelayedTask = script("deleteDelayedTask.lua", Boolean.class);
        resumeDeadTask = script("resumeDeadTask.lua", Boolean.class);
    }

    private static <T> RedisScript<T> script(String filename, Class<T> clz) {
        ResourceScriptSource source = new ResourceScriptSource(new ClassPathResource(PREFIX + filename));
        try {
            String str = source.getScriptAsString();
            return new DefaultRedisScript<>(str, clz);
        } catch (IOException e) {
            throw new TransactionAsyncTaskException(e);
        }
    }
}
