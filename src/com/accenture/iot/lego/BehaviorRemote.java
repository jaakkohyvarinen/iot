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
				continue;
			}
			pilot.stop();
			prev = current;
			switch (current) {
			case 0:
				pilot.stop();
				manualcontrol = false;
				break;
			case 1:
				pilot.rotateLeft();
				continue;
			case 2:
				pilot.rotateRight();
				continue;
			case 3:
				pilot.forward();
				continue;
			case 4:
				pilot.backward();
				continue;
			}
			
		}
	}

	@Override
	public void suppress() {
		manualcontrol = false;
		prev = 0;
	}

}
