package com.mytry.z2m.smarthome1.hivemq.tryautooff.onegroup_service;


import java.util.ArrayList;



import com.mytry.z2m.smarthome1.hivemq.entity.AbstractSmartDeivce;
import com.mytry.z2m.smarthome1.hivemq.entity.PhilipsHueGo2Entity;
import com.mytry.z2m.smarthome1.hivemq.entity.PhilipsHueMotionOutdoorSensorEntity;
import com.mytry.z2m.smarthome1.hivemq.entity.SonoffS31LiteEntity;
import com.mytry.z2m.smarthome1.hivemq.tool.EnumDeviceTrancLogicResult;
import com.mytry.z2m.smarthome1.hivemq.tool.Light_Philips_Hue_GO2_Tool;
import com.mytry.z2m.smarthome1.hivemq.tool.MotionSensor_Philips_Hue_Outdoor_Tool;
import com.mytry.z2m.smarthome1.hivemq.tool.Switcher_Sonoff31Lite_Tool;
import com.mytry.z2m.smarthome1.hivemq.tool.op.MyPublishTool;
/**
 * 
 * 
 * <p>
 * 							description:																			</br>	
 * &emsp;						use different value to publish message each time 									</br>	
 * 																													</br>
 * 用线程控制 自动关灯
 *
 * @author laipl
 *
 */
public class Sonoff31Lite_PhilipsHueGo2_Group1_Service{
	
	String brokerIpAddress1 = "192.168.50.179";
	int brokerPort1 = 1883;
	String clientId1 = "IamClient1";

	private static  volatile Sonoff31Lite_PhilipsHueGo2_Group1_Service instance;
	    

	//Thread Safe Singleton
	//ref: https://en.wikipedia.org/wiki/Double-checked_locking
	public static Sonoff31Lite_PhilipsHueGo2_Group1_Service getInstanceUsingDoubleCheckLocking(){
	    if(instance == null){
	        synchronized (Sonoff31Lite_PhilipsHueGo2_Group1_Service.class) {
	            if(instance == null){
	                instance = new Sonoff31Lite_PhilipsHueGo2_Group1_Service();
	            }
	        }
	    }
	    return instance;
	}

	
	public int myOpen(PhilipsHueMotionOutdoorSensorEntity plipMotionSensorEntity, ISonoff31Lite_PhilipsHueGo2_Group1_Autooff_serv interfaceTryAutoOff1,ArrayList<AbstractSmartDeivce> myDeviceGroup){
		// 设置每个设备打开之间的间隔, 当然你设置成0也可以
		// 设置一定的长时间的间隔, 可以呈现比较好看的 按顺序开灯的效果
		long timeGapEachDeviceTmp = 300L;
		
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

    	//System.out.println("received message: " + publish + "////"+ new String(publish.getPayloadAsBytes()));
    	//System.out.println("received message content(illuminance_lux): " + plipMotionSensorEntity1.getIlluminance_lux());
    	
    	Integer illuminance_luxTmp =  plipMotionSensorEntity.getIlluminance_lux();
	
    	//------------------------------------------------------------------------------
    	if(plipMotionSensorEntity.getOccupancy().equals(true)){
    		// 告诉负责 Sonoff31Lite  和 philips hue go 2的autooff类, 此时 是有人的, 不用开始计算 无人在房间内的时间
    		interfaceTryAutoOff1.setMyrecorded_occupancy(1);
    				
    		if(illuminance_luxTmp.compareTo(150)<=0) {
        		System.out.println("it is too dark, i try to switch on the plug");
            	try {
            		Thread.sleep(200);
        		} catch (InterruptedException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}

            
            	  
            	// 以小组为单位 内 , (如无意外)进行按顺序打开, 如果有一个因为网络堵塞之类的原因, 则这个可以不予以处理, 进行跳过, 
            	// 所以这里我不会 因为 一发现 不成功 就break掉这个循环
        		// 开灯, 传当前 的 开关 状态过去, 因为事务需要判断 要转变成的状态 和 当前状态, 来进行调整
            	// 例如 现在很暗, 但是已经开了 就不开灯了
            	Boolean runResultTmp = null;
            	
            	ArrayList<String> aryList_str_json 		= new ArrayList<String>();
            	ArrayList<String> aryList_topicUrlToPublish = new ArrayList<String>();
            	//
            	for(int groupTmp1=0; groupTmp1 <= myDeviceGroup.size()-1; groupTmp1++) {
            		EnumDeviceTrancLogicResult group_inner_switch_trancLogicResult = null;
                	//
                	//
                	//
                	SonoffS31LiteEntity sonoffS31LiteEntityTmp 	= null;
                	PhilipsHueGo2Entity philipsHueGo2EntityTmp	= null;
                	//
                	String jsonToPublishTmp = null;
                	String topicUrlToPublishTmp = null;
                	//
                	/*
            		if(myDeviceGroup.get(groupTmp1).getClass().getName().equals(SonoffS31LiteEntity.class.getName())==true) {
            			sonoffS31LiteEntityTmp = (SonoffS31LiteEntity) myDeviceGroup.get(groupTmp1);
            			group_inner_switch_result = switcher_Sonoff31LiteToolTmp.mySwitchTransaction("ON", sonoffS31LiteEntityTmp);
            		}
            		else if(myDeviceGroup.get(groupTmp1).getClass().getName().equals(PhilipsHueGo2Entity.class.getName())==true) {
            			philipsHueGo2EntityTmp = (PhilipsHueGo2Entity) myDeviceGroup.get(groupTmp1);
            			group_inner_switch_result = light_Philips_Hue_GO2_ToolTmp.mySwitchTransaction("ON", philipsHueGo2EntityTmp);
            		}
            		
            		if(group_inner_switch_result<=0){
            			runResultTmp = Boolean.valueOf(false);
            		}
            		
            		*/
                	
                	// 如果当前元素是 sonoffS31LiteEntity 
            		if(myDeviceGroup.get(groupTmp1).getClass().getName().equals(SonoffS31LiteEntity.class.getName())==true) {
            			// 取出该元素
            			sonoffS31LiteEntityTmp = (SonoffS31LiteEntity) myDeviceGroup.get(groupTmp1);
            			//
            			//
            			// 判断是否可以 和 需要 去switch
            			group_inner_switch_trancLogicResult = switcher_Sonoff31LiteToolTmp.mySwitchTransactionLogic("ON", sonoffS31LiteEntityTmp);
            			// 判断需要 switch
            			// 然后 去得到 switch操作 所需要的json
            			if(group_inner_switch_trancLogicResult.equals(EnumDeviceTrancLogicResult.NeedToChange)) {
            				//
            				jsonToPublishTmp 	 = switcher_Sonoff31LiteToolTmp.establishPublishJson("ON");
            				topicUrlToPublishTmp = sonoffS31LiteEntityTmp.getTopicUrl_set();
            			}
            			// 判断已经是当前需要的转换的状态了, 不需要去switch了
            			else if(group_inner_switch_trancLogicResult.equals(EnumDeviceTrancLogicResult.NoNeedToChange)) {
            				// do nothing
            			}
            			else if(group_inner_switch_trancLogicResult.equals(EnumDeviceTrancLogicResult.SOMETHING_WRONG)) {
            				
            			}
            			else if(group_inner_switch_trancLogicResult.equals(EnumDeviceTrancLogicResult.DEVICE_NULL)) {
            				
            			}
            			
            		}
            		else if(myDeviceGroup.get(groupTmp1).getClass().getName().equals(PhilipsHueGo2Entity.class.getName())==true) {
            			philipsHueGo2EntityTmp = (PhilipsHueGo2Entity) myDeviceGroup.get(groupTmp1);
            			//group_inner_switch_trancLogicResult = light_Philips_Hue_GO2_ToolTmp.mySwitchTransactionLogic("ON", philipsHueGo2EntityTmp);
            			//
            			//
            			// 判断是否可以 和 需要 去switch
            			group_inner_switch_trancLogicResult = light_Philips_Hue_GO2_ToolTmp.mySwitchTransactionLogic("ON", philipsHueGo2EntityTmp);
            			// 判断需要 switch
            			// 然后 去得到 switch操作 所需要的json
            			if(group_inner_switch_trancLogicResult.equals(EnumDeviceTrancLogicResult.NeedToChange)) {
            				//
            				jsonToPublishTmp 	 = light_Philips_Hue_GO2_ToolTmp.establishPublishJson("ON");
            				topicUrlToPublishTmp = philipsHueGo2EntityTmp.getTopicUrl_set();
            			}
            			// 判断已经是当前需要的转换的状态了, 不需要去switch了
            			else if(group_inner_switch_trancLogicResult.equals(EnumDeviceTrancLogicResult.NoNeedToChange)) {
            				// do nothing
            			}
            			else if(group_inner_switch_trancLogicResult.equals(EnumDeviceTrancLogicResult.SOMETHING_WRONG)) {
            				
            			}
            			else if(group_inner_switch_trancLogicResult.equals(EnumDeviceTrancLogicResult.DEVICE_NULL)) {
            				
            			}
            		
            		
            		}
            		
            		// 判断当前这个元素 有需要去switch操作的json
            		// 分别放到各自的数组里, 到时和其他设备一起处理
            		if(jsonToPublishTmp!=null && topicUrlToPublishTmp!=null) {
            			aryList_topicUrlToPublish.add(topicUrlToPublishTmp);
            			aryList_str_json.add(jsonToPublishTmp);
            		}
            		else {
            			runResultTmp = Boolean.valueOf(false);
            		}
            		
            		
            		


            	}
            	//
            	//
            	// 如果 两个设备 其中有一个 设备 需要 去switch的话, 而另外一个设备可能出问题了, 
            	// 则先去执行 那个需要去switch的
            	// 另外一个 不成功的没关系, 后面会处理
            	if(aryList_str_json.size()>0) {
            		MyPublishTool.myPulibshWithConnectionPool(brokerIpAddress1, brokerPort1, clientId1, aryList_topicUrlToPublish, aryList_str_json, timeGapEachDeviceTmp);
            	}
            	//
            	// 注意!!!!!!
            	// 如果其中有一个或以上 没成功, 则进行重新再申请一次 感应器的状态,
            	// 因为 可能 没成功  是因为 最开始运行时, 那些状态 还是null 
            	// 而我们一开始收到的信息可能就是 Motion sensor, 此时正在处于 这个第一次 占用了callback的执行, 
            	// 因为他们 共同使用同一个 callback
            	// 所以其他subscription 是暂时无法处理 当前这个 callback的
            	if(runResultTmp!=null && runResultTmp.equals(false)==true) {
            		//motionsensor_PhilipHueOutdoorToolTmp.sendGetToNotifySubscriberToGetStatus();
            		motionsensor_PhilipHueOutdoorToolTmp.sendGetToNotifySubscriberToGetStatus(plipMotionSensorEntity);
            	}
            	
            	//
            	
        	}
    	}
    	// 如果当前 感应器表示 没有人了
    	else if(plipMotionSensorEntity.getOccupancy().equals(false)){
    		
    		System.out.println("occupancy is false now");
    		//
    		// Switcher_AutoOff3  这个线程  他自己会根据我们改的值定期检查, 然后进行操作
    		// 例如 20s 没人就关灯
    		interfaceTryAutoOff1.setMyrecorded_occupancy(0);
    	}
		return 1;
	}
	

}
