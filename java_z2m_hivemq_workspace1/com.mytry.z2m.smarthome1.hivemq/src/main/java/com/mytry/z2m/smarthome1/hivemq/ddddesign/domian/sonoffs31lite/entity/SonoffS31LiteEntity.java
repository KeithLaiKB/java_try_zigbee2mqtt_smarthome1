package com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.sonoffs31lite.entity;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.domian.AbstractSmartDeivceEntity;

/**
 * entity这种 anemic模型 会造成 贫血失忆症   --- martin fowler
 * blind yourself
 * 
 * 因为当你的业务起来了以后, 你的数据库实体 会越来越的 attributes,
 * 然而此时, 你把业务都放在了这个的上面, 
 * 使得这个实体看上去 没有办法直接 知道, 部分那些多出来的attribute 的作用 
 * 
 * @author laipl
 *
 */
public class SonoffS31LiteEntity extends AbstractSmartDeivceEntity {
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
