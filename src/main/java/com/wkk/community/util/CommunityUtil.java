package com.wkk.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @Time: 2020/4/29下午9:29
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class CommunityUtil {
    // 生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // md5加密
    public static String md5(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());

    }
}
