package com.aim.config.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 系统启动加载系统权限  用户登入验证权限
 *
 * @AUTO 最核心的地方，就是提供某个资源对应的权限定义，即getAttributes方法返回的结果。
 * 此类在初始化时，应该取到所有资源及其对应角色的定义。 资源源数据定义，将所有的资源和权限对应关系建立起来，即定义某一资源可以被哪些角色去访问
 * @Author AIM
 * @DATE 2018/7/18
 */
@Slf4j
@Component
public class DefaultSecurityMetadataSource extends JdbcDaoSupport implements FilterInvocationSecurityMetadataSource {

    private static Map<String, Collection<ConfigAttribute>> requestMap = null;
    private String sql = "SELECT b.url FROM  `sys_authorities_res` a, `sys_resources` b, `sys_authorities` c WHERE a.res_id = b.id AND a.auth_id = c.id AND c.name = ?";

    //tomcat启动时实例化一次
    public DefaultSecurityMetadataSource(DataSource dataSource) {
        this.setDataSource(dataSource);
        // 应当是资源为key， 权限为value。 资源通常为url， 权限就是那些以ROLE_为前缀的角色。 一个资源可以由多个权限来访问。
        requestMap = new HashMap<String, Collection<ConfigAttribute>>();
        // 在Web服务器启动时，提取系统中的所有权限。
        List<String> query = getJdbcTemplate().query("select sas.name from sys_authorities sas", new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        });
        if (query != null) {
            for (String auth : query) {
                ConfigAttribute ca = new SecurityConfig(auth);
                List<String> list = getJdbcTemplate().queryForList(sql, String.class, auth);
                for (String res : list) {
                    String url = res;
                    // 判断资源文件和权限的对应关系，如果已经存在相关的资源url，则要通过该url为key提取出权限集合，将权限增加到权限集合中。
                    if (requestMap.containsKey(url)) {
                        Collection<ConfigAttribute> value = requestMap.get(url);
                        value.add(ca);
                        requestMap.put(url, value);
                    } else {
                        Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
                        atts.add(ca);
                        requestMap.put(url, atts);
                    }
                }
            }
            log.info("==================> 第一步, 初始化，加载所有url和权限（或角色）的对应关系");
        }
    }

    //参数是要访问的url，返回这个url对于的所有权限（或角色）
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // object 是一个URL，被用户请求的url。
        FilterInvocation filterInvocation = (FilterInvocation) object;
        String url = filterInvocation.getRequestUrl();
        int firstQuestionMarkIndex = url.indexOf("?");
        if (firstQuestionMarkIndex != -1) {
            url = url.substring(0, firstQuestionMarkIndex);
        }
        Iterator<String> ite = requestMap.keySet().iterator();
        while (ite.hasNext()) {
            String requestURL = ite.next();
            // 完全路径url方式路径匹配
            RequestMatcher requestMatcher = new AntPathRequestMatcher(requestURL);
            if (requestMatcher.matches(filterInvocation.getHttpRequest())) {
                Collection<ConfigAttribute> retCollection = requestMap.get(requestURL);
                log.info("====================> 根据URL，找到相关的权限配置");
                return retCollection;
            }
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        log.info("================> 第二步, 判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同,或是否是其超类或超接口");
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> attributes = new HashSet<ConfigAttribute>();
        for (Map.Entry<String, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            attributes.addAll(entry.getValue());
        }
        log.info("=> 第三步, 获取可以访问的权限信息, 总共有这些权限：{}", attributes.toString());
        return attributes;
    }

    /**
     * 刷新缓存
     */
    public void refreshAllConfigAttributes() {
        getAllConfigAttributes();
    }
}