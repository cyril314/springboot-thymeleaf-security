package com.aim.config.security.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @AUTO 重写的登录认证过滤器
 * @Author AIM
 * @DATE 2018/7/18
 */
@Slf4j
public class FilterLoginAuthentication extends UsernamePasswordAuthenticationFilter {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (log.isDebugEnabled()) {
            log.debug("=============================> 登陆访问第一步");
        }
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        if (isEmpty(username) || isEmpty(password)) {
            throw new BadCredentialsException("用户名或密码不能为空！");// 在界面输出自定义的信息！！
        }

        //实现验证
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        //允许设置用户详细属性
        setDetails(request, authRequest);
        //运行
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        Object obj = request.getParameter(USERNAME);
        return null == obj ? "" : obj.toString().trim();
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        Object obj = request.getParameter(PASSWORD);
        return null == obj ? "" : obj.toString().trim();
    }

    @Override
    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        super.setDetails(request, authRequest);
    }

    private boolean isEmpty(String s) {
        if (null == s || "".equals(s) || "".equals(s.trim()) || "null".equalsIgnoreCase(s)) {
            return true;
        } else {
            return false;
        }
    }
}
