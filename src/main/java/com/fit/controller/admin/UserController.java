package com.fit.controller.admin;

import com.fit.base.BaseController;
import com.fit.base.R;
import com.fit.entity.SysUser;
import com.fit.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @className: UserController
 * @description: 用户管理
 * @author: Aim
 * @date: 2023/8/4
 **/
@Controller
@RequestMapping("/admin/user")
public class UserController extends BaseController {

    @Autowired
    private SysUserService service;

    @GetMapping("/list")
    public String list() {
        return "/admin/user/user-list";
    }

    @PostMapping("/page")
    @ResponseBody
    public R page(HttpServletRequest request) {
        Map<String, Object> paramsMap = getRequestParamsMap(request);
        paramsMap.put("disabled", false);
        List<SysUser> list = service.findList(paramsMap);
        int count = service.findCount(paramsMap);

        return R.success(list, count);
    }
}
