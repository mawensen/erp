package com.yufeng.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 客户退货单
 *
 * @author Wensen Ma
 */
@Entity
@Table(name = "t_customerReturnList")
public class CustomerReturnList {

    @Id
    @GeneratedValue
    private Integer id; // 编号

    @Column(length = 100)
    private String customerReturnNumber; // 客户退货单号

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer; // 客户

    @Temporal(TemporalType.TIMESTAMP)
    private Date customerReturnDate; // 客户退货日期

    @Transient
    private Date bCustomerReturnDate; // 起始时间 搜索用到

    @Transient
    private Date eCustomerReturnDate; // 结束时间 搜索用到

    private float amountPayable; // 应付金额

    private float amountPaid; // 实付金额

    private Integer state; // 交易状态 1 已付  2 未付

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user; // 操作用户

    @Column(length = 1000)
    private String remarks; // 备注

    @Transient
    private List<CustomerReturnListGoods> customerReturnListGoodsList = null; // 客户退货单商品集合


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getCustomerReturnNumber() {
        return customerReturnNumber;
    }

    public void setCustomerReturnNumber(String customerReturnNumber) {
        this.customerReturnNumber = customerReturnNumber;
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    public Date getCustomerReturnDate() {
        return customerReturnDate;
    }

    public void setCustomerReturnDate(Date customerReturnDate) {
        this.customerReturnDate = customerReturnDate;
    }

    public float getAmountPayable() {
        return amountPayable;
    }

    public void setAmountPayable(float amountPayable) {
        this.amountPayable = amountPayable;
    }

    public float getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(float amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getbCustomerReturnDate() {
        return bCustomerReturnDate;
    }

    public void setbCustomerReturnDate(Date bCustomerReturnDate) {
        this.bCustomerReturnDate = bCustomerReturnDate;
    }

    public Date geteCustomerReturnDate() {
        return eCustomerReturnDate;
    }

    public void seteCustomerReturnDate(Date eCustomerReturnDate) {
        this.eCustomerReturnDate = eCustomerReturnDate;
    }


    public List<CustomerReturnListGoods> getCustomerReturnListGoodsList() {
        return customerReturnListGoodsList;
    }

    public void setCustomerReturnListGoodsList(List<CustomerReturnListGoods> customerReturnListGoodsList) {
        this.customerReturnListGoodsList = customerReturnListGoodsList;
    }

    @Override
    public String toString() {
        return "CustomerReturnList [id=" + id + ", customerReturnNumber=" + customerReturnNumber + ", customer="
                + customer + ", customerReturnDate=" + customerReturnDate + ", bCustomerReturnDate="
                + bCustomerReturnDate + ", eCustomerReturnDate=" + eCustomerReturnDate + ", amountPayable="
                + amountPayable + ", amountPaid=" + amountPaid + ", state=" + state + ", user=" + user + ", remarks="
                + remarks + "]";
    }


}
