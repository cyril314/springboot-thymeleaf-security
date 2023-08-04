package com.aim.controller.admin;

import com.aim.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @className: UserController
 * @description:
 * @author: Aim
 * @date: 2023/8/4
 **/
@Controller
@RequestMapping
public class UserController {

    @Autowired
    private SysUserService userService;


}
