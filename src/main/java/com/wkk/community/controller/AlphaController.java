package com.wkk.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
