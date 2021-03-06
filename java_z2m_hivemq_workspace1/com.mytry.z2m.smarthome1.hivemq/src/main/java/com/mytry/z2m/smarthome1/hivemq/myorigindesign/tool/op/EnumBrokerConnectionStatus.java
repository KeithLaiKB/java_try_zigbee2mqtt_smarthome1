package com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.op;

public enum EnumBrokerConnectionStatus {
    RELEASED("ConnectionRealeased", 1), BUSY("ConnectionBusy", 2);
    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private EnumBrokerConnectionStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (EnumBrokerConnectionStatus c : EnumBrokerConnectionStatus.values()) {
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
