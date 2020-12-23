package xyz.toby.task.model;

import lombok.Data;

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
    /**
     * 任务handler class name
     */
    private String taskHandler;
}
