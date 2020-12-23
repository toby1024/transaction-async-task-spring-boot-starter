package xyz.toby.task.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import xyz.toby.task.config.RedisProperties;
import xyz.toby.task.exception.TransactionAsyncTaskException;
import xyz.toby.task.model.TaskData;
import xyz.toby.task.utils.JacksonUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static xyz.toby.task.redis.RedisKey.READY;
import static xyz.toby.task.redis.RedisKey.TASK_ID;

/**
 * @Author: zhangbin
 * @Date: 2020/12/16
 */
public class RedisUtil {
    private final StringRedisTemplate stringTemplate;
    private final RedisTemplate<String, TaskData> beanTemplate;


    public RedisUtil(RedisProperties redisProperties) {

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        if (!StringUtils.isEmpty(redisProperties.getPassword())) {
            config.setPassword(RedisPassword.of(redisProperties.getPassword()));
        }
        config.setDatabase(redisProperties.getDatabase());
        LettuceConnectionFactory cf = new LettuceConnectionFactory(config);
        cf.afterPropertiesSet();

        // redis template
        RedisTemplate<String, TaskData> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(TaskData.class));
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(TaskData.class));
        template.setConnectionFactory(cf);
        template.afterPropertiesSet();

        this.beanTemplate = template;
        this.stringTemplate = new StringRedisTemplate(cf);
    }

    /**
     * 提交异步任务
     *
     * @param taskData 任务数据
     * @return
     */
    public Long submitTask(TaskData taskData) {
        String taskId = taskData.getTaskId();

        if (StringUtils.isEmpty(taskId)) {
            throw new TransactionAsyncTaskException("异步任务" + taskData.getTaskHandler() + "必须设置taskId");
        }

        switch (taskData.getTaskType()) {
            case ASYNC:
                return submitAsyncTask(taskData, taskId);
            case DELAYED:
                //todo 延时任务
                break;
                //todo 有依赖关系的任务
            case DEPENDENT:
                break;
            default:
                throw new TransactionAsyncTaskException("unknown task type!");
        }
        return null;
    }

    public List<String> asList(RedisKey... keys) {
        return Arrays.stream(keys).map(RedisKey::getName).collect(Collectors.toList());
    }

    /**
     * 提交普通异步任务到redis
     * @param taskData 任务数据
     * @param taskId 任务id
     * @return 1：提交成功，0：失败，2：
     */
    public Long submitAsyncTask(Object taskData, String taskId) {
        return stringTemplate.execute(Scripts.submitTaskToList,
                                      asList(TASK_ID, READY),
                                      taskId,
                                      JacksonUtil.beanToJson(taskData));
    }
}
