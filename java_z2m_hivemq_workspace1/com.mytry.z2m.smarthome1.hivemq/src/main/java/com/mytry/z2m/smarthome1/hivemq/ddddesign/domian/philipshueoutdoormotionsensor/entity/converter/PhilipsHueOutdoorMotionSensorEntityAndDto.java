package com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.philipshueoutdoormotionsensor.entity.converter;



import com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.philipshueoutdoormotionsensor.entity.PhilipsHueOutdoorMotionSensorEntity;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.philipshueoutdoormotionsensor.mydto.PhilipsHueOutdoorMotionSensorDto;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.philipshueoutdoormotionsensor.mydo.PhilipsHueOutdoorMotionSensorDo;



//https://github.com/Sayi/ddd-cargo/blob/master/ddd-cargo-maven-module-example/cargo-domain/src/main/java/com/deepoove/cargo/domain/aggregate/cargo/Cargo.java
//https://github.com/Sayi/ddd-cargo/blob/master/ddd-cargo-maven-module-example/cargo-domain/src/main/java/com/deepoove/cargo/domain/aggregate/cargo/valueobject/DeliverySpecification.java
// https://github.com/Sayi/ddd-cargo/blob/master/ddd-cargo-maven-module-example/cargo-infrastructure/src/main/java/com/deepoove/cargo/infrastructure/db/dataobject/CargoDO.java

public class PhilipsHueOutdoorMotionSensorEntityAndDto{
	public PhilipsHueOutdoorMotionSensorDto myConvertEntityToDto(PhilipsHueOutdoorMotionSensorEntity myEntity1) {
		PhilipsHueOutdoorMotionSensorDto myDto1 = new PhilipsHueOutdoorMotionSensorDto();
		
		myDto1.setIeeeAddress(myEntity1.getIeeeAddress());
		myDto1.setTopicUrl(myEntity1.getTopicUrl());
		myDto1.setTopicUrl_get(myEntity1.getTopicUrl_get());
		myDto1.setTopicUrl_set(myEntity1.getTopicUrl_set());
		//
		//
		myDto1.setMotion_sensitivity(myEntity1.getMotion_sensitivity());
		myDto1.setOccupancy(myEntity1.getOccupancy());
		myDto1.setTemperature(myEntity1.getTemperature());
		myDto1.setIlluminance(myEntity1.getIlluminance());
		myDto1.setIlluminance_lux(myEntity1.getIlluminance_lux());
		myDto1.setLed_indication(myEntity1.getLed_indication());
		myDto1.setOccupancy_timeout(myEntity1.getOccupancy_timeout());
		myDto1.setBattery(myEntity1.getBattery());
		myDto1.setLinkquality(myEntity1.getLinkquality());
		
		
		return myDto1;
	}


	public PhilipsHueOutdoorMotionSensorEntity myConvertDtoToEntity(PhilipsHueOutdoorMotionSensorDto myDto1) {
		PhilipsHueOutdoorMotionSensorEntity myEntity1 = new PhilipsHueOutdoorMotionSensorEntity();
		
		myEntity1.setIeeeAddress(myDto1.getIeeeAddress());
		myEntity1.setTopicUrl(myDto1.getTopicUrl());
		myEntity1.setTopicUrl_get(myDto1.getTopicUrl_get());
		myEntity1.setTopicUrl_set(myDto1.getTopicUrl_set());
		//
		//
		myEntity1.setMotion_sensitivity(myDto1.getMotion_sensitivity());
		myEntity1.setOccupancy(myDto1.getOccupancy());
		myEntity1.setTemperature(myDto1.getTemperature());
		myEntity1.setIlluminance(myDto1.getIlluminance());
		myEntity1.setIlluminance_lux(myDto1.getIlluminance_lux());
		myEntity1.setLed_indication(myDto1.getLed_indication());
		myEntity1.setOccupancy_timeout(myDto1.getOccupancy_timeout());
		myEntity1.setBattery(myDto1.getBattery());
		myEntity1.setLinkquality(myDto1.getLinkquality());
		
		
		return myEntity1;
	}
	
}
