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
import kieker.common.record.jvm.MemoryRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.sampler.mxbean.MemorySampler;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.monitoring.util.NamedListWriter;

/**
 * @author Micky Singh Multani
 * 
 * @since 1.10
 */
public class TestMemorySampler extends AbstractKiekerTest {

	private volatile String listName;
	private volatile List<IMonitoringRecord> recordListFilledByListWriter;
	private volatile IMonitoringController monitoringController;

	@Before
	public void prepare() {
		this.listName = TestMemorySampler.class.getName();
		this.recordListFilledByListWriter = NamedListWriter.createNamedList(this.listName);
		this.monitoringController = this.createMonitoringController();

	}

	@Test
	public void testAdaptiveMonitoring() throws InterruptedException {

		final long period = 1000; // 1000 ms
		final long offset = 200; // 1st event after 200 ms

		final MemorySampler sampler = new MemorySampler();

		this.monitoringController.schedulePeriodicSampler(sampler, offset, period, TimeUnit.MILLISECONDS);

		Thread.sleep(3500); // sleep 3,5 seconds

		// There should be 4 saved records
		final int numEventsBeforeDisabled = this.recordListFilledByListWriter.size();

		this.monitoringController.disableMonitoring();

		Thread.sleep(2000); // sleep 2 seconds while being disabled

		// There should be no new records while being disabled
		final int numEventsWhileDisabled = this.recordListFilledByListWriter.size() - numEventsBeforeDisabled;

		this.monitoringController.enableMonitoring();

		Thread.sleep(2000); // sleep 2 seconds while being re-enabled

		// There should be at least 1 new record after re-enabling (expecting 2 new records)
		final int numEventsAfterReEnabled = this.recordListFilledByListWriter.size() - numEventsBeforeDisabled;

		final boolean isInstanceOf = this.recordListFilledByListWriter.get(0) instanceof MemoryRecord;

		Assert.assertTrue("Unexpected instance of IMonitoringRecord", isInstanceOf);
		Assert.assertEquals("Unexpected number of triggering events before disabling", 4, numEventsBeforeDisabled);
		Assert.assertEquals("Unexpected number of triggering events while disabled", 0, numEventsWhileDisabled);
		Assert.assertTrue("Unexpected at least one triggering events after being re-enabled. Found " + numEventsAfterReEnabled,
				numEventsAfterReEnabled > 0);
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
