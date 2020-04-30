package com.wkk.community.util;

import com.wkk.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @Time: 2020/4/30下午4:53
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */

/**
 * 持有用户信息， 代替session对象
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }

}
