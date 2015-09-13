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
		EV3LargeRegulatedMotor motor1 = new EV3LargeRegulatedMotor(MotorPort.B);
		EV3LargeRegulatedMotor motor2 = new EV3LargeRegulatedMotor(MotorPort.C);
		DifferentialPilot pilot = new MyPilot(2, 10, motor1, motor2, false);
		pilot.setTravelSpeed(5);

		SharedIRSensor ir = new SharedIRSensor();
		Behavior b1 = new BehaviorForvard(pilot);
		Behavior b2 = new BehaviorProximity(pilot, ir);
		Behavior b3 = new BehaviorRemote(ir, pilot);
		BehaviorBluemix b4 = new BehaviorBluemix (pilot);
		Behavior[] behave = { b3 };
		
		MqttHandler handler = new MqttHandler(b4);
		// Format: d:<orgid>:<type-id>:<device-id>
		handler.connect("07yake.messaging.internetofthings.ibmcloud.com", "d:07yake:MQTTDevice:MQTTDevice",
				"use-token-auth", "WOoN_njI-H76eIb&2S", true);
		// Subscribe the Command events
		// iot-2/cmd/<cmd-type>/fmt/<format-id>
		handler.subscribe("iot-2/cmd/cid/fmt/json", 0);
		pilot.addMoveListener(new TelemetryProvider(handler));
		
		arby = new Arbitrator(behave);
		arby.start();

	}

}
