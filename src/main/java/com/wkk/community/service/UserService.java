package com.wkk.community.service;

import com.wkk.community.dao.UserMapper;
import com.wkk.community.pojo.User;
import com.wkk.community.util.CommunityConstant;
import com.wkk.community.util.CommunityUtil;
import com.wkk.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Time: 2020/4/29上午11:51
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Service
public class UserService implements CommunityConstant {
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;

    //域名
    @Value("${community.path.domain}")
    private String domain;
    // 项目名
    @Value("${server.servlet.context-path}")
    private String contextPath;


    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    public Map<String, Object> register(User user){
        Map<String, Object> map = new HashMap<>();
        // 输入逻辑问题
        if(user == null){
            throw new IllegalArgumentException("参数不能为空");
        }

        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMSG", "账号不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMSG", "密码不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMSG", "邮箱不能为空");
            return map;
        }

        // 判断账号邮箱是否存在
        User u = userMapper.selectByName(user.getUsername());
        if(u != null){
            map.put("usernameMSG", "该账号已存在");
            return map;
        }
        u = userMapper.selectByEmail(user.getEmail());
        if(u!= null){
            map.put("emailMSG", "邮箱已注册");
            return map;
        }

        //------------
        // 注册用户
        //------------
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getSalt() + user.getPassword()));
        // 默认为普通用户
        user.setType(0);
        // 未激活状态
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        user = userMapper.selectByName(user.getUsername());
        // 激活邮件
        Context context = new Context();

        context.setVariable("email", user.getEmail());
        // http://localhost:8080/community/activation/{userId}/activeCode
        String url = domain + contextPath + "/activation/" + user.getId() +"/" + user.getActivationCode();
        context.setVariable("url", url);

        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "Wiki社区激活邮件", content);
        return map;

    }

    public int activation(int userId, String activeCode){
        User user = userMapper.selectById(userId);
        if(user.getStatus() == 1){
            return ACTIVATION_REPEAT;

        }
        else if(user.getActivationCode().equals(activeCode)){
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        }else {
            return ACTIVATION_FAILURE;
        }
    }
}
