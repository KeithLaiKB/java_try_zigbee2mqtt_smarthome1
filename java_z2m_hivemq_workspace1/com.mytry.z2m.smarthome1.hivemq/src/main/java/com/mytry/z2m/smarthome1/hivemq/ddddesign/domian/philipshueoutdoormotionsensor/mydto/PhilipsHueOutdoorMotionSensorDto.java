package com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.philipshueoutdoormotionsensor.mydto;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.AbstractSmartDeivceDto;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.AbstractSmartDeivceEntity;

public class PhilipsHueOutdoorMotionSensorDto extends AbstractSmartDeivceDto {
	private String 	motion_sensitivity = null;
	private Boolean occupancy = null;

	
	private Double temperature = null;
	private Integer illuminance = null;
	private Integer illuminance_lux = null;
	
	
	private Boolean led_indication = null;
	private Integer occupancy_timeout = null;
	
	private Integer battery = null;
	
	
	private Integer linkquality = null;


	
	
	
	
	
	
	public PhilipsHueOutdoorMotionSensorDto() {
		
	}
	
	public PhilipsHueOutdoorMotionSensorDto(String motion_sensitivity, Boolean occupancy, Double temperature,
			Integer illuminance, Integer illuminance_lux, Boolean led_indication, Integer occupancy_timeout,
			Integer battery, Integer linkquality) {
		super();
		this.motion_sensitivity = motion_sensitivity;
		this.occupancy = occupancy;
		this.temperature = temperature;
		this.illuminance = illuminance;
		this.illuminance_lux = illuminance_lux;
		this.led_indication = led_indication;
		this.occupancy_timeout = occupancy_timeout;
		this.battery = battery;
		this.linkquality = linkquality;
	}

	
	
	public void setAttributeFromJson(String str_json) {
		ObjectMapper mapperTmp = new ObjectMapper();
    	LinkedHashMap<String,Object> lkhMapTmp1 = null;
    	TypeReference<LinkedHashMap<String,Object>> tpRfTmp1  = new TypeReference<LinkedHashMap<String,Object>>() {};
    	//
    	try {
    		lkhMapTmp1 = mapperTmp.readValue(str_json, tpRfTmp1);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
		this.motion_sensitivity	 	= (String) lkhMapTmp1.get("motion_sensitivity");
		this.occupancy 				= (Boolean) lkhMapTmp1.get("occupancy");
		this.temperature 			= (Double) lkhMapTmp1.get("temperature");
		this.illuminance 			= (Integer) lkhMapTmp1.get("illuminance");
		this.illuminance_lux 		= (Integer) lkhMapTmp1.get("illuminance_lux");
		this.led_indication 		= (Boolean) lkhMapTmp1.get("led_indication");
		this.occupancy_timeout 		= (Integer) lkhMapTmp1.get("occupancy_timeout");
		this.battery 				= (Integer) lkhMapTmp1.get("battery");
		this.linkquality 			= (Integer) lkhMapTmp1.get("linkquality");
	}
	
	
	
	
	
	@Override
	public String toString() {
		return "PhilipsHueOutdoorMotionSensorDto [motion_sensitivity=" + motion_sensitivity + ", occupancy="
				+ occupancy + ", temperature=" + temperature + ", illuminance=" + illuminance + ", illuminance_lux="
				+ illuminance_lux + ", led_indication=" + led_indication + ", occupancy_timeout=" + occupancy_timeout
				+ ", battery=" + battery + ", linkquality=" + linkquality + "]";
	}

	
	
	public void setMotion_sensitivity(String motion_sensitivity) {
		this.motion_sensitivity = motion_sensitivity;
	}
	public String getMotion_sensitivity() {
		return motion_sensitivity;
	}




	
	public void setOccupancy(Boolean occupancy) {
		this.occupancy = occupancy;
	}
	public Boolean getOccupancy() {
		return occupancy;
	}



	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Double getTemperature() {
		return temperature;
	}




	public void setIlluminance(Integer illuminance) {
		this.illuminance = illuminance;
	}
	public Integer getIlluminance() {
		return illuminance;
	}




	public void setIlluminance_lux(Integer illuminance_lux) {
		this.illuminance_lux = illuminance_lux;
	}
	public Integer getIlluminance_lux() {
		return illuminance_lux;
	}




	public void setLed_indication(Boolean led_indication) {
		this.led_indication = led_indication;
	}
	public Boolean getLed_indication() {
		return led_indication;
	}




	public void setOccupancy_timeout(Integer occupancy_timeout) {
		this.occupancy_timeout = occupancy_timeout;
	}
	public Integer getOccupancy_timeout() {
		return occupancy_timeout;
	}




	public void setBattery(Integer battery) {
		this.battery = battery;
	}
	public Integer getBattery() {
		return battery;
	}



	public void setLinkquality(Integer linkquality) {
		this.linkquality = linkquality;
	}
	public Integer getLinkquality() {
		return linkquality;
	}



	
}
