package com.mytry.z2m.smarthome1.hivemq.myorigindesign.tryautooff.onegroup_service;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient.Mqtt5SubscribeAndCallbackBuilder;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient.Mqtt5SubscribeAndCallbackBuilder.Call.Ex;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.AbstractSmartDeivce;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.PhilipsHueGo2Entity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.PhilipsHueMotionOutdoorSensorEntity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.SonoffS31LiteEntity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.Light_Philips_Hue_GO2_Tool;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.MotionSensor_Philips_Hue_Outdoor_Tool;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.Switcher_Sonoff31Lite_Tool;



/**
 * 
 * 方便快捷控制灯相应的顺序
 * 因为比如 之前 谁先写publish 谁就先进行关灯操作
 * 
 * 																												</br>
 * 用一个进程
 *  philips hue outdoor motion sensor 的状态 
 * 		来	同时判断 进行
 * 		philips hue go 2 开灯关灯
 * 		sonoff Lite31 switcher 来进行开关
 * 
 * 我这样有助于 防止 philips hue go 2 或者  sonoff Lite31 switcher 两个都  没成功, 
 * 		有可能导致这两个线程 都  去获取 philips hue outdoor motion sensor 的状态 
 * 			从而导致 此时 会同时获得两次  philips hue outdoor motion sensor 的状态, 
 * 				所以这是没有必要的, 所以用一个线程就足够了 
 * 
 * 暂时只控制 开的顺序, 管的顺序 可以在auto off 里面跟这里一样设置就好了
 * 
 * 
 * 
 * 
 * 
 * ++++++++++++++
 * entry->发送-> 创建一个request线程 -> 使用单例的Controller ->使用单例的Service
 * 
 * 因为entry 我们用的是callback, 如果 在callback里 直接 调用 某些不创建进程的普通方法,
 * 并且如果 这个方法用了sleep之类睡眠 整个进程的操作, 我们的callback 也是会受到影响的, 
 * 当此时这次的callback 完成之后 才能继续 接收 第二次 的subscription 从而进行 callback的 第二次操作
 * 也就是说 我们的callback 会因为这个sleep 无法按时接收到 第二个subscription, 
 
 * 
 * 
 * 
 * 
 * 
 * @author laipl
 *
 */
public class TstMain_MnSensor_Switch_Light_Group_autooff2_seq_entry {


	
	public static void main(String[] args){


        String topicSensorSub1        	= "zigbee2mqtt/0x001788010644d258";
        String broker       			= "tcp://localhost:1883";
        
        String brokerIpAddress1       	= "192.168.50.179";
        
        String sensorClientId1     = "JavaSample_revcevierTesta";
        
        // philips hue outdoor motion sensor
        String topicSwitchSub1        = "zigbee2mqtt/0x00124b00250c256f";
        
        // phips hue go 2
        String topicLightSub1        = "zigbee2mqtt/0x0017880109e5d363";

        String switchClientId1     	= "JavaSample_revcevierTesta";
        
        int reqTimes = 0;
        
       
        //
        //
        
        Runnable rnb_tryAutoOff1 =new Sonoff31Lite_PhilipsHueGo2_Group1_Autooff_serv();
        Thread trd_tryAutoOff1 =new Thread(rnb_tryAutoOff1);
        trd_tryAutoOff1.start();
        
        ISonoff31Lite_PhilipsHueGo2_Group1_Autooff_serv interfaceTryAutoOff1 =(ISonoff31Lite_PhilipsHueGo2_Group1_Autooff_serv) rnb_tryAutoOff1;
        
        
        //
        //

        final InetSocketAddress LOCALHOST_EPHEMERAL1 = new InetSocketAddress(brokerIpAddress1,1883);
        //Mqtt5Client sampleClient = new Mqtt5Client(broker, clientId, persistence);
        //Mqtt5AsyncClient client1 = Mqtt5Client.builder().serverAddress(LOCALHOST_EPHEMERAL1).identifier(clientId).buildAsync();
        
        
        //Mqtt5AsyncClient client1 = Mqtt5Client.builder().serverAddress(LOCALHOST_EPHEMERAL1).buildAsync();
        Mqtt5AsyncClient client1 = Mqtt5Client.builder().serverAddress(LOCALHOST_EPHEMERAL1).identifier(sensorClientId1).automaticReconnectWithDefaultConfig().buildAsync();
        
        
        
        //Mqtt5Client client1 = Mqtt5Client.builder().identifier(clientId).build();
        /*
        Mqtt5Subscribe subscribeMessage = Mqtt5Subscribe.builder()
                .topicFilter(topic)
                .qos(MqttQos.EXACTLY_ONCE)
                .build();
        client1.subscribe(subscribeMessage)*/
        
        
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
        
        
        
		//------------------------------- 第 A1 种写法 --------------------------------------
		/*
        //ref : hivemq-mqtt-client/examples/src/main/java/com/hivemq/client/mqtt/examples/Mqtt5Features.java /
		// subscribeWith() 是为了获得  Mqtt5SubscribeAndCallbackBuilder
		client1
        .subscribeWith()
        .topicFilter(topic)
        .qos(MqttQos.AT_LEAST_ONCE)
        .callback(publish -> System.out.println("received message: " + publish + "////"+ new String(publish.getPayloadAsBytes())) ) 	// set callback
        .send();		//subscribe callback and something 
        */
        //
        //
        //
        //   
        //------------------------------- 第 A2 种写法 --------------------------------------
        // 因为MqttAsyncClient.MqttSubscribeAndCallbackBuilder -> Mqtt5SubscribeAndCallbackBuilder.Start.Complete -> Mqtt5SubscribeAndCallbackBuilder.Start
        Mqtt5AsyncClient.Mqtt5SubscribeAndCallbackBuilder.Start subscribeBuilder1 = client1.subscribeWith();
        // 一开始 不知道c1的类, 所以鼠标转移到 topicFilter, 可以看到 是Complete
        // 但是我们不知道这个Complete是哪个包里的 Complete
        //com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5SubscriptionBuilderBase<C extends Complete<C>> c1 = subscribeBuilder1.topicFilter(topic);
        //Mqtt5SubscribeAndCallbackBuilder.Start.Complete c1 = subscribeBuilder1.topicFilter(topic);
        
        
        //Mqtt5SubscribeAndCallbackBuilder.Start.Complete c1 = subscribeBuilder1.topicFilter(topic);
        // 点击最后一个 上面的Complete 就可以展开了
        com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient.Mqtt5SubscribeAndCallbackBuilder.Complete c1 = subscribeBuilder1.addSubscription().topicFilter(topicSensorSub1).qos(MqttQos.AT_LEAST_ONCE).applySubscription();
        c1.addSubscription().topicFilter(topicSwitchSub1).qos(MqttQos.AT_LEAST_ONCE).applySubscription();
        c1.addSubscription().topicFilter(topicLightSub1).qos(MqttQos.AT_LEAST_ONCE).applySubscription();
        
        //
        //
        //
        //
        PhilipsHueMotionOutdoorSensorEntity plipMotionSensorEntity1 = new PhilipsHueMotionOutdoorSensorEntity();
        plipMotionSensorEntity1.setTopicUrl("zigbee2mqtt/0x001788010644d258");
        plipMotionSensorEntity1.setTopicUrl_get("zigbee2mqtt/0x001788010644d258/get");
        plipMotionSensorEntity1.setTopicUrl_set("zigbee2mqtt/0x001788010644d258/set");
        
        SonoffS31LiteEntity sonoffS31LiteEntity1 = new SonoffS31LiteEntity();
        sonoffS31LiteEntity1.setTopicUrl("zigbee2mqtt/0x00124b00250c256f");
        sonoffS31LiteEntity1.setTopicUrl_get("zigbee2mqtt/0x00124b00250c256f/get");
        sonoffS31LiteEntity1.setTopicUrl_set("zigbee2mqtt/0x00124b00250c256f/set");
        
        
        PhilipsHueGo2Entity philipsHueGo2Entity1 = new PhilipsHueGo2Entity();
        philipsHueGo2Entity1.setTopicUrl("zigbee2mqtt/0x0017880109e5d363");
        philipsHueGo2Entity1.setTopicUrl_get("zigbee2mqtt/0x0017880109e5d363/get");
        philipsHueGo2Entity1.setTopicUrl_set("zigbee2mqtt/0x0017880109e5d363/set");
        //
        //
        // group
        ArrayList<AbstractSmartDeivce> myDeviceGroup1 = new ArrayList<AbstractSmartDeivce>();
        // 我打算 如果中途不出现问题的话, huego2先亮, 然后 sonoffS31Lite 打开
        myDeviceGroup1.add(philipsHueGo2Entity1);
        myDeviceGroup1.add(sonoffS31LiteEntity1);
        
        //
        //
        //
        // 把这个放到 那个线程 当中, 这样 这里变, Switcher_AutoOff3里的那个对象也会跟着变, 因为他们的引用关系, 具体怎么引用自己查
        ((Sonoff31Lite_PhilipsHueGo2_Group1_Autooff_serv)rnb_tryAutoOff1).setSonoffS31LiteEntity1(sonoffS31LiteEntity1);
        //
        // 把这个放到 那个线程 当中, 这样 这里变, 里的那个对象也会跟着变, 因为他们的引用关系, 具体怎么引用自己查
        ((Sonoff31Lite_PhilipsHueGo2_Group1_Autooff_serv)rnb_tryAutoOff1).setPhilipsHueGo2Entity1(philipsHueGo2Entity1);
        //
        //
        // 因为 这个 调用的是 下面这种 Integer.MAX_VALUE, 所以他一下纸会创建很多, 
        // 所以其实 我们可以直接用 ThreadPoolExecutor 自己去指定 最大数
        // ExecutorService CachedThreadPool1 = Executors.newCachedThreadPool();
        // ThreadPoolExecutor CachedThreadPool1 = new ThreadPoolExecutor(0, Integer.MAX_VALUE,60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        //															0个核心	1000个非核心			60L单位的 生存时间, 生存时间单位 是秒,  用 SynchronousQueue	
        ThreadPoolExecutor cachedThreadPool1 = new ThreadPoolExecutor(0, Integer.valueOf(1000),60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        //
        //
        //
        c1.callback(publish -> 
        	{
        		String jsonRsTmp = new String(publish.getPayloadAsBytes());
        		
        		
        		if(publish.getTopic().toString().equals(topicSensorSub1)==true) {
        			System.out.println("seeeen:"+publish.toString());
        			plipMotionSensorEntity1.setAttributeFromJson(jsonRsTmp);
        			System.out.println("phisssenentity:"+plipMotionSensorEntity1.toString());
        			
        			/*
        	        Runnable rnb_reqGroupTmp =new TstMain_MnSensor_Switch_Light_Group_Request(plipMotionSensorEntity1, interfaceTryAutoOff1, myDeviceGroup1);
        	        Thread trd_reqGroupTmp =new Thread(rnb_reqGroupTmp);
        	        trd_reqGroupTmp.start();
        			*/
        			Runnable rnb_reqGroupTmp =new TstMain_MnSensor_Switch_Light_Group_Request(plipMotionSensorEntity1, interfaceTryAutoOff1, myDeviceGroup1);
        			cachedThreadPool1.execute(rnb_reqGroupTmp);
        			
        			
        		}
        		// 如果当前 获得的信息是 开关信息
        		else if(publish.getTopic().toString().equals(topicSwitchSub1)==true) {
        			//
        			// 记录当前信息
        			System.out.println("swiiiii:"+publish.toString());
        			sonoffS31LiteEntity1.setAttributeFromJson(jsonRsTmp);
        			System.out.println("swiiiiientity:"+sonoffS31LiteEntity1.toString());
        		}
        		// 如果当前 获得的信息是 开关信息
        		else if(publish.getTopic().toString().equals(topicLightSub1)==true) {
        			//
        			// 记录当前信息
        			System.out.println("lightttt:"+publish.toString());
        			philipsHueGo2Entity1.setAttributeFromJson(jsonRsTmp);
        			System.out.println("lighttttentity:"+philipsHueGo2Entity1.toString());
        		}
        		
        		
        	}
        ); 	// set callback
        c1.send();		//subscribe callback and something 
        
        
        System.out.println("enter to exit!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Scanner in =new Scanner(System.in) ;
        //client1.disconnect();
        //client1.
        
	}
}
