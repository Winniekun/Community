package com.wkk.community.dao;

import com.wkk.community.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Time: 2020/4/29上午7:54
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Mapper
public interface UserMapper {
    User selectById(int id);
    User selectByName(String name);
    User selectByEmail(String email);
    int insertUser(User user);
    int updateStatus(int id, int status);
    int updateHeader(int id, String headerUrl);
    int updatePassword(int id, String password);
}
