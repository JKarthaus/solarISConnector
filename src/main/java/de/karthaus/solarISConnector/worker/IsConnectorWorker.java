package de.karthaus.solarISConnector.worker;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.karthaus.solarISConnector.model.SolarHeatingControlContext;

public class IsConnectorWorker implements Runnable {

	private static final String isBaseURL = "https://groker.initialstate.com/api/events";
	private final String USER_AGENT = "Mozilla/5.0";
	private Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private SolarHeatingControlContext solarISConnectorContext;
	private int counter = 0;
	private boolean runFlag = true;

	public void setSolarISConnectorContext(SolarHeatingControlContext solarISConnectorContext) {
		this.solarISConnectorContext = solarISConnectorContext;
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		log.info("Initial State pushDataWorker startet.");
		while (runFlag) {
			try {
				TimeUnit.SECONDS.sleep(1);
				counter++;
				if (counter >= solarISConnectorContext.getIsPushIntervall()) {
					pushData(solarISConnectorContext.getAVGPvData());
					counter = 0;
					solarISConnectorContext.flushPvData();
				}
			} catch (InterruptedException e) {
				runFlag = false;
				Thread.currentThread().interrupt();
				log.info("Initial State pushDataWorker stopped.");
			}
		}
	}

	/**
	 * Push simple Data value to Initial State
	 */
	private void pushData(double value) {
		String isURL = isBaseURL + "?accessKey=" + solarISConnectorContext.getIsAPIkey();
		isURL = isURL + "&bucketKey=" + solarISConnectorContext.getIsBucketKey();
		isURL += "&pvData=" + value;
		log.info("Try to Push Solar Data to {} ", isBaseURL);
		try {
			URL obj = new URL(isURL);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			int responseCode = con.getResponseCode();
			log.debug("\nSending 'POST' request to URL : " + isURL);
			log.info("Response Code from Sending Data to InitialState=" + responseCode);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void setRunFlag(boolean runFlag) {
		this.runFlag = runFlag;
	}

	public boolean isRunFlag() {
		return runFlag;
	}
}
