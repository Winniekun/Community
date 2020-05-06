package com.wkk.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @Time: 2020/5/3上午10:42
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
//@Component
@Aspect
public class AlphaAspect {
    // 切点
    // 第一个*表示方法的返回值
    // service包下所有类的所有方法所有属性
    @Pointcut("execution( * com.wkk.community.service.*.*(..))")
    public void pointCut(){

    }
    // 通知
    @Before("pointCut()")
    public void before(){
        System.out.println("before");
    }

    @After("pointCut()")
    public void after(){
        System.out.println("after");
    }

    @AfterReturning("pointCut()")
    public void afterRetruning(){
        System.out.println("after returning");
    }

    @AfterThrowing("pointCut()")
    public void afterThrowing(){
        System.out.println("after throwing");
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("执行之前");
        Object obj = joinPoint.proceed();
        System.out.println("执行之后");
        return obj;
    }
}

