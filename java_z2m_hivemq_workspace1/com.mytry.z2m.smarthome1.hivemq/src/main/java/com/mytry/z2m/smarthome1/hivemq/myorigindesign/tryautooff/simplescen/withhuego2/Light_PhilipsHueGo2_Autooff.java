package com.mytry.z2m.smarthome1.hivemq.myorigindesign.tryautooff.simplescen.withhuego2;

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
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity.PhilipsHueGo2Entity;
import com.mytry.z2m.smarthome1.hivemq.myorigindesign.tool.Light_Philips_Hue_GO2_Tool;

/**
 * 
 * 
 * <p>
 * 							description:																			</br>	
 * &emsp;						use different value to publish message each time 									</br>	
 * 
 * 用线程控制 自动关灯																													</br>
 * 利用 philips hue outdoor motion sensor 的状态 来控制
 * 		philips hue go 2 开灯关灯
 *
 * @author laipl
 *
 */
public class Light_PhilipsHueGo2_Autooff implements Runnable{
	
	// -1 nothing, 0 false, 1 true
	int myrecorded_occupancy = -1;
	// -1L 代表 初始值, 是不可计算的状态
	long occupance_noone_starttime = -1L;
	//
	PhilipsHueGo2Entity philipsHueGo2Entity1 = null;
	//
	// 20s 后无人存在, 则进行关灯
	long noone_remainTimeLimit = 20*1000L;
	//long noone_remainTimeLimit = 5*60*1000L;

	public Light_PhilipsHueGo2_Autooff() {

	}


	
	
	public void setPhilipsHueGo2Entity1(PhilipsHueGo2Entity philipsHueGo2Entity1) {
		this.philipsHueGo2Entity1 = philipsHueGo2Entity1;
	}
	public PhilipsHueGo2Entity getPhilipsHueGo2Entity1() {
		return philipsHueGo2Entity1;
	}




	public void setMyrecorded_occupancy(int myrecorded_occupancy) {
		this.myrecorded_occupancy = myrecorded_occupancy;
	}
	public int getMyrecorded_occupancy() {
		return myrecorded_occupancy;
	}

	
	
	public void setOccupance_noone_starttime(long occupance_noone_starttime) {
		this.occupance_noone_starttime = occupance_noone_starttime;
	}

	public long getOccupance_noone_starttime() {
		return occupance_noone_starttime;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		//
		while (true) {
			//System.out.println("auttttoo running");
			// 有人在运动
			if(myrecorded_occupancy == 1) {
				// -1L 代表 此时  不开始记录  无人状态时间
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
					if(noone_remainTimeTmp>noone_remainTimeLimit) {
						Light_Philips_Hue_GO2_Tool tryStartTmp= new Light_Philips_Hue_GO2_Tool();
	            		//关灯
	                	System.out.println("no one. then turn off light");
	                	int actResultTmp1 = tryStartTmp.mySwitchTransaction("OFF", this.getPhilipsHueGo2Entity1());
	                	//
	                	if(actResultTmp1==1) {
		                	//关灯以后
		                	//此时灯已经关闭了, 所以暂时不需要再去 记录 和 计算 occupance_noone_starttime
		                	occupance_noone_starttime =-1L;
	                	}

					}
				}
			}
			else if(myrecorded_occupancy == -1) {
				// nothing 因为此时有可能是初始值, 或出现问题

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
