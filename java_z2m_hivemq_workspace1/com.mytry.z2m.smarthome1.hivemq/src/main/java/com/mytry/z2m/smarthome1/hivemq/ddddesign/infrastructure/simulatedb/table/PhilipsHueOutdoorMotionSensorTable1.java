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
public class PhilipsHueOutdoorMotionSensorTable1 implements IPhilipsHueOutdoorMotionSensorTable1 {
	
	// String id
	//String Uuid = null;
	ArrayList<PhilipsHueOutdoorMotionSensorDo> myElements = new ArrayList<PhilipsHueOutdoorMotionSensorDo>();
	
	
	
	
	
	private static volatile PhilipsHueOutdoorMotionSensorTable1 instance;
    

	//Thread Safe Singleton
	//ref: https://en.wikipedia.org/wiki/Double-checked_locking
	public static PhilipsHueOutdoorMotionSensorTable1 getInstanceUsingDoubleCheckLocking(){
	    if(instance == null){
	        synchronized (PhilipsHueOutdoorMotionSensorTable1.class) {
	            if(instance == null){
	                instance = new PhilipsHueOutdoorMotionSensorTable1();
	            }
	        }
	    }
	    return instance;
	}
	
	
	
	
	
	
	public int myAdd(PhilipsHueOutdoorMotionSensorDo myElement) {
		int myResult = 0;
		
		boolean resultTmp = myElements.add(myElement);
		if(resultTmp==false) {
			myResult = -1;
		}
		else if(resultTmp==false) {
			myResult = 1;
		}
		return myResult;
	}
	
	
	/**
	 * 
	 */
	public PhilipsHueOutdoorMotionSensorDo myFindByIeeeAddress(String ieeeAddress) {
		PhilipsHueOutdoorMotionSensorDo myResult = null;
		
		for(int i=0;i<=this.myElements.size()-1;i++) {
			PhilipsHueOutdoorMotionSensorDo eleTmp = myElements.get(i);
			if(eleTmp.getIeeeAddress().equals(ieeeAddress)==true) {
				myResult = eleTmp;
				// 如果用break 可以提高速度, 看你喜欢
				// break;
			}
		}
		
		return myResult;
	}
	
	
	
	public int myUpdate(PhilipsHueOutdoorMotionSensorDo myElement) {
		PhilipsHueOutdoorMotionSensorDo resultTmp = null;
		int myResult = 0;
		
		for(int i=0;i<=this.myElements.size()-1;i++) {
			PhilipsHueOutdoorMotionSensorDo eleTmp = myElements.get(i);
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
	}
	
	
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
