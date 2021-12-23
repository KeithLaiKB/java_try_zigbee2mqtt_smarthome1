package com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PhilipsHueGo2Entity extends AbstractSmartDeivce{
	

	//MQTT publish: topic 'zigbee2mqtt/0x0017880109e5d363', payload '{"brightness":254,"color":{"hue":27,"saturation":92,"x":0.5046,"y":0.4152},"color_mode":"color_temp","color_temp":452,"color_temp_startup":452,"linkquality":207,"state":"ON","update":{"state":"idle"}}'
	//MQTT publish: topic 'zigbee2mqtt/0x0017880109e5d363', payload '{"brightness":254,"color":{"hue":229,"saturation":86,"x":0.1887,"y":0.1796},"color_mode":"hs","color_temp":153,"color_temp_startup":422,"linkquality":204,"state":"ON","update":{"state":"idle"}}'
	
	private String 	state = null;
	
	private Integer brightness = null;
	
	private Map color = null;
	//值为"xy" 或   "hs"
	private String color_mode = null;
	private Integer 	color_temp = null;
	private Integer 	color_temp_startup = null;
	
	
	private Integer linkquality = null;


	public PhilipsHueGo2Entity() {
		
	}
	
	public PhilipsHueGo2Entity(String state, Integer brightness, Map color, String color_mode, Integer color_temp,
			Integer color_temp_startup, Integer linkquality) {
		super();
		this.state = state;
		this.brightness = brightness;
		this.color = color;
		this.color_mode = color_mode;
		this.color_temp = color_temp;
		this.color_temp_startup = color_temp_startup;
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
    	
		this.state = (String) lkhMapTmp1.get("state");;
		this.brightness = (Integer) lkhMapTmp1.get("brightness");;
		this.color = (Map) lkhMapTmp1.get("color");;
		this.color_mode = (String) lkhMapTmp1.get("color_mode");;
		this.color_temp = (Integer) lkhMapTmp1.get("color_temp");;
		this.color_temp_startup = (Integer) lkhMapTmp1.get("color_temp_startup");;
		this.linkquality = (Integer) lkhMapTmp1.get("linkquality");;
		

	}
	
	
	@Override
	public String toString() {
		return "PhilipsHueGo2Entity [state=" + state + ", brightness=" + brightness + ", color=" + color
				+ ", color_mode=" + color_mode + ", color_temp=" + color_temp + ", color_temp_startup="
				+ color_temp_startup + ", linkquality=" + linkquality + "]";
	}





	public void setState(String state) {
		this.state = state;
	}
	public String getState() {
		return state;
	}



	public void setBrightness(Integer brightness) {
		this.brightness = brightness;
	}
	public Integer getBrightness() {
		return brightness;
	}




	
	public void setColor(Map color) {
		this.color = color;
	}
	public Map getColor() {
		return color;
	}




	public void setColor_mode(String color_mode) {
		this.color_mode = color_mode;
	}
	public String getColor_mode() {
		return color_mode;
	}




	public void setColor_temp(Integer color_temp) {
		this.color_temp = color_temp;
	}
	public Integer getColor_temp() {
		return color_temp;
	}



	public void setColor_temp_startup(Integer color_temp_startup) {
		this.color_temp_startup = color_temp_startup;
	}
	public Integer getColor_temp_startup() {
		return color_temp_startup;
	}




	public void setLinkquality(Integer linkquality) {
		this.linkquality = linkquality;
	}

	public Integer getLinkquality() {
		return linkquality;
	}



	
	
}
