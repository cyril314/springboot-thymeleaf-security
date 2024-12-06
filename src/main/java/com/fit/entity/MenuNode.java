package com.fit.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @AUTO 菜单节点
 * @Author AIM
 * @DATE 2019/4/25
 */
@Data
public class MenuNode implements Comparable<Object>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 节点id
     */
    private Long id;

    /**
     * 父节点
     */
    private Long parentId;

    /**
     * 节点名称
     */
    private String name;

    private String title;

    /**
     * 按钮级别
     */
    private Integer level = 0;

    /**
     * 按钮级别(Y  N)
     */
    private String ismenu;

    /**
     * 按钮的排序
     */
    private Integer sortOrder = 0;

    /**
     * 节点的url
     */
    private String url;

    /**
     * 节点图标
     */
    private String icon;

    /**
     * 子节点的集合
     */
    private List<MenuNode> children;

    /**
     * 查询子节点时候的临时集合
     */
    private List<MenuNode> linkedList = new ArrayList<>();

    public MenuNode() {
        super();
    }

    public MenuNode(Long id, Long parentId) {
        super();
        this.id = id;
        this.parentId = parentId;
    }

    private boolean isParent = false;

    public boolean getIsParent() {
        return isParent;
    }

    /**
     * 重写排序比较接口，首先根据等级排序，然后更具排序字段排序
     */
    @Override
    public int compareTo(Object o) {
        MenuNode menuNode = (MenuNode) o;
        Integer num = menuNode.getSortOrder();
        Integer levels = menuNode.getLevel();
        if (num == null) {
            num = 0;
        }
        if (levels == null) {
            levels = 0;
        }
        if (this.level.compareTo(levels) == 0) {
            return this.sortOrder.compareTo(num);
        } else {
            return this.level.compareTo(levels);
        }
    }

    public static void goToParent(List<MenuNode> nodes) {
        for (MenuNode node : nodes) {
            if (node.getChildren() != null) {
                node.setParent(true);
                goToParent(node.getChildren());
            }
        }
    }

    /**
     * 构建页面菜单列表
     */
    public static List<MenuNode> buildTitle(List<MenuNode> nodes, long pid) {
        if (nodes.size() <= 0) {
            return nodes;
        }
        //对菜单排序，返回列表按菜单等级，序号的排序方式排列
        Collections.sort(nodes);
        return buildTree(nodes, pid);
    }

    /**
     * 收集传递的集合中父id相同的TreeNode
     *
     * @param menus 集合列表
     * @param id    父ID
     */
    public static List<MenuNode> buildTree(List<MenuNode> menus, long id) {
        List<MenuNode> children = new ArrayList<MenuNode>();
        for (MenuNode treeNode : menus) {
            //判断该节点的父id，是否与传入的父id相同，相同则递归设置其孩子节点，并将该节点放入children集合中
            if (treeNode.getParentId() == id) {
                //递归设置其孩子节点
                treeNode.setChildren(buildTree(menus, treeNode.getId()));
                //放入children集合
                children.add(treeNode);
            }
        }
        return children;
    }
}