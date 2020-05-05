package com.wkk.community.service;

import com.wkk.community.entity.User;
import com.wkk.community.util.CommunityConstant;
import com.wkk.community.util.CommunityUtil;
import com.wkk.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Time: 2020/5/4下午2:30
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Service
public class FollowService implements CommunityConstant {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;

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

    // 查询某个用户关注的人
    public List<Map<String, Object>> findFollowees(int userId, int offset, int limit){
        String followee = RedisKeyUtil.getFollowee(userId, ENTITY_TYPE_USER);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followee, offset, offset + limit - 1);
        if(targetIds == null){
            return null;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (Integer targetId : targetIds) {
            Map<String, Object> map = new HashMap<>();
            User user = userService.findUserById(targetId);
            map.put("user", user);
            Double score = redisTemplate.opsForZSet().score(followee, targetId);
            map.put("followTime", new Date(score.longValue()));
            list.add(map);
        }
        return list;

    }

    // 查询用户的粉丝
    public List<Map<String, Object>> findFollowers(int entityId, int offset, int limit){
        String follower = RedisKeyUtil.getFollower(ENTITY_TYPE_USER, entityId);
        Set<Integer> followerIds = redisTemplate.opsForZSet().range(follower, offset, offset + limit - 1);
        if(followerIds == null){
            return null;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (Integer followerId : followerIds) {
            Map<String, Object> map = new HashMap<>();
            User user = userService.findUserById(followerId);
            map.put("user", user);
            Double score = redisTemplate.opsForZSet().score(follower, followerId);
            map.put("followTime", new Date(score.longValue()));
            list.add(map);
        }
        return list;


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

    // 查询当前用户是否已关注该实体
    public boolean hasFollowered(int userId, int entityType, int entityId){
        String followee = RedisKeyUtil.getFollowee(userId, entityType);
        return redisTemplate.opsForZSet().score(followee, entityId) != null;
    }




}
