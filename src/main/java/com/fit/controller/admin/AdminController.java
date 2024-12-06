package com.fit.controller.admin;

import com.fit.base.BaseController;
import com.fit.base.PatternAndView;
import com.fit.config.security.utils.SecurityHelper;
import com.fit.entity.MenuNode;
import com.fit.service.MenuNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @AUTO 后台页面控制器
 * @Author AIM
 * @DATE 2018/10/22
 */
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    @Autowired
    private MenuNodeService menuService;

    @GetMapping(value = {"", "/", "/index"})
    public ModelAndView admin() {
        PatternAndView view = new PatternAndView("/admin/index");
        if (SecurityHelper.isAuthenticated()) {
            User user = SecurityHelper.getUser();
            List<MenuNode> menus = menuService.getUserMenuNodes(menuService.getRoles(user.getUsername()));
            view.addObject("menus", menus);
        }
        return view;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "admin/welcome";
    }

    /**
     * @return 返回登陆页
     */
    @GetMapping(value = {"/login", "/login.html"})
    public ModelAndView login(HttpServletRequest request) {
        PatternAndView view = new PatternAndView("/admin/login");
        log.info("后台登陆页面");
        Map<String, Object> paramsMap = getRequestParamsMap(request);
        if (paramsMap.get("type") != null) {
            view.addObject("tip", "账户或密码错误!");
        }
        return view;
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
