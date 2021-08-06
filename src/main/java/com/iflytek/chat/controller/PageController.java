package com.iflytek.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: ymhu5
 * @Description:
 * @Date: 2021/8/6 0:45
 */
@Controller
public class PageController {
    @RequestMapping("/login")
    public String login(){
        System.out.println("========访问login页面==========");

        return "login";
    }

    @RequestMapping("/main")

    public String main(){
        System.out.println("========访问main页面==========");
        return "main";
    }

    @RequestMapping("/loginerror")
    public String longinError(){
        return "loginerror";
    }
}
