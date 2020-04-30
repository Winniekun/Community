package com.wkk.community.controller;

import com.google.code.kaptcha.Producer;
import com.wkk.community.entity.User;
import com.wkk.community.service.UserService;
import com.wkk.community.util.CommunityConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Time: 2020/4/29下午9:09
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Controller
public class FuckLoginController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(FuckLoginController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(){
        return "/site/login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/login";

    }

    /**
     * 生产验证码
     * @param response
     * @param session
     */
    @RequestMapping(value = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        // 生成验证码
        String text = kaptchaProducer.createText();
        // 生成验证码图片
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码存入session
        session.setAttribute("kaptcha", text);
        // 将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);

        } catch (IOException e) {
            logger.error("响应验证码失败: " + e.getMessage());
        }


    }

    // 注册处理
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
            model.addAttribute("emailMSG", map.get("emailMSG"));
            return "/site/register";
        }


    }

    // 登录处理
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String postLogin(String username, String password, String code, boolean rememberMe,
                            Model model, HttpSession session, HttpServletResponse response){
        // 验证码 判断
        String kaptcha = (String) session.getAttribute("kaptcha");
        if(StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMSG", "验证码不正确");
            return "site/login";
        }

        // 检测账号密码
        int expiredSeconds= rememberMe?REMEMBER_EXPIRED_SECONDS:DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if(map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }else {
            model.addAttribute("usernameMSG", map.get("usernameMSG"));
            model.addAttribute("passwordMSG", map.get("passwordMSG"));
            return "site/login";
        }


    }


    //激活码链接
    // http://localhost:8080/community/activation/{userId}/activeCode
    @RequestMapping(value = "/activation/{userId}/{activeCode}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("activeCode") String activeCode){
        int activation = userService.activation(userId, activeCode);
        // 成功跳转登录、失败跳转首页、重复
        if(activation == ACTIVATION_SUCCESS){
            model.addAttribute("msg", "激活成功, 您的账号已经可以正常使用了");
            model.addAttribute("target", "/login");
        }else if(activation == ACTIVATION_REPEAT){
            model.addAttribute("msg", "无效操作，该账号已经激活");
            model.addAttribute("target", "/index");
        }else {
            model.addAttribute("msg", "激活失败，您提供的激活码不正确");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";

    }


}
