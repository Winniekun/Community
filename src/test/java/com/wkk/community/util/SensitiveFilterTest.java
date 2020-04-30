package com.wkk.community.util;

import com.wkk.community.CommunityApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Time: 2020/4/30下午11:11
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
class SensitiveFilterTest {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter(){
        String s = "艹，这里可以赌博，太尼玛吊了，哈哈完了";
        String filter = sensitiveFilter.filter(s);
        System.out.println(filter);

        String a = "☺艹☺，这里可以☺赌☺☺☺博☺，太☺☺尼☺☺☺玛☺☺☺吊了，哈哈完了";
        filter = sensitiveFilter.filter(a);
        System.out.println(filter);
    }
}
