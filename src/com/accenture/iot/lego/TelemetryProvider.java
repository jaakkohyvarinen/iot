package com.accenture.iot.lego;

import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;

import lejos.hardware.lcd.LCD;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;

public class TelemetryProvider implements MoveListener {

	private MqttHandler handler;

	public TelemetryProvider(MqttHandler handler) {
		this.handler = handler;
	}

	@Override
	public void moveStarted(Move event, MoveProvider mp) {

	}

	@Override
	public void moveStopped(Move event, MoveProvider mp) {
//		LCD.drawString("Angle: " + event.getAngleTurned(), 0, 0);
//		LCD.drawString("Distance: " + event.getDistanceTraveled(), 0, 1);
//		LCD.drawString("Speed: " + event.getTravelSpeed(), 0, 2);
//		LCD.drawString("Type: " + event.getMoveType(), 0, 3);
		// Format the Json String
		JSONObject move = new JSONObject();
		try {
			move.put("distance", event.getDistanceTraveled());
			move.put("angle", event.getAngleTurned());
			move.put("speed", event.getTravelSpeed());
			move.put("type", event.getMoveType());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		this.handler.publish("r2d2/telemetry", move.toString(), false, 0);
	}

}
