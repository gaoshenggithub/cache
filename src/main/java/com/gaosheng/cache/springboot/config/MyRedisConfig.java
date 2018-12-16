package com.gaosheng.cache.springboot.config;

import com.gaosheng.cache.springboot.bean.Department;
import com.gaosheng.cache.springboot.bean.Employee;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.net.UnknownHostException;

@Configuration
public class MyRedisConfig {
    @Bean
    public RedisTemplate<Object, Employee>  employeeRedisTemplate(
            RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object,  Employee> template = new RedisTemplate<Object,  Employee>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Employee> serializer = new Jackson2JsonRedisSerializer<Employee>(Employee.class);
        template.setDefaultSerializer(serializer);
        return template;
    }

    //CacheManagerCustomizers可以定制缓存管理规则
    @Bean
    @Primary//将某个缓存管理作为默认的
    public RedisCacheManager employeeCacheManager( RedisTemplate<Object, Employee> redisTemplate){
        RedisCacheManager redisCacheManger = new RedisCacheManager(redisTemplate);
        //key多了一个前缀
        //使用前缀,默认会将CacheName作为key的前缀
        redisCacheManger.setUsePrefix(true);
        return redisCacheManger;
    }

    @Bean
    public RedisTemplate<Object,Department>  deptRedisTemplate(
            RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object,  Department> template = new RedisTemplate<Object, Department>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Department> serializer = new Jackson2JsonRedisSerializer<Department>(Department.class);
        template.setDefaultSerializer(serializer);
        return template;
    }

    //CacheManagerCustomizers可以定制缓存管理规则
    @Bean
    public RedisCacheManager deptCacheManager( RedisTemplate<Object,Department> redisTemplate){
        RedisCacheManager redisCacheManger = new RedisCacheManager(redisTemplate);
        //key多了一个前缀
        //使用前缀,默认会将CacheName作为key的前缀
        redisCacheManger.setUsePrefix(true);
        return redisCacheManger;
    }

}
