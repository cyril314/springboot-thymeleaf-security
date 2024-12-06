package com.fit.config.security.core;

import com.fit.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @AUTO 登录成功后的处理
 * @Author AIM
 * @DATE 2018/6/15
 */
@Slf4j
@Component("securityLoginSuccessHandler")
public class SecurityLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements InitializingBean {

    private String successUrl = "/"; // 登录成功url
    private boolean forwardToDestination = false; // forward方式跳转

    public SecurityLoginSuccessHandler() {
        super();
    }

    public SecurityLoginSuccessHandler(final String defaultTargetUrl) {
        successUrl = defaultTargetUrl;
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
        // 多登录页面处理
        String targetUrl = successUrl; // 每次恢复为默认的
        setDefaultTargetUrl(targetUrl);
        String url = request.getRequestURI();
        if (url.indexOf("admin") != -1) {
            //未登录而访问后台受控资源时，跳转到后台登录页面
            targetUrl = "/admin/";
            setDefaultTargetUrl(targetUrl);
        }
        targetUrl = determineTargetUrl(request, response);

        log.info("=============================> 访问登陆成功处理程序,跳转路径为:" + targetUrl);
        if (this.forwardToDestination) {
            log.debug("登录成功，Forwarding 到页面 " + targetUrl);
            request.getRequestDispatcher(targetUrl).forward(request, response);
        } else {
            log.debug("登录成功，Redirecting 到页面 " + targetUrl);
            // 在redirectStrategy中，'/'代表的是项目根目录而不是服务器根目录。
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    public void setForwardToDestination(final boolean forwardToDestinationParm) {
        this.forwardToDestination = forwardToDestinationParm;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (RequestUtil.isNullOrBlank(getDefaultTargetUrl())) {
            throw new Exception("没有配置defaultTargetUrl");
        }
    }
}
