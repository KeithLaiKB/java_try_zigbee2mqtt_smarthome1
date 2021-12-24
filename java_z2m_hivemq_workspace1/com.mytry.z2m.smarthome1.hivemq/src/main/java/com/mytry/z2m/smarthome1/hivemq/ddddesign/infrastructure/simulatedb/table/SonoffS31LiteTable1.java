package com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.simulatedb.table;

import java.util.ArrayList;

import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.posonoffs31lite.mydo.SonoffS31LiteDo;

public class SonoffS31LiteTable1 implements ISonoffS31LiteTable1{
	// String id
	//String Uuid = null;
	ArrayList<SonoffS31LiteDo> myElements = new ArrayList<SonoffS31LiteDo>();
	
	
	
	private static volatile SonoffS31LiteTable1 instance;
    

	private SonoffS31LiteTable1() {
		
	}
	
	
	//Thread Safe Singleton
	//ref: https://en.wikipedia.org/wiki/Double-checked_locking
	public static SonoffS31LiteTable1 getInstanceUsingDoubleCheckLocking(){
	    if(instance == null){
	        synchronized (SonoffS31LiteTable1.class) {
	            if(instance == null){
	                instance = new SonoffS31LiteTable1();
	            }
	        }
	    }
	    return instance;
	}
	
	
	public int myAdd(SonoffS31LiteDo myElement) {
		int myResult = 0;
		
		boolean resultTmp = myElements.add(myElement);
		if(resultTmp==false) {
			myResult = -1;
		}
		else if(resultTmp==true) {
			myResult = 1;
		}
		return myResult;
	}
	
	
	/**
	 * 
	 */
	public SonoffS31LiteDo myFindByIeeeAddress(String ieeeAddress) {
		SonoffS31LiteDo myResult = null;
		
		for(int i=0;i<=this.myElements.size()-1;i++) {
			SonoffS31LiteDo eleTmp = myElements.get(i);
			if(eleTmp.getIeeeAddress().equals(ieeeAddress)==true) {
				myResult = eleTmp;
				// 如果用break 可以提高速度, 看你喜欢
				// break;
			}
		}
		
		return myResult;
	}
	
	
	
	public int myUpdate(SonoffS31LiteDo myElement) {
		SonoffS31LiteDo resultTmp = null;
		int myResult = 0;
		
		for(int i=0;i<=this.myElements.size()-1;i++) {
			SonoffS31LiteDo eleTmp = myElements.get(i);
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
	
}
