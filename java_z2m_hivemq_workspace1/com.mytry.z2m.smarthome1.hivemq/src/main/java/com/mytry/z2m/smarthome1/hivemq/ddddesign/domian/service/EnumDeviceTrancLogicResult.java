package com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.service;


public enum EnumDeviceTrancLogicResult {
	SOMETHING_WRONG("something is wrong", -1), 
	DEVICE_NULL("device is null", 0), 
	NoNeedToChange("no need to change", 1), 
	NeedToChange("need to change", 2);
    
	// 成员变量
    private String name;
    private int index;

    // 构造方法
    private EnumDeviceTrancLogicResult(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (EnumDeviceTrancLogicResult c : EnumDeviceTrancLogicResult.values()) {
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

