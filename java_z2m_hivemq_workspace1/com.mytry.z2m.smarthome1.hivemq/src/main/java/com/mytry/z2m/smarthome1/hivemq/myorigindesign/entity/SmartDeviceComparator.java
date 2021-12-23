package com.mytry.z2m.smarthome1.hivemq.myorigindesign.entity;

import java.util.Comparator;

public class SmartDeviceComparator implements Comparator<AbstractSmartDeivce> {

	@Override
	public int compare(AbstractSmartDeivce o1, AbstractSmartDeivce o2) {
		// TODO Auto-generated method stub
		if(o1.getMyRunningSequence() > o2.getMyRunningSequence() ) {
			return 1;
		}
		//else if(o1.getMyRunningSequence() < o2.getMyRunningSequence()) {
		else if(o1.getMyRunningSequence() <= o2.getMyRunningSequence()) {
			return -1;
		}
		return 0;
	}

}
