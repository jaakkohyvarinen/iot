package com.accenture.iot.lego;

import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class BehaviorAWS extends BehaviorControlBase {

	private String payload = null;

	public BehaviorAWS(MovePilot pilot) {
		super (pilot);
	}

	@Override
	public boolean takeControl() {
		return payload != null;
	}

	@Override
	public void suppress() {
		super.suppress();
		payload = null;
	}

	public void dothejob(String payload) {
		this.payload = payload;
	}

	@Override
	protected int getControlValue() {
		return Integer.parseInt(payload);
	}

}
