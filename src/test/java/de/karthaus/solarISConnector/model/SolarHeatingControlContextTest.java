package de.karthaus.solarISConnector.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SolarHeatingControlContextTest {

	private SolarHeatingControlContext solarISConnectorContext;
	
	@BeforeEach
	void setUp() throws Exception {
		solarISConnectorContext = new SolarHeatingControlContext();
		
	}

	@Test
	void testAVGCalc() {
		solarISConnectorContext.addPvData(5.0D);
		solarISConnectorContext.addPvData(10D);
		solarISConnectorContext.addPvData(15D);
		assertThat(solarISConnectorContext.getAVGPvData(), is(10.0D));
	}

	@Test
	void testGive0AVGCalc() {
		solarISConnectorContext.addPvData(5.0D);
		solarISConnectorContext.addPvData(10D);
		solarISConnectorContext.addPvData(15D);
		solarISConnectorContext.flushPvData();
		assertThat(solarISConnectorContext.getAVGPvData(), is(0.0D));
	}

	
}
