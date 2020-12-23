package xyz.toby.task;

import lombok.RequiredArgsConstructor;
import xyz.toby.task.exception.TransactionAsyncTaskException;
import xyz.toby.task.handler.BaseAsyncTaskHandler;
import xyz.toby.task.handler.BaseTaskHandler;
import xyz.toby.task.model.TaskData;
import xyz.toby.task.model.TaskInfo;
import xyz.toby.task.model.TaskType;
import xyz.toby.task.redis.RedisUtil;
import xyz.toby.task.utils.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import static xyz.toby.task.config.Constant.*;

/**
 * @Author: zhangbin
 * @Date: 2020/12/23
 */
public class TaskSubmitter {
    private Logger logger = Logger.getLogger(TaskSubmitter.class.getName());

    private RedisUtil redisUtil;

    public TaskSubmitter(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public String submitAsync(TaskInfo taskInfo, Class<BaseAsyncTaskHandler> handlerClass) {
        String taskId = Utils.uuid();
        TaskData data = generateTaskData(taskId, taskInfo, handlerClass);
        data.setTaskType(TaskType.ASYNC);
        data.setReadyTime(System.currentTimeMillis());
        return submitTask(data);
    }

    private String submitTask(TaskData data) {
        Long r = redisUtil.submitTask(data);
        switch (r.intValue()) {
            case LUA_RESULT_FAILED:
                logger.log(Level.FINE, "提交异步任务失败, taskData:" + data);
                throw new TransactionAsyncTaskException("提交异步任务失败, taskData: " + data);
            case LUA_RESULT_REPEATED_BIZ_ID:
                logger.info("task id exists, ignore this submitting");
                break;
            case LUA_RESULT_OK:
                logger.info("[taskId=" + Utils.shortId(data) + "] submitted task, " + data);
                break;
            default:
                throw new TransactionAsyncTaskException();
        }
        return data.getTaskId();
    }

    private TaskData generateTaskData(String taskId, TaskInfo taskInfo, Class<? extends BaseTaskHandler> handlerClass) {
        TaskData data = new TaskData();
        data.setTaskId(taskInfo.getTaskId());
        data.setTimeout(taskInfo.getTimeout());
        data.setMaxRetry(taskInfo.getMaxRetry());
        data.setParams(taskInfo.getParams());

        data.setTaskId(taskId);
        data.setTaskHandler(handlerClass.getName());
        data.setCreatedTime(System.currentTimeMillis());

        data.setRemainingRetry(data.getMaxRetry());
        return data;
    }
}
