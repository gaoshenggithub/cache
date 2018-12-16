package com.gaosheng.cache.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


/**
 * 一.搭建基本环境
 * 1.导入数据库文件,创建出department和employee表
 * 2.创建javaBean封装数据
 * 3.整合MyBatis操作数据库
 * 		1.配置数据源信息
 * 		2.使用注解版的MyBatis;
 * 			1).@MapperScan指定需要扫描的mapper接口所在包
 * 二.快速体验缓存
 * 		步骤:
 * 			1.开启基于注解的缓存@EnableCaching
 * 			2.标注缓存注解即可
 * 				@Cacheable
 * 				@CacheEvict
 * 				@CachePut
 * 默认使用的是ConcurrentMapCacheManager==>>ConcurrentMapCache,将数据保存在ConcurrentMap<Object, Object>中
 * 开发中使用缓存中间件,redis,memcached.ecache;
 * 三.整合redis作为缓存
 * Redis 是一个开源（BSD许可）的，内存中的数据结构存储系统，它可以用作数据库、缓存和消息中间件。
 * 	1.安装redis,
 * 	2.引入redis的start
 * 	3.配置redis
 * 	4.测试缓存原理
 * 		原理:CacheManager===Cache 缓存组件来实际中存储数据
 * 		1).引入redis的starter,容器中保存的是RedisCacheManager;
 * 		2).RedisCacheManager帮我们创建 RedisCache来作为缓存组件:RedisCache通过操作redis来缓存数据的
 *		3).默认保存数据 k-v都是Object,利用序列化保存,如何保存json?
 *			1.引入了redis的starter,cacheManager变为RedisCacheManager;
 *			2.默认创建RedisCacheManger 操作redis的时候使用的是RedisTemplate<Object,Object> redisTemplate;
 *			3.RedisTemplate<Object,Object>是默认的jdk序列化机制
 *		4)自定义CacheManager
 */
@SpringBootApplication
@MapperScan(basePackages = "com.gaosheng.cache.springboot.mapper")
@EnableCaching
public class SpringBoot09CacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot09CacheApplication.class, args);
	}
}
