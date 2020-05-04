package com.wkk.community.service;

import com.wkk.community.util.CommunityUtil;
import com.wkk.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    public void like(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        //首先判断是否已经点赞（是否在集合中）
        Boolean liked = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        if (!liked) {
            redisTemplate.opsForSet().add(entityLikeKey, userId);
        } else {
            redisTemplate.opsForSet().remove(entityLikeKey, userId);
        }

    }

    // 查询实体点赞的数量
    public long findEntityCount(int entityId, int entityType) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);

    }

    // 查询某人对某实体的点赞状态
    public int findEntityLikeStatus(int userId, int entityId, int entityType) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;

    }


}
