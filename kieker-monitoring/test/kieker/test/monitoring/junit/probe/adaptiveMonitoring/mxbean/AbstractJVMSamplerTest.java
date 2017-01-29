/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.junit.probe.adaptiveMonitoring.mxbean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.controller.WaitableController;
import kieker.monitoring.core.sampler.ISampler;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.monitoring.util.NamedListWriter;

/**
 * @author Micky Singh Multani
 *
 * @since 1.10
 */
public abstract class AbstractJVMSamplerTest extends AbstractKiekerTest {

	private final String listName;
	private final String jvmSignature;
	private final ISampler sampler;

	private List<IMonitoringRecord> recordListFilledByListWriter;
	private MonitoringController monitoringController;

	AbstractJVMSamplerTest(final String listName, final String jvmSignature, final ISampler sampler) {
		this.listName = listName;
		this.sampler = sampler;
		this.jvmSignature = jvmSignature;
	}

	protected abstract void isInstanceOf(List<IMonitoringRecord> recordList);

	protected abstract void checkNumEventsBeforeMonitoringDisabled(int records);

	@Before
	public void prepare() {
		this.recordListFilledByListWriter = NamedListWriter.createNamedList(this.listName);
		this.monitoringController = this.createMonitoringController();
	}

	@Test
	public void testAdaptiveMonitoring() throws InterruptedException {

		final long period = 1000; // 1000 ms
		final long offset = 200; // 1st event after 200 ms

		this.monitoringController.schedulePeriodicSampler(this.sampler, offset, period, TimeUnit.MILLISECONDS);

		Thread.sleep(3500); // sleep 3,5 seconds

		// PROBE DEACTIVATION AND REACTIVATION TEST

		final int numEventsBeforeProbeDisabled = this.recordListFilledByListWriter.size();
		this.checkNumEventsBeforeMonitoringDisabled(numEventsBeforeProbeDisabled);

		this.monitoringController.deactivateProbe(this.jvmSignature); // DEACTIVATION (probe)

		Thread.sleep(2000); // sleep 2 seconds while probe being disabled

		// There should be no new records while probe being disabled
		final int numEventsWhileProbeDisabled = this.recordListFilledByListWriter.size() - numEventsBeforeProbeDisabled;
		Assert.assertEquals("Unexpected number of triggering events while probe being disabled", 0, numEventsWhileProbeDisabled);

		this.monitoringController.activateProbe(this.jvmSignature); // REACTIVATION (probe)

		Thread.sleep(2000); // sleep 2 seconds while probe being re-enabled

		// There should be at least 1 new record after re-enabling
		final int numEventsAfterProbeReEnabled = this.recordListFilledByListWriter.size() - numEventsBeforeProbeDisabled;
		Assert.assertTrue("Expected at least one triggering event after being re-enabled. Found " + numEventsAfterProbeReEnabled,
				numEventsAfterProbeReEnabled > 0);

		this.isInstanceOf(this.recordListFilledByListWriter);

		// DISABLING AND RE-ENABLING MONITORING TEST

		final int numEventsBeforeMonitoringDisabled = this.recordListFilledByListWriter.size() - numEventsAfterProbeReEnabled;
		this.checkNumEventsBeforeMonitoringDisabled(numEventsBeforeMonitoringDisabled);

		this.monitoringController.disableMonitoring(); // DEACTIVATION (monitoring)

		Thread.sleep(2000); // sleep 2 seconds while monitoring being disabled

		// There should be no new records while monitoring being disabled
		final int numEventsWhileMonitoringDisabled = this.recordListFilledByListWriter.size() - (numEventsBeforeMonitoringDisabled + numEventsAfterProbeReEnabled);
		Assert.assertEquals("Unexpected number of triggering events while monitoring being disabled", 0, numEventsWhileMonitoringDisabled);

		this.monitoringController.enableMonitoring(); // REACTIVATION (monitoring)

		Thread.sleep(2000); // sleep 2 seconds while monitoring being re-enabled

		// There should be at least one new record
		final int numEventsAfterMonitoringReEnabled = this.recordListFilledByListWriter.size() - (numEventsBeforeMonitoringDisabled + numEventsAfterProbeReEnabled);
		Assert.assertTrue("Expected at least one triggering event after being re-enabled. Found " + numEventsAfterMonitoringReEnabled,
				numEventsAfterMonitoringReEnabled > 0);

		this.monitoringController.terminateMonitoring();
		new WaitableController(this.monitoringController).waitForTermination(5000);
	}

	private MonitoringController createMonitoringController() {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "true");
		config.setProperty(ConfigurationFactory.METADATA, "false");
		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, NamedListWriter.class.getName());
		config.setProperty(NamedListWriter.CONFIG_PROPERTY_NAME_LIST_NAME, this.listName);
		return MonitoringController.createInstance(config);
	}

}
