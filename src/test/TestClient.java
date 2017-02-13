package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Random;

import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;
import org.bouncycastle.openssl.PEMException;

import com.accenture.iot.lego.BehaviorAWS;
import com.accenture.iot.lego.MqttHandler;

public class TestClient {

	public static void main(String[] args) throws InterruptedException, PEMException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		BehaviorAWS b4 = new BehaviorAWS(null);
		MqttHandler handler = new MqttHandler(b4);
		handler.createKeyStore(new File("").getAbsoluteFile() + "/keys/keys.jks", "/R2D2.cert.pem", "/R2D2.private.key",
				"password");

		KeyStore keysjks = KeyStore.getInstance(KeyStore.getDefaultType());
		keysjks.load(new TestClient().getClass().getResourceAsStream("/keys.jks"), "password".toCharArray());

		KeyStore cacerts = KeyStore.getInstance(KeyStore.getDefaultType());
		cacerts.load(new TestClient().getClass().getResourceAsStream("/cacerts"), "changeit".toCharArray());

		handler.connect("a3cmxw710fgyr3.iot.eu-west-1.amazonaws.com", "R2D2", keysjks, "password", cacerts);
		handler.subscribe("r2d2/alexa", 1);
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
			handler.publish("r2d2/telemetry", move.toString(), false, 0);
			Thread.sleep(3000);
			System.out.println("Published [" + move.toString() + "]");
		}
	}
}
