package com.accenture.iot.lego;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class BehaviorMain {

	static Arbitrator arby;

	public static void main(String[] args) {
		System.out.println("EV3 started!");
		EV3LargeRegulatedMotor motor1 = new EV3LargeRegulatedMotor(MotorPort.B);
		EV3LargeRegulatedMotor motor2 = new EV3LargeRegulatedMotor(MotorPort.C);
		DifferentialPilot pilot = new MyPilot(2, 10, motor1, motor2, false);
		pilot.setTravelSpeed(5);
		SharedIRSensor ir = new SharedIRSensor();
		Behavior b1 = new BehaviorForvard(pilot);
		Behavior b2 = new BehaviorProximity(pilot, ir);
		Behavior b3 = new BehaviorRemote(ir, pilot);
		BehaviorAWS b4 = new BehaviorAWS(pilot);
		Behavior[] behave = { b1, b2, b3 };
		MqttHandler handler = new MqttHandler(b4);
		System.out.println("Trying to connect AWS.");
		handler.connect("a3cmxw710fgyr3.iot.eu-central-1.amazonaws.com", "EV3");
		System.out.println("Connection established!");
		handler.subscribe("ev3/telemetry", 0);
		pilot.addMoveListener(new TelemetryProvider(handler));
		System.out.println("EV3 Arbitrator Starting!");
		arby = new Arbitrator(behave);
		arby.start();

	}

}
