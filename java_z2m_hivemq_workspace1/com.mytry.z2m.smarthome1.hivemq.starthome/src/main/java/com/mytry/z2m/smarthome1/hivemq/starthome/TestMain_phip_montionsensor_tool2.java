package com.mytry.z2m.smarthome1.hivemq.starthome;

import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.internal.mqtt.MqttRxClient;
import com.hivemq.client.internal.mqtt.message.publish.MqttPublishBuilder;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilder;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilderBase;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;
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
public class TestMain_phip_montionsensor_tool2 implements Runnable{
	
	// -1 nothing, 0 false, 1 true
	static int myrecorded_occupancy = -1;
	// -1L 代表 初始值, 是不可计算的状态
	static long occupance_noone_starttime = -1L;

	public TestMain_phip_montionsensor_tool2() {

	}


	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//
		while (true) {
			//System.out.println("auttttoo running");
			// 
			if(myrecorded_occupancy == 1) {
				// -1L 代表 此时 无人状态时间 不开始记录
				occupance_noone_starttime = -1L;
			}
			else if(myrecorded_occupancy == 0) {
				// 如果刚才 无人状态 是true, 现在改变了
				if(occupance_noone_starttime==-1L){
					// 则记录当前 无人状态的起始时间
					occupance_noone_starttime = System.nanoTime();
				}
				// 如果刚才 无人状态 是false, 
				else if(occupance_noone_starttime !=-1L){
					// 则记录当前 无人状态的起始时间
					long nowTime = System.nanoTime();
					
					long noone_remainTimeTmp = TimeUnit.NANOSECONDS.toMillis(nowTime-occupance_noone_starttime);
					//超过 20s 则关灯
					if(noone_remainTimeTmp>5*1000L) {
	                	TestMain_phip_montionsensor_tool tryStartTmp= new TestMain_phip_montionsensor_tool();
	            		//开灯
	                	System.out.println("no one. then turn off light");
	                	tryStartTmp.myStart("OFF");
	                	//
	                	//开完灯以后
	                	//此时灯已经关闭了, 所以暂时不需要再去 记录 和 计算 occupance_noone_starttime
	                	occupance_noone_starttime =-1L;
					}
				}
			}
			else if(myrecorded_occupancy == -1) {
				// nothing

			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
