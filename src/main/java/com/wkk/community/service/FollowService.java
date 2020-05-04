package com.wkk.community.service;

import com.wkk.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Time: 2020/5/4下午2:30
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Service
public class FollowService {
    @Autowired
    private RedisTemplate redisTemplate;

    // 关注
    public void follow(int userId, int entityType, int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followee = RedisKeyUtil.getFollowee(userId, entityType);
                String follower = RedisKeyUtil.getFollower(entityType, entityId);
                operations.multi();
                redisTemplate.opsForZSet().add(followee, entityId, System.currentTimeMillis());
                redisTemplate.opsForZSet().add(follower, userId, System.currentTimeMillis());
                return operations.exec();
            }
        });
    }

    // 取消关注
    public void unFollow(int userId, int entityType, int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String follower = RedisKeyUtil.getFollower(entityType, entityId);
                String followee = RedisKeyUtil.getFollowee(userId, entityType);

                operations.multi();
                redisTemplate.opsForZSet().remove(followee, entityId);
                redisTemplate.opsForZSet().remove(follower, userId);
                return operations.exec();
            }
        });

    }

    // 查询某个用户关注的实体数量
    public long findFolloweeCount(int userId, int entityType){
        String followee = RedisKeyUtil.getFollowee(userId, entityType);
        return redisTemplate.opsForZSet().zCard(followee);
    }


    // 查询某个实体的粉丝数量
    public long findFollowerCount(int entityType, int entityId){
        String follower = RedisKeyUtil.getFollower(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(follower);
    }

    // 查询当前用户是够已关注该实体
    public boolean hasFollowered(int userId, int entityType, int entityId){
        String followee = RedisKeyUtil.getFollowee(userId, entityType);
        return redisTemplate.opsForZSet().score(followee, entityId) != null;
    }

}
