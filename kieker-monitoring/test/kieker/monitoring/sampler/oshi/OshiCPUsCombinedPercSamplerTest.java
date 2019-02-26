package kieker.monitoring.sampler.oshi;

import org.junit.Before;
import org.junit.Test;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.sampler.oshi.samplers.CPUsCombinedPercSampler;

public class OshiCPUsCombinedPercSamplerTest {

	private IMonitoringController monitoringController;

	@Before
	public void setUp() throws Exception {
		this.monitoringController = MonitoringController.getInstance();
	}

	@Test
	public void test() {
		final CPUsCombinedPercSampler c = OshiSamplerFactory.INSTANCE.createSensorCPUsCombinedPerc();
		c.sample(this.monitoringController);

	}

}
