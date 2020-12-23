package xyz.toby.task.exception;

/**
 * task bean init exception
 *
 * @Author: zhangbin
 * @Date: 2020/12/16
 */
public class WrongBeanException extends RuntimeException {
    public WrongBeanException(String message) {
        super(message);
    }
}
