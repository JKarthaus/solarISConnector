package de.karthaus.solarISConnector.worker;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.karthaus.solarISConnector.model.SolarHeatingControlContext;

class MQTTWorkerTest {

	private MQTTWorker mqttWorker;
	private SolarHeatingControlContext context;

	@BeforeEach
	void setUp() throws Exception {
		context = new SolarHeatingControlContext();
		mqttWorker = new MQTTWorker();

	}

	@Test
	void IntegrationTestWithExternalBroker() {
		context.setMqttHost("tcp://localhost:1883");
		context.setMqttTopic("/pvData");
		mqttWorker.setSolarISConnectorContext(context);
		Thread test = new Thread(mqttWorker, "workerThread");
		test.start();
		try {
			TimeUnit.SECONDS.sleep(1);
			sendMessage("111");
			TimeUnit.SECONDS.sleep(1);
			sendMessage("112");
			TimeUnit.SECONDS.sleep(1);
			assertThat(context.getAVGPvData(), is(111.5));
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}

	}

	private void sendMessage(String payload) {
		try {
			String[] cmd = { "/bin/sh", "-c", "mosquitto_pub -t \"/pvData\" -m \"" + payload + "\"" };
			Process process = Runtime.getRuntime().exec(cmd);
			int exitCode = process.waitFor();
			assert exitCode == 0;
		} catch (IOException e) {
			e.printStackTrace();
			fail("Problem at sending MQTT Data via mosquitto_pub");
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

}
