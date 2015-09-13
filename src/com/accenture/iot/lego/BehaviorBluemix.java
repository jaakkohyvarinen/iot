package com.accenture.iot.lego;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class BehaviorBluemix implements Behavior {

	private String payload = null;
	private DifferentialPilot pilot;
	
	public BehaviorBluemix (DifferentialPilot pilot) {
		this.pilot = pilot;
	}
	
	@Override
	public boolean takeControl() {
		return payload != null;
	}

	@Override
	public void action() {
		System.out.println(payload);
	}

	@Override
	public void suppress() {
		payload = null;
	}

	public void dothejob(String payload) {
		this.payload = payload;
	}

}
