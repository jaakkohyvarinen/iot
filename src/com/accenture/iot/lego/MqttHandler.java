package com.accenture.iot.lego;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.InvalidPathException;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttHandler implements MqttCallback {

	private BehaviorAWS behavior;

	private MqttClient client = null;

	public MqttHandler(BehaviorAWS behavior) {
		this.behavior = behavior;

	}

	@Override
	public void connectionLost(Throwable throwable) {
		if (throwable != null) {
			throwable.printStackTrace();
		}
	}

	/**
	 * One message is successfully published
	 */
	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		System.out.println(".deliveryComplete() entered");
	}

	/**
	 * Received one subscribed message
	 */
	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
		String payload = new String(mqttMessage.getPayload());
		System.out.println(".messageArrived - Message received on topic " + topic + ": message is " + payload);
		behavior.dothejob(payload);
		System.out.println("R2D2 job done!");
	}

	public void connect(String serverHost, String clientId, KeyStore ks, String password, KeyStore caCerts) {
		if (!isMqttConnected()) {
			try {
				client = new MqttClient("ssl://" + serverHost + ":8883", "R2D2", new MemoryPersistence());
				MqttConnectOptions options = new MqttConnectOptions();
				SSLSocketFactory factory = null;
				if (caCerts == null) {
					factory = getSslSocketFactory(ks, password);
				} else {
					factory = getSslSocketFactory(ks, password, caCerts);
				}
				options.setSocketFactory(factory);
				client.connect(options);
				client.setCallback(this);
				System.out.println("Connected to " + serverHost);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Disconnect MqttClient from the MQTT server
	 */
	public void disconnect() {

		// check if client is actually connected
		if (isMqttConnected()) {
			try {
				// disconnect
				client.disconnect();
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Subscribe MqttClient to a topic
	 * 
	 * @param topic
	 *            to subscribe to
	 * @param qos
	 *            to subscribe with
	 */
	public void subscribe(String topic, int qos) {

		// check if client is connected
		if (isMqttConnected()) {
			try {
				client.subscribe(topic, qos);
				System.out.println("Subscribed: " + topic);

			} catch (MqttException e) {
				e.printStackTrace();
			}
		} else {
			connectionLost(null);
		}
	}

	/**
	 * Unsubscribe MqttClient from a topic
	 * 
	 * @param topic
	 *            to unsubscribe from
	 */
	public void unsubscribe(String topic) {
		// check if client is connected
		if (isMqttConnected()) {
			try {

				client.unsubscribe(topic);
			} catch (MqttException e) {
				e.printStackTrace();
			}
		} else {
			connectionLost(null);
		}
	}

	/**
	 * Publish message to a topic
	 * 
	 * @param topic
	 *            to publish the message to
	 * @param message
	 *            JSON object representation as a string
	 * @param retained
	 *            true if retained flag is required
	 * @param qos
	 *            quality of service (0, 1, 2)
	 */
	public void publish(String topic, String message, boolean retained, int qos) {
		// check if client is connected
		if (isMqttConnected()) {
			// create a new MqttMessage from the message string
			MqttMessage mqttMsg = new MqttMessage(message.getBytes());
			// set retained flag
			mqttMsg.setRetained(retained);
			// set quality of service
			mqttMsg.setQos(qos);
			try {
				client.publish(topic, mqttMsg);
			} catch (MqttPersistenceException e) {
				e.printStackTrace();
			} catch (MqttException e) {
				e.printStackTrace();
			}
		} else {
			connectionLost(new Throwable("Connection not established"));
		}
	}

	/**
	 * Checks if the MQTT client has an active connection
	 * 
	 * @return True if client is connected, false if not.
	 */
	private boolean isMqttConnected() {
		boolean connected = false;
		try {
			if ((client != null) && (client.isConnected())) {
				connected = true;
			}
		} catch (Exception e) {
			// swallowing the exception as it means the client is not connected
			// :-)
		}
		return connected;
	}

	public SSLSocketFactory getSslSocketFactory(KeyStore ks, String password)
			throws InvalidPathException, IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException,
			UnrecoverableKeyException, KeyManagementException, Exception {

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(ks, password.toCharArray());
		SSLContext context = SSLContext.getInstance("TLSv1.2");
		context.init(kmf.getKeyManagers(), null, null);
		return context.getSocketFactory();
	}

	public SSLSocketFactory getSslSocketFactory(KeyStore ks, String password, KeyStore caCerts)
			throws InvalidPathException, IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException,
			UnrecoverableKeyException, KeyManagementException, Exception {

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(ks, password.toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(caCerts);

		SSLContext context = SSLContext.getInstance("TLSv1.2");
		context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		context.init(kmf.getKeyManagers(), null, null);
		return context.getSocketFactory();
	}

	public void createKeyStore(final String path, final String crtFile, final String keyFile, final String password)
			throws IOException, PEMException, KeyStoreException, NoSuchAlgorithmException, CertificateException,
			FileNotFoundException {
		Security.addProvider(new BouncyCastleProvider());
		System.out.println("BouncyCastleProvider added!");
		// load client certificate
		PEMParser parser = new PEMParser(new InputStreamReader(getClass().getResourceAsStream(crtFile)));
		X509CertificateHolder cert = (X509CertificateHolder) parser.readObject();
		parser.close();
		System.out.println("crtfile parsed " + crtFile);
		// load client private key
		parser = new PEMParser(new InputStreamReader(getClass().getResourceAsStream(keyFile)));
		Object obj = parser.readObject();
		parser.close();
		KeyPair key = null;
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
		key = converter.getKeyPair((PEMKeyPair) obj);
		JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
		certConverter.setProvider("BC");

		// Client key and certificates are sent to server
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);
		ks.setCertificateEntry("certificate", certConverter.getCertificate(cert));
		ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(),
				new java.security.cert.Certificate[] { certConverter.getCertificate(cert) });
		ks.store(new FileOutputStream(new File(path)), "password".toCharArray());
	}

}
