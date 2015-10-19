package com.accenture.iot.lego;

import com.ibm.bluemixmqtt.MqttHandler;

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
		BehaviorBluemix b4 = new BehaviorBluemix (pilot);
		Behavior[] behave = { b1, b2, b3 };
		MqttHandler handler = new MqttHandler(b4);
		// Format: d:<orgid>:<type-id>:<device-id>
		System.out.println("Trying to connect IBM-Bluemix.");
		handler.connect("ubqgd7.messaging.internetofthings.ibmcloud.com", "d:ubqgd7:EV3:EV3_1",
				"use-token-auth", "ZBb2Q0R59obhGS-dR+", false);
		// Subscribe the Command events
		// iot-2/cmd/<cmd-type>/fmt/<format-id>
		System.out.println("Connection established!");
		handler.subscribe("iot-2/cmd/cid/fmt/json", 0);
		pilot.addMoveListener(new TelemetryProvider(handler));
		
		System.out.println("EV3 Arbitrator Starting!");
		arby = new Arbitrator(behave);
		arby.start();
		

	}

}
