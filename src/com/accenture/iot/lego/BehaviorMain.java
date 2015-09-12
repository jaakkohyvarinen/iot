package com.accenture.iot.lego;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class BehaviorMain {

	static Arbitrator arby;

	public static void main(String[] args) {
		DifferentialPilot pilot = new DifferentialPilot(2, 10, new EV3LargeRegulatedMotor(MotorPort.B),
				new EV3LargeRegulatedMotor(MotorPort.C), false);
		pilot.setTravelSpeed(5);
		SharedIRSensor ir = new SharedIRSensor();
		Behavior b1 =  new BehaviorForvard(pilot);
		Behavior b2 =  new BehaviorProximity(pilot, ir);
		Behavior [] behave = {b1, b2};
		arby = new Arbitrator(behave);
		arby.start();
	}

}
