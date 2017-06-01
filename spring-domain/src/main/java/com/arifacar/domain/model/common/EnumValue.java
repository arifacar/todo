package com.arifacar.domain.model.common;
import com.arifacar.domain.model.BaseEntity;

import javax.persistence.Entity;

@Entity
public class EnumValue extends BaseEntity{

    private String name;

    private String value;

    private Long orderNo;

    public EnumValue(String name, String value, Long orderNo) {
        this.name = name;
        this.value = value;
        this.orderNo = orderNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }
}