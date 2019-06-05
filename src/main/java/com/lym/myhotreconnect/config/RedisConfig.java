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
//    @Value("${redis.host}")
//    private String host;
//    @Value("${redis.username}")
//    private String masterName;
//    @Value("${redis.password}")
//    private String password;
//    @Value("${redis.port}")
//    private Integer port;
//    @Value("${redis.database}")
//    private Integer database;

    @ApolloConfig
    private Config config;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 总结：如果不使用连接池，每次都会创建一个Jedis连接，可以实现动态切换
     *      如果使用连接池，连接池的连接与redis保持长连接，则不可以实现动态切换
     *      因此，需要确认 redis 客户端是否支持多数据源
     *
     *      MySQL也有该问题，直接使用druid连接池，更新了配置不会生效
     *      因此需要使用AbstractRoutingDataSource，生成新的数据源并可同时使用多个数据源
     * @return
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(config.getProperty("redis.host","localhost123"));
        redisStandaloneConfiguration.setPort(Integer.valueOf(config.getProperty("redis.port","6379")));
        redisStandaloneConfiguration.setDatabase(Integer.valueOf(config.getProperty("redis.database","0")));
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        jedisConnectionFactory.setUsePool(true);
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
