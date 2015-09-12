package com.accenture.iot.lego;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class BehaviorProximity implements Behavior {

	private DifferentialPilot pilot = null;
	private SharedIRSensor ir = null;
	private boolean backing_up = false;

	public BehaviorProximity(DifferentialPilot pilot, SharedIRSensor ir) {
		this.pilot = pilot;
		this.ir = ir;
	}

	@Override
	public boolean takeControl() {
		return (ir.getDistance() < 40);
	}

	@Override
	public void action() {

		pilot.travel((double) -30);
		pilot.rotate(90);
		
		backing_up = false;

	}

	@Override
	public void suppress() {
		while (backing_up) {
			Thread.yield();
		}

	}

}
