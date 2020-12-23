package xyz.toby.task.handler;

/**
 * 异步任务需要实现这个接口，starter通过扫描这个接口的实现类来注册异步任务
 *
 * @Author: zhangbin
 * @Date: 2020/12/16
 */
public interface BaseTaskHandler<T> {
    String DUMMY_BIZ_ID = "transaction-async-task-id";

    /**
     * 业务方需要实现这个回调方法，根据自己传给perform方法的params参数，返回一个taskId,
     * 以实现业务除重: 对于一个taskHandler, 多次提交taskId相同的任务, 框架只会执行一次.
     *
     * @param params 调用方传给perform*方法的参数
     */
    String getTaskId(T params);

    /**
     * 处理任务死亡逻辑
     *
     * @param params 调用方传给perform*方法的参数
     */
    default void onDead(T params) {
    }
}
