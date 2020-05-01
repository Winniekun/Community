package com.wkk.community.controller;

import com.wkk.community.util.CommunityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Time: 2020/4/28下午7:39
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@RestController
@RequestMapping("/alpha")
public class AlphaController {
    @RequestMapping("/")
    public String alpha(){
        return "hello world";
    }

    // cookie
    @RequestMapping(value = "/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        // 生成cookie
        Cookie cookie = new Cookie("wkk", CommunityUtil.generateUUID());
        // 设置生效范围
        cookie.setPath("/community/alpha");
        // 设置cookie的生效时间（默认存在内存中， 设置时间之后会存在硬盘中）
        cookie.setMaxAge(60*10);
        // 发送给客户端
        response.addCookie(cookie);
        return "set cookie";

    }

    @RequestMapping(value = "/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("wkk") String wkk){
        System.out.println(wkk);
        return "get cookie";
    }

    @RequestMapping(value = "/session/set", method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session){
        session.setAttribute("id", 1);
        session.setAttribute("name", "wkk");
        return "set session";

    }
    @RequestMapping(value = "/session/get", method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session){
        System.out.println(session.getAttribute("name"));
        System.out.println(session.getAttribute("id"));
        return "get session";

    }

    // ajax示例
    @RequestMapping(value = "/ajax", method = RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name, int age){
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJsonString(0, "操作成功");

    }


}
