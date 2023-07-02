package ru.clevertec.NewsManager.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 The RedisConfig class provides configuration for Redis in the NewsManager application.
 It defines the RedisTemplate and ObjectMapper beans used for interacting with Redis.
 */
@Profile("prod")
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    /**
     Creates a RedisTemplate bean with String key and value serializers.
     @param connectionFactory The RedisConnectionFactory used to create the RedisTemplate.
     @return The RedisTemplate instance.
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    /**
     Creates an ObjectMapper bean with JavaTimeModule registered.
     @return The ObjectMapper instance.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}

