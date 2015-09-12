package com.accenture.iot.lego;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.SampleProvider;

public class SharedIRSensor extends Thread {

	private EV3IRSensor ir = new EV3IRSensor(SensorPort.S4);
	private SampleProvider sp = ir.getDistanceMode();

	private int control = 0;
	private int distance = 0;

	private void initAndStart() {
		this.setDaemon(true);
		this.start();
	}

	public SharedIRSensor() {
		// Does nothing else than init
		initAndStart();
	}

	public SharedIRSensor(EV3IRSensor ir) {
		this.ir = ir;
		this.sp = ir.getDistanceMode();
		initAndStart();
	}

	public void run () {
		while (true) {
			float [] sample = new float[sp.sampleSize()];
			control = ir.getRemoteCommand(0);
			sp.fetchSample(sample, 0);
			if (sample [0] < 1){
				this.distance = 255;
			} else {
				this.distance = (int) sample [0];
			}
			LCD.drawString("Control: " + control, 0,0);
			LCD.drawString("Distance: " + distance + " ",  0, 1);
			Thread.yield();
		}
	}

	public int getDistance() {
		return this.distance;
	}
	
	public int getIRControlValue(){
		return this.control;
	}
	
}
