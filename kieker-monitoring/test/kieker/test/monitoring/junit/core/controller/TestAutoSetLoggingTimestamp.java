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

package kieker.test.monitoring.junit.core.controller;

import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.monitoring.util.NamedPipeFactory;

/**
 * Tests, whether the property <i>setLoggingTimestamp</i> works properly.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.3
 */
public class TestAutoSetLoggingTimestamp extends AbstractKiekerTest { // NOCS

	private void executeTestSetLoggingTimestamp(final boolean setLoggingTimestampEnabled) {
		final String pipeName = NamedPipeFactory.createPipeName();

		// We will pass the property Configuration.AUTO_SET_LOGGINGTSTAMP as and additional configuration property

		final Properties additionalConfigurationProperties = new Properties();
		if (setLoggingTimestampEnabled) {
			// auto set logging timestamps enabled
			additionalConfigurationProperties.put(ConfigurationFactory.AUTO_SET_LOGGINGTSTAMP, Boolean.toString(true));
		} else {
			// auto set logging timestamps disabled
			additionalConfigurationProperties.put(ConfigurationFactory.AUTO_SET_LOGGINGTSTAMP, Boolean.toString(false));
		}

		final IMonitoringController monitoringController = NamedPipeFactory.createMonitoringControllerWithNamedPipe(pipeName, additionalConfigurationProperties);

		// We will now register a custom IPipeReader which receives records through the pipe and collects these in a list. On purpose, we are not using the
		// corresponding PipeReader that comes with Kieker.

		final List<IMonitoringRecord> receivedRecords = NamedPipeFactory.createAndRegisterNamedPipeRecordCollector(pipeName);

		final EmptyRecord rSent = new EmptyRecord();
		final long timestampSent = rSent.getLoggingTimestamp();
		monitoringController.newMonitoringRecord(new EmptyRecord());

		Assert.assertEquals("Test invalid: exactly one record should have been received", 1, receivedRecords.size());

		// Note, that rSent and rReceived are actually the same, but since this is implementation knowledge, we'll fetch the logged record from the list
		final EmptyRecord rReceived = (EmptyRecord) receivedRecords.get(0);
		final long timestampReceived = rReceived.getLoggingTimestamp();

		if (setLoggingTimestampEnabled) {
			Assert.assertTrue("The logging timestamps must not be equal since setLoggingTimestamp property is set true (timestampSent: " + timestampSent + "; "
					+ "timestampReceived: " + timestampReceived, timestampSent != timestampReceived);
		} else {
			Assert.assertTrue("The logging timestamps must be equal since setLoggingTimestamp property is set false (timestampSent: " + timestampSent + "; "
					+ "timestampReceived: " + timestampReceived, timestampSent == timestampReceived);
		}
	}

	/**
	 * Test if setting the automatic logging timestamp works.
	 */
	@Test
	public void testSetLoggingTimestampEnabled() { // NOPMD (assert in method)
		this.executeTestSetLoggingTimestamp(true);
	}

	/**
	 * Test if setting the explicit logging timestamp works.
	 */
	@Test
	public void testSetLoggingTimestampDisabled() { // NOPMD (assert in method)
		this.executeTestSetLoggingTimestamp(false);
	}
}
