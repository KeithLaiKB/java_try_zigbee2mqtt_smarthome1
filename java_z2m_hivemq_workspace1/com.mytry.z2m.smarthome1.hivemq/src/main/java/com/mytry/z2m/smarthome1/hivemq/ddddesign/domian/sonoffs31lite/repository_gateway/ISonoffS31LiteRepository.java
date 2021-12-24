package com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.sonoffs31lite.repository_gateway;

import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.posonoffs31lite.mydo.SonoffS31LiteDo;

public interface ISonoffS31LiteRepository {
	int myAdd(SonoffS31LiteDo mydo1);
	
	
	int myUpdate(SonoffS31LiteDo mydo1);

	
	SonoffS31LiteDo myFindByIeeeAddress(String ieeeAddress);
}
