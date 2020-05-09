package com.wkk.community.actuator;

import com.wkk.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Time: 2020/5/9上午10:34
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Component
@Endpoint(id = "database")
public class DatabaseEndpoint {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseEndpoint.class);

    @Autowired
    private DataSource dataSource;

    @ReadOperation
    public String checkConnection() {
        try (
                Connection connection = dataSource.getConnection();
        ) {
            return CommunityUtil.getJsonString(0, "获取链接成功");
        } catch (SQLException throwables) {
            logger.error("获取链接失败: " + throwables.getMessage());
            return CommunityUtil.getJsonString(1, "获取链接失败");
        }
    }
}
