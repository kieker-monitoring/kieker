package kieker.test.monitoring.junit.core;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.Kieker;
import kieker.monitoring.core.configuration.Configuration;
import kieker.test.monitoring.junit.core.util.DummyRecord;
import kieker.test.monitoring.junit.core.util.DummyRecordCountWriter;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
/**
 * @author Andre van Hoorn, Jan Waller
 */
public class TestMonitoringControllerRecordsPassedInMonitoringStates extends TestCase {
	
	public void testRecordsPassedToWriterWhenEnabled() {
		final Configuration configuration = Configuration.createDefaultConfiguration();
		configuration.setProperty(Configuration.CONTROLLER_NAME, "jUnit");
		configuration.setProperty(Configuration.WRITER_CLASSNAME, DummyRecordCountWriter.class.getName());
		configuration.setProperty(Configuration.PREFIX + "jUnit", "true");
		final Kieker kieker = Kieker.createAdditionalKieker(configuration);

		Assert.assertTrue("Failed to enable monitoring", kieker.enableMonitoring());
		kieker.newMonitoringRecord(new DummyRecord());
		Assert.assertEquals("Unexpected number of records received", 1, ((DummyRecordCountWriter)kieker.getMonitoringWriter()).getNumDummyRecords());
		kieker.terminateMonitoring();
	}

	public void testNoRecordsPassedToWriterWhenDisabled() {
		final Configuration configuration = Configuration.createDefaultConfiguration();
		configuration.setProperty(Configuration.CONTROLLER_NAME, "jUnit");
		configuration.setProperty(Configuration.WRITER_CLASSNAME, DummyRecordCountWriter.class.getName());
		configuration.setProperty(Configuration.PREFIX + "jUnit", "true");
		final Kieker kieker = Kieker.createAdditionalKieker(configuration);

		Assert.assertTrue("Failed to disable monitoring", kieker.disableMonitoring());
		kieker.newMonitoringRecord(new DummyRecord());
		Assert.assertEquals("Unexpected number of records received", 0, ((DummyRecordCountWriter)kieker.getMonitoringWriter()).getNumDummyRecords());
		kieker.terminateMonitoring();
	}

	public void testNoRecordsPassedToWriterWhenTerminated() {
		final Configuration configuration = Configuration.createDefaultConfiguration();
		configuration.setProperty(Configuration.CONTROLLER_NAME, "jUnit");
		configuration.setProperty(Configuration.WRITER_CLASSNAME, DummyRecordCountWriter.class.getName());
		configuration.setProperty(Configuration.PREFIX + "jUnit", "true");
		final Kieker kieker = Kieker.createAdditionalKieker(configuration);

		kieker.terminateMonitoring();
		kieker.newMonitoringRecord(new DummyRecord());
		Assert.assertEquals("Unexpected number of records received", 0, ((DummyRecordCountWriter)kieker.getMonitoringWriter()).getNumDummyRecords());
	}
}
