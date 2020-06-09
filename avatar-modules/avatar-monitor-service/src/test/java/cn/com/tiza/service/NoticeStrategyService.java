package cn.com.tiza.service;

import cn.com.tiza.MonitorApplication;
import cn.com.tiza.dal.beetlsql.AbstractTestClass;
import cn.com.tiza.dal.beetlsql.SqlKit;
import cn.com.tiza.dao.UserDao;
import com.google.common.collect.Lists;
import com.netflix.discovery.converters.Auto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author tz0920
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = MonitorApplication.class)
public class NoticeStrategyService extends AbstractTestClass {
    @Autowired
    private StringRedisTemplate redisTemplate;



    @Test
    public void addKey(){
        redisTemplate.opsForValue().set("expireTest_01","test",10, TimeUnit.SECONDS);
    }
}
