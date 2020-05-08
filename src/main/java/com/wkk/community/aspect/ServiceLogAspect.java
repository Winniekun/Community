package com.wkk.community.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Time: 2020/5/3上午10:59
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
//@Component
//@Aspect
public class ServiceLogAspect {
    private static final Logger loggerr = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution( * com.wkk.community.service.*.*(..))")
    public void pointCut(){

    }

    @Before("pointCut()")
    public void before(JoinPoint joinPoint){
        // 用户[1,2,3] 在 [time] 访问了 [com.wkk.community.service.xxx()]
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //加入kakfa之后需判空
        if(requestAttributes == null){
            return;
        }
        HttpServletRequest request = requestAttributes.getRequest();

        // 获取ip
        String ip = request.getRemoteHost();
        // 时间
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // 获取某个类 某个方法
        String target = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        loggerr.info(String.format("用户[%s] 在 [%s] 访问了 [%s]", ip, now, target));

    }

}
