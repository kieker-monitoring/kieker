/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.test.monitoring.junit.probe.adaptiveMonitoring.oshi;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.system.MemSwapUsageRecord;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.signaturePattern.SignatureFactory;
import kieker.monitoring.sampler.oshi.IOshiSamplerFactory;
import kieker.monitoring.sampler.oshi.OshiSamplerFactory;
import kieker.monitoring.sampler.oshi.samplers.MemSwapUsageSampler;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.monitoring.util.NamedListWriter;

/**
 *
 * @author Matteo Sassano
 *
 * @since 1.14
 */
public class TestMemSwapUsageSampler extends AbstractKiekerTest { // NOCS

	private volatile String listName;
	private volatile List<IMonitoringRecord> recordListFilledByListWriter;
	private volatile IMonitoringController monitoringController;

	@Before
	public void prepare() {
		this.listName = TestMemSwapUsageSampler.class.getName();
		this.recordListFilledByListWriter = NamedListWriter.createNamedList(this.listName);
		this.monitoringController = this.createMonitoringController();
	}

	@Test
	public void testDeactivationAndReactivation() throws InterruptedException {
		final long period = 1500; // 1500 ms
		final long offset = 300; // i.e., 1st event after 300 ms

		final IOshiSamplerFactory oshiFactory = OshiSamplerFactory.INSTANCE;
		final MemSwapUsageSampler sampler = oshiFactory.createSensorMemSwapUsage();

		this.monitoringController.schedulePeriodicSampler(sampler, offset, period, TimeUnit.MILLISECONDS);

		Thread.sleep(3600); // sleep 3.6 seconds

		// Expecting sampling trigger events at milliseconds 300, 1800, 3300
		final int numEventsBeforeDisabled = this.recordListFilledByListWriter.size();

		final String pattern = SignatureFactory.createMemSwapSignature();
		this.monitoringController.deactivateProbe(pattern);

		Thread.sleep(2000); // sleep 2 seconds while being disabled
		// There should be no new trigger events
		final int numEventsWhileDisabled = this.recordListFilledByListWriter.size() - numEventsBeforeDisabled;

		this.monitoringController.activateProbe(pattern);

		Thread.sleep(2000); // sleep 2 seconds while being re-enabled

		// There should be at least one new trigger event

		final int numEventsAfterReEnabled = this.recordListFilledByListWriter.size() - numEventsBeforeDisabled;

		final boolean isInstanceOf = this.recordListFilledByListWriter.get(0) instanceof MemSwapUsageRecord;

		Assert.assertTrue("Unexpected instance of IMonitoringRecord", isInstanceOf);
		Assert.assertEquals("Unexpected number of triggering events before disabling", 3, numEventsBeforeDisabled);
		Assert.assertEquals("Unexpected number of triggering events while disabled", 0, numEventsWhileDisabled);
		Assert.assertTrue("Unexpected at least one triggering events after being re-enabled. Found " + numEventsAfterReEnabled,
				numEventsAfterReEnabled > 0); // NOCS (MagicNumberCheck)
		this.monitoringController.terminateMonitoring();
	}

	private IMonitoringController createMonitoringController() {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationConstants.ADAPTIVE_MONITORING_ENABLED, "true");
		config.setProperty(ConfigurationConstants.META_DATA, "false");
		config.setProperty(ConfigurationConstants.WRITER_CLASSNAME, NamedListWriter.class.getName());
		config.setProperty(NamedListWriter.CONFIG_PROPERTY_NAME_LIST_NAME, this.listName);
		return MonitoringController.createInstance(config);
	}

}
