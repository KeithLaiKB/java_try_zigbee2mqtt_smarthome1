package com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.impl;

import java.util.ArrayList;

import com.mytry.z2m.smarthome1.hivemq.ddddesign.applicationlayer.controller.SonoffS31Lite_PhilipsHueGo2_Group1_Controller;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.philipshueoutdoormotionsensor.repository_gateway.IPhilipsHueOutdoorMotionSensorRepository;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.philipshueoutdoormotionsensor.mydo.PhilipsHueOutdoorMotionSensorDo;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.simulatedb.DeviceSimulateDatabase;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.simulatedb.table.IPhilipsHueOutdoorMotionSensorTable1;



public class PhilipsHueOutdoorMotionSensorRepository implements IPhilipsHueOutdoorMotionSensorRepository {

	private static  volatile PhilipsHueOutdoorMotionSensorRepository instance;
    

	//Thread Safe Singleton
	//ref: https://en.wikipedia.org/wiki/Double-checked_locking
	public static PhilipsHueOutdoorMotionSensorRepository getInstanceUsingDoubleCheckLocking(){
	    if(instance == null){
	        synchronized (PhilipsHueOutdoorMotionSensorRepository.class) {
	            if(instance == null){
	                instance = new PhilipsHueOutdoorMotionSensorRepository();
	            }
	        }
	    }
	    return instance;
	}
	
	
	
	
	
	public int myAdd(PhilipsHueOutdoorMotionSensorDo mydo1) {
		DeviceSimulateDatabase deviceSimulateDatabase1 =DeviceSimulateDatabase.getInstanceUsingDoubleCheckLocking();
		
		// ------------------------- 下面的 部分应该是 在服务器中的数据 库中该做的事情, 只是我们 把服务器中的数据库 模拟了出来---------------------------
		// 获取数据库的表接口
		IPhilipsHueOutdoorMotionSensorTable1 iPhilipsHueOutdoorMotionSensorTable1 = deviceSimulateDatabase1.getiPhilipsHueOutdoorMotionSensorTable1();
		// 去查询
		int myResult= iPhilipsHueOutdoorMotionSensorTable1.myAdd(mydo1);

		return myResult;
	}
	
	
	public int myUpdate(PhilipsHueOutdoorMotionSensorDo mydo1) {
		DeviceSimulateDatabase deviceSimulateDatabase1 =DeviceSimulateDatabase.getInstanceUsingDoubleCheckLocking();
		
		// ------------------------- 下面的 部分应该是 在服务器中的数据 库中该做的事情, 只是我们 把服务器中的数据库 模拟了出来---------------------------
		// 获取数据库的表接口
		IPhilipsHueOutdoorMotionSensorTable1 iPhilipsHueOutdoorMotionSensorTable1 = deviceSimulateDatabase1.getiPhilipsHueOutdoorMotionSensorTable1();
		// 去查询
		int myResult= iPhilipsHueOutdoorMotionSensorTable1.myUpdate(mydo1);

		return myResult;
	}

	
	public PhilipsHueOutdoorMotionSensorDo myFindByIeeeAddress(String ieeeAddress) {
		DeviceSimulateDatabase deviceSimulateDatabase1 =DeviceSimulateDatabase.getInstanceUsingDoubleCheckLocking();
		
		// ------------------------- 下面的 部分应该是 在服务器中的数据 库中该做的事情, 只是我们 把服务器中的数据库 模拟了出来---------------------------
		// 获取数据库的表接口
		IPhilipsHueOutdoorMotionSensorTable1 iPhilipsHueOutdoorMotionSensorTable1 = deviceSimulateDatabase1.getiPhilipsHueOutdoorMotionSensorTable1();
		// 去查询
		PhilipsHueOutdoorMotionSensorDo myDo1= iPhilipsHueOutdoorMotionSensorTable1.myFindByIeeeAddress(ieeeAddress);

		return myDo1;
	}
}
