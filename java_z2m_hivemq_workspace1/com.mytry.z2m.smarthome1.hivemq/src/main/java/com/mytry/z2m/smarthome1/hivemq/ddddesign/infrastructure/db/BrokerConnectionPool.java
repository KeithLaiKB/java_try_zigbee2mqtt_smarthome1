package com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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

	
	/**
	 * 和C的volatile不太一样,
	 * java 的volatile是为了
	 * 防止指令重排
	 * 指的是instance = new Singleton()，我们感觉是一步操作的实例化对象，实际上对于JVM指令，是分为三步的：
	 * 1. 分配内存空间
	 * 2. 初始化对象
	 * 3. 将对象指向刚分配的内存空间
	 * 
	 * 有些但是 编译器为为了性能优化，
	 * 可能会把第二步和第三步进行重排序，
	 * 顺序就成了：
	 * 1. 分配内存空间
	 * 2. 将对象指向刚分配的内存空间
	 * 3. 初始化对象
	 * 
	 * 也就是说 如果重排成 这个顺序, 当进行 double check lock这个方式的时候
	 * 当线程一 运行到	2. 将对象指向刚分配的内存空间, 此时 
	 * 		3. 初始化对象  并没有执行, 
	 * 			也就是说 空间有了,对象还没创建完成 
	 * 但是 当线程二 进行判断   instance != null
	 * 		然后 return instance 的时候
	 * 		注意 这个 instance 此时 是并没有 对象的, 因为此时 对象还没有创建完成
	 * 如果此时线程二 继续马上做这个instance的操作  的话是会出问题的, 因为 对象还没有创建完成 
	 * 	ref：https://www.zhihu.com/question/485984234/answer/2245819035
	 * 
	 * 
	 * 
	 */
	
	private static volatile BrokerConnectionPool instance;
	    

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
	private BrokerConnection addConnection(String brokerIpAddress1, int brokerPort, String clientId) {
		// 判断 当前已有的size  加上    我要加的size 是否超过 maxSize
		if(initSize + arylist_brokerConnection.size() + 1 <= maxSize){
			//
			System.out.println("新建了"+  1 +"个连接");	
			
				
			BrokerConnection broCon = new BrokerConnection(EnumBrokerConnectionStatus.RELEASED, brokerIpAddress1, brokerPort, clientId);
			//
			// 往池子里 添加元素
			boolean resultTmp = arylist_brokerConnection.add(broCon);
			if(resultTmp==true) {
				System.out.println("新建可用连接:"+broCon.getMyUuid1());
				return broCon;
			}
			else {
				System.err.println(this.getClass().getName()+": some thing wrong when adding connection in connection pool");
				return null;
			}
		}
		System.err.println("connection num in connection pool is full, couldn't add more conection");
		return null;
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
	
	/*
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
	*/
	
	
	
	private IBrokerConnection getAvailableIBrokerConnectionByBrokerIpAddressAndBrokerPortAndClientId(String brokerIpAddress, int brokerPort, String clientId) {
		IBrokerConnection iBroCon= null;
		
		for(int i =0;i<=arylist_brokerConnection.size()-1; i++ ){
			BrokerConnection broCon = arylist_brokerConnection.get(i);
			// 如果 这个 连接的 IP 和 port 相同
			if(brokerIpAddress.equals(broCon.getBrokerIpAddress())==true && brokerPort==broCon.getBrokerPort()) {
				// 如果 这个 连接的 client 相同 
				if(clientId.equals(broCon.getClient1Identifier())==true) {
					// 如果这个 连接对象 可用
					if(broCon.getStatus().equals(EnumBrokerConnectionStatus.RELEASED)==true){
						System.out.println("找到可用连接:"+broCon.getMyUuid1());
						iBroCon = (IBrokerConnection) broCon;
						return iBroCon;
					}
					else {
						System.out.println("连接:"+broCon.getMyUuid1()+", 该对象繁忙");
					}
				}
			}

		}

		return null;
	}
	
	public IBrokerConnection getAvailableIBrokerConnectionByBrokerIpAddressAndBrokerPortAndClientIdToUse(String brokerIpAddress, int brokerPort, String clientId) {
		IBrokerConnection iBrokerConnection = this.getAvailableIBrokerConnectionByBrokerIpAddressAndBrokerPortAndClientId(brokerIpAddress, brokerPort, clientId);
		// 没有 找到 制定参数的 连接对象, 则自己创造一个 
		if(iBrokerConnection == null) {
			iBrokerConnection = this.addConnection(brokerIpAddress, brokerPort, clientId);
			System.out.println("add connection:"+brokerIpAddress+":"+brokerPort+", clientId is:"+clientId);
		}
		else {
			
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
