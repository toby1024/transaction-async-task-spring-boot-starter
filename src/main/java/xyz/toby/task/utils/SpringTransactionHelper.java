package xyz.toby.task.utils;

import lombok.experimental.UtilityClass;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * spring事务判断
 * @Author: zhangbin
 * @Date: 2020/12/17
 */
@UtilityClass
public class SpringTransactionHelper {
    /**
     * 是否开启spring事务
     * @return spring事务开启标志
     */
    public static boolean isSpringTransactionActive() {
        // #ARCH-308
        return TransactionSynchronizationManager.isSynchronizationActive() &&
                TransactionSynchronizationManager.isActualTransactionActive();
    }
}
