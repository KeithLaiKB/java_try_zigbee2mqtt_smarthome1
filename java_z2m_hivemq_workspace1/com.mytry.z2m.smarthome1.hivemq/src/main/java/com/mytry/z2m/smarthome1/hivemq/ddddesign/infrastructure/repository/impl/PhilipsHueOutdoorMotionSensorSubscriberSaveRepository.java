package com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.impl;

import java.util.ArrayList;

import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.philipshueoutdoormotionsensor.mydo.PhilipsHueOutdoorMotionSensorDo;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.simulatedb.DeviceSimulateDatabase;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.simulatedb.table.IPhilipsHueOutdoorMotionSensorTable1;



public class PhilipsHueOutdoorMotionSensorSubscriberSaveRepository {
	// String id
	//String Uuid = null;
	ArrayList<PhilipsHueOutdoorMotionSensorDo> myElements = new ArrayList<PhilipsHueOutdoorMotionSensorDo>();
	
	public int myFindByIeeeAddress(PhilipsHueOutdoorMotionSensorDo mydo1) {
		DeviceSimulateDatabase deviceSimulateDatabase1 =DeviceSimulateDatabase.getInstanceUsingDoubleCheckLocking();
		
		// ------------------------- 下面的 部分应该是 在服务器中的数据 库中该做的事情, 只是我们 把服务器中的数据库 模拟了出来---------------------------
		// 获取数据库的表接口
		IPhilipsHueOutdoorMotionSensorTable1 iPhilipsHueOutdoorMotionSensorTable1 = deviceSimulateDatabase1.getiPhilipsHueOutdoorMotionSensorTable1();
		// 去查询
		int myResult= iPhilipsHueOutdoorMotionSensorTable1.myUpdate(mydo1);

		return myResult;
	}

}
