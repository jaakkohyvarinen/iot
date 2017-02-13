package com.accenture.iot.lego;

import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class BehaviorProximity implements Behavior {

	private MovePilot pilot = null;
	private SharedIRSensor ir = null;

	public BehaviorProximity(MovePilot pilot, SharedIRSensor ir) {
		this.pilot = pilot;
		this.ir = ir;
	}

	@Override
	public boolean takeControl() {
		return (ir.getDistance() < 40);
	}

	@Override
	public void action() {
		pilot.travel((double) 30);
		pilot.rotate(90);

	}

	@Override
	public void suppress() {

	}

}
