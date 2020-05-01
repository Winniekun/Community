package com.wkk.community.util;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Time: 2020/5/1上午10:36
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
class CommunityUtilTest {

    @Test
    public void testJson(){
        Map<String, Object> map = new HashMap<>();
        map.put("name", "wkk");
        map.put("age", 24);
        System.out.println(CommunityUtil.getJsonString(0, "ok", map));
    }

}
