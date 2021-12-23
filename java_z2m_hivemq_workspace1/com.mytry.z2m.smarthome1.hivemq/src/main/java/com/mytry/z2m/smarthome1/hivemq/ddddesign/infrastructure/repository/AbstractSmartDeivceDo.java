package com.mytry.z2m.smarthome1.hivemq.ddddesign.infrastructure.repository;

public abstract class AbstractSmartDeivceDo {
	private String ieeeAddress = null;
	private String topicUrl = null;
	private String topicUrl_get = null;
	private String topicUrl_set = null;
	
	// 例如 在一个组内 进行的顺序
	private Integer myRunSequence = null;
	

	/**
	 * 当设备多的时候可以设置, 运行的先后顺序, 比如哪个灯先亮
	 */
	Integer myRunningSequence = null;
	
	
	
	
	
	
	public Integer getMyRunSequence() {
		return myRunSequence;
	}
	public void setMyRunSequence(Integer myRunSequence) {
		this.myRunSequence = myRunSequence;
	}
	
	
	
	
	
	public void setIeeeAddress(String ieeeAddress) {
		this.ieeeAddress = ieeeAddress;
	}
	public String getIeeeAddress() {
		return ieeeAddress;
	}



	public void setTopicUrl(String topicUrl) {
		this.topicUrl = topicUrl;
	}
	public String getTopicUrl() {
		return topicUrl;
	}



	public void setTopicUrl_get(String topicUrl_get) {
		this.topicUrl_get = topicUrl_get;
	}
	public String getTopicUrl_get() {
		return topicUrl_get;
	}



	public void setTopicUrl_set(String topicUrl_set) {
		this.topicUrl_set = topicUrl_set;
	}
	public String getTopicUrl_set() {
		return topicUrl_set;
	}



	public void setMyRunningSequence(Integer myRunningSequence) {
		this.myRunningSequence = myRunningSequence;
	}
	public Integer getMyRunningSequence() {
		return myRunningSequence;
	}


	
}

