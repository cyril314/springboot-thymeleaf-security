package com.aim.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * @AUTO 菜单节点
 * @Author AIM
 * @DATE 2019/4/25
 */
@Data
public class MenuNode implements Comparable, Serializable {

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
    public static List<MenuNode> buildTitle(List<MenuNode> nodes) {
        if (nodes.size() <= 0) {
            return nodes;
        }
        //对菜单排序，返回列表按菜单等级，序号的排序方式排列
        Collections.sort(nodes);
        return mergeList(nodes, nodes.get(nodes.size() - 1).getLevel(), null);
    }

    /**
     * 递归合并数组为子数组，最后返回第一层
     *
     * @param menuList
     * @param rank 等级
     * @param listMap
     * @return
     */
    private static List<MenuNode> mergeList(List<MenuNode> menuList, int rank, Map<Long, List<MenuNode>> listMap) {
        //保存当次调用总共合并了多少元素
        int n;
        //保存当次调用总共合并出来的list
        Map<Long, List<MenuNode>> currentMap = new HashMap<>();
        //由于按等级从小到大排序，需要从后往前排序
        //判断该节点是否属于当前循环的等级,不等于则跳出循环
        for (n = menuList.size() - 1; n >= 0 && menuList.get(n).getLevel() == rank; n--) {
            //判断之前的调用是否有返回以该节点的id为key的map，有则设置为children列表。
            if (listMap != null && listMap.get(menuList.get(n).getId()) != null) {
                menuList.get(n).setChildren(listMap.get(menuList.get(n).getId()));
            }
            if (menuList.get(n).getParentId() != null && menuList.get(n).getParentId() != 0) {
                //判断当前节点所属的pid是否已经创建了以该pid为key的键值对，没有则创建新的链表
                currentMap.computeIfAbsent(menuList.get(n).getParentId(), k -> new LinkedList<>());
//                if (!(currentMap.containsKey(menuList.get(n).getParentId()))) {
//                    currentMap.put(menuList.get(n).getParentId(), new LinkedList<MenuNode>());
//                }
                //将该节点插入到对应的list的头部
                currentMap.get(menuList.get(n).getParentId()).add(0, menuList.get(n));
            }
        }
        if (n < 0) {
            return menuList;
        } else {
            return mergeList(new ArrayList<>(menuList.subList(0, n + 1)), menuList.get(n).getLevel(), currentMap);
        }
    }
}