/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
import kieker.common.record.system.CPUUtilizationRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.configuration.ConfigurationKeys;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.signaturePattern.SignatureFactory;
import kieker.monitoring.sampler.oshi.IOshiSamplerFactory;
import kieker.monitoring.sampler.oshi.OshiSamplerFactory;
import kieker.monitoring.sampler.oshi.samplers.CPUsDetailedPercSampler;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.monitoring.util.NamedListWriter;

/**
 *
 * @author Matteo Sassano
 *
 * @since 1.14
 */
public class TestCPUsDetailedPercSampler extends AbstractKiekerTest { // NOCS

	private volatile String listName;
	private volatile List<IMonitoringRecord> recordListFilledByListWriter;
	private volatile IMonitoringController monitoringController;

	@Before
	public void prepare() {
		this.listName = TestCPUsDetailedPercSampler.class.getName();
		this.recordListFilledByListWriter = NamedListWriter.createNamedList(this.listName);
		this.monitoringController = this.createMonitoringController();
	}

	@Test
	public void testDeactivationAndReactivation() throws InterruptedException {
		final long period = 1000; // 1000 ms
		final long offset = 300; // i.e., 1st event after 300 ms

		final IOshiSamplerFactory oshiFactory = OshiSamplerFactory.INSTANCE;
		final CPUsDetailedPercSampler sampler = oshiFactory.createSensorCPUsDetailedPerc();

		this.monitoringController.schedulePeriodicSampler(sampler, offset, period, TimeUnit.MILLISECONDS);

		Thread.sleep(2800); // sleep 2.8 seconds

		// Expecting sampling trigger events at milliseconds 300, 1300, 2300
		final int numEventsBeforeDisabled = this.recordListFilledByListWriter.size();
		Assert.assertTrue("Unexpected number of triggering events before disabling", numEventsBeforeDisabled > 0);
		final int mod = numEventsBeforeDisabled % 3;
		Assert.assertEquals("Unexpected number of triggering events before disabling", 0, mod);
		final int numCPUs = numEventsBeforeDisabled / 3;

		final String pattern4AllCPUs = SignatureFactory.createCPUSignature();
		this.monitoringController.deactivateProbe(pattern4AllCPUs);

		Thread.sleep(2000); // sleep 2 seconds while being disabled
		// There should be no new trigger events
		final int numEventsWhileDisabled = this.recordListFilledByListWriter.size() - numEventsBeforeDisabled;
		Assert.assertEquals("Unexpected number of triggering events while disabled", 0, numEventsWhileDisabled);

		this.monitoringController.activateProbe(pattern4AllCPUs);

		Thread.sleep(2000); // sleep 2 seconds while being re-enabled
		// There should be 2 times numCPUs new trigger events
		final int numEventsAfterReEnabled = this.recordListFilledByListWriter.size() - numEventsBeforeDisabled;
		Assert.assertEquals(
				"Unexpected 2 times numCPUs triggering events after being re-enabled. Found " + numEventsAfterReEnabled + " (numCPUs = " + numCPUs + ")",
				numEventsAfterReEnabled, 2 * numCPUs); // NOCS (MagicNumberCheck)

		final String pattern4CPU0 = SignatureFactory.createCPUSignature(0);
		this.monitoringController.deactivateProbe(pattern4CPU0);

		Thread.sleep(2000); // sleep 2 seconds while cpu0 being disabled

		final int numEventsWhileCPU0Disabled = this.recordListFilledByListWriter.size() - numEventsAfterReEnabled - numEventsBeforeDisabled;
		Assert.assertEquals("Unexpected number of triggering events while cpu0 disabled. Found" + numEventsWhileCPU0Disabled + " (numCPUs = " + numCPUs + ")",
				2 * (numCPUs - 1), numEventsWhileCPU0Disabled);

		final boolean isInstanceOf = this.recordListFilledByListWriter.get(0) instanceof CPUUtilizationRecord;
		Assert.assertTrue("Unexpected instance of IMonitoringRecord", isInstanceOf);

		this.monitoringController.terminateMonitoring();
	}

	private IMonitoringController createMonitoringController() {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationKeys.ADAPTIVE_MONITORING_ENABLED, "true");
		config.setProperty(ConfigurationKeys.META_DATA, "false");
		config.setProperty(ConfigurationKeys.WRITER_CLASSNAME, NamedListWriter.class.getName());
		config.setProperty(NamedListWriter.CONFIG_PROPERTY_NAME_LIST_NAME, this.listName);
		return MonitoringController.createInstance(config);
	}

}
