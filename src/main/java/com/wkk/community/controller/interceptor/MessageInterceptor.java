package com.wkk.community.controller.interceptor;

import com.wkk.community.entity.User;
import com.wkk.community.service.MessageService;
import com.wkk.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Time: 2020/5/6下午5:43
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Component
public class MessageInterceptor implements HandlerInterceptor {
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(hostHolder.getUser() != null &&  modelAndView != null) {
            int noticeUnReadCount = messageService.findNoticeUnReadCount(user.getId(), null);
            int letterUnReadCount = messageService.findLetterUnReadCount(user.getId(), null);
            int count = noticeUnReadCount + letterUnReadCount;
            modelAndView.addObject("count", count);
        }
    }
}
