package com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.posonoffs31lite.mydo.converter;



import com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.philipshueoutdoormotionsensor.entity.PhilipsHueOutdoorMotionSensorEntity;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.sonoffs31lite.entity.SonoffS31LiteEntity;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.philipshueoutdoormotionsensor.mydo.PhilipsHueOutdoorMotionSensorDo;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.posonoffs31lite.mydo.SonoffS31LiteDo;



//https://github.com/Sayi/ddd-cargo/blob/master/ddd-cargo-maven-module-example/cargo-domain/src/main/java/com/deepoove/cargo/domain/aggregate/cargo/Cargo.java
//https://github.com/Sayi/ddd-cargo/blob/master/ddd-cargo-maven-module-example/cargo-domain/src/main/java/com/deepoove/cargo/domain/aggregate/cargo/valueobject/DeliverySpecification.java
// https://github.com/Sayi/ddd-cargo/blob/master/ddd-cargo-maven-module-example/cargo-infrastructure/src/main/java/com/deepoove/cargo/infrastructure/db/dataobject/CargoDO.java

public class SonoffS31LiteDoAndEntity{
	public SonoffS31LiteEntity myConvertDoToEntity(SonoffS31LiteDo myDo1) {
		SonoffS31LiteEntity myEntity1 = new SonoffS31LiteEntity();
		
		myEntity1.setIeeeAddress(myDo1.getIeeeAddress());
		myEntity1.setTopicUrl(myDo1.getTopicUrl());
		myEntity1.setTopicUrl_get(myDo1.getTopicUrl_get());
		myEntity1.setTopicUrl_set(myDo1.getTopicUrl_set());

		myEntity1.setState(myDo1.getState());
		myEntity1.setLinkquality(myDo1.getLinkquality());

		
		
		return myEntity1;
	}


	public SonoffS31LiteDo myConvertEntityToDo(SonoffS31LiteEntity myEntity1) {
		SonoffS31LiteDo myDo1 = new SonoffS31LiteDo();
		//
		//
		myDo1.setIeeeAddress(myEntity1.getIeeeAddress());
		myDo1.setTopicUrl(myEntity1.getTopicUrl());
		myDo1.setTopicUrl_get(myEntity1.getTopicUrl_get());
		myDo1.setTopicUrl_set(myEntity1.getTopicUrl_set());
		//
		//
		myDo1.setState(myEntity1.getState());
		myDo1.setLinkquality(myEntity1.getLinkquality());

		
		return myDo1;
	}
	
}
