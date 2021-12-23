package com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.simulatedb;

import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.simulatedb.table.IPhilipsHueOutdoorMotionSensorTable1;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.simulatedb.table.PhilipsHueOutdoorMotionSensorTable1;

/**
 * 因为 Iot的 mqtt 是一个 发布中 和 订阅者的模式, 我们可以一直打开订阅, 每次 mqtt 发送来的 消息,
 * 我们可以就当他 近似于 同步的状态
 * 那么 我们 每次 服务 去找状态的时候  可以直接 去找我们模拟的数据库中的 状态就可以了
 * 
 * 进程1  开关操作:	request		---setSwitchState---> controller ---setSwitchState--->  service ---setSwitchState---> mqtt_broker
 * 进程2  订阅:    	mqtt_broker 	---publish----------> 记录到 我们 模拟的 数据库中
 * ++++++++++++++++++ 下面这个操作 是我的非主流操作, 因为 订阅者 获得的信息 已经在我当前的 模拟的数据库中了, 直接取就可以了, 没必要在发送请求到 broker了 ++++++++++++++++++ 
 * 进程3  获取:    	request		---getSwitchState---> controller ---getSwitchState--->  service ---getSwitchState---> repository ---getSwitchState---> simulateDatebase 
 * 
 * 
 * dataflow:
 * publishRepository 	------> mqtt_broker
 * 
 * mqtt_broker       	------> subscriberWriteRepository ------> db 
 * 								(因为是订阅, 所以服务器一发布, 注意 这个订阅信息是自动收到的,
 * 									这并不是 我们手动 申请 写数据写到db 的)
 * 
 * readRepository    	------> db
 *                   	<------ db 
 * 
 * +++++++++++++++++++++++++++++++++++++++++ 
 * 当然你可以帮上面 的流程 放在一起看, 来对比
 * publishRepository 	------> mqtt_broker ----过一段时间 自动----> subscriberWriteRepository ------> db 
 * 
 * 
 * readRepository    	------> db
 *                   	<------ db
 * 
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * 正常的spring 应当是
 * writeRepository 		------> db
 * 
 * readRepository   	------> db
 *                   	<------ db
 * 
 * 
 * @author laipl
 *
 */
public class DeviceSimulateDatabase {
	private static volatile DeviceSimulateDatabase instance;
	
	//Thread Safe Singleton
	//ref: https://en.wikipedia.org/wiki/Double-checked_locking
	public static DeviceSimulateDatabase getInstanceUsingDoubleCheckLocking(){
	    if(instance == null){
	        synchronized (DeviceSimulateDatabase.class) {
	            if(instance == null){
	                instance = new DeviceSimulateDatabase();
	            }
	        }
	    }
	    return instance;
	}

	
	
	
	public IPhilipsHueOutdoorMotionSensorTable1 getiPhilipsHueOutdoorMotionSensorTable1() {
		IPhilipsHueOutdoorMotionSensorTable1 myResult = PhilipsHueOutdoorMotionSensorTable1.getInstanceUsingDoubleCheckLocking();
		return myResult;
	}

	
	
	
	
	
}
