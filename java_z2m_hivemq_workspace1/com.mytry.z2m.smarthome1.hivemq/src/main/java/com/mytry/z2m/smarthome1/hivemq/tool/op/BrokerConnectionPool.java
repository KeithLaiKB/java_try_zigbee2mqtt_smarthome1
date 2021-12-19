package com.mytry.z2m.smarthome1.hivemq.tool.op;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;

public class BrokerConnectionPool {

	private int initSize = 0;
	private int maxSize = 4;
	
	// synchronizedList 可以使得 对 这个 arraylist 进行add()、remove()、size() 时 不会发生资源争抢的 问题
	// 但是 如果你用Iterator(我觉得Iterator。next()??) 来访问 这个 synchronizedList, 它将会跳出这规则, 会发生资源争抢的问题
	// 所以操作Iterator来访问时, 需要 一个 手动的  synchronize 来限制, 如下:
	// ref: https://docs.oracle.com/javase/7/docs/api/java/util/Collections.html#synchronizedList(java.util.List)
	/*
	  List list = Collections.synchronizedList(new ArrayList());
      ...
  	  synchronized (list) {
      Iterator i = list.iterator(); // Must be in synchronized block
      while (i.hasNext())
          foo(i.next());
  	}
  */
	private final static List<BrokerConnection> arylist_brokerConnection =  Collections.synchronizedList(new ArrayList<BrokerConnection>());

	
	private BrokerConnectionPool(){}

	private static BrokerConnectionPool instance;
	    

	//Thread Safe Singleton
	//ref: https://en.wikipedia.org/wiki/Double-checked_locking
	public static BrokerConnectionPool getInstanceUsingDoubleCheckLocking(){
	    if(instance == null){
	        synchronized (BrokerConnectionPool.class) {
	            if(instance == null){
	                instance = new BrokerConnectionPool();
	            }
	        }
	    }
	    return instance;
	}
	
	
	/*
	//创建数据库连接
	private void initConnection() {
		// 判断 初始的size是否超过 maxSize
		if(initSize <= maxSize){
			//
			for(int i = 0; i <= initSize-1; i++){
				System.out.println("初始化了"+ (i + 1) +"个连接");
				
				
				BrokerConnection broCon = new BrokerConnection();
				
				
				arylist_brokerConnection.add(broCon);
			}
		}
	}*/
	
	//创建数据库连接
	private int addConnection(String brokerIpAddress1, int brokerPort, String clientId) {
		// 判断 当前已有的size  加上    我要加的size 是否超过 maxSize
		if(arylist_brokerConnection.size() + 1 <= maxSize){
			//
			System.out.println("初始化了"+  1 +"个连接");	
				
			BrokerConnection broCon = new BrokerConnection(EnumBrokerConnectionStatus.RELEASED, brokerIpAddress1, brokerPort, clientId);
					
			boolean resultTmp = arylist_brokerConnection.add(broCon);
			if(resultTmp==true) {
				return 1;
			}
			else {
				System.err.println(this.getClass().getName()+": some thing wrong when adding connection in connection pool");
				return -1;
			}
		}
		System.out.println("connection num in connection pool is full, couldn't add more conection");
		return -1;
	}
	
	
	
	
	
	//获取可用连接对象
	// ref: vhttps://github.com/aloys-jun/connect-pool/blob/c9f5210521e466abebc60c22fe845d393fd861d3/connect-pool/src/main/java/com/aloys/connect/pool/PoolConnection.java
	// private 这里把 BrokerConnection 的操作弄成private 感觉更安全
	// 使得BrokerConnection 的所有操作 只能在本文操作 
	private BrokerConnection getAvailableConnection() {

		for(BrokerConnection broCon : arylist_brokerConnection){
			if(broCon.getStatus().equals(EnumBrokerConnectionStatus.RELEASED)==true){
				return broCon;
			}
		}

		return null;
	}
	
	public IBrokerConnection getAvailableIConnection() {
		IBrokerConnection iBroCon= null;
		
		for(int i =0;i<=arylist_brokerConnection.size()-1; i++ ){
			BrokerConnection broCon = arylist_brokerConnection.get(i);
			if(broCon.getStatus().equals(EnumBrokerConnectionStatus.RELEASED)==true){
				iBroCon = (IBrokerConnection) broCon;
				return iBroCon;
			}
		}

		return null;
	}
	
	
	
	
	private IBrokerConnection getAvailableIBrokerConnectionByBrokerIpAddressAndBrokerPortAndClientId(String brokerIpAddress, int brokerPort, String clientId) {
		IBrokerConnection iBroCon= null;
		
		for(int i =0;i<=arylist_brokerConnection.size()-1; i++ ){
			BrokerConnection broCon = arylist_brokerConnection.get(i);
			if(broCon.getStatus().equals(EnumBrokerConnectionStatus.RELEASED)==true){
				if(brokerIpAddress.equals(broCon.getBrokerIpAddress())==true && brokerPort==broCon.getBrokerPort()) {
					if(clientId.equals(broCon.getClient1Identifier())==true) {
						
						iBroCon = (IBrokerConnection) broCon;
						return iBroCon;
						
					}
				}
			}
		}

		return null;
	}
	
	public IBrokerConnection getAvailableIBrokerConnectionByBrokerIpAddressAndBrokerPortAndClientIdToUse(String brokerIpAddress, int brokerPort, String clientId) {
		IBrokerConnection iBrokerConnection = this.getAvailableIBrokerConnectionByBrokerIpAddressAndBrokerPortAndClientId(brokerIpAddress, brokerPort, clientId);
		if(iBrokerConnection == null) {
			this.addConnection(brokerIpAddress, brokerPort, clientId);
		}
		return iBrokerConnection;
	}
	
	
	
	/*
	// 这里用public 就是让这个 send 给外面使用的, 这样 brokerConnection 就可以不被外面发现
	public com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilder.Send<CompletableFuture<Mqtt5PublishResult>> myConnection() throws Exception{
		String clientId     = "JavaSample_publisher1";
		com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilder.Send<CompletableFuture<Mqtt5PublishResult>> publishBuilder_sendTmp = null;
		
		BrokerConnection broCon = this.getAvailableConnection();
		publishBuilder_sendTmp = broCon.myConnect(clientId);
		

		if(publishBuilder_sendTmp == null) {
			throw new Exception(this.getClass().getName()+": myConnection():"+ "couldn't get publishBuilder_Send, maybe connection Wrong");
		}
		
		return publishBuilder_sendTmp;
		
	}
	*/
	
	

	





}
