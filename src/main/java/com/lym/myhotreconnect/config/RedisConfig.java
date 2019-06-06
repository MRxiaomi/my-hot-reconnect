package com.lym.myhotreconnect.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.lym.myhotreconnect.service.event.ApolloConfigChangeEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @ApolloConfig
    private Config config;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 纠正：使用连接池也能切换缓存配置，因为这里切换的是JedisConnectionFactory，在进行缓存操作的时候从连接工厂获取连接（类似数据源）
     * @return
     */
    private JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(config.getProperty("redis.host","localhost123"));
        redisStandaloneConfiguration.setPort(Integer.valueOf(config.getProperty("redis.port","6379")));
        redisStandaloneConfiguration.setDatabase(Integer.valueOf(config.getProperty("redis.database","0")));
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }

    @Bean("redisTemplate")
    @ConditionalOnMissingBean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return redisTemplate;
    }

    @EventListener
    public void refreshRedisConn(ApolloConfigChangeEvent apolloConfigChangeEvent){
        for (String key : apolloConfigChangeEvent.getConfigChangeEvent().changedKeys()) {
            if (key.startsWith("redis.")) {
                RedisTemplate redisTemplate = (RedisTemplate)applicationContext.getBean("redisTemplate");
                redisTemplate.setConnectionFactory(jedisConnectionFactory());
                redisTemplate.afterPropertiesSet();
                System.out.println("【缓存】动态切换成功...");
                break;
            }
        }
    }
}
