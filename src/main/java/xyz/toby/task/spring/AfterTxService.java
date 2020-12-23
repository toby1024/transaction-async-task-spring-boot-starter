package xyz.toby.task.spring;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;

import java.util.logging.Logger;

/**
 * @Author: zhangbin
 * @Date: 2020/12/17
 */
public class AfterTxService extends TransactionSynchronizationAdapter {
    private Logger logger = Logger.getLogger(AfterTxService.class.getName());
    private final Runnable runnable;

    public AfterTxService(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void afterCommit() {
        logger.info("[tx] Submitting async task after committing transaction");
        runnable.run();
    }

    @Override
    public void afterCompletion(int status) {
        if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
            logger.info("[tx] Occur transaction rollback, async task isn't submitted!");
        }
    }
}
