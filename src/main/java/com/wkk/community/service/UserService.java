package com.wkk.community.service;

import com.wkk.community.dao.UserMapper;
import com.wkk.community.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Time: 2020/4/29上午11:51
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Service
public class UserService {
    @Autowired(required = false)
    private UserMapper userMapper;
    public User findUserById(int id){
        return userMapper.selectById(id);
    }
}
