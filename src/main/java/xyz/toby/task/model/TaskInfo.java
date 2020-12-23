package xyz.toby.task.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
public class TaskInfo {

    /**
     * 除重机制: 提交多个bizId相同的任务，只会执行一个任务
     */
    String taskId;

    /**
     * 任务执行的超时时间, 单位秒
     */
    Integer timeout;

    /**
     * 任务最大重试次数
     */
    Integer maxRetry;

    /**
     * 任务携带的参数
     */
    String params;

    public TaskInfo(String taskId, Integer timeout, Integer maxRetry, String params) {
        this.taskId = taskId;
        this.timeout = timeout;
        this.maxRetry = maxRetry;
        this.params = params;
    }
}
