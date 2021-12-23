package com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.posonoffs31lite.mydo;


import java.util.LinkedHashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.AbstractSmartDeivceDo;

/**
 * 这只是在模仿, 因为我们没有 用什么hibernate 之类的, 所以暂时没有什么持久化的对象(Persistent Object)
 * 
 * @author laipl
 *
 */
public class SonoffS31LiteDo extends AbstractSmartDeivceDo{
	private String 	state = null;
	private Integer linkquality = null;
	
	public SonoffS31LiteDo() {
		
	}
	public SonoffS31LiteDo(String state, Integer linkquality) {
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
