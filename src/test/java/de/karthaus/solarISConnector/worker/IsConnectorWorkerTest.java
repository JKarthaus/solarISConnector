package de.karthaus.solarISConnector.worker;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.karthaus.solarISConnector.model.SolarHeatingControlContext;

class IsConnectorWorkerTest {

	private IsConnectorWorker isConnectorWorker;
	private SolarHeatingControlContext context;

	@BeforeEach
	void setUp() throws Exception {
		context = new SolarHeatingControlContext();
		isConnectorWorker = new IsConnectorWorker();
		isConnectorWorker.setSolarISConnectorContext(context);
	}

	@Test
	void testFlushPVData() {
		context.setIsPushIntervall(1);
		context.addPvData(10D);
		Thread test = new Thread(isConnectorWorker, "workerThread");
		test.start();
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
		assertThat(context.getPvDataCount(), is(0));
		test.interrupt();
	}

	@Test
	void testPushPVData() {
		context.addPvData(10D);
		context.setIsPushIntervall(1);
		context.setIsAPIkey("lcN0JjeIwH5R4zV2LVW5kYYaCuMUUPJh");
		context.setIsBucketKey("K5A3XQ49FNYQ");
		Thread test = new Thread(isConnectorWorker, "workerThread");
		test.start();
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
		assertThat(context.getPvDataCount(), is(0));
		test.interrupt();
	}

}
