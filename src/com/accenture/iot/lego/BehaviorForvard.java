package com.accenture.iot.lego;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class BehaviorForvard implements Behavior {
	
	private DifferentialPilot pilot = null;

	public BehaviorForvard (DifferentialPilot pilot) {
		this.pilot = pilot;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		if (!pilot.isMoving()){
			pilot.forward();
		}

	}

	@Override
	public void suppress() {
		pilot.stop();

	}

}
