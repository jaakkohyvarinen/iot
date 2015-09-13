package com.accenture.iot.lego;

import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;

public class MyPilot extends DifferentialPilot {

	public MyPilot(double wheelDiameter, double trackWidth, RegulatedMotor leftMotor, RegulatedMotor rightMotor,
			boolean reverse) {
		super(wheelDiameter, trackWidth, leftMotor, rightMotor, reverse);
	}
	
	public void rotateRight () {
		super.rotateRight();
		movementActive();
	}
}
