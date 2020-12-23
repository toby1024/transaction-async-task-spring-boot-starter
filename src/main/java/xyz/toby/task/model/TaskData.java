package xyz.toby.task.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import xyz.toby.task.json.RawJsonDeserializer;

/**
 * @Author: zhangbin
 * @Date: 2020/12/16
 */
@Data
public class TaskData {
    /**
     * 任务id，唯一性
     */
    private String taskId;
    private TaskType taskType;
    private int timeout;
    private int maxRetry;

    private long createdTime;
    private Long readyTime;
    private int remainingRetry;

    @JsonDeserialize(using = RawJsonDeserializer.class)
    String params;
    /**
     * 任务handler class name
     */
    private String taskHandler;

    @Override
    public String toString() {
        return "TaskData{" +
                "taskId='" + taskId + '\'' +
                ", taskType=" + taskType +
                ", timeout=" + timeout +
                ", maxRetry=" + maxRetry +
                ", createdTime=" + createdTime +
                ", readyTime=" + readyTime +
                ", remainingRetry=" + remainingRetry +
                ", params='" + params + '\'' +
                ", taskHandler='" + taskHandler + '\'' +
                '}';
    }
}
