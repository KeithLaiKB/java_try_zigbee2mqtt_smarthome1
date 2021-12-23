package com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.simulatedb.table;

import java.util.ArrayList;

import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.philipshueoutdoormotionsensor.mydo.PhilipsHueOutdoorMotionSensorDo;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.simulatedb.DeviceSimulateDatabase;


/**
 * 
 * 这个table1 
 * 代表这是数据库一张  确定的数据库表 
 * 表的 名字就叫做PhilipsHueOutdoorMotionSensorTable1 
 * 
 * @author laipl
 *
 */
public interface IPhilipsHueOutdoorMotionSensorTable1 {
	
	/**
	 * 屏蔽掉 getInstance, 因为这个是给外不用的
	 * @param myElement
	 * @return
	 */
	
	int myAdd(PhilipsHueOutdoorMotionSensorDo myElement);
	
	PhilipsHueOutdoorMotionSensorDo myFindByIeeeAddress(String ieeeAddress);
	
	int myUpdate(PhilipsHueOutdoorMotionSensorDo myElement);
	
	
	// 暂时 不做这个操作, 因为这个数据库 只是 相当于一个 相近于同步 服务器中对应实体 的东西
	/*
	public int myDelete(PhilipsHueOutdoorMotionSensorPo myElement) {
		PhilipsHueOutdoorMotionSensorPo resultTmp = null;
		int myResult = 0;
		
		for(int i=0;i<=this.myElements.size()-1;i++) {
			PhilipsHueOutdoorMotionSensorPo eleTmp = myElements.get(i);
			if(eleTmp.getIeeeAddress().equals(myElement.getIeeeAddress())==true) {
				resultTmp = myElements.set(i, myElement);
			}
		}
		
		if(resultTmp==null) {
			myResult = -1;
		}
		else if(resultTmp!=null) {
			myResult = 1;
		}
		return myResult;
	}*/
}
