package com.fit.config;

import com.fit.config.security.core.*;
import com.fit.config.security.service.DefaultFilterSecurityInterceptor;
import com.fit.config.security.service.UserDetailServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 * @AUTO 权限设置
 * @Author AIM
 * @DATE 2018/10/29
 */
@Configuration
@EnableWebSecurity
@ComponentScan("com.fit.config.security")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DefaultFilterSecurityInterceptor defaultFilterSecurityInterceptor;
    @Autowired
    private SecurityLoginSuccessHandler securityLoginSuccessHandler;
    @Autowired
    private SecurityLoginFailureHandler securityLoginFailureHandler;
    @Autowired
    private SecurityLogoutSuccessHandler securityLogoutSuccessHandler;
    @Autowired
    private UserDetailServices userDetailServices;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 解决不允许显示在iframe的问题
        http.headers().frameOptions().disable();
        // 关闭csrf
        http.csrf().disable();
        // 重写了UsernamePasswordAuthenticationFilter
        http.addFilterBefore(openIdAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // 自定义过滤器
        http.addFilterBefore(defaultFilterSecurityInterceptor, FilterSecurityInterceptor.class);
        // 设置权限
        http.exceptionHandling().authenticationEntryPoint(new DefaultLoginUrlEntryPoint("/login"))
                .and().authorizeRequests()
                .antMatchers("/assets/**", "/static/**", "/html/**", "/swagger-resources/**", "swagger-ui.html",
                        "/webjars/**", "/favicon.ico", "/", "/index", "/login", "/admin/login", "/login_check").permitAll()
                .anyRequest().authenticated() //任何请求,登录后可以访问
                .and().formLogin()
                .successHandler(securityLoginSuccessHandler) // 登录成功
                .failureHandler(securityLoginFailureHandler) // 登录失败
                .loginProcessingUrl("/login_check")//form表单POST请求url提交地址，默认为/login
                .failureUrl("/login?error").permitAll() //登录页面用户任意访问
                .and().logout()
                .logoutSuccessHandler(securityLogoutSuccessHandler).permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailServices).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                try {
                    return DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes("UTF-8")); // MD5加密密码
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encode(rawPassword).equals(encodedPassword);
            }
        });
        //不删除凭据，以便记住用户
        auth.eraseCredentials(false);
    }

    /**
     * 自定义登陆验证接口
     */
    public FilterLoginAuthentication openIdAuthenticationFilter() throws Exception {
        FilterLoginAuthentication fla = new FilterLoginAuthentication();
        fla.setAuthenticationManager(authenticationManager()); //只有post请求才拦截
        fla.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/**/login_check", "POST"));
        fla.setAuthenticationSuccessHandler(securityLoginSuccessHandler);
        fla.setAuthenticationFailureHandler(securityLoginFailureHandler);
        return fla;
    }
}
