package com.mytry.z2m.smarthome1.hivemq.tool.op;

import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;

public class BrokerConnection implements IBrokerConnection{

	//String brokerIpAddress1 = "192.168.50.179";

	private String myUuid1 = null;
	




	//private BrokerConnection connect;
	//false--繁忙，true--空闲
	private EnumBrokerConnectionStatus status;
	
	Mqtt5AsyncClient client1 =null;
	com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilder.Send<CompletableFuture<Mqtt5PublishResult>> publishBuilder_send1=null;
	
	
	//
	//
	public BrokerConnection() {
		this.myUuid1 = UUID.randomUUID().toString().replaceAll("-","");
	}
	
	public BrokerConnection(EnumBrokerConnectionStatus status, String brokerIpAddress1, int brokerPort, String clientId) {
		this.myUuid1 = UUID.randomUUID().toString().replaceAll("-","");
		//
		this.myInit(brokerIpAddress1, brokerPort, clientId);
		this.status = status;
	}
	
	//
	//
	//
	// -------------------------------------------------------------------
	public void setMyUuid1(String myUuid1) {
		this.myUuid1 = myUuid1;
	}
	public String getMyUuid1() {
		return myUuid1;
	}
	//
	public EnumBrokerConnectionStatus getStatus() {
		return status;
	}
	public void setStatus(EnumBrokerConnectionStatus status) {
		this.status = status;
	}
	//
	// -------------------------------------------------------------------
	//
	public String getClient1Identifier() {
		return this.client1.getConfig().getClientIdentifier().get().toString();
	}
	
	/**
	 * such as 192.168.50.179
	 * @return
	 */
	public String getBrokerIpAddress() {
		/*
		String result_serverHost = new String(this.client1.getConfig().getServerHost());
		String result_serverPort = new String(String.valueOf(this.client1.getConfig().getServerPort()));
		String resultTmp = new String(result_serverHost+ ":" +result_serverPort);
		*/
		return this.client1.getConfig().getServerHost();
	}
	
	public int getBrokerPort() {
		return this.client1.getConfig().getServerPort();
	}
	
	//

	//
	//
	//
	//
	//
	
	private void myInit(String brokerIpAddress1, int brokerPort, String clientId) {
		final InetSocketAddress LOCALHOST_EPHEMERAL1 = new InetSocketAddress(brokerIpAddress1,brokerPort);
        this.client1 = Mqtt5Client.builder().serverAddress(LOCALHOST_EPHEMERAL1).identifier(clientId).buildAsync();
	}
	//

	
	//释放连接池中的连接对象
	@Override
	public com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilder.Send<CompletableFuture<Mqtt5PublishResult>> myConnect(){
		
        //------------------------------- client connect --------------------------------------
        System.out.println("try connect");
        //注意这里的
        // 有点像 MqttAsyncClient sampleClient.connect(connOpts, null, null).waitForCompletion(-1); 
        // 在pahoMqtt 这里 waitForCompletion(-1)的-1, 是指一直不停地等待
        // 但是这里填写-1 是不等待，这里是等于0
        CompletableFuture<Mqtt5ConnAck> cplfu_connect_rslt = this.client1.connect().orTimeout(1000, TimeUnit.MILLISECONDS);
        //CompletableFuture<Mqtt5ConnAck> cplfu_connect_rslt = client1.connect();
        System.out.println("try connecting");
        while (cplfu_connect_rslt.isDone()==false) {
        	//System.out.println(topicUrlToPublish + "   waitttt too much");
        	try {
        		Thread.sleep(500);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
        
        // 因为 .isDone() 无论是否有无 CompletedExceptrionally, 他都是true
        // !!!!疑问!!!!!!!, isCancelled()这里可能需要修改, 不太清楚isCanceled这里
        // 因为 .isCancelled() 无论是否有无 via cancellation, 他都是true
        if(cplfu_connect_rslt.isCompletedExceptionally()==false && cplfu_connect_rslt.isCancelled()== false) {
        	this.publishBuilder_send1 = this.client1.publishWith();
        	// 被使用时设置成 繁忙
        	this.setStatus(EnumBrokerConnectionStatus.BUSY);
        }
        else {
        	this.publishBuilder_send1 = null;
        }
        
        // 不返回 client1 的原因是, 防止外部对client此进行串改
        // return client1;
		return this.publishBuilder_send1;
	}
	//
	
	
	
	//释放连接池中的连接对象
	@Override
	public void myReleaseConnect(){
		System.out.println("-----------正在 释放连接-----------");
		
		
		this.client1.disconnect();
		
		//wait
    	try {
    		Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    	this.status = EnumBrokerConnectionStatus.RELEASED;
    	System.out.println("-----------完成 释放连接, 该连接可用-----------");
	}
	
	
	//释放连接池中的连接对象
	public void myDeleteConnect(){
		System.out.println("-----------删除该连接-----------");
		
		
		this.client1.disconnect();
		
		//wait
    	try {
    		Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
       	this.status = EnumBrokerConnectionStatus.RELEASED;
    	// 清空 client1 和 那个外部可使用的send
    	this.client1 = null;
    	this.publishBuilder_send1 = null;
	}
	
	
}
