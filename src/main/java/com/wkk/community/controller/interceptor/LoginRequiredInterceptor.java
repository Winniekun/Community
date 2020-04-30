package com.wkk.community.controller.interceptor;

import com.wkk.community.annotation.LoginRequired;
import com.wkk.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.net.ssl.HostnameVerifier;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Time: 2020/4/30下午9:32
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 拦截的是方法
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired annotation = method.getAnnotation(LoginRequired.class);
            // 需要登录才能访问, 但是并未登录
            if(annotation != null && hostHolder.getUser() == null){
                // 未登录 跳转登录界面
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }

        return true;
    }
}
