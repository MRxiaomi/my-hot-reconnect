package com.lym.myhotreconnect.web;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RedisController {
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping("/key/{key}")
    public String findKey(@PathVariable("key") String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @GetMapping("/key/set")
    public String save(@RequestParam String key,@RequestParam String value) {
        redisTemplate.opsForValue().set(key,value);
        return "set done";
    }
}
