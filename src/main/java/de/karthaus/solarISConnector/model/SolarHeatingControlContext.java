package de.karthaus.solarISConnector.model;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SolarHeatingControlContext {

	LocalTime lastSend;

	Map<LocalTime, Double> pvData = Collections.synchronizedMap(new HashMap<LocalTime, Double>());

	String mqttHost;
	String mqttTopic;
	String isAPIkey;
	String isBucketKey;
	int isPushIntervall;

	public double getAVGPvData() {
		if (pvData.isEmpty())
			return 0;
		return pvData.values().stream().mapToDouble(Double::doubleValue).average().getAsDouble();
	}

	public String getIsBucketKey() {
		return isBucketKey;
	}

	public void setIsBucketKey(String isBucketKey) {
		this.isBucketKey = isBucketKey;
	}

	public void addPvData(Double value) {
		pvData.put(LocalTime.now(), value);
	}

	public int getPvDataCount() {
		return pvData.size();
	}

	public void flushPvData() {
		pvData.clear();
		lastSend = LocalTime.now();
	}

	public int getIsPushIntervall() {
		return isPushIntervall;
	}

	public void setIsPushIntervall(int isPushIntervall) {
		this.isPushIntervall = isPushIntervall;
	}

	public LocalTime getLastSend() {
		return lastSend;
	}

	public void setLastSend(LocalTime lastSend) {
		this.lastSend = lastSend;
	}

	public String getMqttHost() {
		return mqttHost;
	}

	public void setMqttHost(String mqttHost) {
		this.mqttHost = mqttHost;
	}

	public String getMqttTopic() {
		return mqttTopic;
	}

	public void setMqttTopic(String mqttTopic) {
		this.mqttTopic = mqttTopic;
	}

	public String getIsAPIkey() {
		return isAPIkey;
	}

	public void setIsAPIkey(String isAPIkey) {
		this.isAPIkey = isAPIkey;
	}

}
