package com.boco.nscs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String home(){
        return "redirect:/services";
    }
}
