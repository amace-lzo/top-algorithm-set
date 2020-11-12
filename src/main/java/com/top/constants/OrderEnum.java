package com.top.constants;

/**
 * 排序枚举类
 */
public enum OrderEnum {

    ASC(1,"升序"),

    DESC(2,"降序");

    OrderEnum(int flag, String name) {

        this.flag = flag;

        this.name = name;

    }

    private int flag;

    private String name;

}
