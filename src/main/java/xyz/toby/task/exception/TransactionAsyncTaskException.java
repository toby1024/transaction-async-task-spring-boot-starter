package xyz.toby.task.exception;

/**
 * @Author: zhangbin
 * @Date: 2020/12/16
 */
public class TransactionAsyncTaskException extends RuntimeException {
    public TransactionAsyncTaskException(String message) {
        super(message);
    }

    public TransactionAsyncTaskException(Throwable cause) {
        super(cause);
    }
}
