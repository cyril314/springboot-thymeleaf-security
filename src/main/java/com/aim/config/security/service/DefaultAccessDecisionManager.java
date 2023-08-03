package com.aim.config.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @AUTO 负责权限的控制，如果请求的url在权限集合中有这个url对应的值，则放行
 * @Author AIM
 * @DATE 2018/7/18
 */
@Component
public class DefaultAccessDecisionManager implements AccessDecisionManager {

    private final Logger logger = LoggerFactory.getLogger(DefaultAccessDecisionManager.class);

    /**
     * 检查用户是否够权限访问资源
     *
     * @param auth             从spring的全局缓存SecurityContextHolder中拿到的，里面是用户的权限信息
     * @param obj              请求url
     * @param configAttributes 所需的权限
     */
    @Override
    public void decide(Authentication auth, Object obj, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
        if (logger.isDebugEnabled()) {
            logger.info("判断用户是否有访问权限");
        }
        if (configAttributes == null) {
            return;
        }
        for (ConfigAttribute ca : configAttributes) {
            String needRole = ca.getAttribute();
            for (GrantedAuthority ga : auth.getAuthorities()) {
                if (needRole.equals(ga.getAuthority())) {
                    return;
                }
            }
        }
        //注意：执行这里，后台是会抛异常的，但是界面会跳转到所配的access-denied-page页面
        throw new AccessDeniedException("没有权限访问!");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
