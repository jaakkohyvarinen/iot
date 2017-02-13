package com.accenture.iot.lego;

import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class BehaviorForvard implements Behavior {
	
	private MovePilot pilot = null;

	public BehaviorForvard (MovePilot pilot) {
		this.pilot = pilot;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		if (!pilot.isMoving()){
			pilot.backward();
		}

	}

	@Override
	public void suppress() {
		pilot.stop();

	}

}
