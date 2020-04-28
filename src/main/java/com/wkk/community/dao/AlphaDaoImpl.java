package com.wkk.community.dao;

import org.springframework.stereotype.Component;

/**
 * @Time: 2020/4/28下午7:59
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Component
public class AlphaDaoImpl implements AlphaDao{
    @Override
    public String select() {
        return "wkk";
    }
}
