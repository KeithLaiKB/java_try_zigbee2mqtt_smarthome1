package com.mytry.z2m.smarthome1.hivemq.myorigindesign.tryautooff.simplescen.withhuego2;

import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

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
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.PhilipsHueGo2Entity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.PhilipsHueMotionOutdoorSensorEntity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.SonoffS31LiteEntity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.Light_Philips_Hue_GO2_Tool;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.MotionSensor_Philips_Hue_Outdoor_Tool;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.Switcher_Sonoff31Lite_Tool;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tryautooff.simplescen.Switcher_Sonoff31Lite_Autooff;

/**
 * 单独一个进程																												</br>
 * 利用 philips hue outdoor motion sensor 的状态 来控制
 * 		philips hue go 2 
 * 			开灯关灯
 * 
 * 
 * 和
 * 单独一个进程																												</br>
 * 利用 philips hue outdoor motion sensor 的状态 来控制
 * 		sonoff Lite31 switcher 
 * 			来进行开关
 * 
 * 也就是说这上面就已经出现一共两个进程了
 * 
 * @author laipl
 *
 */
public class TstMain_MnSensor_Switch_Light_Group_autooff {


	
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
        
        Runnable rnb_tryAutoOff1 =new Switcher_Sonoff31Lite_Autooff();
        Thread trd_tryAutoOff1 =new Thread(rnb_tryAutoOff1);
        trd_tryAutoOff1.start();
        
        Runnable rnb_try_PhilipsHueGo2AutoOff1 = new Light_PhilipsHueGo2_Autooff();
        Thread trd_try_PhilipsHueGo2AutoOff1 =new Thread(rnb_try_PhilipsHueGo2AutoOff1);
        trd_try_PhilipsHueGo2AutoOff1.start();
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
        // 把这个放到 那个线程 当中, 这样 这里变, Switcher_AutoOff3里的那个对象也会跟着变, 因为他们的引用关系, 具体怎么引用自己查
        ((Switcher_Sonoff31Lite_Autooff)rnb_tryAutoOff1).setSonoffS31LiteEntity1(sonoffS31LiteEntity1);
        //
        // 把这个放到 那个线程 当中, 这样 这里变, 里的那个对象也会跟着变, 因为他们的引用关系, 具体怎么引用自己查
        ((Light_PhilipsHueGo2_Autooff)rnb_try_PhilipsHueGo2AutoOff1).setPhilipsHueGo2Entity1(philipsHueGo2Entity1);
        //
        //
        //
        //PhilipsHueMotionOutdoorSensorEntity plipMotionSensorEntity1 = null;
        //SonoffS31LiteEntity sonoffS31LiteEntity1 = null;
        
        c1.callback(publish -> 
        	{
        		String jsonRsTmp = new String(publish.getPayloadAsBytes());
        		
        		
        		if(publish.getTopic().toString().equals(topicSensorSub1)==true) {
        			System.out.println("seeeen:"+publish.toString());
        			plipMotionSensorEntity1.setAttributeFromJson(jsonRsTmp);
        			System.out.println("phisssenentity:"+plipMotionSensorEntity1.toString());
        			
        			/*
                	try {
                		Thread.sleep(200000);
            		} catch (InterruptedException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}*/
        			
        			Switcher_Sonoff31Lite_Tool 			switcher_Sonoff31LiteToolTmp			= new Switcher_Sonoff31Lite_Tool();
        			Light_Philips_Hue_GO2_Tool			light_Philips_Hue_GO2_ToolTmp			= new Light_Philips_Hue_GO2_Tool();
        			MotionSensor_Philips_Hue_Outdoor_Tool 	motionsensor_PhilipHueOutdoorToolTmp= new MotionSensor_Philips_Hue_Outdoor_Tool();
        			// 注意一下!!!!!!!
            		//
            		//
            		//场景一, publisher 那边   没有    设置retain
            		//	程序启动前
            		// 		1.我的手尝试运动
            		//			场景很暗(illuminance_lux < 150)
            		//			 	publisher 发送信息( 因为 没有 设置retain, 这条消息 不会 被保留 ) 
            		//	启动程序后
            		//  	1.subscriber 无法    接收到消息
            		//		2.我的手尝试运动
            		//			场景很暗(illuminance_lux < 150)
            		//				publisher 发送信息
            		//				subscriber 接收到消息
            		//
            		//场景二, publisher 那边    有    设置retain
            		//	程序启动后
            		// 		1.我的手尝试运动
            		//			场景很暗(illuminance_lux < 150)
            		//			 	publisher 发送信息( 因为 有   设置retain, 这条消息    会   被保留 )  
            		//	启动程序
            		//  	1.subscriber 接收到消息
            		//
            		// 结论 可以看出retain带来的不同的结果
            		// 	场景一, 坐在电脑前动	->	不动一段混时间	->	我坐在电脑前, 不动, 悄悄手指点击启动程序
            		//		灯是没有办法开的
            		//						然后需要再手动一下	->	灯才打开 
            		//
            		// 	场景一, 坐在电脑前动	->	不动一段混时间	->	我坐在电脑前, 不动, 悄悄手指点击启动程序
            		//		灯是没有办法开的
            		//------------------------------------------------
            		//
            		// 因为 pulisher 发布过了一次消息 后没有保存信息的
            		// 所以 我们直接 subscribe, 最初始的时候 是没有信息的
                	// 例如启动这个程序前  运动触发了 监测现在 确实灯很暗, 但是 这个很暗的信息 是没有保存的
            		// 所以我们subscribe 是接收不到内容的, 因为我们没有运动
   	
                	System.out.println("received message: " + publish + "////"+ new String(publish.getPayloadAsBytes()));
                	System.out.println("received message content(illuminance_lux): " + plipMotionSensorEntity1.getIlluminance_lux());
                	
                	Integer illuminance_luxTmp =  plipMotionSensorEntity1.getIlluminance_lux();
            	
                	//------------------------------------------------------------------------------
                	if(plipMotionSensorEntity1.getOccupancy().equals(true)){
                		// 告诉负责 Sonoff31Lite 的autooff类, 此时 是有人的, 不用开始计算 无人在房间内的时间
                		((Switcher_Sonoff31Lite_Autooff)rnb_tryAutoOff1).setMyrecorded_occupancy(1);
                		// 告诉负责 philips hue go 2 的autooff类, 此时 是有人的, 不用开始计算 无人在房间内的时间
                		((Light_PhilipsHueGo2_Autooff)rnb_try_PhilipsHueGo2AutoOff1).setMyrecorded_occupancy(1);
                				
                		if(illuminance_luxTmp.compareTo(150)<=0) {
                    		System.out.println("it is too dark, i try to switch on the plug");
                        	try {
                        		Thread.sleep(500);
                    		} catch (InterruptedException e) {
                    			// TODO Auto-generated catch block
                    			e.printStackTrace();
                    		}
                    		//开灯, 传当前 的 开关 状态过去, 因为事务需要判断 要转变成的状态 和 当前状态, 来进行调整
                        	// 例如 现在很暗, 但是已经开了 就不开灯了
                        	int switchTrancResultTemp = switcher_Sonoff31LiteToolTmp.mySwitchTransaction("ON", sonoffS31LiteEntity1);
                        	int light_switchTrancResultTemp1 = light_Philips_Hue_GO2_ToolTmp.mySwitchTransaction("ON", philipsHueGo2Entity1);
                        	//
                        	// 注意!!!!!!
                        	// 如果其中有一个或以上 没成功, 则进行重新再申请一次 感应器的状态,
                        	// 因为 可能 没成功  是因为 最开始运行时, 那些状态 还是null 
                        	// 而我们一开始收到的信息可能就是 Motion sensor, 此时正在处于 这个第一次 占用了callback的执行, 
                        	// 因为他们 共同使用同一个 callback
                        	// 所以其他subscription 是暂时无法处理 当前这个 callback的
                        	if(switchTrancResultTemp == 0 || light_switchTrancResultTemp1 == 0) {
                        		//motionsensor_PhilipHueOutdoorToolTmp.sendGetToNotifySubscriberToGetStatus();
                        		motionsensor_PhilipHueOutdoorToolTmp.sendGetToNotifySubscriberToGetStatus(plipMotionSensorEntity1);
                        	}                   	
                        	
                        	
                        	//
                        	
                        	//因为光 有可能在这个数字上波动, 我们的灯可能会每隔几分钟获得结果  随着 数值 上下波动而 不停地开关灯
                        	//所以 一旦低于这个数值, 我们就让他开灯, 然后等十五分钟后再去看看
                        	try {
                        		//Thread.sleep(150000);
                        		//先用15秒调试
                        		Thread.sleep(500);
                    		} catch (InterruptedException e) {
                    			// TODO Auto-generated catch block
                    			e.printStackTrace();
                    		}
                    	}
                	}
                	// 如果当前 感应器表示 没有人了
                	else if(plipMotionSensorEntity1.getOccupancy().equals(false)){
                		
                		System.out.println("occupancy is false now");
                		//
                		// Switcher_AutoOff3  这个线程  他自己会根据我们改的值定期检查, 然后进行操作
                		// 例如 20s 没人就关灯
                		// 告诉负责 Sonoff31Lite 的autooff类, 	此时 是没有人的, 开始计算 无人在房间内的时间
                		((Switcher_Sonoff31Lite_Autooff)rnb_tryAutoOff1).setMyrecorded_occupancy(0);
                		// 告诉负责 philips hue go 2 的autooff类, 此时 是没有人的, 开始计算 无人在房间内的时间
                		((Light_PhilipsHueGo2_Autooff)rnb_try_PhilipsHueGo2AutoOff1).setMyrecorded_occupancy(0);
                	}
        			
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
        
        //Switcher_Sonoff31lite_Toola tryStartTmp2= new Switcher_Sonoff31lite_Toola();
        //tryStartTmp2.getStatus0();
        //
        
        
        System.out.println("enter to exit!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Scanner in =new Scanner(System.in) ;
        //client1.disconnect();
        //client1.
        
	}
}
