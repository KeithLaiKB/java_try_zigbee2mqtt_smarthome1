package com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.sonoffs31lite.entity.converter;



import com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.sonoffs31lite.dto.SonoffS31LiteDto;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.sonoffs31lite.entity.SonoffS31LiteEntity;



//https://github.com/Sayi/ddd-cargo/blob/master/ddd-cargo-maven-module-example/cargo-domain/src/main/java/com/deepoove/cargo/domain/aggregate/cargo/Cargo.java
//https://github.com/Sayi/ddd-cargo/blob/master/ddd-cargo-maven-module-example/cargo-domain/src/main/java/com/deepoove/cargo/domain/aggregate/cargo/valueobject/DeliverySpecification.java
// https://github.com/Sayi/ddd-cargo/blob/master/ddd-cargo-maven-module-example/cargo-infrastructure/src/main/java/com/deepoove/cargo/infrastructure/db/dataobject/CargoDO.java

public class SonoffS31LiteEntityAndDto{
	public SonoffS31LiteDto myConvertEntityToDto(SonoffS31LiteEntity myEntity1) {
		SonoffS31LiteDto myDto1 = new SonoffS31LiteDto();
		//
		//
		myDto1.setIeeeAddress(myEntity1.getIeeeAddress());
		myDto1.setTopicUrl(myEntity1.getTopicUrl());
		myDto1.setTopicUrl_get(myEntity1.getTopicUrl_get());
		myDto1.setTopicUrl_set(myEntity1.getTopicUrl_set());
		//
		//
		myDto1.setState(myEntity1.getState());
		myDto1.setLinkquality(myEntity1.getLinkquality());

		
		
		return myDto1;
	}


	public SonoffS31LiteEntity myConvertDtoToEntity(SonoffS31LiteEntity myDto1) {
		SonoffS31LiteEntity myEntity1 = new SonoffS31LiteEntity();
		//
		//
		myEntity1.setIeeeAddress(myDto1.getIeeeAddress());
		myEntity1.setTopicUrl(myDto1.getTopicUrl());
		myEntity1.setTopicUrl_get(myDto1.getTopicUrl_get());
		myEntity1.setTopicUrl_set(myDto1.getTopicUrl_set());
		//
		//
		myEntity1.setState(myDto1.getState());
		myEntity1.setLinkquality(myDto1.getLinkquality());

		
		return myEntity1;
	}
	
}
