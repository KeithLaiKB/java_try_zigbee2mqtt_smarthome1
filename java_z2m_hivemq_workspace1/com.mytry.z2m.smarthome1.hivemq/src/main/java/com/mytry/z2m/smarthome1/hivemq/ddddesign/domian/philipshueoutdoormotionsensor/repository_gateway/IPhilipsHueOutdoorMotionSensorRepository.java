package com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.philipshueoutdoormotionsensor.repository_gateway;

import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.philipshueoutdoormotionsensor.mydo.PhilipsHueOutdoorMotionSensorDo;

public interface IPhilipsHueOutdoorMotionSensorRepository {
	int myAdd(PhilipsHueOutdoorMotionSensorDo mydo1);
	
	
	int myUpdate(PhilipsHueOutdoorMotionSensorDo mydo1);

	
	PhilipsHueOutdoorMotionSensorDo myFindByIeeeAddress(String ieeeAddress);
}
