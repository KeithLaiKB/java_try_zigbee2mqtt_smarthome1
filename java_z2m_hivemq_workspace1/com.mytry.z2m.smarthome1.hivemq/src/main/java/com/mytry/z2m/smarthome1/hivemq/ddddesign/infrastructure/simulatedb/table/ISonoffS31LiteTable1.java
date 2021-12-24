package com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.simulatedb.table;

import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.posonoffs31lite.mydo.SonoffS31LiteDo;

public interface ISonoffS31LiteTable1 {

	/**
	 * 屏蔽掉 getInstance, 因为这个是给外不用的
	 * @param myElement
	 * @return
	 */
	
	int myAdd(SonoffS31LiteDo myElement);
	
	SonoffS31LiteDo myFindByIeeeAddress(String ieeeAddress);
	
	int myUpdate(SonoffS31LiteDo myElement);
}
