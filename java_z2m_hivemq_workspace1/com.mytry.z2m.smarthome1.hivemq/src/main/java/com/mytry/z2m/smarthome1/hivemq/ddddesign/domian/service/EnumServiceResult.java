package com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.service;

public enum EnumServiceResult {
	SOMETHING_WRONG("something_is_wrong", -2), 
	FAIL("fail", -1), 
	MYNULL("my_null", 0), 
	SUCCESS("success", 1);
    
	// 成员变量
    private String name;
    private int index;

    // 构造方法
    private EnumServiceResult(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (EnumServiceResult c : EnumServiceResult.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}