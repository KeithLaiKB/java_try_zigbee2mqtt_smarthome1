package com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.internal.mqtt.MqttRxClient;
import com.hivemq.client.internal.mqtt.message.publish.MqttPublishBuilder;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient.Mqtt5SubscribeAndCallbackBuilder;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient.Mqtt5SubscribeAndCallbackBuilder.Call.Ex;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilder;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilderBase;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.PhilipsHueGo2Entity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.SonoffS31LiteEntity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.op.MyPublishTool;
/**
 * 
 * 
 * <p>
 * 							description:																			</br>	
 * &emsp;						use different value to publish message each time 									</br>	
 * 																													</br>
 *
 *
 * @author laipl
 *
 */
public class Switcher_Sonoff31Lite_Tool {

	private static int myId=0;

	String brokerIpAddress1 = "192.168.50.179";
	
	public Switcher_Sonoff31Lite_Tool() {
		this.myId= this.myId +1;
	}

	
	/**
	 * 
	 * @param mySwitchState: "ON","OFF"
	 * @return 
	 */
	/*
	public int mySwitch(String mySwitchState)  {
	    //String topic        = "MQTT Examples";
	    String topic        = "zigbee2mqtt/0x00124b00250c256f/set";
	    return this.publish(topic, mySwitchState);
    }
	*/
	
	/**
	 * 
	 * @param mySwitchToState: "ON","OFF"    ????????????????????????
	 * @return -1 ?????? ?????????, 0 ?????? ??????????????????, ?????? ??????????????????????????? ???????????????????????????  	1????????????
	 */
	public EnumDeviceTrancLogicResult mySwitchTransactionLogic(String mySwitchToState, SonoffS31LiteEntity sonoffS31LiteEntity1)  {
		//
		//
		//
	    if(sonoffS31LiteEntity1 == null) {
	    	System.err.println("entity is null");
	    	return EnumDeviceTrancLogicResult.DEVICE_NULL;
	    }
		//
    	//--------------------------
    	// ??????????????????
	    EnumDeviceTrancLogicResult trancLogicResultTemp = null;
    	//

    	// ???????????? ?????? main??????subscriber?????????
    	String switchStateTmp = sonoffS31LiteEntity1.getState();
    	//
    	//---------------------------------------------------------------
    	//
    	//
    	//
    	//
    	// ?????????null, ??????????????????, ??????????????? ??????????????????
    	// ?????? ?????????????????? ???????????????, ??????switcher ??????????????????null, ???????????????????????????
    	// ???????????????????????????, ????????????????????????, ??????switcher???null????????????
    	// ?????? ?????????main ????????????subscription, ??????callback??????????????????sleep ????????????callback???????????????
    	// ???????????? ?????????callback ?????? switcher ?????????, ?????????callback ???????????????sensor???????????? ???
    	// ??????switcher ??????????????????????????????
    	// ?????? ????????????, ????????????, ??????????????????????????????????????????????????????, 
    	// ??????????????????????????????, ?????????????????? subscription
    	// ??????????????? ????????? ????????????switcher?????????, 
    	// ?????????????????????entity???????????????????????????????????????
    	// ??????getState?????????entity?????? attribute ?????????
    	if(switchStateTmp == null){
        	// ??????????????? ?????? ??????broker?????? ?????????subscriber, ?????? main??????subscriber
    		System.out.println("mySwitchTransaction"+"SonoffS31Lite switcher null state, try to get hue go 2 light status");
        	//this.sendGetToNotifySubscriberToGetStatus();  
    		this.sendGetToNotifySubscriberToGetStatus(sonoffS31LiteEntity1);  
        	//
        	//publishResultTemp = 0;
    		trancLogicResultTemp = EnumDeviceTrancLogicResult.DEVICE_NULL;
    	}
    	//if(switchStateTmp!=null &&(switchStateTmp.equals("ON")==true || switchStateTmp.equals("OFF")==true) ) {
    	else if(switchStateTmp!=null &&(switchStateTmp.equals("ON")==true || switchStateTmp.equals("OFF")==true) ) {
    		// ?????? ???????????? ??? ???????????????????????? ??????, ???????????????
    		if(switchStateTmp.equals(mySwitchToState)==true) {
    			System.err.println("mySwitchTransaction"+"SonoffS31Lite switcher same state");
    			// do nothing
    			//publishResultTemp = 1;
    			trancLogicResultTemp = EnumDeviceTrancLogicResult.NoNeedToChange;
    		}
    		// ?????? ???????????? ??? ???????????????????????? ??????, ???  ?????????
    		else if(switchStateTmp.equals(mySwitchToState)==false) {
    			System.err.println("mySwitchTransaction"+"SonoffS31Lite switcher different state, changing");
    			//publishResultTemp = this.mySwitch(mySwitchToState);
    			//
    			//
    			//publishResultTemp = publish(philipsHueGo2Entity1.getTopicUrl_set() , mySwitchToState);
    			//publishResultTemp = publish(brokerIpAddress1, philipsHueGo2Entity1.getTopicUrl_set() , mySwitchToState);
    			trancLogicResultTemp = EnumDeviceTrancLogicResult.NeedToChange;
    		}
    	}
    	else {
    		System.err.println(this.getClass().getName() +":mySwitchTransactionLogic"+" something wrong");
    		trancLogicResultTemp = EnumDeviceTrancLogicResult.SOMETHING_WRONG;
    	}

    	//return publishResultTemp;
    	return trancLogicResultTemp;
    }
	
	
	/**
	 * 
	 * 0 ??? ?????????, -1 ??? ???????????????????????????, 
	 * 1 ????????????????????????????????????????????????????????? ????????? ??????????????????
	 * 2 ????????????????????? 
	 * 
	 */
	public int mySwitchTransaction(String mySwitchToState, SonoffS31LiteEntity sonoffS31LiteEntity1)  {
		//
		//
		int trancResultTemp = 0;
		int publishResultTmp = 0;
		
		EnumDeviceTrancLogicResult trancLogicResultTemp = mySwitchTransactionLogic(mySwitchToState, sonoffS31LiteEntity1); 
		if(trancLogicResultTemp == null) {
			System.out.println(this.getClass().getName().toString()+"/mySwitchTransaction"+ "something is wrong");
			trancResultTemp = -1;
		}
		else if (trancLogicResultTemp.equals(EnumDeviceTrancLogicResult.SOMETHING_WRONG)) {
			System.out.println(this.getClass().getName().toString()+"/mySwitchTransaction"+ "something is wrong when this method calling mySwitchTransactionLogic");
			trancResultTemp = -1;
		}
		else if (trancLogicResultTemp.equals(EnumDeviceTrancLogicResult.DEVICE_NULL)) {
			trancResultTemp = -1;
		}
		else if (trancLogicResultTemp.equals(EnumDeviceTrancLogicResult.NoNeedToChange)) {
			trancResultTemp = 1;
		}
		else if (trancLogicResultTemp.equals(EnumDeviceTrancLogicResult.NeedToChange)) {
			publishResultTmp = publish(brokerIpAddress1, sonoffS31LiteEntity1.getTopicUrl_set() , mySwitchToState);
			if(publishResultTmp == 1) {
				trancResultTemp = 2;
			}
			else {
				// 
				trancResultTemp = -1;
			}
		}
		
    	return trancResultTemp;
    }

	
	
	
	
	public String establishPublishJson(String mySwitchState)  {
		//
		// ?????? json
		LinkedHashMap<String,Object> lhmap1 = new LinkedHashMap<>();
    	//lhmap1.put("linkquality", 132);
    	if(mySwitchState.contentEquals("ON")==true) {
    		lhmap1.put("state", "ON");
    	}
    	else if(mySwitchState.contentEquals("OFF")==true) {
    		lhmap1.put("state", "OFF");
    	}
    	else if(mySwitchState.contentEquals("")==true) {
    		lhmap1.put("state", "");
    	}
    	//
    	//
    	ObjectMapper mapperTmp = new ObjectMapper();
    	String str_content_tmp = null;
		try {
			str_content_tmp = mapperTmp.writeValueAsString(lhmap1);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		return str_content_tmp;
	}
	/**
	 * ????????? ??????broker????????? ????????????????????????, ?????????????????????????????????, ???????????????????????????publish ???????????????, ????????????????????????????????????
	 * 
	 * @param brokerIpAddress
	 * @param topicUrlToPublish
	 * @param mySwitchState
	 * @return
	 */
	public int publish(String brokerIpAddress, String topicUrlToPublish , String mySwitchState)  {
		
		String clientId     = "JavaSample_publisher1";
		final InetSocketAddress LOCALHOST_EPHEMERAL1 = new InetSocketAddress(brokerIpAddress,1883);
		//
		// ?????? json
		LinkedHashMap<String,Object> lhmap1 = new LinkedHashMap<>();
    	//lhmap1.put("linkquality", 132);
    	if(mySwitchState.contentEquals("ON")==true) {
    		lhmap1.put("state", "ON");
    	}
    	else if(mySwitchState.contentEquals("OFF")==true) {
    		lhmap1.put("state", "OFF");
    	}
    	else if(mySwitchState.contentEquals("")==true) {
    		lhmap1.put("state", "");
    	}
    	//
    	//
    	ObjectMapper mapperTmp = new ObjectMapper();
    	String str_content_tmp = null;
		try {
			str_content_tmp = mapperTmp.writeValueAsString(lhmap1);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ArrayList<String> aryList_str_jsonTmp = new ArrayList<String>();
		aryList_str_jsonTmp.add(str_content_tmp);
		
		return MyPublishTool.myPulibsh(LOCALHOST_EPHEMERAL1, clientId, topicUrlToPublish, aryList_str_jsonTmp);
	}
	
	
	
	public int publish(String topicUrlToPublish , String mySwitchState)  {

        String clientId     = "JavaSample_publisher1";
        //MemoryPersistence persistence = new MemoryPersistence();

        
        //------------------------------- ?????? mqtt client --------------------------------------
        final InetSocketAddress LOCALHOST_EPHEMERAL1 = new InetSocketAddress(brokerIpAddress1,1883);
        //
        //
        Mqtt5AsyncClient client1 = Mqtt5Client.builder().serverAddress(LOCALHOST_EPHEMERAL1).identifier(clientId).buildAsync();
        //Mqtt5AsyncClient client1 = Mqtt5Client.builder().serverAddress(LOCALHOST_EPHEMERAL1).identifier(clientId).automaticReconnectWithDefaultConfig().buildAsync();
        
        //------------------------------- client connect --------------------------------------
        System.out.println("try connect");
        //???????????????
        // ????????? MqttAsyncClient sampleClient.connect(connOpts, null, null).waitForCompletion(-1); 
        // ???pahoMqtt ?????? waitForCompletion(-1)???-1, ???????????????????????????
        // ??????????????????-1 ??????????????????????????????0
        CompletableFuture<Mqtt5ConnAck> cplfu_connect_rslt = client1.connect().orTimeout(1000, TimeUnit.MILLISECONDS);
        //CompletableFuture<Mqtt5ConnAck> cplfu_connect_rslt = client1.connect();
        System.out.println("try connecting");
        while (cplfu_connect_rslt.isDone()==false) {
        	System.out.println(this.myId + "   waitttt too much");
        	try {
        		Thread.sleep(500);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
        //wait
        int times = 0;

    	System.out.println("mypublisher:" + this.myId + ",connected");
		

		
		
		
		//------------------------------- client publish --------------------------------------
		// ref:https://www.zigbee2mqtt.io/devices/BASICZBR3.html
        for(int i=0;i<=0;i++) {
        	LinkedHashMap<String,Object> lhmap1 = new LinkedHashMap<>();
        	//lhmap1.put("linkquality", 132);
        	if(mySwitchState.contentEquals("ON")==true) {
        		lhmap1.put("state", "ON");
        	}
        	else if(mySwitchState.contentEquals("OFF")==true) {
        		lhmap1.put("state", "OFF");
        	}
        	else if(mySwitchState.contentEquals("")==true) {
        		lhmap1.put("state", "");
        	}
        	//
        	//
        	ObjectMapper mapperTmp = new ObjectMapper();
        	String str_content_tmp=null;
			try {
				str_content_tmp = mapperTmp.writeValueAsString(lhmap1);
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	//client1.publishWith().topic(topic).qos(MqttQos.AT_LEAST_ONCE).payload(content.getBytes()).send();
        	
        	
        	com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilder.Send<CompletableFuture<Mqtt5PublishResult>>  publishBuilder1 = client1.publishWith();
        	com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilder.Send.Complete<CompletableFuture<Mqtt5PublishResult>> c1 = publishBuilder1.topic(topicUrlToPublish);
        	c1.qos(MqttQos.AT_LEAST_ONCE);
        	c1.payload(str_content_tmp.getBytes());
        	
        	
        	// send(): the result when the built Mqtt5Publish is sent by the parent
        	c1.send();
        	System.out.println("hello:"+str_content_tmp);
        	
        }
        
        client1.disconnect();
        try {
    		Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("mypublisher:" + this.myId + ",disconnected");
        return 0;

    }

	
	
	
	/**
	 * ???????????? ?????????????????? get, ????????? ??????????????? ?????????????????????subscriber ?????????????????????
	 * @return
	 */
	/*
	public int sendGetToNotifySubscriberToGetStatus() {
		this.publish("zigbee2mqtt/0x00124b00250c256f"+"/get", "");
		return 1;
	}*/
	
	
	
	/**
	 * ???????????? ?????????????????? get, ????????? ??????????????? ?????????????????????subscriber ?????????????????????
	 * "zigbee2mqtt/0x00124b00250c256f"+"/get"
	 * @return
	 */
	public int sendGetToNotifySubscriberToGetStatus(SonoffS31LiteEntity sonoffS31LiteEntity1) {
		//this.publish(sonoffS31LiteEntity1.getTopicUrl_get(), "");
		
		String clientId     = "JavaSample_publisher1";
		final InetSocketAddress LOCALHOST_EPHEMERAL1 = new InetSocketAddress(brokerIpAddress1,1883);
		//
		// ?????? json
		LinkedHashMap<String,Object> lhmap1 = new LinkedHashMap<>();
    	lhmap1.put("state", "");

    	//
    	//
    	ObjectMapper mapperTmp = new ObjectMapper();
    	String str_content_tmp = null;
		try {
			str_content_tmp = mapperTmp.writeValueAsString(lhmap1);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ArrayList<String> aryList_str_jsonTmp = new ArrayList<String>();
		aryList_str_jsonTmp.add(str_content_tmp);
		
		return MyPublishTool.myPulibsh(LOCALHOST_EPHEMERAL1, clientId, sonoffS31LiteEntity1.getTopicUrl_get(), aryList_str_jsonTmp);
	}
	
	
	
	
	
	
	
	
	
	
	//-------------------------------------------------------------------------------
	private class MyMessageTmp{
		String myJsonContent = null;
		public MyMessageTmp() {
			
		}
		
		public void setMyJsonContent(String myJsonContent) {
			this.myJsonContent = myJsonContent;
		}
		
		public String getMyJsonContent() {
			return this.myJsonContent;
		}
		
		public Map getMyJsonContentMap() {
			//
        	ObjectMapper mapperTmp = new ObjectMapper();
        	LinkedHashMap<String,Object> lkhMapTmp1 = null;
        	TypeReference<LinkedHashMap<String,Object>> tpRfTmp1  = new TypeReference<LinkedHashMap<String,Object>>() {};
        	//
        	try {
        		lkhMapTmp1 = mapperTmp.readValue(myJsonContent, tpRfTmp1);
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	

		
        	return lkhMapTmp1;
		}
		
	}
}
