package com.aim.controller.admin;

import com.aim.base.PatternAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @AUTO 后台页面控制器
 * @Author AIM
 * @DATE 2018/10/22
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping(value = {"", "/", "/index"})
    public String admin() {
        return "admin/index";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "admin/welcome";
    }

    /**
     * @return 返回登陆页
     */
    @GetMapping(value = {"/login", "/login.html"})
    public ModelAndView login() {
        logger.info("后台登陆页面");
        return new PatternAndView("/admin/login");
    }

    /**
     * @return 退出, 跳转到登陆页面
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/admin/login";
    }

    @PostMapping("/menus")
    public String menus(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }

}
