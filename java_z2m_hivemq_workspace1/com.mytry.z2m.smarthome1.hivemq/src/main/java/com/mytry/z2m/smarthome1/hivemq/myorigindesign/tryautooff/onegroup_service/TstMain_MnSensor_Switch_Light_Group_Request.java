package com.mytry.z2m.smarthome1.hivemq.myorigindesign.tryautooff.onegroup_service;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient.Mqtt5SubscribeAndCallbackBuilder;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient.Mqtt5SubscribeAndCallbackBuilder.Call.Ex;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.AbstractSmartDeivce;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.PhilipsHueGo2Entity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.PhilipsHueMotionOutdoorSensorEntity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.SonoffS31LiteEntity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.Light_Philips_Hue_GO2_Tool;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.MotionSensor_Philips_Hue_Outdoor_Tool;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.Switcher_Sonoff31Lite_Tool;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tryautooff.onegroup.Sonoff31Lite_PhilipsHueGo2_Group1_Autooff;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tryautooff.simplescen.Switcher_Sonoff31Lite_Autooff;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tryautooff.simplescen.withhuego2.Light_PhilipsHueGo2_Autooff;

/**
 * 
 * 方便快捷控制灯相应的顺序
 * 因为比如 之前 谁先写publish 谁就先进行关灯操作
 * 
 * 																												</br>
 * 用一个进程
 *  philips hue outdoor motion sensor 的状态 
 * 		来	同时判断 进行
 * 		philips hue go 2 开灯关灯
 * 		sonoff Lite31 switcher 来进行开关
 * 
 * 我这样有助于 防止 philips hue go 2 或者  sonoff Lite31 switcher 两个都  没成功, 
 * 		有可能导致这两个线程 都  去获取 philips hue outdoor motion sensor 的状态 
 * 			从而导致 此时 会同时获得两次  philips hue outdoor motion sensor 的状态, 
 * 				所以这是没有必要的, 所以用一个线程就足够了 
 * 
 * 暂时只控制 开的顺序, 管的顺序 可以在auto off 里面跟这里一样设置就好了
 * 
 * @author laipl
 *
 */
public class TstMain_MnSensor_Switch_Light_Group_Request implements Runnable{

	Sonoff31Lite_PhilipsHueGo2_Group1_Controller sonoff31Lite_PhilipsHueGo2_Group1_Controller = Sonoff31Lite_PhilipsHueGo2_Group1_Controller.getInstanceUsingDoubleCheckLocking();
	
	PhilipsHueMotionOutdoorSensorEntity plipMotionSensorEntity 				= null;
	ISonoff31Lite_PhilipsHueGo2_Group1_Autooff_serv interfaceTryAutoOff1 	= null;
	ArrayList<AbstractSmartDeivce> myDeviceGroup 							= null;
	

	public TstMain_MnSensor_Switch_Light_Group_Request() {
		
	}
	
	public TstMain_MnSensor_Switch_Light_Group_Request(PhilipsHueMotionOutdoorSensorEntity plipMotionSensorEntity,
			ISonoff31Lite_PhilipsHueGo2_Group1_Autooff_serv interfaceTryAutoOff1,
			ArrayList<AbstractSmartDeivce> myDeviceGroup) {
		super();
		this.plipMotionSensorEntity = plipMotionSensorEntity;
		this.interfaceTryAutoOff1 = interfaceTryAutoOff1;
		this.myDeviceGroup = myDeviceGroup;
	}

	
	public void setPlipMotionSensorEntity(PhilipsHueMotionOutdoorSensorEntity plipMotionSensorEntity) {
		this.plipMotionSensorEntity = plipMotionSensorEntity;
	}
	public PhilipsHueMotionOutdoorSensorEntity getPlipMotionSensorEntity() {
		return plipMotionSensorEntity;
	}


	public void setInterfaceTryAutoOff1(ISonoff31Lite_PhilipsHueGo2_Group1_Autooff_serv interfaceTryAutoOff1) {
		this.interfaceTryAutoOff1 = interfaceTryAutoOff1;
	}
	public ISonoff31Lite_PhilipsHueGo2_Group1_Autooff_serv getInterfaceTryAutoOff1() {
		return interfaceTryAutoOff1;
	}


	public void setMyDeviceGroup(ArrayList<AbstractSmartDeivce> myDeviceGroup) {
		this.myDeviceGroup = myDeviceGroup;
	}
	public ArrayList<AbstractSmartDeivce> getMyDeviceGroup() {
		return myDeviceGroup;
	}


	
	
	



	@Override
	public void run() {
		// TODO Auto-generated method stub
		sonoff31Lite_PhilipsHueGo2_Group1_Controller.myOpen(this.plipMotionSensorEntity, this.interfaceTryAutoOff1, this.myDeviceGroup);
	}


	

}
