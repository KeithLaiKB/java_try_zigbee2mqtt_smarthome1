package com.mytry.z2m.smarthome1.hivemq.entity;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SonoffS31LiteEntity extends AbstractSmartDeivce {
	private String 	state = null;
	private Integer linkquality = null;
	
	public SonoffS31LiteEntity() {
		
	}
	public SonoffS31LiteEntity(String state, Integer linkquality) {
		super();
		this.state = state;
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
    	
    	
		this.state	 					= (String) lkhMapTmp1.get("state");
		this.linkquality 				= (Integer) lkhMapTmp1.get("linkquality");

	}
	
	
	
	
	
	@Override
	public String toString() {
		return "SonoffS31LiteEntity [state=" + state + ", linkquality=" + linkquality + "]";
	}
	
	public String getState() {
		return state;
	}
	public Integer getLinkquality() {
		return linkquality;
	}

	
	public void setState(String state) {
		this.state = state;
	}
	public void setLinkquality(Integer linkquality) {
		this.linkquality = linkquality;
	}

}
