package com.wkk.community.controller;

import com.wkk.community.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @Time: 2020/5/7下午11:46
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Controller
public class DataController {
    @Autowired
    private DataService dataService;

    @RequestMapping(value = "/data", method = {RequestMethod.GET, RequestMethod.POST})
    public String getData(){
        return "site/admin/data";
    }

    // 统计网站UV
    @RequestMapping(value = "/data/uv", method = RequestMethod.POST)
    public String getUV(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model){
        long calculateUV = dataService.calculateUV(start, end);
        model.addAttribute("uvResult", calculateUV);
        model.addAttribute("uvStartDate", start);
        model.addAttribute("uvEndDate", end);
        return "forward:/data";
//        return "site/admin/data";
    }


    // 统计网站UV
    @RequestMapping(value = "/data/dau", method = RequestMethod.POST)
    public String getDAV(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model){
        long calculateDAU = dataService.calculateDAU(start, end);
        model.addAttribute("dauResult", calculateDAU);
        model.addAttribute("dauStartDate", start);
        model.addAttribute("dauEndDate", end);
        return "forward:/data";
//        return "site/admin/data";
    }

}
