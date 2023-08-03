package com.aim.config.security.core;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @AUTO 后台用户凭证AuthenticationToken
 * @Author AIM
 * @DATE 2018/6/15
 */
public class SecurityAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = 1L;
    private String captcha; // 验证码

    public SecurityAuthenticationToken(final Object principal, final Object credentials, final Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public SecurityAuthenticationToken(final Object principal, final Object credentials, final Collection<? extends GrantedAuthority> authorities, final String captchaParm) {
        super(principal, credentials, authorities);
        this.captcha = captchaParm;
    }

    public String getCaptcha() {
        return captcha;
    }

}
