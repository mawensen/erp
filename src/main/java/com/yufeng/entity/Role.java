package com.yufeng.entity;

import javax.persistence.*;

/**
 * 角色实体
 *
 * @author Wensen Ma
 */
@Entity
@Table(name = "t_role")
public class Role {

    @Id
    @GeneratedValue
    private Integer id; // 编号

    @Column(length = 50)
    private String name; // 角色名称

    @Column(length = 1000)
    private String remarks; // 备注

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "[id=" + id + ", name=" + name + ", remarks=" + remarks + "]";
    }


}
