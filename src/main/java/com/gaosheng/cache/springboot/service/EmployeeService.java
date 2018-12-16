package com.gaosheng.cache.springboot.service;

import com.gaosheng.cache.springboot.bean.Employee;
import com.gaosheng.cache.springboot.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.cache.annotation.*;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "emp",cacheManager = "employeeCacheManger")
public class EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    /*
     * 将方法的运行结果进行缓存,以后再要相同的数据,直接从缓存中获取,不用调用方法
     *
     *
     * 原理:
     *      1.自动配置类:CacheAutoConfiguration
     *      2.缓存的配置类
     *      org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.JCacheCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.HazelcastCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.HazelcastCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.CouchbaseCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.GuavaCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration
     *      3.那个配置类默认生效:SimpleCacheConfiguration
     *
     *      4.给容器中注册了一个CacheManager:ConcurrentMapCacheManager
     *
     *      5.可以获取和创建ConcurrentMapCache类型的缓存组件,他的作用是将数据保存在ConcurrentMap中
     *
     *      运行流程:
     *      @Cacheable:
     *      1.方法运行之前,先去查询Cache(缓存组件)按照cacheNames指定的名称获取;
     *      (CacheManager先获取查找相应的缓存),第一次获取缓存如果没有Cache组件会自动创建,
     *      2.去Cache中查找到缓存的内容,使用一个key,默认就是方法的参数;
     *          key是按照某种特定策略生成的;默认是使用keyGenerator生成的,默认是是使用SimpleCacheConfiguration生成key;
     *              SimpleCacheConfiguration生成key的策略;
     *                  如果没有参数:key=new SimpleKey();
     *                  如果有一个参数:key = 参数的值
     *                  如果有多个参数:key=new SimpleKey(params);
     *      3.没有查到缓存就调用目标方法;
     *      4.将目标方法返回的结果,放进缓存中,
     *
     *      @Cacheable标注的方法执行之间会先检查缓存中有没有这个数据,默认按照参数的值作为key去查询缓存
     *      如果没有就运行方法并将结果放入缓存;以后再来调用就可以直接使用缓存中的数据;
     *
     *      核心:
     *          1).使用CacheManager[ConcurrentMapCacheManager]按照名字得到Cache[ConcurrentMapCache]组件
     *          2).key使用Generator生成的,默认是SimpleKeyGenerator
     *
     *
     *
     *
     * CacheManager管理多个Cache组件,对缓存的真正CRUD操作在Cache组件中,每一个缓存组件都有自己的名字;
     * 几个属性
     *      cacheName/value;指定缓存组件的名字,将返回结果放回那个缓存中,是数组方式,可以指定多个缓存
     *      key:缓存数据使用key;可以用它来指定,默认是使用方法参数的值 1-默认是参数返回值-->key-value
     *          编写SpEL:  #id;参数id的值   #a0    #p0  #root.args[0]
     *      keyGenerator:key的生成器;可以自己指定生成器的组件id
     *              key/keyGenerator:二选一使用
     *      cacheManager:指定缓存管理器;或者cacheResolver指定获取解析器
     *      condition:指定符合条件的情况下才缓存;(使用SpEL表达式)
     *              codition="#id>0"
     *      uncless:否定缓存;当当前uncless指定条件为true,方法返回就不会被缓存.可以获取到结果进行判定
     *                  uncless = "#result == null"
     *      sync:是否使用异步的模式:不能喝unless共存
     *
     *
     * @param id
     * @return
     */
    @Cacheable(cacheNames ={"emp"}/*,key = "#root.args[0]",keyGenerator = "myKeyGenerator",condition = "#id>1",unless = "#id==2"*/)
    public Employee getEmployee(Integer id){
        Employee empById = employeeMapper.getEmpById(id);
        System.out.println("查询"+id+"的员工信息"+empById);
        return empById;
    }

    /**
     * @CachePut:既调用方法,又更新缓存数据;
     * 修改数据库的同时,同时更新缓存
     * 运行时机:
     *  1.先调用目标方法
     *  2.将目标方法的结果缓存起来
     * 测试步骤:
     * 测试步骤
     *      1.查询1号员工,查询到的结果会放在缓存中;
     *          key:1   value :  lastName : 张三
     *      2.以后查询的结果还是缓存结果
     *      3.更新1号员工[lastName:zhangsan  gender:0]
     *          将方法的返回值放进缓存了;
     *          key:传入的的是employee,返回的也是employee
     *      4.查询1号员工?
     *          应该是更新之后的结果:
     *              key="#employee.id" :使用传入的参数的员工id'
     *              key="#result.id" : 使用返回的id
     *              @Cacheable的key是不能用#result
     *          结果出现了还是之前数据缓存[1号员工没有在缓存数据之中]
     *
     *
     * @param
     * @return
     */

    @CachePut(value = "emp",key = "#result.id"/* , key = "#result.id"*/)
    public Employee updateEmployee(Employee employee){
        employeeMapper.updateEmp(employee);
        System.out.println("updateEmp"+employee);
        return employee;

    }

    /**
     * @CacheEvcit:缓存清除
     *  key:指定要清除的数据
     *  allEntries=true:指定清除缓存中所有的数据
     *  beforeInvocation=false:缓存的清除是否在方法之前执行,默认表示
     *      默认代表清除缓存是在方法执行之后,如果出现异常缓存就不会清除
     *
     *  beforeInvocation=true:
     *      代表清除缓存操作是在方法运行执行之前,无论方法是否出现异常,缓存都清除
     *
     * @param id
     */
    @CacheEvict(value = "emp" /*,key = "#id"*//*,allEntries = true*/,beforeInvocation = true)
    public void delEmployee(Integer id){
        //employeeMapper.deleteEmp(id);
        System.out.println("deleteEmployee"+id+"员工");

        //String str = null;
        //str.length();
    }


    @Caching(
            cacheable = {
                    @Cacheable(value = "emp",key = "#lastName")
        },
            put = {
                    @CachePut(value = "emp",key = "#result.id"),
                    @CachePut(value = "emp",key = "#result.email")
        }
    )
    public  Employee getEmployeeByLastName(String  lastName){
        return employeeMapper.getEmpByLastName(lastName);
    }
}
