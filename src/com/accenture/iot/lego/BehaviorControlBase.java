package com.accenture.iot.lego;

import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public abstract class BehaviorControlBase implements Behavior {

	private MovePilot pilot;
	private int prev = 0;
	private boolean manualcontrol = false;
	
	public BehaviorControlBase (MovePilot pilot) {
		this.pilot = pilot;
	}
	
	@Override
	public abstract  boolean takeControl();

	@Override
	public void action() {
		manualcontrol = true;
		while (manualcontrol) {
			int current = getControlValue();
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
				pilot.rotate(-50000,true);
				continue;
			case 2:
				pilot.rotate(50000,true);
				continue;
			case 3:
				pilot.backward();
				continue;
			case 4:
				pilot.forward();
				continue;
			}
		}
	}

	@Override
	public void suppress() {
		manualcontrol = false;
		prev = 0;
	}
	
	abstract protected int getControlValue ();

}
