package xyz.toby.task;

/**
 * start
 *
 * @Author: zhangbin
 * @Date: 2020/12/16
 */

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import xyz.toby.task.annotation.TransactionAsyncTask;
import xyz.toby.task.config.RedisProperties;
import xyz.toby.task.exception.TransactionAsyncTaskException;
import xyz.toby.task.exception.WrongBeanException;
import xyz.toby.task.handler.BaseTaskHandler;
import xyz.toby.task.spring.AfterTxService;
import xyz.toby.task.utils.JacksonUtil;
import xyz.toby.task.utils.ScheduledUtil;
import xyz.toby.task.utils.SpringTransactionHelper;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import static xyz.toby.task.config.Constant.METHOD_NAME_DELAYED;
import static xyz.toby.task.config.Constant.METHOD_NAME_GET_BIZ_ID;


@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class TransactionAsyncTaskConfigure implements ApplicationContextAware {

    private Logger logger = Logger.getLogger(TransactionAsyncTaskConfigure.class.getName());

    private Map<String, BaseTaskHandler> taskRegister;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(TransactionAsyncTask.class);
        Set<Map.Entry<String, Object>> entries = beans.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Object handler = entry.getValue();
            if (!(handler instanceof BaseTaskHandler)) {
                throw new WrongBeanException("define transaction async task bean error");
            }
            Class<?> clazz = handler.getClass();
            TransactionAsyncTask annotation = clazz.getAnnotation(TransactionAsyncTask.class);
            validateTaskAnnotation(annotation, clazz.getName());

        }
    }

    /**
     * 代理异步任务，不要直接执行，放入redis等待轮询调用
     * @param bean 代理的bean
     * @param methodName 执行的方法
     * @param callback 回调函数
     * @return Enhancer
     */
    private Object proxyHandler(Object bean, String methodName, Callback callback) {
        BaseTaskHandler taskHandler = (BaseTaskHandler) bean;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bean.getClass());
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            if (methodName.equals(method.getName())) {
                // Since the anonymous class only accepts final variables,
                // so the target value is wrapped in a wrapped object
                final WrappedScheduledTime wrappedScheduledTime = new WrappedScheduledTime();
                if (METHOD_NAME_DELAYED.equals(methodName)) {
                    wrappedScheduledTime.setValue(ScheduledUtil.getScheduledTime((int) args[1]));
                }

                Object actualParams = args[0];
                String params = JacksonUtil.beanToJson(actualParams);

                // call the callback method to compute taskId
                String taskId = taskHandler.getTaskId(actualParams);

                // delay the submitting if it's in a transaction
                if (SpringTransactionHelper.isSpringTransactionActive()) {
                    logger.info("[tx] It's in a active transaction, delay the task submitting");
                    TransactionSynchronizationManager.registerSynchronization(
                            new AfterTxService(() -> callback.run(args, taskId, params, wrappedScheduledTime)));
                } else {
                    logger.info("[tx] It's not in a active transaction, submit task directly");
                    callback.run(args, taskId, params, wrappedScheduledTime);
                }
                return METHOD_NAME_DELAYED.equals(methodName) ? wrappedScheduledTime.getValue() : null;
            } else if (METHOD_NAME_GET_BIZ_ID.equals(method.getName())) {
                //noinspection unchecked
                return taskHandler.getTaskId(args[0]);
            } else {
                throw new TransactionAsyncTaskException(method.getName());
            }
        });
        return enhancer.create();
    }

    /**
     * 校验注解配置项
     * @param annotation
     * @param handlerName
     * @return
     */
    private boolean validateTaskAnnotation(TransactionAsyncTask annotation, String handlerName) {
        //todo 注解配置验证
        return true;
    }

    @Setter
    @Getter
    private static class WrappedScheduledTime {
        private long value;
    }

    @FunctionalInterface
    private interface Callback {
        void run(Object[] args, String taskId, String params, WrappedScheduledTime wrappedScheduledTime);
    }

}
