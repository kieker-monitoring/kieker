/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.junit.core.controller;

import java.util.List;
import java.util.Properties;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.configuration.ConfigurationKeys;
import kieker.monitoring.core.controller.MonitoringController;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.monitoring.util.NamedPipeFactory;

/**
 * Tests, whether the property <i>setLoggingTimestamp</i> works properly.
 *
 * @author Andre van Hoorn, Christian Wulf
 *
 * @since 1.3
 */
public class TestAutoSetLoggingTimestamp extends AbstractKiekerTest { // NOCS // NOPMD

	@Test
	public void testSetLoggingTimestampEnabled() throws InterruptedException {
		final EmptyRecord record = new EmptyRecord();

		final IMonitoringRecord receivedRecord = this.executeTestSetLoggingTimestamp(true);

		Assert.assertThat(record.getLoggingTimestamp(), CoreMatchers.is(CoreMatchers.not(receivedRecord.getLoggingTimestamp())));
	}

	@Test
	public void testSetLoggingTimestampDisabled() throws InterruptedException {
		final EmptyRecord record = new EmptyRecord();

		final IMonitoringRecord receivedRecord = this.executeTestSetLoggingTimestamp(false);

		Assert.assertThat(record.getLoggingTimestamp(), CoreMatchers.is(receivedRecord.getLoggingTimestamp()));
	}

	private EmptyRecord executeTestSetLoggingTimestamp(final boolean autoSetLoggingTimestamp) throws InterruptedException {
		final String pipeName = NamedPipeFactory.createPipeName();

		// We will pass the property Configuration.AUTO_SET_LOGGINGTSTAMP as an additional configuration property
		final Properties additionalConfigurationProperties = new Properties();
		additionalConfigurationProperties.put(ConfigurationKeys.AUTO_SET_LOGGINGTSTAMP, Boolean.toString(autoSetLoggingTimestamp));
		final MonitoringController monitoringController = NamedPipeFactory.createMonitoringControllerWithNamedPipe(pipeName, additionalConfigurationProperties);

		// We will now register a custom IPipeReader which receives records through the pipe and collects these in a list. On purpose, we are not using the
		// corresponding PipeReader that comes with Kieker.

		final List<IMonitoringRecord> receivedRecords = NamedPipeFactory.createAndRegisterNamedPipeRecordCollector(pipeName);
		monitoringController.newMonitoringRecord(new EmptyRecord());
		monitoringController.terminateMonitoring();
		monitoringController.waitForTermination(5000);

		Assert.assertEquals("Test invalid: exactly one record should have been received", 1, receivedRecords.size());

		// Note, that rSent and rReceived are actually the same, but since this is implementation knowledge, we'll fetch the logged record from the list
		return (EmptyRecord) receivedRecords.get(0);
	}
}
