package com.wkk.community.service;

import com.wkk.community.dao.DiscussPostMapper;
import com.wkk.community.util.CommunityUtil;
import com.wkk.community.util.HostHolder;
import com.wkk.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @Time: 2020/5/4上午9:23
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;


    // 更新，支持事物管理
    public void like(int userId, int entityType, int entityId, int entityUserId) {
//        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
//        //首先判断是否已经点赞（是否在集合中）
//        Boolean liked = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
//        if (!liked) {
//            redisTemplate.opsForSet().add(entityLikeKey, userId);
//        } else {
//            redisTemplate.opsForSet().remove(enti                System.out.println(redisOperations.opsForSet().members(key));
//tyLikeKey, userId);
//        }
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                // 判断是否点赞
                Boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);

                // 操作
                operations.multi();

                // isMember为真 表示取消点赞 所以 删除
                if (isMember) {
                    operations.opsForSet().remove(entityLikeKey, userId);
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    operations.opsForSet().add(entityLikeKey, userId);
                    operations.opsForValue().increment(userLikeKey);
                }


                return operations.exec();

            }
        });

    }

    // 查询帖子点赞的数量
    public long findEntityCount(int entityId, int entityType) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);

    }

    // 查询某人对某帖子的点赞状态
    public int findEntityLikeStatus(int userId, int entityId, int entityType) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;

    }

    // 查询某人获取的点赞数量
    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count.intValue();

    }


}
