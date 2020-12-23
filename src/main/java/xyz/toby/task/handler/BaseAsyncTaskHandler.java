package xyz.toby.task.handler;

/**
 * 普通异步任务
 *
 * @Author: zhangbin
 * @Date: 2020/12/16
 */
public interface BaseAsyncTaskHandler<T> extends BaseTaskHandler<T> {
    /**
     * 异步任务执行方法
     * 业务实现这个接口，在代码运行时进行回调
     *
     * @param params
     */
    void performAsync(T params);
}
