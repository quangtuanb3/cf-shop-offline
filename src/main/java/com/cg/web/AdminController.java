package com.cg.web;

import com.cg.model.Role;
import com.cg.model.user.User;
import com.cg.user.IUserService;
import com.cg.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboards")
public class AdminController {

    private final AppUtils appUtils;
    private final IUserService userService;

    @GetMapping()
    public ModelAndView showDashboardPage() {
        String userName = appUtils.getPrincipalUsername();
        ModelAndView modelAndView = new ModelAndView();
        userName = userName.substring(0, userName.indexOf("@"));
        modelAndView.addObject("userName", userName);
        modelAndView.setViewName("dashboard/list-product");
        return modelAndView;
    }

    @GetMapping("/revenue")
    public String showRevenue(Model model) {
        setModel(model);
        return "/dashboard/revenue";
    }

    private void setModel(Model model) {
        String username = appUtils.getPrincipalUsername();
        User user = userService.findByUsername(username);
//        Role role = user.getRole();
//        String roleCode = role.getCode();
        username = username.substring(0, username.indexOf("@"));
        model.addAttribute("userName", username);
//        model.addAttribute("roleCode", roleCode);
    }

    @GetMapping("/products")
    public String showListProduct(Model model) {
        setModel(model);
        return "dashboard/list-product";
    }

    @GetMapping("/staff")
    public String showListCustomer(Model model) {
        setModel(model);
        return "dashboard/list-user";
    }
}