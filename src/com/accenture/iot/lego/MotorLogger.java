package com.accenture.iot.lego;

import lejos.hardware.lcd.LCD;
import lejos.robotics.navigation.DifferentialPilot;

public class MotorLogger  extends Thread{

	private DifferentialPilot pilot;
	
	public MotorLogger (DifferentialPilot pilot) {
		this.pilot = pilot;
		initAndStart();
	}
	
	private void initAndStart() {
		this.setDaemon(true);
		this.start();
	}
	
	public void run () {
		while (true){
			LCD.drawString("Move: " + pilot.getMovement().getDistanceTraveled(), 0,2);
			Thread.yield();
		}
	}
	
}
