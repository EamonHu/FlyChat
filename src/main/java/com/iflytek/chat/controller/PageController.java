package com.iflytek.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: ymhu5
 * @Description: 网页跳转
 * @Date: 2021/8/6 0:45
 */
@Controller
public class PageController {
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/main")

    public String main(){
        return "main";
    }

    @RequestMapping("/loginerror")
    public String longinError(){
        return "loginerror";
    }
}
