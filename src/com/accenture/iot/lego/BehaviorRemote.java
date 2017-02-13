package com.accenture.iot.lego;

import lejos.robotics.navigation.MovePilot;

public class BehaviorRemote extends BehaviorControlBase {

	private SharedIRSensor ir;

	public BehaviorRemote(SharedIRSensor ir, MovePilot pilot) {
		super (pilot);
		this.ir = ir;
	}

	@Override
	public boolean takeControl() {
		return (ir.getIRControlValue() != 0);
		
	}

	protected int getControlValue () {
		return ir.getIRControlValue();
	}

}
