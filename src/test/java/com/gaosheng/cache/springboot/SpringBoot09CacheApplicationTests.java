package com.gaosheng.cache.springboot;

import com.gaosheng.cache.springboot.bean.Employee;
import com.gaosheng.cache.springboot.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBoot09CacheApplicationTests {
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private RedisTemplate redisTemplate;//k-v都是操作对象的

	@Autowired
	private StringRedisTemplate stringRedisTemplate;//k-v都是操作字符串的

	@Autowired
	private RedisTemplate employeeRedisTemplate;

	@Test
	public void contextLoads() {
	}


	/**Redis常用的五大数据类型
	 * String(字符串). List(列表). Set(集合). Hash(散列). ZSet(有序集合)
	 * 		stringRedisTemplate.opsForValue();[String(字符串)]
	 		stringRedisTemplate.opsForList();[List(列表)]
	        stringRedisTemplate.opsForSet();[Set(无序集合)]
	        stringRedisTemplate.opsForHash();[Hash(散列)]
	        stringRedisTemplate.opsForZSet();[ZSet(有序集合)]
	 */
	@Test
	public void test1(){
		//给Redis中保存数据
		//stringRedisTemplate.opsForValue().append("msg","hello");
		//读取数据
		//String msg = stringRedisTemplate.opsForValue().get("msg");
		//System.out.println(msg);
//		stringRedisTemplate.opsForList().leftPush("mylist","1");
//		stringRedisTemplate.opsForList().leftPush("mylist","2");
//		String mylist = stringRedisTemplate.opsForList().leftPop("mylist");
//		System.out.println(mylist);
	}

	@Test
	public void test2(){
		//默认如果保存对象,使用jdk序列化机制,序列化后的数据保存到redis中
		Employee employee = employeeService.getEmployee(2);
		//redisTemplate.opsForValue().set("emp-01",employee);

		//1.将数据以json的方式保存
		//(1).自己将数据转化为json
		//(2).redisTemplate默认的序列化规则
		employeeRedisTemplate.opsForValue().set("emp-02",employee);

	}
}
