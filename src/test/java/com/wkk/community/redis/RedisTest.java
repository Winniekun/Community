package com.wkk.community.redis;

import com.wkk.community.CommunityApplication;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @Time: 2020/5/3下午9:07
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testString(){
        String resdisKey = "test:String";
        redisTemplate.opsForValue().set(resdisKey, 1);
        System.out.println(redisTemplate.opsForValue().get(resdisKey));
        System.out.println(redisTemplate.opsForValue().increment(resdisKey));
        System.out.println(redisTemplate.opsForValue().decrement(resdisKey));

    }

    @Test
    public void testHash(){
        String redisKey = "test:hash";
        redisTemplate.opsForHash().put(redisKey, "id", 1);
        redisTemplate.opsForHash().put(redisKey, "name", "维坤坤");
        System.out.println(redisTemplate.opsForHash().get(redisKey, "id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey, "name"));

    }

    @Test
    public void testList(){
        String redisKey = "test:List";
        redisTemplate.opsForList().leftPush(redisKey, 1);
        redisTemplate.opsForList().leftPush(redisKey, 2);
        redisTemplate.opsForList().leftPush(redisKey, 3);
        System.out.println("列表的size为 " + redisTemplate.opsForList().size(redisKey));

        System.out.println("索引为2的值为 " + redisTemplate.opsForList().index(redisKey, 2));

        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
    }

    @Test
    public void testSet(){
        String redisKey = "test:set";
        redisTemplate.opsForSet().add(redisKey, "孔维坤", "维坤坤","老五", "维坤坤");
        System.out.println(redisTemplate.opsForSet().size(redisKey));
        System.out.println(redisTemplate.opsForSet().pop(redisKey));
        System.out.println(redisTemplate.opsForSet().members(redisKey));
    }

    @Test
    public void testSortedSet(){
        String redisKey = "test:zSet";
        redisTemplate.opsForZSet().add(redisKey, "维坤坤", 100);
        redisTemplate.opsForZSet().add(redisKey, "孔维坤", 99);
        redisTemplate.opsForZSet().add(redisKey, "韩欢欢", 98);
        redisTemplate.opsForZSet().add(redisKey, "老赖皮", 90);
        // 共有多少数据
        System.out.println(redisTemplate.opsForZSet().zCard(redisKey));
        System.out.println(redisTemplate.opsForZSet().score(redisKey, "维坤坤"));
        // 分数从低到高
        System.out.println(redisTemplate.opsForZSet().rank(redisKey, "老赖皮"));
        System.out.println(redisTemplate.opsForZSet().rank(redisKey, "韩欢欢"));
        System.out.println(redisTemplate.opsForZSet().rank(redisKey, "维坤坤"));
        System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey, "维坤坤"));
        System.out.println(redisTemplate.opsForZSet().range(redisKey, 0, 3));
        System.out.println(redisTemplate.opsForZSet().reverseRange(redisKey, 0, 3));

    }

    @Test
    public void testExpire(){
        redisTemplate.delete("test:set");
        System.out.println(redisTemplate.hasKey("test:set"));
        redisTemplate.expire("test:zSet", 2, TimeUnit.SECONDS);
    }

    // 多次访问同一个key
    @Test
    public void testBound(){
        String keys = "test:String";
        BoundValueOperations boundValueOperations = redisTemplate.boundValueOps(keys);
        System.out.println(boundValueOperations.get());
        System.out.println(boundValueOperations.increment());

    }

    // 编程式事物
    @Test
    public void testTx(){
        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String key = "test:tx";
                // 操作
                redisOperations.multi();

                redisOperations.opsForSet().add(key, "wkk", "kwk","hhh");

                System.out.println(redisOperations.opsForSet().members(key));

                return redisOperations.exec();
            }
        });
        System.out.println(obj);
    }
}
