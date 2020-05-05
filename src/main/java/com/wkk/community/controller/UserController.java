package com.wkk.community.controller;

import com.wkk.community.annotation.LoginRequired;
import com.wkk.community.entity.User;
import com.wkk.community.service.FollowService;
import com.wkk.community.service.LikeService;
import com.wkk.community.service.UserService;
import com.wkk.community.util.CommunityConstant;
import com.wkk.community.util.CommunityUtil;
import com.wkk.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Time: 2020/4/30下午5:30
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private FollowService followService;
    @Autowired
    private HostHolder hostHolder;

    //域名
    @Value("${community.path.domain}")
    private String domain;
    // 项目名
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${community.path.upload}")
    private String uploadPath;

    // 个人主页
    @RequestMapping(value = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("该用户不存在");
        }
        // 用户
        model.addAttribute("user", user);
        // 点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);

        // 粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否已关注
        boolean hasFollowed = false;
        if(hostHolder.getUser() != null){
            hasFollowed = followService.hasFollowered(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "site/profile";
    }


    @LoginRequired
    //返回设置界面
    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "site/setting";
    }

    @LoginRequired
    // 上传文件请求处理
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片");
            return "/site/setting";
        }
        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件格式不正确");
            return "site/setting";
        }
        // 生成随机文件名
        filename = CommunityUtil.generateUUID() + suffix;
        //确定文件的存放路径
        File dest = new File(uploadPath + "/" + filename);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("文件上传失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败, 服务器发生异常!", e);
        }
        // 上传成功，更新头像路径(web资源访问路径)
        // http://localhost:8080:/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + filename;
        userService.updateHeader(user.getId(), headerUrl);
        return "redirect:/index";
    }

    // 登录后修改密码
    @LoginRequired
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public String updatePassword(String oldPassword, String newPasswordI, String newPasswordII, Model model) {
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPasswordI) || StringUtils.isBlank(newPasswordII)) {
            model.addAttribute("passwordMSG", "您还没有填写密码");
            return "/site/setting";
        }
        User user = hostHolder.getUser();
        oldPassword = CommunityUtil.md5(user.getSalt() + oldPassword);
        if (!oldPassword.equals(user.getPassword())) {
            model.addAttribute("oldPasswordMSG", "旧密码输入错误");
            return "/site/setting";
        }
        if (!newPasswordI.equals(newPasswordII)) {
            model.addAttribute("newPasswordMSG", "两次密码不一样");
            return "/site/setting";
        }
        newPasswordI = CommunityUtil.md5(user.getSalt() + newPasswordI);
        userService.updatePassword(user.getId(), newPasswordI);
        return "redirect:/login";

    }

    // 获取用户头像
    // http://localhost:8080:/community/user/header/xxx.png
    @RequestMapping(value = "/header/{filename}", method = RequestMethod.GET)
    public void getUserHeader(@PathVariable("filename") String filename, HttpServletResponse response) {
        // 服务器存放的路径
        filename = uploadPath + "/" + filename;
        // 图片后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(filename);
                OutputStream outputStream = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取图像失败: " + e.getMessage());
        }

    }


}
