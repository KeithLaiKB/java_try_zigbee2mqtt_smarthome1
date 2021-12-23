package com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.philipshueoutdoormotionsensor.mydo.convertor;



import com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.philipshueoutdoormotionsensor.entity.PhilipsHueOutdoorMotionSensorEntity;

import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.philipshueoutdoormotionsensor.mydo.PhilipsHueOutdoorMotionSensorDo;



//https://github.com/Sayi/ddd-cargo/blob/master/ddd-cargo-maven-module-example/cargo-domain/src/main/java/com/deepoove/cargo/domain/aggregate/cargo/Cargo.java
//https://github.com/Sayi/ddd-cargo/blob/master/ddd-cargo-maven-module-example/cargo-domain/src/main/java/com/deepoove/cargo/domain/aggregate/cargo/valueobject/DeliverySpecification.java
// https://github.com/Sayi/ddd-cargo/blob/master/ddd-cargo-maven-module-example/cargo-infrastructure/src/main/java/com/deepoove/cargo/infrastructure/db/dataobject/CargoDO.java

public class PhilipsHueOutdoorMotionSensorDoAndEntity{
	public PhilipsHueOutdoorMotionSensorEntity myConvertDoToEntity(PhilipsHueOutdoorMotionSensorDo myDo1) {
		PhilipsHueOutdoorMotionSensorEntity myEntity1 = new PhilipsHueOutdoorMotionSensorEntity();
		
		myEntity1.setIeeeAddress(myDo1.getIeeeAddress());
		myEntity1.setTopicUrl(myDo1.getTopicUrl());
		myEntity1.setTopicUrl_get(myDo1.getTopicUrl_get());
		myEntity1.setTopicUrl_set(myDo1.getTopicUrl_set());

		myEntity1.setMotion_sensitivity(myDo1.getMotion_sensitivity());
		myEntity1.setOccupancy(myDo1.getOccupancy());
		myEntity1.setTemperature(myDo1.getTemperature());
		myEntity1.setIlluminance(myDo1.getIlluminance());
		myEntity1.setIlluminance_lux(myDo1.getIlluminance_lux());
		myEntity1.setLed_indication(myDo1.getLed_indication());
		myEntity1.setOccupancy_timeout(myDo1.getOccupancy_timeout());
		myEntity1.setBattery(myDo1.getBattery());
		myEntity1.setLinkquality(myDo1.getLinkquality());
		
		
		return myEntity1;
	}


	public PhilipsHueOutdoorMotionSensorEntity myConvertEntityToDo(PhilipsHueOutdoorMotionSensorDo myDo1) {
		PhilipsHueOutdoorMotionSensorEntity myEntity1 = new PhilipsHueOutdoorMotionSensorEntity();
		
		myEntity1.setIeeeAddress(myDo1.getIeeeAddress());
		myEntity1.setTopicUrl(myDo1.getTopicUrl());
		myEntity1.setTopicUrl_get(myDo1.getTopicUrl_get());
		myEntity1.setTopicUrl_set(myDo1.getTopicUrl_set());

		myEntity1.setMotion_sensitivity(myDo1.getMotion_sensitivity());
		myEntity1.setOccupancy(myDo1.getOccupancy());
		myEntity1.setTemperature(myDo1.getTemperature());
		myEntity1.setIlluminance(myDo1.getIlluminance());
		myEntity1.setIlluminance_lux(myDo1.getIlluminance_lux());
		myEntity1.setLed_indication(myDo1.getLed_indication());
		myEntity1.setOccupancy_timeout(myDo1.getOccupancy_timeout());
		myEntity1.setBattery(myDo1.getBattery());
		myEntity1.setLinkquality(myDo1.getLinkquality());
		
		
		return myEntity1;
	}
	
}
