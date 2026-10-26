package com.example.flight.booking;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConnectionTest {

    @Bean
    CommandLineRunner testRedis(RedisTemplate<String, String> redisTemplate) {
        return args -> {
            try {
                redisTemplate.opsForValue().set("test:key", "Redis Connected");

                String value = redisTemplate.opsForValue().get("test:key");

                System.out.println("Redis is connected!");
                System.out.println("Value: " + value);
            } catch (Exception e) {
                System.err.println("Redis connection failed: " + e.getMessage());
            }
        };
    }
}