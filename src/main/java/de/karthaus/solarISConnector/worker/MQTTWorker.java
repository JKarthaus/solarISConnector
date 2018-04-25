package de.karthaus.solarISConnector.worker;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.karthaus.solarISConnector.Activator;
import de.karthaus.solarISConnector.model.SolarHeatingControlContext;

public class MQTTWorker implements MqttCallback, Runnable {

	private final static Logger log = LoggerFactory.getLogger(MQTTWorker.class);

	private SolarHeatingControlContext context;

	private MqttClient mqttClient = null;

	private Random randomGen = new Random();

	private boolean isConnected = false;

	private boolean runFlag = true;

	@Override
	public void run() {
		log.info("MQTT Worker startet.");
		while (runFlag) {
			try {
				if (!isConnected) {
					isConnected = connectToBroker();
					log.debug("Server connected:{}", isConnected);
				}
				TimeUnit.SECONDS.sleep(60);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.info("MQTT Worker stopped.");
			}
		}
		if (!runFlag && isConnected) {
			try {
				mqttClient.disconnect();
			} catch (MqttException e) {
				log.error(e.getMessage());
			}
		}

	}

	/**
	 * 
	 * @return
	 */
	private boolean connectToBroker() {
		try {
			log.info("Try to connect to {} listenOnTopic {} ", context.getMqttHost(), context.getMqttTopic());
			if (mqttClient == null) {
				mqttClient = new MqttClient(context.getMqttHost(), Activator.BUNDLE_ID + "" + randomGen.nextInt(100));
			}
			if (!mqttClient.isConnected()) {
				mqttClient.connect();
			}
			mqttClient.subscribe(context.getMqttTopic());
			mqttClient.setCallback(this);
			log.info("{} connected ", context.getMqttHost());
			// --

		} catch (MqttException e) {
			mqttClient = null;
			log.error(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public void connectionLost(Throwable arg0) {
		log.debug("connectionLost");
		mqttClient = null;
		isConnected = false;
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		log.debug("deliveryComplete");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		log.info(message.getPayload().toString());
		log.debug("messageArrived");
		try {
			context.addPvData(new Double(new String(message.getPayload())));
		} catch (Exception e) {
			log.error("Error at processing MQTT Message {} to PV Value", message.getPayload());
		}
	}

	public void setSolarISConnectorContext(SolarHeatingControlContext solarISConnectorContext) {
		this.context = solarISConnectorContext;
	}

	public boolean isRunFlag() {
		return runFlag;
	}

	public void setRunFlag(boolean runFlag) {
		this.runFlag = runFlag;
	}

}
