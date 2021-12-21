package com.mytry.z2m.smarthome1.hivemq.tool.op;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;

public class MyPublishTool {

	
	/**
	 * 
	 * @param LOCALHOST_EPHEMERAL1
	 * @param clientId
	 * @param topicUrlToPublish
	 * @param arraylist_str_json 这个可以存放多个json, 可以批量一次性多次 发送 不同的数据 到同一个 topic, 当然 这其实不一定是好的, 但是以防万一需要, 这样就不需要频繁的 connect 和 disconnect 了
	 * @return 成功返回1
	 */
	public static int myPulibsh(InetSocketAddress LOCALHOST_EPHEMERAL1, String clientId, String topicUrlToPublish, ArrayList<String> aryList_str_jsonTmp) {
        //
        Mqtt5AsyncClient client1 = Mqtt5Client.builder().serverAddress(LOCALHOST_EPHEMERAL1).identifier(clientId).buildAsync();
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
        	System.out.println(topicUrlToPublish + "   waitttt too much");
        	try {
        		Thread.sleep(500);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
        //wait
        System.out.println("mypublisher:" + topicUrlToPublish +"/"+"clientId:"+ client1.getConfig().getClientIdentifier().get().toString()+"/"+ client1.getConfig().getServerHost() + ", connected");
        
        for(int i=0; i<=aryList_str_jsonTmp.size()-1; i++) {
        	String str_jsonTmp = aryList_str_jsonTmp.get(i);
            com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilder.Send<CompletableFuture<Mqtt5PublishResult>>  publishBuilder1 = client1.publishWith();
        	com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilder.Send.Complete<CompletableFuture<Mqtt5PublishResult>> c1 = publishBuilder1.topic(topicUrlToPublish);
        	c1.qos(MqttQos.AT_LEAST_ONCE);
        	c1.payload(str_jsonTmp.getBytes());
        	c1.send();
        }

        client1.disconnect();
        try {
    		Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("mypublisher:" + topicUrlToPublish + ", disconnected");
        return 1;
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param LOCALHOST_EPHEMERAL1
	 * @param clientId
	 * @param topicUrlToPublish
	 * @param arraylist_str_json 这个可以存放json 和 相对应的 topicUrl, 可以批量一次性多次 发送 不同的数据 到同一个 topic, 当然 这其实不一定是好的, 但是以防万一需要, 这样就不需要频繁的 connect 和 disconnect 了
	 * @return
	 */
	public static int myPulibsh(String brokerIpAddress, int brokerPort, String clientId, ArrayList<String>  aryList_topicUrlToPublish, ArrayList<String> aryList_str_jsonTmp) {
        //
		com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilder.Send<CompletableFuture<Mqtt5PublishResult>> publishBuilder_sendTmp = null; 
		
		BrokerConnectionPool brokerConnectionPool1 = BrokerConnectionPool.getInstanceUsingDoubleCheckLocking();
		try {
			// 获得BrokerConnection 接口
			IBrokerConnection iBroCon = brokerConnectionPool1.getAvailableIBrokerConnectionByBrokerIpAddressAndBrokerPortAndClientIdToUse(brokerIpAddress, brokerPort, clientId);
			// 利用 BrokerConnection 接口  去connect 我们的broker 
			publishBuilder_sendTmp = iBroCon.myConnect();
			//
			//
			if (publishBuilder_sendTmp!=null) {
		        for(int i=0; i<=aryList_str_jsonTmp.size()-1; i++) {
		        	
		        	String str_jsonTmp = aryList_str_jsonTmp.get(i);
		        	String topicUrlToPublishTmp = aryList_topicUrlToPublish.get(i);
		        	
		        	com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilder.Send.Complete<CompletableFuture<Mqtt5PublishResult>> c1 = publishBuilder_sendTmp.topic(topicUrlToPublishTmp);
		        	c1.qos(MqttQos.AT_LEAST_ONCE);
		        	c1.payload(str_jsonTmp.getBytes());
		        	c1.send();
		        	
		        	//每一次发送可以稍微缓一下, 虽然我感觉没什么用
		            try {
		        		Thread.sleep(500);
		    		} catch (InterruptedException e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    		}
		        }
			}
			//关闭连接
			iBroCon.myReleaseConnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return 1;
	}
}
