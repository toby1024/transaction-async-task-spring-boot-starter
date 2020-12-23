package xyz.toby.task.annotation;

import java.lang.annotation.*;

/**
 * 开启事务异步任务
 *
 * @author zhangbin
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableTransactionAsyncTask {
    boolean value() default true;
}
