package com.accenture.iot.lego;

import lejos.hardware.lcd.LCD;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;

public class MotorLogger2  implements MoveListener{

	@Override
	public void moveStarted(Move event, MoveProvider mp) {
//		LCD.drawString("Started: " + event,  0,0);
		
	}

	@Override
	public void moveStopped(Move event, MoveProvider mp) {
		LCD.drawString("Angle: " + event.getAngleTurned(), 0,0);
		LCD.drawString("Distance: " + event.getDistanceTraveled(), 0,1);
		LCD.drawString("Speed: " + event.getTravelSpeed(), 0,2);
		LCD.drawString("Type: " + event.getMoveType(), 0,3);
	}

	
}
