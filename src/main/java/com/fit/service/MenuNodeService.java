package com.fit.service;

import com.fit.entity.MenuNode;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @AUTO 菜单节点服务
 * @Author AIM
 * @DATE 2019/4/25
 */
@Service
public class MenuNodeService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取角色ID
     *
     * @param username 用户名
     */
    public List<Long> getRoles(String username) {
        String sql = "SELECT r.`role_id` FROM `sys_user_role` r LEFT JOIN `sys_user` u ON r.`user_id`=u.`id` WHERE u.`username`=?";
        return jdbcTemplate.queryForList(sql, Long.class, username);
    }

    /**
     * 获取栏目列表
     */
    public List<MenuNode> getUserMenuNodes(List<Long> roleIds) {
        if (roleIds == null || roleIds.size() == 0) {
            return new ArrayList<MenuNode>();
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT r.`id`,r.`pid` as `parentId`,r.`icon`,r.`name`,REPLACE(r.`url`,'#','javascript:;') AS `url`,");
            sb.append("r.`type`,r.`sort` as `sortOrder`,r.`levels`,r.`ismenu` FROM `sys_resources` r");
            sb.append(" INNER JOIN `sys_authorities_res` ar ON r.`id` = ar.`res_id`");
            sb.append(" INNER JOIN `sys_role_auth` ra ON ar.`auth_id` = ra.`auth_id`");
            sb.append(" WHERE ra.`role_id` IN ( ");
            for (int i = 0; i < roleIds.size(); i++) {
                sb.append("?,");
            }
            if (sb.toString().endsWith(",")) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append(" ) AND r.ismenu = 'Y' AND r.`enabled`=1 AND r.`pid` != 0 ORDER BY r.`levels`,r.`sort` ASC");
            List<MenuNode> menus = jdbcTemplate.query(sb.toString(), BeanPropertyRowMapper.newInstance(MenuNode.class), roleIds.toArray(new Object[roleIds.size()]));
            return MenuNode.buildTitle(menus, 1);
        }
    }

    /**
     * 根据配置文件设置过滤接口文档信息
     *
     * @param nodes    查询到的数据
     * @param menuName 指定的栏目名称
     */
    private static List<MenuNode> filterMenuByName(List<MenuNode> nodes, String menuName) {
        List<MenuNode> menuNodesCopy = new ArrayList<>(nodes.size());
        for (MenuNode menuNode : nodes) {
            if (!menuName.equals(menuNode.getName())) {
                menuNodesCopy.add(menuNode);
            }
            List<MenuNode> childrenList = menuNode.getChildren();
            if (childrenList != null && childrenList.size() > 0) {
                menuNode.setChildren(filterMenuByName(childrenList, menuName));
            }
        }
        return menuNodesCopy;
    }
}
