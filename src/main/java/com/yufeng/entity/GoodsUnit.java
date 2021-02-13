package com.yufeng.entity;

import javax.persistence.*;

/**
 * 商品单位实体
 *
 * @author Wensen Ma
 */
@Entity
@Table(name = "t_goodsunit")
public class GoodsUnit {

    @Id
    @GeneratedValue
    private Integer id; // 编号

    @Column(length = 10)
    private String name; // 商品单位名称


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

    @Override
    public String toString() {
        return "[id=" + id + ", name=" + name + "]";
    }


}
