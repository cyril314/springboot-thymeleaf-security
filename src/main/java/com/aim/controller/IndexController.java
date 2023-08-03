package com.aim.controller;

import com.aim.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @className: IndexController
 * @description: 首页
 * @author: Aim
 * @date: 2023/8/2
 **/
@Controller
public class IndexController extends BaseController {

    @GetMapping({"", "/", "/index"})
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("username", "root");
        return mv;
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        Map<String, Object> paramsMap = getRequestParamsMap(request);
        if (paramsMap.get("type") != null) {
            model.addAttribute("tip", "账户或密码错误!");
        }
        return "/login";
    }
}
