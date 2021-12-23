package com.mytry.z2m.smarthome1.hivemq.myorigindesign.tryautooff.onegroup_service;

/**
 * 
 * 隔离掉 Sonoff31Lite_PhilipsHueGo2_Group1_Autooff_serv 这个Runnable里的 run 方法, 避免让别人 直接 stop这个runnable 
 * 
 * @author laipl
 *
 */
public interface ISonoff31Lite_PhilipsHueGo2_Group1_Autooff_serv {
	void setMyrecorded_occupancy(int myrecorded_occupancy);
}
