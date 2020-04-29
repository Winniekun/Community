package com.wkk.community.controller;

import com.wkk.community.pojo.User;
import com.wkk.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @Time: 2020/4/29下午9:09
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Controller
public class FuckLoginController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String postRegister(Model model, User user){
        Map<String, Object> map = userService.register(user);
        // 注册成功
        if(map == null || map.isEmpty()){
            model.addAttribute("msg", "注册成功，我们已经向您的邮箱发送了一封激活邮件，请尽快激活");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        }else {
            model.addAttribute("usernameMSG", map.get("usernameMSG"));
            model.addAttribute("passwordMSG", map.get("passwordMSG"));
            model.addAttribute("mailMSG", map.get("mailMSG"));
            return "/site/register";
        }


    }

}
