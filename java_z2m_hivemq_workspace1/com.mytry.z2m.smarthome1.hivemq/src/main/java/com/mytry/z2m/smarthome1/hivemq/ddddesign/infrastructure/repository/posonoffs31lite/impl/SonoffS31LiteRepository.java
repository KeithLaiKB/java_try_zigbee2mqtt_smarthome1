package com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.posonoffs31lite.impl;

import java.util.ArrayList;

import com.mytry.z2m.smarthome1.hivemq.ddddesign.applicationlayer.controller.SonoffS31Lite_PhilipsHueGo2_Group1_Controller;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.philipshueoutdoormotionsensor.repository_gateway.IPhilipsHueOutdoorMotionSensorRepository;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.sonoffs31lite.repository_gateway.ISonoffS31LiteRepository;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.philipshueoutdoormotionsensor.mydo.PhilipsHueOutdoorMotionSensorDo;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.posonoffs31lite.mydo.SonoffS31LiteDo;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.simulatedb.DeviceSimulateDatabase;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.simulatedb.table.IPhilipsHueOutdoorMotionSensorTable1;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.simulatedb.table.ISonoffS31LiteTable1;



public class SonoffS31LiteRepository implements ISonoffS31LiteRepository {

	private static  volatile SonoffS31LiteRepository instance;
    

	//Thread Safe Singleton
	//ref: https://en.wikipedia.org/wiki/Double-checked_locking
	public static SonoffS31LiteRepository getInstanceUsingDoubleCheckLocking(){
	    if(instance == null){
	        synchronized (SonoffS31LiteRepository.class) {
	            if(instance == null){
	                instance = new SonoffS31LiteRepository();
	            }
	        }
	    }
	    return instance;
	}
	
	
	
	
	
	public int myAdd(SonoffS31LiteDo mydo1) {
		DeviceSimulateDatabase deviceSimulateDatabase1 =DeviceSimulateDatabase.getInstanceUsingDoubleCheckLocking();
		
		// ------------------------- 下面的 部分应该是 在服务器中的数据 库中该做的事情, 只是我们 把服务器中的数据库 模拟了出来---------------------------
		// 获取数据库的表接口
		ISonoffS31LiteTable1 iSonoffS31LiteTable1 = deviceSimulateDatabase1.getISonoffS31LiteTable1();
		// 去查询
		int myResult= iSonoffS31LiteTable1.myAdd(mydo1);

		return myResult;
	}
	
	
	public int myUpdate(SonoffS31LiteDo mydo1) {
		DeviceSimulateDatabase deviceSimulateDatabase1 =DeviceSimulateDatabase.getInstanceUsingDoubleCheckLocking();
		
		// ------------------------- 下面的 部分应该是 在服务器中的数据 库中该做的事情, 只是我们 把服务器中的数据库 模拟了出来---------------------------
		// 获取数据库的表接口
		ISonoffS31LiteTable1 iSonoffS31LiteTable1 = deviceSimulateDatabase1.getISonoffS31LiteTable1();
		// 去查询
		int myResult= iSonoffS31LiteTable1.myUpdate(mydo1);

		return myResult;
	}

	
	public SonoffS31LiteDo myFindByIeeeAddress(String ieeeAddress) {
		DeviceSimulateDatabase deviceSimulateDatabase1 =DeviceSimulateDatabase.getInstanceUsingDoubleCheckLocking();
		
		// ------------------------- 下面的 部分应该是 在服务器中的数据 库中该做的事情, 只是我们 把服务器中的数据库 模拟了出来---------------------------
		// 获取数据库的表接口
		ISonoffS31LiteTable1 iSonoffS31LiteTable1 = deviceSimulateDatabase1.getISonoffS31LiteTable1();
		// 去查询
		SonoffS31LiteDo myDo1= iSonoffS31LiteTable1.myFindByIeeeAddress(ieeeAddress);

		return myDo1;
	}
}
