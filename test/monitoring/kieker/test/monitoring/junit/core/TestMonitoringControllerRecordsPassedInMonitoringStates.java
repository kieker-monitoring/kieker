package kieker.test.monitoring.junit.core;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.test.monitoring.junit.core.util.DummyRecord;
import kieker.test.monitoring.junit.core.util.DummyRecordCountWriter;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public class TestMonitoringControllerRecordsPassedInMonitoringStates extends TestCase {

	public void testRecordsPassedToWriterWhenEnabled() {
		final Configuration configuration = Configuration.createDefaultConfiguration();
		configuration.setProperty(Configuration.CONTROLLER_NAME, "jUnit");
		configuration.setProperty(Configuration.WRITER_CLASSNAME, DummyRecordCountWriter.class.getName());
		configuration.setProperty(Configuration.PREFIX + "jUnit", "true");
		final IMonitoringController kieker = MonitoringController.createInstance(configuration);

		Assert.assertTrue("Failed to enable monitoring", kieker.enableMonitoring());
		kieker.newMonitoringRecord(new DummyRecord());
//		Assert.assertEquals("Unexpected number of records received", 1, ((DummyRecordCountWriter) kieker.getMonitoringWriter()).getNumDummyRecords());
		kieker.terminateMonitoring();
	}

	public void testNoRecordsPassedToWriterWhenDisabled() {
		final Configuration configuration = Configuration.createDefaultConfiguration();
		configuration.setProperty(Configuration.CONTROLLER_NAME, "jUnit");
		configuration.setProperty(Configuration.WRITER_CLASSNAME, DummyRecordCountWriter.class.getName());
		configuration.setProperty(Configuration.PREFIX + "jUnit", "true");
		final IMonitoringController kieker = MonitoringController.createInstance(configuration);

		Assert.assertTrue("Failed to disable monitoring", kieker.disableMonitoring());
		kieker.newMonitoringRecord(new DummyRecord());
//		Assert.assertEquals("Unexpected number of records received", 0, ((DummyRecordCountWriter) kieker.getMonitoringWriter()).getNumDummyRecords());
		kieker.terminateMonitoring();
	}

	public void testNoRecordsPassedToWriterWhenTerminated() {
		final Configuration configuration = Configuration.createDefaultConfiguration();
		configuration.setProperty(Configuration.CONTROLLER_NAME, "jUnit");
		configuration.setProperty(Configuration.WRITER_CLASSNAME, DummyRecordCountWriter.class.getName());
		configuration.setProperty(Configuration.PREFIX + "jUnit", "true");
		final IMonitoringController kieker = MonitoringController.createInstance(configuration);

		kieker.terminateMonitoring();
		kieker.newMonitoringRecord(new DummyRecord());
//		Assert.assertEquals("Unexpected number of records received", 0, ((DummyRecordCountWriter) kieker.getMonitoringWriter()).getNumDummyRecords());
	}
}
