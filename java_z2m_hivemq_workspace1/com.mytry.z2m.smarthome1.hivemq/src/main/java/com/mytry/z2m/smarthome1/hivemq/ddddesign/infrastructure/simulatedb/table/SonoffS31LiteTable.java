package com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.simulatedb.table;

import java.util.ArrayList;

import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.posonoffs31lite.mydo.SonoffS31LiteDo;

public class SonoffS31LiteTable {
	// String id
	//String Uuid = null;
	ArrayList<SonoffS31LiteDo> myElements = new ArrayList<SonoffS31LiteDo>();
	
	public int myAdd(SonoffS31LiteDo myElement) {
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
	
}
