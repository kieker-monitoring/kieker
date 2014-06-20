/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
import kieker.common.record.jvm.ClassLoadingRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.signaturePattern.SignatureFactory;
import kieker.monitoring.sampler.mxbean.ClassLoadingSampler;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.monitoring.util.NamedListWriter;

/**
 * @author Micky Singh Multani
 * 
 * @since 1.10
 */
public class TestClassLoadingSampler extends AbstractKiekerTest {

	private volatile String listName;
	private volatile List<IMonitoringRecord> recordListFilledByListWriter;
	private volatile IMonitoringController monitoringController;

	@Before
	public void prepare() {
		this.listName = TestClassLoadingSampler.class.getName();
		this.recordListFilledByListWriter = NamedListWriter.createNamedList(this.listName);
		this.monitoringController = this.createMonitoringController();
	}

	@Test
	public void testAdaptiveMonitoring() throws InterruptedException {

		final long period = 1000; // 1000 ms
		final long offset = 200; // 1st event after 200 ms

		final ClassLoadingSampler sampler = new ClassLoadingSampler();

		this.monitoringController.schedulePeriodicSampler(sampler, offset, period, TimeUnit.MILLISECONDS);

		Thread.sleep(3500); // sleep 3,5 seconds

		// PROBE DEACTIVATION AND REACTIVATION TEST

		// There should be 4 saved records
		final int numEventsBeforeProbeDisabled = this.recordListFilledByListWriter.size();

		final String pattern = SignatureFactory.createJVMClassLoadSignature();
		this.monitoringController.deactivateProbe(pattern);

		Thread.sleep(2000); // sleep 2 seconds while probe being disabled

		// There should be no new records while probe being disabled
		final int numEventsWhileProbeDisabled = this.recordListFilledByListWriter.size() - numEventsBeforeProbeDisabled;

		this.monitoringController.activateProbe(pattern);

		Thread.sleep(2000); // sleep 2 seconds while probe being re-enabled

		// There should be at least 1 new record after re-enabling (expecting 2)
		final int numEventsAfterProbeReEnabled = this.recordListFilledByListWriter.size() - numEventsBeforeProbeDisabled;

		final boolean isInstanceOf = this.recordListFilledByListWriter.get(0) instanceof ClassLoadingRecord;

		Assert.assertTrue("Unexpected instance of IMonitoringRecord", isInstanceOf);
		Assert.assertEquals("Unexpected number of triggering events before disabling", 4, numEventsBeforeProbeDisabled);
		Assert.assertEquals("Unexpected number of triggering events while disabled", 0, numEventsWhileProbeDisabled);
		Assert.assertTrue("Expected at least one triggering event after being re-enabled. Found " + numEventsAfterProbeReEnabled,
				numEventsAfterProbeReEnabled > 0);

		// DISABLING AND RE-ENABLING MONITORING TEST

		// There should be 6 saved records
		final int numEventsBeforeMonitoringDisabled = this.recordListFilledByListWriter.size();

		this.monitoringController.disableMonitoring();

		Thread.sleep(2000); // sleep 2 seconds while monitoring being disabled

		// There should be no new records while monitoring being disabled
		final int numEventsWhileMonitoringDisabled = this.recordListFilledByListWriter.size() - numEventsBeforeMonitoringDisabled;

		this.monitoringController.enableMonitoring();

		Thread.sleep(2000); // sleep 2 seconds while monitoring being re-enabled

		// There should be at least one new record
		final int numEventsAfterMonitoringReEnabled = this.recordListFilledByListWriter.size() - numEventsBeforeProbeDisabled;

		Assert.assertEquals("Unexpected number of triggering events before disabling", 6, numEventsBeforeMonitoringDisabled);
		Assert.assertEquals("Unexpected number of triggering events while disabled", 0, numEventsWhileMonitoringDisabled);
		Assert.assertTrue("Expected at least one triggering event after being re-enabled. Found " + numEventsAfterMonitoringReEnabled,
				numEventsAfterMonitoringReEnabled > 0);

		this.monitoringController.terminateMonitoring();
	}

	private IMonitoringController createMonitoringController() {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "true");
		config.setProperty(ConfigurationFactory.METADATA, "false");
		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, NamedListWriter.class.getName());
		config.setProperty(NamedListWriter.CONFIG_PROPERTY_NAME_LIST_NAME, this.listName);
		return MonitoringController.createInstance(config);
	}

}
