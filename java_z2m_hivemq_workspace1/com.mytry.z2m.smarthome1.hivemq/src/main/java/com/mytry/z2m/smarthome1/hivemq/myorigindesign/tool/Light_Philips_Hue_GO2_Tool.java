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


import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;

import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.PhilipsHueGo2Entity;
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
public class Light_Philips_Hue_GO2_Tool {

	private static int myId=0;

	String brokerIpAddress1 = "192.168.50.179";
	
	public Light_Philips_Hue_GO2_Tool() {
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
	    String topic        = "zigbee2mqtt/0x0017880109e5d363/set";
	    return this.publish(topic, mySwitchState);
    }
    */
	
	
	/**
	 * 
	 * @param mySwitchToState: "ON","OFF"    要转换成为的状态
	 * @return -1 代表 有问题, 0 代表 任务完成失败, 因为 网络慢等其他小问题 需要重新再发送请求  	1代表成功
	 */
	public EnumDeviceTrancLogicResult mySwitchTransactionLogic(String mySwitchToState, PhilipsHueGo2Entity philipsHueGo2Entity1)  {
		//
		//
		//
	    if(philipsHueGo2Entity1 == null) {
	    	System.err.println("entity is null");
	    	return EnumDeviceTrancLogicResult.DEVICE_NULL;
	    }
		//
    	//--------------------------
    	// 判断当前状态
	    EnumDeviceTrancLogicResult trancLogicResultTemp = null;
    	//

    	// 然后取出 当前 main中的subscriber的状态
    	String switchStateTmp = philipsHueGo2Entity1.getState();
    	//
    	//---------------------------------------------------------------
    	//
    	//
    	//
    	//
    	// 如果是null, 就去发送信息, 等待下一次 调用这个方法
    	// 例如 第一次传感器 过来的时候, 我的switcher 可能初始值是null, 那么就不去发送信息
    	// 第二次你运动的时候, 再传来信息的时候, 不是switcher是null就可以了
    	// 因为 虽然我main 用了两个subscription, 但是callback中只要有一个sleep 那么整个callback都无法运行
    	// 也就是说 即使我callback 写了 switcher 的监听, 但整个callback 当前都在被sensor这个分支 用
    	// 所以switcher 状态是没有办法改变的
    	// 但是 不用担心, 如无意外, 也就是第一次可能会出现这样的状况而已, 
    	// 因为每次进行开关以后, 我们这边是有 subscription
    	// 所以我们是 会收到 当前这个switcher的状态, 
    	// 然后我们的这个entity就会把当前状态记录更新下来
    	// 每次getState也是从entity中的 attribute 来获得
    	if(switchStateTmp == null){
        	// 先放松一个 请求 去让broker通知 所有的subscriber, 包括 main中的subscriber
    		System.out.println("mySwitchTransaction"+"hue go 2 light null state, try to get hue go 2 light status");
        	//this.sendGetToNotifySubscriberToGetStatus();  
    		this.sendGetToNotifySubscriberToGetStatus(philipsHueGo2Entity1);  
        	//
        	//publishResultTemp = 0;
    		trancLogicResultTemp = EnumDeviceTrancLogicResult.DEVICE_NULL;
    	}
    	//if(switchStateTmp!=null &&(switchStateTmp.equals("ON")==true || switchStateTmp.equals("OFF")==true) ) {
    	else if(switchStateTmp!=null &&(switchStateTmp.equals("ON")==true || switchStateTmp.equals("OFF")==true) ) {
    		// 如果 当前状态 和 想要改变成的状态 一直, 则无需改变
    		if(switchStateTmp.equals(mySwitchToState)==true) {
    			System.err.println("mySwitchTransaction"+"hue go 2 light same state");
    			// do nothing
    			//publishResultTemp = 1;
    			trancLogicResultTemp = EnumDeviceTrancLogicResult.NoNeedToChange;
    		}
    		// 如果 当前状态 和 想要改变成的状态 一直, 则  需改变
    		else if(switchStateTmp.equals(mySwitchToState)==false) {
    			System.err.println("mySwitchTransaction"+"hue go 2 light different state, changing");
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
	 * 0 是 默认值, -1 是 各种原因导致的失败, 
	 * 1 是不需要去改变状态，因为当前的设备状态 已经是 要改变的状态
	 * 2 是改变状态成功 
	 * 
	 * @param mySwitchToState
	 * @param philipsHueGo2Entity1
	 * @return
	 */
	public int mySwitchTransaction(String mySwitchToState, PhilipsHueGo2Entity philipsHueGo2Entity1)  {
		//
		//
		int trancResultTemp = 0;
		int publishResultTmp = 0;
		
		EnumDeviceTrancLogicResult trancLogicResultTemp = mySwitchTransactionLogic(mySwitchToState, philipsHueGo2Entity1); 
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
			publishResultTmp = publish(brokerIpAddress1, philipsHueGo2Entity1.getTopicUrl_set() , mySwitchToState);
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
		// 制作 json
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
	 * 这个把 连接broker的操作 抽出去当工具类了, 使得代码看起来更舒服了, 但实际操作跟下面的publish 差不多作用, 用这个阅读起来我感觉更好
	 * 
	 * @param brokerIpAddress
	 * @param topicUrlToPublish
	 * @param mySwitchState
	 * @return 成功返回1
	 */
	public int publish(String brokerIpAddress, String topicUrlToPublish , String mySwitchState)  {
		
		String clientId     = "JavaSample_publisher1";
		final InetSocketAddress LOCALHOST_EPHEMERAL1 = new InetSocketAddress(brokerIpAddress,1883);
		//
		// 制作 json
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

        
        //------------------------------- 创建 mqtt client --------------------------------------
        final InetSocketAddress LOCALHOST_EPHEMERAL1 = new InetSocketAddress(brokerIpAddress1,1883);
        //
        //
        Mqtt5AsyncClient client1 = Mqtt5Client.builder().serverAddress(LOCALHOST_EPHEMERAL1).identifier(clientId).buildAsync();
        //Mqtt5AsyncClient client1 = Mqtt5Client.builder().serverAddress(LOCALHOST_EPHEMERAL1).identifier(clientId).automaticReconnectWithDefaultConfig().buildAsync();
        
        //------------------------------- client connect --------------------------------------
        System.out.println("try connect");
        //注意这里的
        // 有点像 MqttAsyncClient sampleClient.connect(connOpts, null, null).waitForCompletion(-1); 
        // 在pahoMqtt 这里 waitForCompletion(-1)的-1, 是指一直不停地等待
        // 但是这里填写-1 是不等待，这里是等于0
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
	 * 这个方法 是发送了这个 get, 可以让 服务器那边 进行通知所有的subscriber 现在当前的状态
	 * @return
	 */
	/*
	public int sendGetToNotifySubscriberToGetStatus() {
		this.publish("zigbee2mqtt/0x0017880109e5d363"+"/get", "");
		return 1;
	}*/
	
	/**
	 * "zigbee2mqtt/0x0017880109e5d363"+"/get"
	 * 
	 * @return
	 */
	/**
	 * 这个方法 是发送了这个 get, 可以让 服务器那边 进行通知所有的subscriber 现在当前的状态
	 * @return
	 */
	/*
	public int sendGetToNotifySubscriberToGetStatus() {
		this.publish("zigbee2mqtt/0x0017880109e5d363"+"/get", "");
		return 1;
	}*/
	
	/**
	 * "zigbee2mqtt/0x0017880109e5d363"+"/get"
	 * 
	 * @return
	 */
	public int sendGetToNotifySubscriberToGetStatus(PhilipsHueGo2Entity philipsHueGo2Entity1) {
		//this.publish(philipsHueGo2Entity1.getTopicUrl_get(), "");
		
		String clientId     = "JavaSample_publisher1";
		final InetSocketAddress LOCALHOST_EPHEMERAL1 = new InetSocketAddress(brokerIpAddress1,1883);
		//
		// 制作 json
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
		
		return MyPublishTool.myPulibsh(LOCALHOST_EPHEMERAL1, clientId, philipsHueGo2Entity1.getTopicUrl_get(), aryList_str_jsonTmp);
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
