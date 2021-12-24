package com.mytry.z2m.smarthome1.hivemq.ddddesign.applicationlayer;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.sonoffs31lite.repository_gateway.ISonoffS31LiteRepository;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.posonoffs31lite.impl.SonoffS31LiteRepository;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.posonoffs31lite.mydo.SonoffS31LiteDo;

public class SonoffS31LiteSubscriber_ForKeepRunning implements Runnable {
	private String ieeeAddress = null;
	private String topicUrl = null;
	private String topicUrl_get = null;
	private String topicUrl_set = null;
	
	
	
	private String brokerIpAddress1 = null;
	private int brokerPort1 = -1;
	private String clientId1 = null;
	//
	// 当 myThreadRunflag 设置成不是1 时, run方法将会结束 
	int myThreadRunflag = 0;
	
	public SonoffS31LiteSubscriber_ForKeepRunning() {
		
	}

	//
	//
	public void setIeeeAddress(String ieeeAddress) {
		this.ieeeAddress = ieeeAddress;
	}
	public String getIeeeAddress() {
		return ieeeAddress;
	}

	
	public void setTopicUrl(String topicUrl) {
		this.topicUrl = topicUrl;
	}
	public String getTopicUrl() {
		return topicUrl;
	}

	
	public void setTopicUrl_get(String topicUrl_get) {
		this.topicUrl_get = topicUrl_get;
	}
	public String getTopicUrl_get() {
		return topicUrl_get;
	}


	public void setTopicUrl_set(String topicUrl_set) {
		this.topicUrl_set = topicUrl_set;
	}
	public String getTopicUrl_set() {
		return topicUrl_set;
	}




	public void setBrokerIpAddress1(String brokerIpAddress1) {
		this.brokerIpAddress1 = brokerIpAddress1;
	}
	public String getBrokerIpAddress1() {
		return brokerIpAddress1;
	}


	public void setBrokerPort1(int brokerPort1) {
		this.brokerPort1 = brokerPort1;
	}
	public int getBrokerPort1() {
		return brokerPort1;
	}


	public void setClientId1(String clientId1) {
		this.clientId1 = clientId1;
	}
	public String getClientId1() {
		return clientId1;
	}




	public int getMyThreadRunflag() {
		return myThreadRunflag;
	}

	public void setMyThreadRunflag(int myThreadRunflag) {
		this.myThreadRunflag = myThreadRunflag;
	}







	@Override
	public void run() {
		// TODO Auto-generated method stub
		//
        final InetSocketAddress LOCALHOST_EPHEMERAL1 = new InetSocketAddress(this.brokerIpAddress1,this.brokerPort1);
        
        //Mqtt5AsyncClient client1 = Mqtt5Client.builder().serverAddress(LOCALHOST_EPHEMERAL1).buildAsync();
        Mqtt5AsyncClient client1 = Mqtt5Client.builder().serverAddress(LOCALHOST_EPHEMERAL1).identifier(clientId1).automaticReconnectWithDefaultConfig().buildAsync();
        //client1.connect();
        //------------------------------- client connect --------------------------------------
        // 一定要注意 connect之后 如果不用thenAccept之类的方法, 就一定要 让他 等一等 , 等connect成功!!!!!!!!!!!!!!!!
        // 不然刚connect 就去publish 会出现第一条无法publish, 然后成功publish第二条的现象
        // 有点像 MqttAsyncClient sampleClient.connect(connOpts, null, null).waitForCompletion(-1); 需要block自己然后直到连接成功才进行下一步
        // 只是我选择 用段时间等待而已
        CompletableFuture<Mqtt5ConnAck> cplfu_connect_rslt = client1.connect();
        //wait
    	while (cplfu_connect_rslt.isDone() == false) {
    		// 这里的 sleep 可以不用, 不影响主逻辑
    		// 只不过 这里加了个 sleep, 可以减少 不停地loop, 因为太多loop会给计算机带来的资源消耗
        	try {
        		Thread.sleep(500);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	System.out.println("mysubscriber connected");
        
        
        //------------------------------- 第 A2 种写法 --------------------------------------
        // 因为MqttAsyncClient.MqttSubscribeAndCallbackBuilder -> Mqtt5SubscribeAndCallbackBuilder.Start.Complete -> Mqtt5SubscribeAndCallbackBuilder.Start
        Mqtt5AsyncClient.Mqtt5SubscribeAndCallbackBuilder.Start subscribeBuilder1 = client1.subscribeWith();
        
        com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient.Mqtt5SubscribeAndCallbackBuilder.Complete c1 = subscribeBuilder1.addSubscription().topicFilter(this.topicUrl).qos(MqttQos.AT_LEAST_ONCE).applySubscription();
        
        c1.callback(publish -> 
    	{
    		String jsonRsTmp = new String(publish.getPayloadAsBytes());
    		ISonoffS31LiteRepository iSonoffS31LiteRepository1 = SonoffS31LiteRepository.getInstanceUsingDoubleCheckLocking();
    		//
    		SonoffS31LiteDo sonoffS31LiteDoTmp=iSonoffS31LiteRepository1.myFindByIeeeAddress(this.ieeeAddress);
    		//
    		//
    		int repoOpreationResultTmp = 0;
    		if(sonoffS31LiteDoTmp == null) {
    			sonoffS31LiteDoTmp = new SonoffS31LiteDo();
    			//
    			sonoffS31LiteDoTmp.setIeeeAddress("0x00124b00250c256f");
    			sonoffS31LiteDoTmp.setTopicUrl("zigbee2mqtt/0x00124b00250c256f");
    			sonoffS31LiteDoTmp.setTopicUrl_get("zigbee2mqtt/0x00124b00250c256f/get");
    			sonoffS31LiteDoTmp.setTopicUrl_set("zigbee2mqtt/0x00124b00250c256f/set");
    	        //
    			sonoffS31LiteDoTmp.setAttributeFromJson(jsonRsTmp);
    			//
    			repoOpreationResultTmp = iSonoffS31LiteRepository1.myAdd(sonoffS31LiteDoTmp);
    			if(repoOpreationResultTmp == 1) {
    				System.out.println("myAdd sonoffS31Lite switcher successfully");
    			}
    		}
    		else if(sonoffS31LiteDoTmp != null) {
    			sonoffS31LiteDoTmp.setAttributeFromJson(jsonRsTmp);
    			repoOpreationResultTmp = iSonoffS31LiteRepository1.myUpdate(sonoffS31LiteDoTmp);
    			System.out.println("myUpdate sonoffS31Lite switcher successfully");
    		}
    		//
    	}); 	// set callback
        c1.send();		//subscribe callback and something 
        //
        //
        //
        // 为了保持这个线程一直活着
		while (myThreadRunflag == 1) {
			// 减少循环的次数
        	try {
        		Thread.sleep(1000);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
		}
		//
		//
		//
		// 断开连接
		client1.disconnect();
		//
		// 稍微等一下 让连接断开
    	try {
    		Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("SonoffS31LiteSubscriber_ForKeepRunning disconnect");
	}


}
