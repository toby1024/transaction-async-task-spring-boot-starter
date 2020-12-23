package xyz.toby.task.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import xyz.toby.task.exception.TransactionAsyncTaskException;

/**
 * @Author: zhangbin
 * @Date: 2020/12/16
 */
@UtilityClass
public class JacksonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String beanToJson(Object o){
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new TransactionAsyncTaskException(e);
        }
    }
}
