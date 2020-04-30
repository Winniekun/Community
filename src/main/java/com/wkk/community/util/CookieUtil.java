package com.wkk.community.util;

import org.springframework.http.HttpRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Time: 2020/4/30下午4:31
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class CookieUtil {
    /**
     * 获取cookies中指定的value
     * @param request 请求
     * @param name key
     * @return
     */
    public static String getValue(HttpServletRequest request, String name){
        if(request==null || name == null){
            throw new IllegalArgumentException("参数为空");
        }
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
