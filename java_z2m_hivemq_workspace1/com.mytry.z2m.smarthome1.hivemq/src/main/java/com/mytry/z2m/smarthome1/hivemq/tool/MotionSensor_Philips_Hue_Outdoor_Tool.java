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
public class MotionSensor_Philips_Hue_Outdoor_Tool {

	private static int myId=0;

    
    //String brokerIpAddress1       			= "127.0.0.1";
	String brokerIpAddress1       			= "192.168.50.179";
	
	public MotionSensor_Philips_Hue_Outdoor_Tool() {
		this.myId= this.myId +1;
	}

	
	/**
	 * 
	 * @param mySwitchState: "ON","OFF"
	 * @return
	 */
	public int mySwitch(String mySwitchState)  {
	    //String topic        = "MQTT Examples";
	    String topic        = "zigbee2mqtt/0x001788010644d258/set";
	    return this.publish(topic, mySwitchState);
    }
	
	
	/**
	 * 
	 * @param mySwitchToState: "ON","OFF"    要转换成为的状态
	 * @return
	 */
	public int mySwitchTransaction(String mySwitchToState, SonoffS31LiteEntity sonoffS31LiteEntity1)  {
	    
    	return -1;
    }

	/**
	 * ref: https://www.zigbee2mqtt.io/devices/9290019758.html
	 * @param topicUrlToPublish
	 * @param mySwitchState
	 * @return
	 */
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
        	
        	// 这个可以放开, 然后改一下相关事务就好
        	/*
        	if(mySwitchState.contentEquals("ON")==true) {
        		lhmap1.put("state", "ON");
        	}
        	else if(mySwitchState.contentEquals("OFF")==true) {
        		lhmap1.put("state", "OFF");
        	}
        	else if(mySwitchState.contentEquals("")==true) {
        		lhmap1.put("state", "");
        	}*/
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
    		Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("mypublisher:" + this.myId + ",disconnected");
        return 0;

    }

	
	
	
	/**
	 * 这个方法 是发送了这个 get, 可以让 服务器那边 进行通知所有的subscriber 现在当前的状态
	 * 
	 * 我自己尝试过发现motion_sensity 后面别的subscriber 获得的 不仅仅是motion_sensitivity, 还有其他属性, 
	 * 所以我觉得随便挑一个属性去申请 
	 *  这样就够了
	 * 
	 * @return
	 */
	public int sendGetToNotifySubscriberToGetStatus() {
		this.publish("zigbee2mqtt/0x001788010644d258"+"/get", "motion_sensitivity");
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
