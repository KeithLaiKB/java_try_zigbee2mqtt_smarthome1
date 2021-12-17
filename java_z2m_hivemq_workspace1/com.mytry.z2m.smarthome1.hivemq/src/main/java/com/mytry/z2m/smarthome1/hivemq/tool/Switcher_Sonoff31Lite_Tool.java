package com.mytry.z2m.smarthome1.hivemq.tool;

import java.net.InetSocketAddress;
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
import com.mytry.z2m.smarthome1.hivemq.entity.SonoffS31LiteEntity;
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
	public int mySwitch(String mySwitchState)  {
	    //String topic        = "MQTT Examples";
	    String topic        = "zigbee2mqtt/0x00124b00250c256f/set";
	    return this.publish(topic, mySwitchState);
    }
	
	
	
	/**
	 * 
	 * @param mySwitchToState: "ON","OFF"    要转换成为的状态
	 * @return -1 代表 有问题, 0 代表 任务完成失败, 因为 网络慢等其他小问题 需要重新再发送请求  	1代表成功
	 */
	public int mySwitchTransaction(String mySwitchToState, SonoffS31LiteEntity sonoffS31LiteEntity1)  {
	    //String topic        = "MQTT Examples";
		//
		//
		//
	    if(sonoffS31LiteEntity1 == null) {
	    	System.err.println("entity is null");
	    	return -1;
	    }

    	//
		//
		//
		//
    	//--------------------------
    	// 判断当前状态
    	int publishResultTemp = -1;
    	//

    	// 然后取出 当前 main中的subscriber的状态
    	String switchStateTmp = sonoffS31LiteEntity1.getState();
    	//
    	//---------------------------------------------------------------
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
    		System.out.println("mySwitchTransaction"+"switcher null state, try to get switcher status");
        	//this.sendGetToNotifySubscriberToGetStatus(); 
    		this.sendGetToNotifySubscriberToGetStatus(sonoffS31LiteEntity1);
        	//
        	publishResultTemp = 0;
    	}
    	//if(switchStateTmp!=null &&(switchStateTmp.equals("ON")==true || switchStateTmp.equals("OFF")==true) ) {
    	else if(switchStateTmp!=null &&(switchStateTmp.equals("ON")==true || switchStateTmp.equals("OFF")==true) ) {
    		// 如果 当前状态 和 想要改变成的状态 一直, 则无需改变
    		if(switchStateTmp.equals(mySwitchToState)==true) {
    			System.err.println("mySwitchTransaction"+"switcher same state");
    			// do nothing
    			publishResultTemp = 1;
    		}
    		// 如果 当前状态 和 想要改变成的状态 一直, 则  需改变
    		else if(switchStateTmp.equals(mySwitchToState)==false) {
    			System.err.println("mySwitchTransaction"+"switcher different state, changing");
    			//publishResultTemp = this.mySwitch(mySwitchToState);
    			
    			publishResultTemp = this.publish(sonoffS31LiteEntity1.getTopicUrl_set(),mySwitchToState);
    			publishResultTemp = 1;
    		}
    	}
    	else {
    		System.err.println(this.getClass().getName() +":mySwitchTransaction"+" something wrong");
    		publishResultTemp = -1;
    	}

    	return publishResultTemp;
    }

	
	public int publish(String topicUrlToPublish , String mySwitchState)  {

        //String topic        = "MQTT Examples";
        //String topic        = "zigbee2mqtt/0x00124b00250c256f/get";
        //String content      = "Message from MqttPublishSample";
        String content      = "你好";
        //String content      = "hi_myfriend";
        int qos             = 2;
        //String broker       = "tcp://iot.eclipse.org:1883";
        String broker       = "tcp://localhost:1883";
        //String broker       = "ssl://localhost:8883";
        String clientId     = "JavaSample_publisher1";
        //MemoryPersistence persistence = new MemoryPersistence();

        
        //------------------------------- 创建 mqtt client --------------------------------------
        /*
        Mqtt5BlockingClient client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost("broker.hivemq.com")
                .buildBlocking();
        */
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
        	
        	
        	/* 这一部分是问题, 删掉就没问题了
        	CompletableFuture<Mqtt5PublishResult> completableFuture1 = c1.send();
            //
        	
        	// 等待 发送 publish
            long waitTimesTmp = 0L;
            long waitLimitTimesTmp = 10000L;
            while (completableFuture1.isDone() == false) {
            	try {
    				Thread.sleep(500);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
            	waitTimesTmp = waitTimesTmp + 500L;
            	if (waitTimesTmp> waitLimitTimesTmp) {
            		System.err.println(this.getClass().getName()+ ":publish time out,"+waitTimesTmp+","+waitLimitTimesTmp);
            		System.err.println(topicUrlToPublish+"/"+str_content_tmp);
            		return -1;
            		//throw new Exception(this.getClass().getName()+ "getStatus time out");
            		
            	}
            } 
        	 
        	 */

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
		this.publish("zigbee2mqtt/0x00124b00250c256f"+"/get", "");
		return 1;
	}*/
	
	
	
	/**
	 * 这个方法 是发送了这个 get, 可以让 服务器那边 进行通知所有的subscriber 现在当前的状态
	 * "zigbee2mqtt/0x00124b00250c256f"+"/get"
	 * @return
	 */
	public int sendGetToNotifySubscriberToGetStatus(SonoffS31LiteEntity sonoffS31LiteEntity1) {
		this.publish(sonoffS31LiteEntity1.getTopicUrl_get(), "");
		return 1;
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
