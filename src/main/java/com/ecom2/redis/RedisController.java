package com.ecom2.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class RedisController {

    private final BaseRedisService redisService;

    @PostMapping("/redis")
    public void set(){
        redisService.set("hihi", "haha");
    }
}
