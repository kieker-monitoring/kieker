package kieker.monitoring.sampler.oshi;

import org.junit.Before;
import org.junit.Test;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.sampler.oshi.samplers.CPUsDetailedPercSampler;

public class OshiCPUsDetailedPercSamplerTest {

	private IMonitoringController monitoringController;

	@Before
	public void setUp() throws Exception {
		this.monitoringController = MonitoringController.getInstance();
	}

	@Test
	public void test() {
		final CPUsDetailedPercSampler c = OshiSamplerFactory.INSTANCE.createSensorCPUsDetailedPerc();
		for (int i = 0; i < 20; i++) {
			System.out.println("Iteration: " + i);
			c.sample(this.monitoringController);
			try {
				Thread.sleep(1100);
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
