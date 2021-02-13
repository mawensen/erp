package com.yufeng.entity;

import javax.persistence.*;

/**
 * 菜单实体
 *
 * @author Wensen Ma
 */
@Entity
@Table(name = "t_menu")
public class Menu {

    @Id
    @GeneratedValue
    private Integer id; // 编号

    @Column(length = 50)
    private String name; // 菜单名称

    @Column(length = 200)
    private String url; // 菜单地址

    private Integer state; // 菜单节点类型 1 根节点 0 叶子节点


    @Column(length = 100)
    private String icon; // 图标

    private Integer pId; // 父菜单Id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    @Override
    public String toString() {
        return "[id=" + id + ", name=" + name + ", url=" + url + ", state=" + state + ", icon=" + icon + ", pId="
                + pId + "]";
    }


}
