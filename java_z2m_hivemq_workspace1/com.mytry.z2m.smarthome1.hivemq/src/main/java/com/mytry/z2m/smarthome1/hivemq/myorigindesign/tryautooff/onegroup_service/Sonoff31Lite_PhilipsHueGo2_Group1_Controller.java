package com.mytry.z2m.smarthome1.hivemq.myorigindesign.tryautooff.onegroup_service;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.internal.mqtt.MqttRxClient;
import com.hivemq.client.internal.mqtt.message.publish.MqttPublishBuilder;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilder;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilderBase;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.AbstractSmartDeivce;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.PhilipsHueGo2Entity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.PhilipsHueMotionOutdoorSensorEntity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.SonoffS31LiteEntity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.Light_Philips_Hue_GO2_Tool;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.Switcher_Sonoff31Lite_Tool;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.op.BrokerConnectionPool;
/**
 * 
 * 
 * <p>
 * 							description:																			</br>	
 * &emsp;						use different value to publish message each time 									</br>	
 * 																													</br>
 * 其实我觉得这里的controller可以没有的, 只是 我想模仿, 多个访问 访问 单例的controller, 然后访问单例的 service
 *
 * @author laipl
 *
 */
public class Sonoff31Lite_PhilipsHueGo2_Group1_Controller{
	


	private static  volatile Sonoff31Lite_PhilipsHueGo2_Group1_Controller instance;
	    

	//Thread Safe Singleton
	//ref: https://en.wikipedia.org/wiki/Double-checked_locking
	public static Sonoff31Lite_PhilipsHueGo2_Group1_Controller getInstanceUsingDoubleCheckLocking(){
	    if(instance == null){
	        synchronized (Sonoff31Lite_PhilipsHueGo2_Group1_Controller.class) {
	            if(instance == null){
	                instance = new Sonoff31Lite_PhilipsHueGo2_Group1_Controller();
	            }
	        }
	    }
	    return instance;
	}
	
	
	
	
	
	
	Sonoff31Lite_PhilipsHueGo2_Group1_Service sonoff31Lite_PhilipsHueGo2_Group1_Service1 = Sonoff31Lite_PhilipsHueGo2_Group1_Service.getInstanceUsingDoubleCheckLocking();
	
	
	
	public int myOpen(PhilipsHueMotionOutdoorSensorEntity plipMotionSensorEntity, ISonoff31Lite_PhilipsHueGo2_Group1_Autooff_serv interfaceTryAutoOff1,ArrayList<AbstractSmartDeivce> myDeviceGroup){
		int resultTmp = sonoff31Lite_PhilipsHueGo2_Group1_Service1.myOpen(plipMotionSensorEntity, interfaceTryAutoOff1, myDeviceGroup);
		return resultTmp;
	}
	
	
	
	
	
	
	
	
	
	

	

}
