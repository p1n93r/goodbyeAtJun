package com.test.web.controller;

import com.test.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @author P1n93r
 * 用于显示页面的控制器类
 */
@Controller
public class ShowViewController {

    @RequestMapping("/base")
    public String showBase(){
        return "base";
    }

    @RequestMapping("/index")
    public String showIndex(){
        return "index";
    }

    @RequestMapping("/author")
    public String showAuthor(){
        return "author";
    }

    @RequestMapping("/cancel")
    public String showCancle(){
        return "cancel";
    }

}
