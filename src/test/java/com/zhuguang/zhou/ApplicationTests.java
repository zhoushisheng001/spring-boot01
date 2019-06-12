package com.zhuguang.zhou;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhuguang.zhou.pojo.ManPersion;
import com.zhuguang.zhou.pojo.Persion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void test01() {
		ValueOperations valueOperations = redisTemplate.opsForValue();
		String name = String.valueOf(valueOperations.get("name"));
		System.out.println(name);
	}

}

