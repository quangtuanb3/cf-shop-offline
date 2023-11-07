package com.cg.web;

import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("")
public class HomeController {

    @Autowired
    private AppUtils appUtils;

    @GetMapping("/login")
    public String showLoginPage(){
        return "login/login";
    }

    @GetMapping("/shop")
    public ModelAndView showShopPage() {
        String userName = appUtils.getPrincipalUsername();
        ModelAndView modelAndView = new ModelAndView();
        userName = userName.substring(0, userName.indexOf("@"));
        modelAndView.addObject("userName",userName);
        modelAndView.setViewName("shop/index");
        return modelAndView;
    }
}
