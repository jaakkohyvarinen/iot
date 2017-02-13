package com.accenture.iot.lego;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import test.TestClient;

public class BehaviorMain {

	static Arbitrator arby;

	public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		System.out.println("EV3 started!");
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.B, 3).offset(7);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.C, 3).offset(-7);
		MovePilot pilot = new MovePilot(new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL));
		pilot.setLinearSpeed(10);
		SharedIRSensor ir = new SharedIRSensor();
		Behavior b1 = new BehaviorForvard(pilot);
		Behavior b2 = new BehaviorProximity(pilot, ir);
		Behavior b3 = new BehaviorRemote(ir, pilot);
		BehaviorAWS b4 = new BehaviorAWS(pilot);
		Behavior[] behave = { b1,b2,b4, b3  };
		MqttHandler handler = new MqttHandler(b4);
		System.out.println("Trying to connect AWS.");
		KeyStore keysjks = KeyStore.getInstance(KeyStore.getDefaultType());
		keysjks.load(new TestClient().getClass().getResourceAsStream("/keys.jks"), "password".toCharArray());
		System.out.println("Keys.jks loaded!");
//		KeyStore cacerts = KeyStore.getInstance(KeyStore.getDefaultType());
//		cacerts.load(new TestClient().getClass().getResourceAsStream("/cacerts"), "changeit".toCharArray());
//		System.out.println("cacerts loaded!");
		handler.connect("a3cmxw710fgyr3.iot.eu-west-1.amazonaws.com", "R2D2", keysjks, "password", null);
		System.out.println("Connection established!");
		handler.subscribe("r2d2/alexa", 1);
		pilot.addMoveListener(new TelemetryProvider(handler));
		System.out.println("EV3 Arbitrator Starting!");
		arby = new Arbitrator(behave);
		arby.go();
	}

}
