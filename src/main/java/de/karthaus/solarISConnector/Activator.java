package de.karthaus.solarISConnector;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import de.karthaus.solarISConnector.model.SolarHeatingControlContext;
import de.karthaus.solarISConnector.worker.IsConnectorWorker;
import de.karthaus.solarISConnector.worker.MQTTWorker;

public class Activator implements BundleActivator, ManagedService {

	private Logger log = Logger.getLogger(this.getClass().getName());
	public static final String BUNDLE_ID = "solarISConnector";

	private ServiceRegistration serviceReg;
	private SolarHeatingControlContext solarISConnectorContext = new SolarHeatingControlContext();

	private MQTTWorker mqttWorker = new MQTTWorker();
	private Thread mqttThread = new Thread(mqttWorker, "mqttWorkerThread");

	private IsConnectorWorker isConnectorWorker = new IsConnectorWorker();
	private Thread initialStateThread = new Thread(isConnectorWorker, "initialStateWorkerThread");

	@Override
	public void updated(Dictionary properties) throws ConfigurationException {
		if (properties == null) {
			log.info("config is null - Please give me a config File");
			return;
		}
		log.info("Mqtt and Initialstate Config was set.");
		solarISConnectorContext.setIsAPIkey((String) properties.get("initialStateAPIKey"));
		solarISConnectorContext.setIsBucketKey((String) properties.get("initialStateBucketKey"));
		solarISConnectorContext
				.setIsPushIntervall(new Integer((String) properties.get("initialStatePushIntervall")).intValue());
		solarISConnectorContext.setMqttHost((String) properties.get("mqttHost"));
		solarISConnectorContext.setMqttTopic((String) properties.get("mqttTopic"));
		mqttWorker.setSolarISConnectorContext(solarISConnectorContext);
		isConnectorWorker.setSolarISConnectorContext(solarISConnectorContext);
	}

	private void startWorker() {
		if (!mqttThread.isAlive()) {
			log.info("MQTT Thread Startet");
			mqttWorker.setRunFlag(true);
			mqttThread.start();
		} else {
			log.info("MQTT Thread already running");
		}
		if (!initialStateThread.isAlive()) {
			log.info("InitialState Thread Startet");
			isConnectorWorker.setRunFlag(true);
			initialStateThread.start();
		} else {
			log.info("InitialState Thread already running");
		}
	}

	private void stopworker() {
		if (mqttThread.isAlive()) {
			log.info("MQTT Thread stopped");
			mqttWorker.setRunFlag(false);
			mqttThread.interrupt();
		} else {
			log.info("MQTT Thread already stopped");
		}
		if (initialStateThread.isAlive()) {
			log.info("InitialState Thread stopped");
			isConnectorWorker.setRunFlag(false);
			initialStateThread.interrupt();
		} else {
			log.info("InitialState Thread already stopped");
		}
	}

	@Override
	public void start(BundleContext context) throws Exception {
		Hashtable<String, Object> properties = new Hashtable<String, Object>();
		properties.put(Constants.SERVICE_PID, BUNDLE_ID);
		serviceReg = context.registerService(ManagedService.class.getName(), this, properties);
		startWorker();
		log.info("Bundle " + BUNDLE_ID + " startet.");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		stopworker();
		log.info("Bundle " + BUNDLE_ID + " stopped.");

	}

}
