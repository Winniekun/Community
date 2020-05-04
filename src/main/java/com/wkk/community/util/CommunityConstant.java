package com.wkk.community.util;

import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

/**
 * @Time: 2020/4/30上午12:43
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public interface  CommunityConstant {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态的登录凭证的超时时间
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 记住状态的登录凭证超时时间
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    /**
     * 实体类型: 帖子类型
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * 实体类型: 评论类型
     */
    int ENTITY_TYPE_COMMENT = 2;

    /**
     * 实体类型: 用户类型
     */
    int ENTITY_TYPE_USER = 3;
}
