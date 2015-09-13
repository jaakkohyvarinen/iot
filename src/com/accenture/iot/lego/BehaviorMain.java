package com.accenture.iot.lego;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class BehaviorMain {

	static Arbitrator arby;

	public static void main(String[] args) {
		EV3LargeRegulatedMotor motor1 = new EV3LargeRegulatedMotor(MotorPort.B);
		EV3LargeRegulatedMotor motor2 = new EV3LargeRegulatedMotor(MotorPort.C);
		DifferentialPilot pilot = new MyPilot(2, 10, motor1, motor2, false);
		pilot.setTravelSpeed(5);
		pilot.addMoveListener(new MotorLogger2());
//		new MotorLogger(pilot);
		SharedIRSensor ir = new SharedIRSensor();
		Behavior b1 = new BehaviorForvard(pilot);
		Behavior b2 = new BehaviorProximity(pilot, ir);
		Behavior b3 = new BehaviorRemote(ir, pilot);
		Behavior[] behave = { b3 };
		arby = new Arbitrator(behave);
		arby.start();

	}

}
