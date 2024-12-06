package com.fit.config.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @AUTO 从数据库中读入用户的密码，角色信息，是否锁定，账号是否过期
 * @Author AIM
 * @DATE 2018/7/17
 */
@Component("userDetailServices")
public class UserDetailServices implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(UserDetailServices.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String user_sql = "SELECT u.`username`,u.`password` FROM `sys_user` u WHERE u.`username` = ?";

    private final String sql = "SELECT a.name FROM `sys_authorities` a, `sys_role_auth` b, `sys_user_role` c, `sys_user` d "
            + "WHERE a.id = b.auth_id AND b.role_id = c.role_id AND c.user_id = d.id AND d.`username` = ?";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        boolean isDebug = logger.isDebugEnabled();
        if (isDebug) {
            logger.debug("===================================> 登陆访问第二步");
        }
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("用户名不能为空！");
        }
        Map<String, ?> userMap = jdbcTemplate.queryForMap(user_sql, username);
        if (userMap == null) {
            throw new UsernameNotFoundException("不存在用户名！");
        }
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql, username);
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            if (map != null && map.get("name") != null) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(map.get("name").toString());
                // 1：此处将权限信息添加到 GrantedAuthority; 对象中，在后面进行全权限验证时会使用GrantedAuthority 对象。
                grantedAuthorities.add(grantedAuthority);
            }
        }
        if (isDebug) {
            logger.debug("得到其权限：" + grantedAuthorities);
        }

        return new User(userMap.get("username").toString(), userMap.get("password").toString(), grantedAuthorities);
    }
}
