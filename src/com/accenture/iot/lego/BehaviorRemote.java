package com.accenture.iot.lego;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class BehaviorRemote implements Behavior {

	private SharedIRSensor ir;
	private DifferentialPilot pilot;
	private int prev = 0;
	private boolean manualcontrol = false;

	public BehaviorRemote(SharedIRSensor ir, DifferentialPilot pilot) {
		this.ir = ir;
		this.pilot = pilot;
	}

	@Override
	public boolean takeControl() {
		return (ir.getIRControlValue() != 0);
	}

	@Override
	public void action() {
		manualcontrol = true;
		while (manualcontrol) {
			int current = ir.getIRControlValue();
			if (prev == current) {
				return;
			}
			switch (current) {
			case 1:
				pilot.rotateLeft();
				break;
			case 2:
				pilot.rotateRight();
				break;
			case 3:
				pilot.forward();
				break;
			case 4:
				pilot.backward();
				break;
			}
			prev = current;
		}
	}

	@Override
	public void suppress() {
		manualcontrol = false;
	}

}
