package com.fit.config.security.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @AUTO 退出成功操作
 * @Author AIM
 * @DATE 2018/6/15
 */
@Slf4j
@Component("securityLogoutSuccessHandler")
public class SecurityLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    private String successUrl = "/login"; // 退出成功url

    public SecurityLogoutSuccessHandler() {
        super();
    }

    public SecurityLogoutSuccessHandler(final String defaultTargetUrl) {
        successUrl = defaultTargetUrl;
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
        String targetUrl = successUrl;
        setDefaultTargetUrl(targetUrl);
        String url = request.getRequestURI();
        if (url.indexOf("admin") != -1) {
            //未登录而访问后台受控资源时，跳转到后台登录页面
            targetUrl = "/admin/";
            setDefaultTargetUrl(targetUrl);
        }
        log.info("=============================> 访问退出登陆处理程序,跳转路径为:" + targetUrl);
        super.onLogoutSuccess(request, response, authentication);
    }

}
