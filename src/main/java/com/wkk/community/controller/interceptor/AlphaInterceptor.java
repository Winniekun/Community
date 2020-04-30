package com.wkk.community.controller.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Time: 2020/4/30下午3:59
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Component
public class AlphaInterceptor implements HandlerInterceptor {
    private static  final Logger logger = LoggerFactory.getLogger(AlphaInterceptor.class);

    // controller 处理请求之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("preHandler: " + handler.toString());
        return true;
    }

    // controller 处理请求之后执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.debug("postHadnler: " + handler.toString());
    }

    // 模板引擎执行完之后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.debug("afterCompletion: " + handler.toString());
    }
}
