package com.yufeng.entity;

import javax.persistence.*;

/**
 * 用户角色关联实体
 *
 * @author Wensen Ma
 */
@Entity
@Table(name = "t_userRole")
public class UserRole {

    @Id
    @GeneratedValue
    private Integer id; // 编号

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user; // 用户

    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role; // 角色

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "[id=" + id + ", user=" + user + ", role=" + role + "]";
    }


}
