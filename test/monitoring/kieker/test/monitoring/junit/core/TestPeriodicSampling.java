package kieker.test.monitoring.junit.core;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.core.sampler.ScheduledSamplerJob;
import kieker.test.monitoring.junit.util.DefaultConfigurationFactory;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class TestPeriodicSampling extends TestCase {
	public void testPeriodicSampler() throws InterruptedException {
		final Configuration configuration =
				DefaultConfigurationFactory
						.createDefaultConfigurationWithDummyWriter();
		final IMonitoringController monitoringController =
				MonitoringController.createInstance(configuration);

		final AtomicInteger numTriggers = new AtomicInteger(0);
		final ISampler samplingCounter = new ISampler() {

			@Override
			public void sample(final IMonitoringController monitoringController)
					throws Exception {
				numTriggers.incrementAndGet();
			}
		};

		final long period = 3000; // 3000 ms
		final long offset = 300;  // i.e., 1st event after 300 ms

		final ScheduledSamplerJob samplerJob = 
			monitoringController.schedulePeriodicSampler(samplingCounter, offset,
				period, TimeUnit.MILLISECONDS);
		
		Thread.sleep(6600); // sleep 6.6 seconds
		
		// Expecting sampling trigger events at milliseconds 300, 3300, 6300
		final int numEventsBeforeRemoval = numTriggers.get();
		
		monitoringController.removeScheduledSampler(samplerJob);
		
		Thread.sleep(10000); // sleep another 10 seconds
		
		/* There should be no new trigger events  */
		
		final int numEventsAfterRemoval = numTriggers.get();

		Assert.assertEquals("Unexpected number of triggering events before removal", 3,
				numEventsBeforeRemoval);
		Assert.assertEquals("Unexpected number of triggering events before removal", 3,
				numEventsAfterRemoval);
		
		
		monitoringController.terminateMonitoring();
	}
}
