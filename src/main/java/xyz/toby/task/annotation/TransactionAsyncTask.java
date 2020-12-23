package xyz.toby.task.annotation;

import java.lang.annotation.*;

/**
 * 异步任务注解
 *
 * @author zhangbin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TransactionAsyncTask {
    int DEFAULT_FIXED_RETRY_INTERVAL = -1;

    /**
     * 任务执行超时时间, 单位秒. 必填
     */
    int timeout();

    /**
     * 最大重试次数. 默认为0
     */
    int maxRetry() default 0;

    /**
     * 任务重试的时间间隔, 单位秒。
     * <p>
     * 若设置这个值，表示任务重试策略改为"固定时间间隔":
     * 和默认的重试策略不同，当前重试策略下，每次重试的时间间隔都是固定的，
     */
    int fixedRetryInterval() default DEFAULT_FIXED_RETRY_INTERVAL;

    /**
     * 是否开启无限重试。
     * <p>
     * 开启之后任务将没有重试次数上限，会一直重试，直到任务执行成功为止。 所以设置了这个值后 maxRetry 这一属性的值将变得没有用。
     */
    boolean isInfiniteRetry() default false;
}
