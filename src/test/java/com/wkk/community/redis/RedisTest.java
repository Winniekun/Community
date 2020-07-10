package com.wkk.community.redis;

import com.wkk.community.CommunityApplication;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
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
        String resdisKey = "like:user:111";
//        redisTemplate.opsForValue().set(resdisKey, 1);
        System.out.println(redisTemplate.opsForValue().get(resdisKey));
//        System.out.println(redisTemplate.opsForValue().increment(resdisKey));
//        System.out.println(redisTemplate.opsForValue().decrement(resdisKey));

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

    // hyperLogLog
    // 统计20万个重复数据的独立总数
    @Test
    public void testHyperLogLog(){
        String redisKey = "test:hyperLogLog:01";
        for (int i = 0; i <= 100000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey, i);
        }

        for (int i = 0; i <= 100000; i++) {
            int r = (int) (Math.random() * 100000 + 1);
            redisTemplate.opsForHyperLogLog().add(redisKey, r);

        }
        Long size = redisTemplate.opsForHyperLogLog().size(redisKey);
        System.out.println(size);

    }
    // 数据合并
    @Test
    public void testCombineHLLG(){
        String redisKey1 = "test:hyperLogLog:02";
        for (int i = 0; i <= 10000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey1, i);
        }
        String redisKey2 = "test:hyperLogLog:03";
        for (int i = 5001; i <= 15000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey2, i);
        }
        String redisKey3 = "test:hyperLogLog:04";
        for (int i = 10001; i <= 20000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey3, i);
        }
        String unionKey = "test:hyperLogLog:union";
        redisTemplate.opsForHyperLogLog().union(unionKey, redisKey1, redisKey2,redisKey3);

        Long size = redisTemplate.opsForHyperLogLog().size(unionKey);
        System.out.println(size);


    }

    // bitmap
    // 统计一组数据的布尔值
    @Test
    public void testBitMap(){
        String redisKey = "test:bm:01";

        redisTemplate.opsForValue().setBit(redisKey, 1, true);
        redisTemplate.opsForValue().setBit(redisKey, 4, true);
        redisTemplate.opsForValue().setBit(redisKey, 7, true);

        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 0));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 1));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 2));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 4));

        // 统计
        Object execute = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.bitCount(redisKey.getBytes());
            }
        });

        System.out.println(execute);

    }

    // 统计三组数据的布尔值 并对着三组数据做or运算
    @Test
    public void testBitMapOperation(){
        String redisKey1 = "test:bm:02";
        redisTemplate.opsForValue().setBit(redisKey1, 0 ,true);
        redisTemplate.opsForValue().setBit(redisKey1, 1 ,true);
        redisTemplate.opsForValue().setBit(redisKey1, 2 ,true);

        String redisKey2 = "test:bm:03";
        redisTemplate.opsForValue().setBit(redisKey2, 2 ,true);
        redisTemplate.opsForValue().setBit(redisKey2, 3 ,true);
        redisTemplate.opsForValue().setBit(redisKey2, 4 ,true);

        String redisKey3 = "test:bm:04";
        redisTemplate.opsForValue().setBit(redisKey3, 4 ,true);
        redisTemplate.opsForValue().setBit(redisKey3, 5 ,true);
        redisTemplate.opsForValue().setBit(redisKey3, 6 ,true);

        String redisKey = "test:bm:or";
        Object execute = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.bitOp(RedisStringCommands.BitOperation.OR, redisKey.getBytes(),
                        redisKey1.getBytes(),
                        redisKey2.getBytes(), redisKey3.getBytes());
                return redisConnection.bitCount(redisKey.getBytes());
            }
        });

        System.out.println(execute);

    }
}
