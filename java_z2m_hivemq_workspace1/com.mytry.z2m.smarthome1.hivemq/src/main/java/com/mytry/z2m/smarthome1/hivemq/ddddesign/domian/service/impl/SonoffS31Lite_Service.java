package com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.service.impl;

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
import com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.philipshueoutdoormotionsensor.mydto.PhilipsHueOutdoorMotionSensorDto;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.service.ISonoff31Lite_PhilipsHueGo2_Group1_Service;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.service.ISonoffS31Lite_Service;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.AbstractSmartDeivce;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.PhilipsHueGo2Entity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.PhilipsHueMotionOutdoorSensorEntity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.SonoffS31LiteEntity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.EnumDeviceTrancLogicResult;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.Light_Philips_Hue_GO2_Tool;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.MotionSensor_Philips_Hue_Outdoor_Tool;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.Switcher_Sonoff31Lite_Tool;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.op.BrokerConnectionPool;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.op.MyPublishTool;
/**
 * 
 * 
 * <p>
 * 							description:																			</br>	
 * &emsp;						use different value to publish message each time 									</br>	
 * 																													</br>
 * 用线程控制 自动关灯
 *
 * @author laipl
 *
 */
public class SonoffS31Lite_Service implements ISonoffS31Lite_Service{
	
	String brokerIpAddress1 = "192.168.50.179";
	int brokerPort1 = 1883;
	String clientId1 = "IamClient1";

	private static  volatile SonoffS31Lite_Service instance;
	    

	//Thread Safe Singleton
	//ref: https://en.wikipedia.org/wiki/Double-checked_locking
	public static SonoffS31Lite_Service getInstanceUsingDoubleCheckLocking(){
	    if(instance == null){
	        synchronized (SonoffS31Lite_Service.class) {
	            if(instance == null){
	                instance = new SonoffS31Lite_Service();
	            }
	        }
	    }
	    return instance;
	}

	public int saveSonoffS31Lite(PhilipsHueOutdoorMotionSensorDto philipsHueOutdoorMotionSensor1) {
		
		return 1;
	}

}
