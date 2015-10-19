package test;

import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;

import com.accenture.iot.lego.BehaviorBluemix;
import com.ibm.bluemixmqtt.MqttHandler;

public class TestClient {

	public static void main(String[] args) throws InterruptedException {
		BehaviorBluemix b4 = new BehaviorBluemix(null);
		MqttHandler handler = new MqttHandler(b4);
		// Format: d:<orgid>:<type-id>:<device-id>
		handler.connect("ubqgd7.messaging.internetofthings.ibmcloud.com", "d:ubqgd7:EV3:EV3_1", "use-token-auth",
				"ZBb2Q0R59obhGS-dR+", false);

		while (true) {
			JSONObject move = new JSONObject();
			try {
				move.put("distance", "123");
				move.put("angle", "123");
				move.put("speed", "123");
				move.put("time", System.currentTimeMillis());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			// Publish device events to the app
			// iot-2/evt/<event-id>/fmt/<format>
			handler.publish("iot-2/evt/eid/fmt/json", move.toString(), false, 0);
			Thread.sleep(10000);
			System.out.println("Test");
		}
	}

}
