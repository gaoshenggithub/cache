package com.gaosheng.cache.springboot.service;

import com.gaosheng.cache.springboot.bean.Department;
import com.gaosheng.cache.springboot.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "dept",cacheManager = "deptCacheManager")//抽取公共配置
public class DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Qualifier("deptCacheManager")
    @Autowired
    RedisCacheManager deptCacheManager;

    /**
     * 缓存的数据能存入redis;
     * 第二次从缓存中查询就不能反序列化回来;
     * 存的是dept的json数据;CacheManager默认使用RedisTemplate<Object,Employee>进行redis操作
     * @param id
     * @return
     */
//    @Cacheable(value = "dept")
//    public Department getDept(Integer id){
//        System.out.println("查询"+id);
//        Department department = departmentMapper.getDeptById(id);
//        return department;
//    }

    public Department getDept(Integer id){
        System.out.println("查询"+id);
        Department department = departmentMapper.getDeptById(id);
        Cache cache = deptCacheManager.getCache("dept");
        cache.put("dept:"+id, department);
        return department;
    }
}
