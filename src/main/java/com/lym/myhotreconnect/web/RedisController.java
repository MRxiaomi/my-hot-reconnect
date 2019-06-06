package com.lym.myhotreconnect.web;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liuyumeng
 *
 * 测试接口：
 * Redis连接后，不重启项目，动态切换数据源
 */

@RestController
public class RedisController {
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 查询缓存
     * @param key
     * @return
     */
    @GetMapping("/key/{key}")
    public String findKey(@PathVariable("key") String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 更新缓存
     * @param
     * @return
     */
    @GetMapping("/key/set")
    public String save(@RequestParam String key,@RequestParam String value) {
        redisTemplate.opsForValue().set(key,value);
        return "set done";
    }
}
