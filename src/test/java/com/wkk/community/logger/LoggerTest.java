package com.wkk.community.logger;

import com.wkk.community.CommunityApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Time: 2020/4/29下午5:56
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
public class LoggerTest {
    private Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void loggerTest(){
        System.out.println(logger.getName());
        logger.trace("TRACE ====>");
        logger.debug("DEBUG ====>");
        logger.info("INFO ======>");
        logger.warn("WARN ======>");
        logger.error("ERROR ====>");
    }
}
