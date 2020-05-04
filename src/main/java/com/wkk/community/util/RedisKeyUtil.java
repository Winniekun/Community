package com.wkk.community.util;

import com.sun.mail.imap.AppendUID;

/**
 * @Time: 2020/5/4上午8:23
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";

    // 某个帖子的赞的key
    // like:entity:entityType:entityId: set(UserId1, UserId2...)
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 某个用户的赞的key
    // like:user:userId --> int()
    public static String getUserLikeKey(int userId){
        return PREFIX_USER_LIKE + SPLIT + userId;
    }
}
