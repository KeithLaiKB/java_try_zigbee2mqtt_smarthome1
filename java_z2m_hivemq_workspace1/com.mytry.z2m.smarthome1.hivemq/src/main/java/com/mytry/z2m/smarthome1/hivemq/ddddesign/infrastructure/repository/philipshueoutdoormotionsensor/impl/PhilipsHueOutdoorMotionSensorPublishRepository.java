package com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.philipshueoutdoormotionsensor.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.db.MyPublishTool;
import com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository.philipshueoutdoormotionsensor.mydo.PhilipsHueOutdoorMotionSensorDo;





public class PhilipsHueOutdoorMotionSensorPublishRepository {
	// String id
	//String Uuid = null;
	ArrayList<PhilipsHueOutdoorMotionSensorDo> myElements = new ArrayList<PhilipsHueOutdoorMotionSensorDo>();
	
	
	// "zigbee2mqtt/0x00124b00250c256f/set"
	public int myPublish_set(String brokerIpAddress, int brokerPort, String clientId, PhilipsHueOutdoorMotionSensorDo myDo1, Map<String,Object> publishContentMap1, long timeGapEachDevice) {
		//
		ArrayList<String> aryList_topicUrlToPublish =new ArrayList<String>();
		ArrayList<String> aryList_str_json =new ArrayList<String>();
		//
		//
		//
		// 添加 topicUrl 到存url的数组
		aryList_topicUrlToPublish.add(myDo1.getTopicUrl_set());
		//
		//
    	ObjectMapper mapperTmp = new ObjectMapper();
    	String str_content_tmp = null;
		try {
			str_content_tmp = mapperTmp.writeValueAsString(publishContentMap1);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//
		// 添加 json 到存json的数组
		aryList_str_json.add(str_content_tmp);
		//
		MyPublishTool.myPulibshWithConnectionPool(brokerIpAddress, brokerPort, clientId, aryList_topicUrlToPublish, aryList_str_json, timeGapEachDevice);
		return 1;
	}
	
	// 和上面唯一不同的也就是  myDo1.getTopicUrl_get() 
	// "zigbee2mqtt/0x00124b00250c256f/get"
	public int myPublish_get(String brokerIpAddress, int brokerPort, String clientId, PhilipsHueOutdoorMotionSensorDo myDo1, Map<String,Object> publishContentMap1, long timeGapEachDevice) {
		//
		ArrayList<String> aryList_topicUrlToPublish =new ArrayList<String>();
		ArrayList<String> aryList_str_json =new ArrayList<String>();
		//
		//
		//
		// 添加 topicUrl 到存url的数组
		aryList_topicUrlToPublish.add(myDo1.getTopicUrl_get());
		//
		//
    	ObjectMapper mapperTmp = new ObjectMapper();
    	String str_content_tmp = null;
		try {
			str_content_tmp = mapperTmp.writeValueAsString(publishContentMap1);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//
		// 添加 json 到存json的数组
		aryList_str_json.add(str_content_tmp);
		//
		MyPublishTool.myPulibshWithConnectionPool(brokerIpAddress, brokerPort, clientId, aryList_topicUrlToPublish, aryList_str_json, timeGapEachDevice);
		return 1;
	}

}
