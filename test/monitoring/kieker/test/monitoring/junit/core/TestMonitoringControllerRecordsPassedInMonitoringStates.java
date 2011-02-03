package kieker.test.monitoring.junit.core;

import java.util.Properties;
import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.IMonitoringController;
import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.core.configuration.ConfigurationProperties;
import kieker.monitoring.core.configuration.IMonitoringConfiguration;
import kieker.monitoring.core.configuration.MonitoringConfiguration;
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
		final Properties properties = ConfigurationProperties.getDefaultProperties();
		properties.setProperty(ConfigurationProperties.MONITORING_DATA_WRITER_CLASSNAME, DummyRecordCountWriter.class.getName());
		final IMonitoringConfiguration config = MonitoringConfiguration.createConfiguration("jUnit-DummyRecordCounter", properties);
		final IMonitoringController ctrl = new MonitoringController(config);

		Assert.assertTrue("Failed to enable monitoring", ctrl.enableMonitoring());
		ctrl.newMonitoringRecord(new DummyRecord());
		Assert.assertEquals("Unexpected number of records received", 1, ((DummyRecordCountWriter)ctrl.getMonitoringLogWriter()).getNumDummyRecords());
		ctrl.terminateMonitoring();
	}

	public void testNoRecordsPassedToWriterWhenDisabled() {
		final Properties properties = ConfigurationProperties.getDefaultProperties();
		properties.setProperty(ConfigurationProperties.MONITORING_DATA_WRITER_CLASSNAME, DummyRecordCountWriter.class.getName());
		final IMonitoringConfiguration config = MonitoringConfiguration.createConfiguration("jUnit-DummyRecordCounter", properties);
		final IMonitoringController ctrl = new MonitoringController(config);

		Assert.assertTrue("Failed to disable monitoring", ctrl.disableMonitoring());
		ctrl.newMonitoringRecord(new DummyRecord());
		Assert.assertEquals("Unexpected number of records received", 0, ((DummyRecordCountWriter)ctrl.getMonitoringLogWriter()).getNumDummyRecords());
		ctrl.terminateMonitoring();
	}

	public void testNoRecordsPassedToWriterWhenTerminated() {
		final Properties properties = ConfigurationProperties.getDefaultProperties();
		properties.setProperty(ConfigurationProperties.MONITORING_DATA_WRITER_CLASSNAME, DummyRecordCountWriter.class.getName());
		final IMonitoringConfiguration config = MonitoringConfiguration.createConfiguration("jUnit-DummyRecordCounter", properties);
		final IMonitoringController ctrl = new MonitoringController(config);

		ctrl.terminateMonitoring();
		ctrl.newMonitoringRecord(new DummyRecord());
		Assert.assertEquals("Unexpected number of records received", 0, ((DummyRecordCountWriter)ctrl.getMonitoringLogWriter()).getNumDummyRecords());
	}
}
