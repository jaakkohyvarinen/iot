package test;

import java.util.Date;
import java.util.Random;

import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;

import com.accenture.iot.lego.BehaviorAWS;
import com.accenture.iot.lego.MqttHandler;

public class TestClient {

	public static void main(String[] args) throws InterruptedException {
		BehaviorAWS b4 = new BehaviorAWS(null);
		MqttHandler handler = new MqttHandler(b4);
		handler.connect("a3cmxw710fgyr3.iot.eu-central-1.amazonaws.com", "EV3");
		Random randomGenerator = new Random();
		while (true) {
			JSONObject move = new JSONObject();
			try {
				move.put("distance", randomGenerator.nextInt());
				move.put("angle", randomGenerator.nextInt());
				move.put("speed", randomGenerator.nextInt());
				move.put("time", (new Date(System.currentTimeMillis()).toString()));
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			handler.publish("ev3/telemetry", move.toString(), false, 0);
			Thread.sleep(1000);
			System.out.println("Published [" + move.toString() + "]");
		}
	}

}
